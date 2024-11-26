package igc.mirror.poi.service;

import igc.mirror.exception.common.IllegalEntityStateException;
import igc.mirror.poi.model.*;
import igc.mirror.poi.parser.model.XLSXParser;
import igc.mirror.poi.view.FileType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
@Slf4j
public class POIService {
    public Workbook parseExcel(InputStream excelFile, Boolean skipData, String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum, Integer maxColNum) {
        Workbook wb = null;
        try (InputStream wrapFile = FileMagic.prepareToCheckMagic(excelFile)) {
            FileType ft = identificationFileType(wrapFile);
            ExcelParser parser = switch (ft) {
                case XLS -> new XLSParser(wrapFile);
                case XLSX -> new XLSXParser(wrapFile);
                default -> null;
            };

            assert parser != null;
            parser.process(skipData, sheetFilter, minRowNum, minColNum, maxRowNum, maxColNum);
            wb = parser.getWorkbook();
        } catch (IOException e) {
            throw new IllegalEntityStateException("Ошибка при разборе файла", null, null); //RuntimeException(e);
        }

        return wb;
    }

    public Workbook parseExcel(InputStream excelFile) {
        return parseExcel(excelFile, false, null, null, null, null, null);
    }

    public Workbook parseExcel(InputStream excelFile, String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum, Integer maxColNum) {
        return parseExcel(excelFile, false, sheetFilter, minRowNum, minColNum, maxRowNum, maxColNum);
    }

    private FileType identificationFileType(InputStream file) throws IOException {
        return switch (FileMagic.valueOf(file)) {
            case OLE2 -> FileType.XLS;
            case OOXML -> FileType.XLSX;
            default -> null;
        };
    }
}
