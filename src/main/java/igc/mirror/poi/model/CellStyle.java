package igc.mirror.poi.model;

import lombok.Data;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;

@Data
public class CellStyle {
    private String font;
    private short fontSize;
    private String color;
    private String bgColor;
    private boolean bold;
    private boolean italic;
    private boolean underline;
    private String 	format;
    private boolean wrapText;
    private Integer identSpace;
    private HorizontalAlignment hAlign;
    private VerticalAlignment vAlign;
}
