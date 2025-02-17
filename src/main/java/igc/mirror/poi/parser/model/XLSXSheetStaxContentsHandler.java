package igc.mirror.poi.parser.model;

import igc.mirror.poi.parser.ref.CellDataType;
import igc.mirror.poi.view.CellType;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.BuiltinFormats;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.model.SharedStrings;
import org.apache.poi.xssf.model.Styles;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.*;
import java.io.InputStream;

@Slf4j
public class XLSXSheetStaxContentsHandler implements AutoCloseable {
    private static final XMLInputFactory FACTORY = XMLInputFactory.newInstance();

    private static final String ROW_PART_NAME = "row";
    private static final String CELL_PART_NAME = "c";

    private static final String ROW_NUM_ATTR_NAME = "r";
    private static final String CELL_TYPE_ATTR_NAME = "t";
    private static final String CELL_STYLE_REF_ATTR_NAME = "s";
    private static final String CELL_REF_ATTR_NAME = "r";

    private final XLSXSheetContentsHandler contentsHandler;
    private final Styles styles;
    private final SharedStrings strings;
    private final DataFormatter dataFormatter;

    private final XMLEventReader reader;

    private int nextRowNum;

    public XLSXSheetStaxContentsHandler(InputStream xssfSheetInputStream, XLSXSheetContentsHandler contentsHandler, Styles styles, SharedStrings strings, DataFormatter dataFormatter) throws XMLStreamException {
        this.contentsHandler = contentsHandler;
        this.styles = styles;
        this.strings = strings;
        this.dataFormatter = dataFormatter;
        this.reader = FACTORY.createXMLEventReader(xssfSheetInputStream);
    }

    @Override
    public void close() throws Exception {
        if (reader != null) {
            try {
                reader.close();
            } catch (XMLStreamException e) { // empty
            }
        }
    }

    private String getAttributeValue(StartElement element, String attributeName) {
        Attribute attribute = element.getAttributeByName(new QName(attributeName));
        if (attribute != null)
            return attribute.getValue();
        else return null;
    }

    public void processSheet() throws XMLStreamException {
        this.nextRowNum = 0;

        while (reader.hasNext()) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().compareTo(ROW_PART_NAME) == 0) {
                    processRow(reader, startElement);
                }
            }
        }
    }

    private void processRow(XMLEventReader rowReader, StartElement lastRowStartElement) throws XMLStreamException {
        boolean isEndRow = false;
        boolean firstCell = true;

        String attributeRowNumValue = getAttributeValue(lastRowStartElement, ROW_NUM_ATTR_NAME);
        int currentRowNum = (attributeRowNumValue != null) ? Integer.parseInt(attributeRowNumValue) - 1 : this.nextRowNum;

        while (rowReader.hasNext() && !isEndRow) {
            XMLEvent event = rowReader.nextEvent();
            if (event.isStartElement()) {
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().compareTo(CELL_PART_NAME) == 0) {
                    if (firstCell) {
                        firstCell = false;
                        this.contentsHandler.startRow(currentRowNum);
                    }

                    processCell(rowReader, startElement, currentRowNum);
                }
            }
            if (event.isEndElement()) {
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart().compareTo(ROW_PART_NAME) == 0) {
                    if (!firstCell) {
                        this.contentsHandler.endRow(currentRowNum);
                    }

                    // Некоторые листы могут не иметь заданного значения строки, но если сам Excel
                    // может их прочитать, то делаем и мы
                    nextRowNum = currentRowNum + 1;
                    isEndRow = true;
                }
            }
        }
    }

    private void processCell(XMLEventReader reader, StartElement startElement, int currentRowNum) throws XMLStreamException {
        boolean isEndCell = false;

        Cell rawCell = new Cell(null,
                getAttributeValue(startElement, CELL_REF_ATTR_NAME),
                getCurrentCellDataType(startElement),
                -1, "", null);

        if (rawCell.getDataType() == CellDataType.NUMBER) {
            XSSFCellStyle currentStyle = getCurrentCellStyle(startElement);
            if (currentStyle != null) {
                rawCell.setFormatIndex(currentStyle.getIndex());
                rawCell.setFormatText(currentStyle.getDataFormatString());
                if (rawCell.getFormatText() == null)
                    rawCell.setFormatText(BuiltinFormats.getBuiltinFormat(rawCell.getFormatIndex()));
            }
        }

        while (reader.hasNext() && !isEndCell) {
            XMLEvent event = reader.nextEvent();
            if (event.isStartElement()) {
                switch (event.asStartElement().getName().getLocalPart()) {
                    case "v" -> rawCell.setRawValue("");
                    case "f" -> {
                        if ((rawCell.getDataType() == CellDataType.NUMBER)) {
                            rawCell.setDataType(CellDataType.FORMULA);
                        }
                    }
                    default ->
                            log.error("[{}] Element not defined! {}", rawCell.getReferenceText(), event.asStartElement().getName().getLocalPart());
                }
            }

            if (event.isCharacters()) {
                parseCharacters(rawCell, event);
                log.trace("Characters: {}", rawCell.getRawValue());
            }

            if (event.isEndElement()) {
                switch (event.asEndElement().getName().getLocalPart()) {
                    case "c" -> isEndCell = true;
                    case "v" -> {
                        fillRawCell(rawCell);
                        this.contentsHandler.cell(rawCell);
                    }
                    default -> log.trace("unexpected tag...");
                }
            }
        }
    }

    private void fillRawCell(Cell rawCell) {
        switch (rawCell.getDataType()) {
            case BOOLEAN -> {
                rawCell.setRawValue(rawCell.getRawValue().compareTo("0") == 0 ? "FALSE" : "TRUE");
                rawCell.setCellType(CellType.STRING);
            }
            case ERROR -> {
                rawCell.setRawValue("ERROR:" + rawCell.getRawValue());
                rawCell.setCellType(CellType.STRING);
            }
            case FORMULA -> {
                if (rawCell.getFormatText() != null) {
                    try {
                        Double.parseDouble(rawCell.getRawValue());
                        if (DateUtil.isADateFormat(rawCell.getFormatIndex(), rawCell.getFormatText()))
                            rawCell.setCellType(CellType.DATE);
                        else
                            rawCell.setCellType(CellType.NUMBER);
                    } catch (NumberFormatException e) {
                        rawCell.setCellType(CellType.STRING);
                    }
                }
            }
            case INLINE_STRING -> {
                rawCell.setCellType(CellType.STRING);
                rawCell.setRawValue(
                        new XSSFRichTextString(rawCell.getRawValue()).toString()
                );
            }
            case SST_STRING -> {
                try {
                    int sstIndex = Integer.parseInt(rawCell.getRawValue());
                    rawCell.setRawValue(new XSSFRichTextString(this.strings.getItemAt(sstIndex).getString()).toString());
                    rawCell.setCellType(CellType.STRING);
                } catch (NumberFormatException ex) {
                    log.error("Failed to parse SST index {}", rawCell.getRawValue(), ex);
                }
            }
            case NUMBER -> {
                if (!rawCell.getRawValue().isEmpty() && (rawCell.getFormatText() != null)) {
                    if (DateUtil.isADateFormat(rawCell.getFormatIndex(), rawCell.getFormatText()))
                        rawCell.setCellType(CellType.DATE);
                    else
                        rawCell.setCellType(CellType.NUMBER);
                } else
                    rawCell.setCellType(CellType.STRING);
            }
        }
    }

    private void parseCharacters(Cell rawCell, XMLEvent event) {
        Characters c = event.asCharacters();
        if (c.isCData()) {
            log.trace("Characters : (CDATA): {}", c.getData());
        } else if (c.isIgnorableWhiteSpace()) {
            log.info("Characters : (IGNORABLE SPACE)");
        } else if (c.isWhiteSpace()) {
            log.info("Characters : (EMPTY SPACE)");
        } else {
            rawCell.setRawValue(c.getData());
        }
    }

    private CellDataType getCurrentCellDataType(StartElement element) {
        String cellTypeByAttr = getAttributeValue(element, CELL_TYPE_ATTR_NAME);

        return (cellTypeByAttr != null) ? switch (cellTypeByAttr) {
            case "b" -> CellDataType.BOOLEAN;
            case "e" -> CellDataType.ERROR;
            case "inlineStr" -> CellDataType.INLINE_STRING;
            case "s" -> CellDataType.SST_STRING;
            case "str" -> CellDataType.FORMULA;
            default -> CellDataType.NUMBER;
        } : CellDataType.NUMBER;
    }

    private XSSFCellStyle getCurrentCellStyle(StartElement element) {
        if (this.styles != null) {
            String cellStyleAsStringRef = getAttributeValue(element, CELL_STYLE_REF_ATTR_NAME);
            if (cellStyleAsStringRef != null) {
                int styleIndex = Integer.parseInt(cellStyleAsStringRef);
                return this.styles.getStyleAt(styleIndex);
            } else if (this.styles.getNumCellStyles() > 0) {
                return this.styles.getStyleAt(0);
            }
        }

        return null;
    }
}
