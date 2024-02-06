package igc.mirror.service.exchange;

public class Log {
    public static final String INSERT_ERROR_TEXT = "Ошибка при добавлении данных: ";
    public static final String UPDATE_ERROR_TEXT = "Ошибка при обновлении данных: ";
    public static final String RECORD_WITH_CODE_TEXT = "Запись c кодом ";
    public static final String RECORD_OBSOLETE_TEXT = " устарела";
    public static final String RECORDS_RECEIVED_TEXT = "Получено записей: ";
    public static final String RECORDS_SAVED_TEXT = ". Сохранено записей: ";
    public static final String RECORDS_DELETED_TEXT = ". Дополнительно: отмечено на удаление записей: ";
    public static final String RECORDS_LIST_EMPTY_TEXT = "Отсутствуют записи для обработки";
    public static final String RECORDS_INVALID_TEXT = "Некорректная запись (код/наименование не указаны): ";

    /**
     * Тип сообщения
     **/
    private LogType logType;

    /**
     * Текст сообщения
     **/
    private String logText;

    public Log(LogType logType, String logText) {
        this.logType = logType;
        this.logText = logText;
    }

    public LogType getLogType() {
        return logType;
    }

    public void setLogType(LogType logType) {
        this.logType = logType;
    }

    public String getLogText() {
        return logText;
    }

    public void setLogText(String logText) {
        this.logText = logText;
    }
}
