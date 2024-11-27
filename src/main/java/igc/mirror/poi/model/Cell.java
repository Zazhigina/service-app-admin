package igc.mirror.poi.model;

import igc.mirror.poi.view.CellType;

public interface Cell {
    CellType getCellType();
    int getNum();
    void printValue();
}
