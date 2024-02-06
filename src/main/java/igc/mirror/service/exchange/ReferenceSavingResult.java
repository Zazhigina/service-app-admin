package igc.mirror.service.exchange;

import java.util.List;

public class ReferenceSavingResult {

    /**
     * Идентификатор сообщения
     **/
    private long exchangeId;

    /**
     * Статус сохранения
     **/
    private LogType result;

    /**
     * Журнал обработки записей
     **/
    private List<Log> logs;

//    public ReferenceSavingResult(long exchangeId) {
//        this.exchangeId = exchangeId;
//    }

    public long getExchangeId() {
        return exchangeId;
    }

    public void setExchangeId(long exchangeId) {
        this.exchangeId = exchangeId;
    }

    public LogType getResult() {
        return result;
    }

    public void setResult(LogType result) {
        this.result = result;
    }

    public List<Log> getLogs() {
        return logs;
    }

    public void setLogs(List<Log> logs) {
        this.logs = logs;
    }
}
