package igc.mirror.poi.parser.model;

import igc.mirror.poi.parser.ref.CellDataType;
import igc.mirror.poi.view.CellType;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Cell {
    private String rawValue;
    private String referenceText;
    private CellDataType dataType;
    private int formatIndex;
    private String formatText;
    private CellType cellType;
}
