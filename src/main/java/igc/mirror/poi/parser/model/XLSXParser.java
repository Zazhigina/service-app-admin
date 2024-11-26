package igc.mirror.poi.parser.model;


import igc.mirror.poi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.StylesTable;
import org.springframework.util.StopWatch;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.regex.Matcher;

@Slf4j
public class XLSXParser implements ExcelParser {
    private Workbook wb;
    private final InputStream xlsxSource;
    private Locale locale;

    public XLSXParser(InputStream xlsxSource) {
        this.xlsxSource = xlsxSource;
    }

    private boolean sheetNeeded(DataFilter dataFilter, String sheetName) {
        if (dataFilter.sheetFilterPattern() != null) {
            Matcher sheetFilterMatcher = dataFilter.sheetFilterPattern().matcher(sheetName);
            return sheetFilterMatcher.lookingAt();
        } else
            return true;
    }

    @Override
    public void process() {
        process(false, null, null, null, null, null);
    }

    @Override
    public void process(Boolean skipData) {
        process(skipData, null, null, null, null, null);
    }

    @Override
    public void process(String sheetFilter) {
        process(false, sheetFilter, null, null, null, null);
    }

    @Override
    public void process(String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum, Integer maxColNum) {
        process(false, sheetFilter, minRowNum, minColNum, maxRowNum, maxColNum);
    }

    @Override
    public void process(Boolean skipData, String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum, Integer maxColNum) {
        StopWatch sw = new StopWatch("Parse XLSX");
        sw.start();
        this.wb = new Workbook();
        try {
            if (Boolean.TRUE.equals(skipData))
                processOnlySheet();
            else {
                DataFilter dataFilter = new DataFilter(sheetFilter, minRowNum, minColNum, maxRowNum, maxColNum);
                processAllData(dataFilter);
            }
            //TODO: need implementation exception!!!
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (OpenXML4JException | SAXException e) {
            throw new RuntimeException(e);
        }
        sw.stop();
        log.trace(sw.shortSummary());
    }

    @Override
    public Workbook getWorkbook() {
        return this.wb;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    private void processOnlySheet() {
        // TODO document why this method is empty
    }

    private void processAllData(DataFilter dataFilter) throws IOException, OpenXML4JException, SAXException {
        OPCPackage opc = OPCPackage.open(this.xlsxSource);
        XSSFReader xssfReader = new XSSFReader(opc);
        SharedStrings strings = new ReadOnlySharedStringsTable(opc, true);
        StylesTable styles = xssfReader.getStylesTable();
        XLSXSheetContentsHandler sheetExtractor = new XLSXSheetContentsHandler();

        XSSFReader.SheetIterator sheetIterator = (XSSFReader.SheetIterator) xssfReader.getSheetsData();

        int currentSheetNum = 0;
        while (sheetIterator.hasNext()) {
            try (InputStream sheetStream = sheetIterator.next()) {
                String sheetName = sheetIterator.getSheetName();
                if (sheetNeeded(dataFilter, sheetName)) {
                    Sheet sheet = new Sheet();
                    sheet.setName(sheetName);
                    sheet.setNum(currentSheetNum);

                    sheetExtractor.setFilledSheet(sheet);
                    sheetExtractor.setDataFilter(dataFilter);

                    processSheet(sheetExtractor, styles, strings, sheetStream);

                    wb.addSheet(sheet);
                    currentSheetNum++;
                    sheetExtractor.reset();
                } else
                    log.trace("Пропуск листа [{}]", sheetName);
            }
        }
    }

    private void processSheet(XLSXSheetContentsHandler sheetExtractor, StylesTable styles, SharedStrings strings, InputStream sheetInputStream) {
        DataFormatter formatter;
        if (locale == null) {
            formatter = new DataFormatter();
        } else {
            formatter = new DataFormatter(locale);
        }

        try(XLSXSheetStaxContentsHandler handler = new XLSXSheetStaxContentsHandler(
                sheetInputStream,
                sheetExtractor,
                styles,
                strings,
                formatter
        )) {
            handler.processSheet();
        } catch (Exception e) {
            throw new IllegalStateException("SAX parser appears to be broken - " + e.getMessage());
        }
    }
}
