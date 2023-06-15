package igc.mirror.dto;

import java.util.Map;

public class TemplateDto {
    private String title;
    private Map<String, String> variables;
    private FileDto templateBody;

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

    public FileDto getTemplateBody() {
        return templateBody;
    }

    public void setTemplateBody(FileDto templateBody) {
        this.templateBody = templateBody;
    }
}
