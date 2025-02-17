package igc.mirror.poi.model;

public interface ExcelParser {

    void process();

    void process(Boolean skipData);

    void process(String sheetFilter);

    void process(String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum, Integer maxColNum);

    void process(Boolean skipData, String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum, Integer maxColNum);

    Workbook getWorkbook();
}
