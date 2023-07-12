package igc.mirror.template.dto;

import java.util.Map;

public class TemplateDto {

    /**
     * Заголовок шаблона
     */
    private String title;

    /**
     * Карта переменных шаблона со значениями по умолчанию
     */
    private Map<String, String> variables;

    /**
     * Информация о файле шаблона
     */
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
