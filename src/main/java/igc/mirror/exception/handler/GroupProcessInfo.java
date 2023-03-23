package igc.mirror.exception.handler;

import java.util.List;

public class GroupProcessInfo {
    private String processName;
    private int successfulResults;
    private List<ExceptionInfo> exceptions;

    public GroupProcessInfo(String processName, int successfulResults, List<ExceptionInfo> exceptions) {
        this.processName = processName;
        this.successfulResults = successfulResults;
        this.exceptions = exceptions;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public int getSuccessfulResults() {
        return successfulResults;
    }

    public void setSuccessfulResults(int successfulResults) {
        this.successfulResults = successfulResults;
    }

    public List<ExceptionInfo> getExceptions() {
        return exceptions;
    }

    public void setExceptions(List<ExceptionInfo> exceptions) {
        this.exceptions = exceptions;
    }
}
