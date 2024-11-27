package igc.mirror.poi.parser.model;

import igc.mirror.poi.model.*;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellReference;

@Slf4j
public class XLSXSheetContentsHandler {
    @Setter
    private Sheet filledSheet;

    private boolean firstCellOfRow;
    private DataFilter dataFilter;
    private Row row;

    protected XLSXSheetContentsHandler() {
        this.firstCellOfRow = true;
    }

    private boolean cellNeeded(int currentRow, int currentCol) {
        return (
                (dataFilter.minRowNum() == null || (currentRow + 1 >= dataFilter.minRowNum())) &&
                        (dataFilter.minColNum() == null || currentCol + 1 >= dataFilter.minColNum()) &&
                        (dataFilter.maxRowNum() == null || currentRow + 1 <= dataFilter.maxRowNum()) &&
                        (dataFilter.maxColNum() == null || currentCol + 1 <= dataFilter.maxColNum())
        );
    }

    private boolean rowNeeded(int currentRowNum) {
        return ((dataFilter.minRowNum() == null || (currentRowNum + 1 >= dataFilter.minRowNum())) &&
                (dataFilter.maxRowNum() == null || currentRowNum + 1 <= dataFilter.maxRowNum())
        );
    }

    public void startRow(int rowNum) {
        log.trace("Start row {}", rowNum);
        this.firstCellOfRow = true;
        if (rowNeeded(rowNum))
            row = new Row(null, rowNum);
    }

    public void endRow(int rowNum) {
        log.trace("end row {}", rowNum);
        if (rowNeeded(rowNum))
            filledSheet.addRow(row);
    }

    public void cell(String cellReference, String formattedValue) {
        log.trace("Cell: [{}, {}]", cellReference, formattedValue);
        CellReference cellReferenceItem = new CellReference(cellReference);
        if (cellNeeded(cellReferenceItem.getCol(), cellReferenceItem.getRow())) {
            CellString cell = new CellString(cellReferenceItem.getCol(), formattedValue);

            if (row != null)
                row.addCell(cell);
            else
                log.warn("Пустая строка");
        }
    }

    public void cell(Cell rawCell) {
        log.trace("RAW CELL VALUE: {}", rawCell);
        CellReference cellReferenceItem = new CellReference(rawCell.getReferenceText());
        if (cellNeeded(cellReferenceItem.getCol(), cellReferenceItem.getRow())) {
            igc.mirror.poi.model.Cell cell = switch (rawCell.getCellType()) {
                case NUMBER ->
                        new CellNumber(cellReferenceItem.getCol(), Double.parseDouble(rawCell.getRawValue()), rawCell.getRawValue());
                case DATE ->
                        new CellDate(cellReferenceItem.getCol(), DateUtil.getJavaDate(Double.parseDouble(rawCell.getRawValue())), rawCell.getRawValue());
                case STRING -> new CellString(cellReferenceItem.getCol(), rawCell.getRawValue());
                case BLANK -> new CellBlank(cellReferenceItem.getCol());
            };
            if (row != null)
                row.addCell(cell);
            else
                log.warn("Пустая строка");
        }
    }

    public void endSheet() {

    }

    public void reset() {
        this.filledSheet = null;
        firstCellOfRow = true;
    }

    public void setDataFilter(DataFilter dataFilter) {
        this.dataFilter = dataFilter;
    }
}
