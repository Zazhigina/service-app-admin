package igc.mirror.poi.model;

import igc.mirror.poi.view.CellType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.StylesTable;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public class XLSXParser implements ExcelParser {
    private Boolean skipData;
    private Sheet sheet;
    private Workbook wb;
    private Row row;

    private Pattern sheetFilterPattern;
    private Integer minRowNum;
    private Integer minColNum;
    private Integer maxRowNum;
    private Integer maxColNum;

    ///
    private InputStream inputStreamXLSXSource;

    public XLSXParser(InputStream inputStreamXLSXSource) {
        this.inputStreamXLSXSource = inputStreamXLSXSource;
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
        try {
            if (Boolean.TRUE.equals(skipData))
                this.processSheet();
            else
                processData(sheetFilter, minRowNum, minColNum, maxRowNum, maxColNum);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (OpenXML4JException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (XMLStreamException e) {
            throw new RuntimeException(e);
        }
    }

    private void processSheet() throws IOException, OpenXML4JException, ParserConfigurationException, SAXException {
        log.trace("Запуск процедуры разбора файла в формате XLSX (получение структуры)");
//        Stopwatch stopwatch 			= Stopwatch.createStarted();

        this.wb 					    = new Workbook();
        XSSFReader xssfReader 			= new XSSFReader(OPCPackage.open(this.inputStreamXLSXSource));
        XSSFReader.SheetIterator iter	= (XSSFReader.SheetIterator) xssfReader.getSheetsData();

        int index = 0;
        while (iter.hasNext()) {
            try(InputStream stream = iter.next()) {
                InputSource sheetSource = new InputSource(stream);

                this.sheet 				= new Sheet();
                this.sheet.setLastCellNum(0);
                this.sheet.setLastRowNum(0);
                this.sheet.setName(iter.getSheetName());
                this.sheet.setNum(index);

                XMLReader sheetParser 	= XMLHelper.newXMLReader(); // SAXHelper.newXMLReader();
                ContentHandler handler 	= new SheetStructHandler();
                sheetParser.setContentHandler(handler);
                sheetParser.parse(sheetSource);

                this.wb.addSheet(this.sheet);
            }
            ++index;
        }

//        log.trace("Затраченное время на разбор файла в формате XLSX (получение структуры) - {}", stopwatch);
    }

    private void processData(String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum, Integer maxColNum)
            throws IOException, OpenXML4JException, XMLStreamException, SAXException {
        log.trace("Запуск процедуры разбора файла в формате XLSX (STAX)");
//        Stopwatch stopwatch = Stopwatch.createStarted();

        SheetStaxContentsHandler parser = null;
        DataFormatter formatter = new DataFormatter();

        this.wb 					    = new Workbook();

        OPCPackage xlsxPackage = OPCPackage.open(this.inputStreamXLSXSource);
        ReadOnlySharedStringsTable strings = new ReadOnlySharedStringsTable(xlsxPackage);
        XSSFReader xssfReader = new XSSFReader(xlsxPackage);
        StylesTable styles = xssfReader.getStylesTable();

        int index = 0;

        if (sheetFilter != null) {
            log.trace("Установлена фильтрация по листам документа [{}]", sheetFilter);
            sheetFilterPattern = Pattern.compile(sheetFilter, Pattern.CASE_INSENSITIVE);
        } else
            sheetFilterPattern = null;

        if (minRowNum != null) {
            this.minRowNum = minRowNum;
            log.trace("Установлена фильтрация (начало) по строкам документа {}", this.minRowNum);
        }

        if (minColNum != null) {
            this.minColNum = minColNum;
            log.trace("Установлена фильтрация (начало) по столбцам документа {}", this.minColNum);
        }

        if (maxRowNum != null) {
            this.maxRowNum = maxRowNum;
            log.trace("Установлена фильтрация (окончание) по строкам документа {}", this.maxRowNum);
        }

        if (maxColNum != null) {
            this.maxColNum = maxColNum;
            log.trace("Установлена фильтрация (окончание) по столбцам документа {}", this.maxColNum);
        }

        XSSFReader.SheetIterator iter = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (iter.hasNext()) {
            InputStream stream = iter.next();
            if (sheetNeeded(iter.getSheetName())) {
                this.sheet = new Sheet();
//                this.curr_sheet_index = index;
                this.sheet.setName(iter.getSheetName());
                this.sheet.setNum(index);

                parser = new SheetStaxContentsHandler(stream, styles, strings, formatter, new SheetDataHandler());
                parser.process();
                this.wb.addSheet(this.sheet);
            } else
                log.trace("Пропуск {}", iter.getSheetName());

            stream.close();
            ++index;
        }

        this.wb.recalculateSheetSize();
//        log.trace("Затраченное время на разбор файла в формате XLSX - " + stopwatch);
    }

    @Override
    public Workbook getWorkbook() {
        return wb;
    }

    private boolean sheetNeeded(String sheetName) {
        if (this.sheetFilterPattern != null) {
            Matcher sheetFilterMatcher = this.sheetFilterPattern.matcher(sheetName);
            return sheetFilterMatcher.lookingAt();
        } else
            return true;
    }

    private class SheetDataHandler implements SheetStaxContentsHandler.StaxContentsHandler {
        private boolean firstCellOfRow = false;
        private int currentRowNum = -1;
        private int currentColNum = -1;

        private boolean cellNeeded(int currentRow, int currentCol) {
            return (
                    ( minRowNum == null || (currentRow + 1 >= minRowNum) ) &&
                            (minColNum == null || currentCol + 1 >= minColNum) &&
                            (maxRowNum == null || currentRow + 1 <= maxRowNum) &&
                            (maxColNum == null || currentCol + 1 <= maxColNum)
            );
        }

        @Override
        public void startRow(int rowNum) {
            firstCellOfRow = true;
            currentRowNum = rowNum;
            currentColNum = -1;
            if ( ( minRowNum == null || (currentRowNum + 1 >= minRowNum) ) &&
                ( maxRowNum == null || currentRowNum + 1 <= maxRowNum)
            )
                row = new Row(null, currentRowNum);
        }

        @Override
        public void endRow(int rowNum) {
            currentRowNum = rowNum;
            if ( ( minRowNum == null || (currentRowNum + 1 >= minRowNum) ) &&
                    ( maxRowNum == null || currentRowNum + 1 <= maxRowNum)
            ) {
                sheet.addRow(row);
            }
        }

        @Override
        public void cell(String cellReference, CellType cellType, String formatCell, String textValue, Double dblValue,
                         Date dateValue) {

            Cell cell = null;
            if (firstCellOfRow) {
                firstCellOfRow = false;
            }

            String currentCellReference = cellReference;
            if (currentCellReference == null) {
                currentCellReference = new CellAddress(currentRowNum, currentColNum).formatAsString();
            }

            // Did we miss any cells?
            currentColNum = (new CellReference(currentCellReference)).getCol();
            currentRowNum = (new CellReference(currentCellReference)).getRow();
            if (cellNeeded(currentRowNum, currentColNum)) {
                cell = switch (cellType) {
                    case NUMBER -> new CellNumber(currentColNum, dblValue);
                    case DATE -> new CellDate(currentColNum, dateValue);
                    default -> new CellString(currentColNum, textValue);
                };

                if (row != null)
                    row.addCell(cell);
                else
                    log.warn("Пустая строка");
            }
        }
    }

    private class SheetStructHandler extends DefaultHandler {
        private int rowNum;
        private int nextRowNum; // some sheets do not have rowNums, Excel can read them so we should try to

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if ("row".equals(localName)) {
                String rowNumStr = attributes.getValue("r");
                if (rowNumStr != null) {
                    rowNum = Integer.parseInt(rowNumStr) - 1;
                } else {
                    rowNum = nextRowNum;
                }
            } // c => cell
            else if ("c".equals(localName)) {
                // handle them correctly as well
                String currentCellReference = attributes.getValue("r");
                sheet.setLastCellNum(Math.max(sheet.getLastCellNum(), (new CellAddress(currentCellReference)).getColumn()+ 1) );
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            if ("row".equals(localName)) {
                sheet.setLastRowNum(Math.max(sheet.getLastRowNum(), rowNum + 1));
                nextRowNum = rowNum + 1;
            }
        }
    }
}
