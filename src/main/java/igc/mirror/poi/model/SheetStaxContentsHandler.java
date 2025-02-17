package igc.mirror.poi.model;

import igc.mirror.poi.view.CellType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.eventusermodel.ReadOnlySharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.InputStream;
import java.util.Date;

import static org.apache.poi.xssf.usermodel.XSSFRelation.NS_SPREADSHEETML;

// TODO: Требуется рефакторинг!
@Slf4j
public class SheetStaxContentsHandler {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    static final QName Q_ROW = new QName(NS_SPREADSHEETML, "row");
    static final QName Q_CELL = new QName(NS_SPREADSHEETML, "c");

    static final QName Q_ROW_NUM = new QName("", "r");
    static final QName Q_CELL_TYPE = new QName("", "t");
    static final QName Q_CELL_REF = new QName("", "r");
    static final QName Q_CELL_STYLE_REF = new QName("", "s");

    static final QName Q_CELL_VALUE = new QName(NS_SPREADSHEETML, "v");
    static final QName Q_CELL_FORMULA = new QName(NS_SPREADSHEETML, "f");

    private ReadOnlySharedStringsTable sharedStringsTable;

    private XMLEventReader reader = null;
    private int rowNum;
    private int nextRowNum;

    private xssfDataType nextDataType;
    private short formatIndex;
    private String formatString;
    private String cellRef;

    private StylesTable stylesTable;
    private final DataFormatter formatter;

    private StringBuffer value = new StringBuffer();

    private final StaxContentsHandler output;

    enum xssfDataType {
        BOOLEAN, ERROR, FORMULA, INLINE_STRING, SST_STRING, NUMBER,
    }

    public SheetStaxContentsHandler(InputStream is, StylesTable styles, ReadOnlySharedStringsTable strings,
                                    DataFormatter dataFormatter, StaxContentsHandler sheetContentsHandler)
            throws XMLStreamException {
        reader = FACTORY.createXMLEventReader(is);

        this.stylesTable = styles;
        this.sharedStringsTable = strings;
        this.output = sheetContentsHandler;
        this.nextDataType = xssfDataType.NUMBER;
        this.formatter = dataFormatter;
    }

    private String getAttribute(StartElement element, QName name) {
        Attribute attr = element.getAttributeByName(name);
        if (attr != null)
            return attr.getValue();
        else
            return null;
    }

    public void processCell(XMLEventReader cellReader, StartElement element) throws XMLStreamException {
        boolean isEndCell = false;


        /* ATTR CELL */
        this.nextDataType = xssfDataType.NUMBER;
        this.formatIndex = -1;
        this.formatString = null;
        this.cellRef = getAttribute(element, Q_CELL_REF);

        String cellStyleStr = getAttribute(element, Q_CELL_STYLE_REF);
        String cellType = getAttribute(element, Q_CELL_TYPE);

        if ("b".equals(cellType))
            nextDataType = xssfDataType.BOOLEAN;
        else if ("e".equals(cellType))
            nextDataType = xssfDataType.ERROR;
        else if ("inlineStr".equals(cellType))
            nextDataType = xssfDataType.INLINE_STRING;
        else if ("s".equals(cellType))
            nextDataType = xssfDataType.SST_STRING;
        else if ("str".equals(cellType)) {
            nextDataType = xssfDataType.FORMULA;
        } else {
            XSSFCellStyle style = null;
            if (stylesTable != null) {
                if (cellStyleStr != null) {
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    style = stylesTable.getStyleAt(styleIndex);
                } else if (stylesTable.getNumCellStyles() > 0) {
                    style = stylesTable.getStyleAt(0);
                }
            }
            if (style != null) {
                this.formatIndex = style.getDataFormat();
                this.formatString = style.getDataFormatString();
                if (this.formatString == null)
                    this.formatString = BuiltinFormats.getBuiltinFormat(this.formatIndex);
            }
        }

        QName name = null;
        while (cellReader.hasNext() && !isEndCell) {
            XMLEvent event = cellReader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                name = start.getName();
                if (Q_CELL_VALUE.equals(name)) {
                    this.value.setLength(0);
                } else if (Q_CELL_FORMULA.equals(name)) {
                    if (nextDataType == xssfDataType.NUMBER) {
                        nextDataType = xssfDataType.FORMULA;
                    }
                } else {
                    log.error("[{}]Element not defined = {}, {}", this.cellRef, name.getLocalPart(),	name.getNamespaceURI());
                }
            }

            if (event.isCharacters()) {
                Characters c = event.asCharacters();
                if (c.isCData()) {
                    log.info("Characters : {}", "(CDATA):" + c.getData());
                } else if (c.isIgnorableWhiteSpace()) {
                    log.info("Characters : {}", "(IGNORABLE SPACE)");
                } else if (c.isWhiteSpace()) {
                    log.info("Characters : {}", "(EMPTY SPACE)");
                } else {
                    this.value.append(c.getData());
                }
            }

            if (event.isEndElement()) {
                EndElement end = event.asEndElement();
                name = end.getName();
                if (Q_CELL.equals(end.getName())) {
                    isEndCell = true;
                }

                else if (Q_CELL_VALUE.equals(name)) {
//					logger.info("Value");
                    CellType type = null;
                    String formatStr = null;
                    Double dblValue = null;
                    Date dateValue = null;
                    switch (this.nextDataType) {
                        case BOOLEAN:
                            char first = value.charAt(0);
                            formatStr = first == '0' ? "FALSE" : "TRUE";
                            type = CellType.STRING;
                            break;

                        case ERROR:
                            formatStr = "ERROR:" + value;
                            type = CellType.STRING;
                            break;

                        case FORMULA:
                            String fv = value.toString();

                            if (this.formatString != null) {
                                try {
                                    // Try to use the value as a formattable number
                                    dblValue = Double.parseDouble(fv);
                                    if (DateUtil.isADateFormat(this.formatIndex, this.formatString)) {
                                        type = CellType.DATE;
                                        dateValue = DateUtil.getJavaDate(dblValue);
                                    } else {
                                        formatStr = formatter.formatRawCellContents(dblValue, this.formatIndex, this.formatString);
                                        type = CellType.NUMBER;
                                    }
                                } catch (NumberFormatException e) {
                                    // Formula is a String result not a Numeric one
                                    formatStr = fv;
                                    type = CellType.STRING;
                                }
                            } else {
                                // No formatting applied, just do raw value in all cases
                                formatStr = fv;
                                type = CellType.STRING;
                            }
                            break;

                        case INLINE_STRING:
                            XSSFRichTextString rtsi = new XSSFRichTextString(value.toString());
                            formatStr = rtsi.toString();
                            type = CellType.STRING;
                            break;

                        case SST_STRING:
                            String sstIndex = this.value.toString();
                            try {
                                int idx = Integer.parseInt(sstIndex);
//							XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getEntryAt(idx));
                                XSSFRichTextString rtss = new XSSFRichTextString(sharedStringsTable.getItemAt(idx).getString());
                                formatStr = rtss.toString();
                                type = CellType.STRING;
                            } catch (NumberFormatException ex) {
                                log.error("Failed to parse SST index {}", sstIndex, ex);
                            }
                            break;

                        case NUMBER:
                            String n = this.value.toString();
                            if (this.formatString != null && n.length() > 0) {
                                dblValue = Double.parseDouble(n);
                                if (DateUtil.isADateFormat(this.formatIndex, this.formatString)) {
                                    type = CellType.DATE;
                                    dateValue = DateUtil.getJavaDate(dblValue);
                                } else {
                                    formatStr = formatter.formatRawCellContents(dblValue, this.formatIndex, this.formatString);
                                    type = CellType.NUMBER;
                                }
                            } else {
                                type = CellType.STRING;
                                formatStr = n;
                            }

                            break;

                        default:
                            formatStr = "(TODO: Unexpected type: " + nextDataType + ")";
                            break;
                    }
                    this.output.cell(this.cellRef, type, this.formatString, formatStr, dblValue, dateValue);
                }
            }
        }
    }

    public void processRow(XMLEventReader rowReader, StartElement element) throws XMLStreamException {
        boolean isEndRow = false;
        boolean firstCell = true;
        /* ATTR ROW */
        String rowNumStr = getAttribute(element, Q_ROW_NUM);
        if (rowNumStr != null) {
            rowNum = Integer.parseInt(rowNumStr) - 1;
        } else {
            rowNum = nextRowNum;
        }

        //
        while (rowReader.hasNext() && !isEndRow) {
            XMLEvent event = rowReader.nextEvent();
            if (event.isStartElement()) {
                StartElement start = event.asStartElement();
                if (Q_CELL.equals(start.getName())) {
                    if (firstCell) {
                        firstCell = false;
                        output.startRow(rowNum);
                    }
                    processCell(rowReader, start);
                }
            }

            if (event.isEndElement()) {
                EndElement end = event.asEndElement();
                if (Q_ROW.equals(end.getName())) {
                    if (!firstCell) {
                        output.endRow(rowNum);
                    }

                    // Некоторые листы могут не иметь заданного значения строки, но если сам Excel
                    // может их прочитать, то делаем и мы
                    nextRowNum = rowNum + 1;
                    isEndRow = true;
                }
            }
        }
    }

    public void process() throws XMLStreamException {
        try {
            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();
                if (event.isStartElement()) {
                    StartElement start = event.asStartElement();
                    if (Q_ROW.equals(start.getName())) {
                        processRow(reader, start);
                    }
                }
            }
        } finally {
            reader.close();
        }
    }

    public interface StaxContentsHandler {
        /** A row with the (zero based) row number has started */
        void startRow(int rowNum);

        /** A row with the (zero based) row number has ended */
        void endRow(int rowNum);

        /**
         * A cell, with the given formatted value (may be null), and possibly a comment
         * (may be null), was encountered
         */
        void cell(String cellReference, CellType cellType, String formatCell, String strValue, Double dblValue,
                         Date dateValue);
    }
}
