package igc.mirror.template.dto;

import java.util.Map;

public class TemplateDto {
    private String title;
    private Map<String, String> variables;
    private FileInfoDto templateFileInfo;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }

    public FileInfoDto getTemplateFileInfo() {
        return templateFileInfo;
    }

    public void setTemplateFileInfo(FileInfoDto templateFileInfo) {
        this.templateFileInfo = templateFileInfo;
    }
}
