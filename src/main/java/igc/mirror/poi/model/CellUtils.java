package igc.mirror.poi.model;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

public class CellUtils {

    private CellUtils() {
        throw new IllegalStateException("Utility class");
    }

    private static String getISO8601StringForDate(Date date) {
        String dateISO8601 = "yyyy-MM-dd'T'HH:mm:ss'Z'";
        DateFormat dateFormat = new SimpleDateFormat(dateISO8601, Locale.US);
        return dateFormat.format(date);
    }

    public static String getTextValue(Cell c) {
        return switch (c.getCellType()) {
            case NUMBER -> {
                CellNumber cn = ((CellNumber) c);
                if (cn.getFormattedValue() != null) {
                    yield cn.getFormattedValue();
                } else {
                    Double value = cn.getVal();
                    DecimalFormat formatter;
                    if (value - value.intValue() > 0.0)
                        formatter = new DecimalFormat("0.00");
                    else {
                        formatter = new DecimalFormat("#");
                        formatter.setMaximumFractionDigits(0);
                    }

                    yield formatter.format(value);
                }
            }
            case DATE -> getISO8601StringForDate(((CellDate) c).getVal());
            case STRING -> ((CellString) c).getVal();
            case BLANK -> null;
        };
    }

    public static Long getLongValue(Cell c) {
        return switch (c.getCellType()) {
            case NUMBER -> ((CellNumber) c).getVal().longValue();
            case DATE -> ((CellDate) c).getVal().getTime();
            case STRING -> Long.valueOf(((CellString) c).getVal());
            case BLANK -> null;
        };
    }

    public static Integer getIntValue(Cell c) {
        Integer value = null;
        switch (c.getCellType()) {
            case NUMBER:
                CellNumber cd = (CellNumber) c;
                value = cd.getVal().intValue();
                break;
            case DATE:
                break;
            case STRING:
                CellString cs = (CellString) c;
                value = Integer.valueOf(cs.getVal());
                break;
            default:
                break;
        }
        return value;
    }

    public static BigDecimal getBigDecimalValue(Cell c) {
        BigDecimal value = null;
        switch (c.getCellType()) {
            case NUMBER:
                CellNumber cd = (CellNumber) c;
                value = BigDecimal.valueOf(cd.getVal());
                break;
            case DATE:
                break;
            case STRING:
                CellString cs = (CellString) c;
                value = new BigDecimal(cs.getVal());
                break;
            default:
                break;
        }
        return value;
    }

    public static LocalDateTime getDateValue(Cell c) {
        LocalDateTime value = null;
        switch (c.getCellType()) {
            case NUMBER:
                break;
            case DATE:
                CellDate ct = (CellDate) c;
                value = ct.getVal().toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime();
                break;
            case STRING:
//                CellString cs = (CellString)c;
//                value = DateUtils.parseDate(cs.getVal(), "dd.mm.yyyy");
                break;
            default:
                break;
        }
        return value;
    }
}
