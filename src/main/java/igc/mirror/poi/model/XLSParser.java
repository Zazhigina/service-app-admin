package igc.mirror.poi.model;

import igc.mirror.exception.common.LoadFileException;
import igc.mirror.poi.view.CellType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Data
public class XLSParser implements ExcelParser, HSSFListener {
    // имя обрабатываемой книги
    private String currentSheetName;
    // Обработка формул со строковым результатом
    private int nextRow;
    private int nextColumn;
    private boolean outputNextStringRecord;
    // Для обработки страниц
    private int sheetIndex = -1;
    private BoundSheetRecord[] orderedBSRs;
    private ArrayList<BoundSheetRecord> boundSheetRecords = new ArrayList<>();
    //
    private SSTRecord sstRecord;
    private FormatTrackingHSSFListener formatListener;


    /////////////////////////////////////////////////////////////
    //
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
    private InputStream inputStreamXLSSource;

    public XLSParser(@NotNull InputStream is) {
        this.inputStreamXLSSource = is;
    }

    @Override
    public Workbook getWorkbook() {
        return this.wb;
    }

    @Override
    public void process(Boolean skipData, String sheetFilter, Integer minRowNum, Integer minColNum, Integer maxRowNum, Integer maxColNum) {
        this.skipData = skipData;
        // установка фильтрации
        if (sheetFilter != null) {
            log.trace("Установлена фильтрация по листам документа {}", sheetFilter);
            sheetFilterPattern = Pattern.compile(sheetFilter, Pattern.CASE_INSENSITIVE);
        } else
            sheetFilterPattern = null;

        if (Boolean.FALSE.equals(skipData)) {
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
        }

        this.formatListener = new FormatTrackingHSSFListener(this);

        // фильтры для парсера
        HSSFRequest request 		= new HSSFRequest();
        if(Boolean.TRUE.equals(skipData)) {
            request.addListener(formatListener, BoundSheetRecord.sid);
            request.addListener(formatListener, BOFRecord.sid);
            request.addListener(formatListener, RowRecord.sid);
        } else {
            request.addListener(formatListener, BoundSheetRecord.sid);
            request.addListener(formatListener, BOFRecord.sid);
            request.addListener(formatListener, SSTRecord.sid);
            request.addListener(formatListener, FormulaRecord.sid);
            request.addListener(formatListener, StringRecord.sid);
            request.addListener(formatListener, LabelRecord.sid);
            request.addListener(formatListener, LabelSSTRecord.sid);
            request.addListener(formatListener, NumberRecord.sid);
            // нужно для нормальной работы FormatTrackingHSSFListener.formatNumberDateCell
            request.addListener(formatListener, ExtendedFormatRecord.sid);
        }

        this.wb = new Workbook();
        this.sheet = null;

        POIFSFileSystem fs = null;
        try {
            fs = new POIFSFileSystem(this.inputStreamXLSSource);
        } catch (IOException e) {
            throw new LoadFileException("Ошибка при предварительной обработке файла", e);
        }

        HSSFEventFactory factory 	= new HSSFEventFactory();
        try {
            factory.processWorkbookEvents(request, fs);
        } catch (IOException e) {
            throw new LoadFileException("Ошибка при разборе файла", e);
        }

        if(Boolean.FALSE.equals(skipData))
            this.wb.recalculateSheetSize();
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
    public void processRecord(Record fileRecord) {
        if (Boolean.TRUE.equals(this.skipData))
            this.processRecordStructure(fileRecord);
        else
            this.processRecordData(fileRecord);
    }

    private void processRecordData(Record fileRecord) {
        int currentRow = -1;
        int currentCol = -1;

        String textValue = null;
        double dblValue = Double.NaN;
        Date dateValue = null;
        String formatCell = null;
        CellType cellType = CellType.BLANK;

        switch (fileRecord.getSid()) {
            case BoundSheetRecord.sid:
                boundSheetRecords.add((BoundSheetRecord) fileRecord);
                break;

            case BOFRecord.sid:
                BOFRecord br = (BOFRecord) fileRecord;
                if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    sheetIndex++;
                    if (orderedBSRs == null) {
                        orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                    }
                    currentSheetName = orderedBSRs[sheetIndex].getSheetname();
                    sheet = new Sheet();
                    sheet.setName(currentSheetName);
                    sheet.setNum(sheetIndex);
                    if(sheetNeeded(currentSheetName)) {
                        wb.addSheet(sheet);
                    }
                }
                break;

            case SSTRecord.sid:
                sstRecord = (SSTRecord) fileRecord;
                break;

            case FormulaRecord.sid:
                FormulaRecord formulaRecord = (FormulaRecord) fileRecord;
                if (formulaRecord.hasCachedResultString()) {
                    // Результат формулы - строковое значение,
                    // Которое хранится в следующей записи
                    outputNextStringRecord = true;
                    nextRow = formulaRecord.getRow();
                    nextColumn = formulaRecord.getColumn();
                } else {
                    currentRow = formulaRecord.getRow();
                    currentCol = formulaRecord.getColumn();

                    dblValue = formulaRecord.getValue();

                    formatCell = formatListener.getFormatString(formulaRecord);
                    cellType = CellType.NUMBER;
                    if (DateUtil.isADateFormat(formatListener.getFormatIndex(formulaRecord), formatCell)) {
                        cellType = CellType.DATE;
                        dateValue = DateUtil.getJavaDate(dblValue);
                    }

                }
                break;

            case StringRecord.sid:
                StringRecord stringRecord = (StringRecord) fileRecord;

                if (outputNextStringRecord) {
                    // String for formula
                    textValue = stringRecord.getString();

                    cellType = CellType.STRING;
                    currentRow = nextRow;
                    currentCol = nextColumn;
                    outputNextStringRecord = false;
                }
                break;

            case LabelRecord.sid:
                LabelRecord lrec = (LabelRecord) fileRecord;

                currentRow = lrec.getRow();
                currentCol = lrec.getColumn();
                textValue = lrec.getValue();
                cellType = CellType.STRING;
                break;

            case LabelSSTRecord.sid:
                LabelSSTRecord lsrec = (LabelSSTRecord) fileRecord;

                if (sstRecord != null) {
                    currentRow = lsrec.getRow();
                    currentCol = lsrec.getColumn();
                    textValue = sstRecord.getString(lsrec.getSSTIndex()).toString();
                    cellType = CellType.STRING;
                }
                break;

            case NumberRecord.sid:
                NumberRecord numrec = (NumberRecord) fileRecord;

                currentRow = numrec.getRow();
                currentCol = numrec.getColumn();
                textValue = formatListener.formatNumberDateCell(numrec);
                formatCell = formatListener.getFormatString(numrec);
                dblValue = numrec.getValue();

                if (DateUtil.isADateFormat(formatListener.getFormatIndex(numrec), formatCell)) {
                    cellType = CellType.DATE;
                    dateValue = DateUtil.getJavaDate(dblValue);
                } else {
                    cellType = CellType.NUMBER;
                }

                break;

            default:
                break;
        }

        if (
                (currentRow != -1 && currentCol != -1) &&
                (List.of(CellType.NUMBER, CellType.DATE, CellType.STRING).contains(cellType) || (textValue != null && !textValue.isEmpty())) &&
                        sheetNeeded(sheet.getName()) &&
                        cellNeeded(currentRow, currentCol)
        )
            onNewCell(currentRow, currentCol, cellType, textValue, dblValue, dateValue);
    }

    private void onNewCell(int currentRow, int currentCol, CellType cellType, String textValue, double dblValue, Date dateValue) {
//        if (
//                (List.of(CellType.NUMBER, CellType.DATE, CellType.STRING).contains(cellType) || (textValue != null && !textValue.isEmpty())) &&
//                sheetNeeded(sheet.getName()) &&
//                        cellNeeded(currentRow, currentCol)
//        ) {
            Cell cell = switch (cellType) {
                case NUMBER -> new CellNumber(currentCol, dblValue);
                case DATE -> new CellDate(currentCol, dateValue);
                default -> new CellString(currentCol, textValue);
            };

            if (this.row == null || this.row.getNum() != currentRow) {
                this.row = new Row(null, currentRow);
                sheet.addRow(this.row);
            }

            this.row.addCell(cell);
//        }
    }

    private boolean cellNeeded(int currentRow, int currentCol) {
        return (
                ( this.minRowNum == null || (currentRow + 1 >= this.minRowNum) ) &&
                        (this.minColNum == null || currentCol + 1 >= this.minColNum) &&
                        (this.maxRowNum == null || currentRow + 1 <= this.maxRowNum) &&
                        (this.maxColNum == null || currentCol + 1 <= this.maxColNum)
        );
    }

    private boolean sheetNeeded(String sheetName) {
        if (this.sheetFilterPattern != null) {
            Matcher sheetFilterMatcher = this.sheetFilterPattern.matcher(sheetName);
            return sheetFilterMatcher.lookingAt();
        } else
            return true;
    }

    private void processRecordStructure(Record fileRecord) {
        switch (fileRecord.getSid()) {
            case BoundSheetRecord.sid:
                boundSheetRecords.add((BoundSheetRecord)fileRecord);
                break;

            case BOFRecord.sid:
                BOFRecord br = (BOFRecord) fileRecord;
                if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    sheetIndex++;
                    if (orderedBSRs == null) {
                        orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                    }
                    sheet = new Sheet();
                    sheet.setName(orderedBSRs[sheetIndex].getSheetname());
                    sheet.setNum(sheetIndex);
                    sheet.setLastCellNum(0);
                    sheet.setLastRowNum(0);
                    wb.addSheet(sheet);
                }
                break;
            case RowRecord.sid:
                RowRecord rowRecord = (RowRecord) fileRecord;
                sheet.setLastCellNum(Math.max(rowRecord.getLastCol(), sheet.getLastCellNum()));
                sheet.setLastRowNum(Math.max(sheet.getLastRowNum(), rowRecord.getRowNumber()));
                break;

            default:
                break;
        }
    }
}
