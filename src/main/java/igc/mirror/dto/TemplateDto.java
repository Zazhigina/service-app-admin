package igc.mirror.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.io.Resource;

import java.time.LocalDateTime;
import java.util.Map;

public class TemplateDto {
    private String title;
    @JsonIgnore
    private Resource resource;
    private String resourceName;
    private LocalDateTime resourceCreateDate;
    private Map<String, String> variables;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Resource getResource() {
        return resource;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public LocalDateTime getResourceCreateDate() {
        return resourceCreateDate;
    }

    public void setResourceCreateDate(LocalDateTime resourceCreateDate) {
        this.resourceCreateDate = resourceCreateDate;
    }

    public Map<String, String> getVariables() {
        return variables;
    }

    public void setVariables(Map<String, String> variables) {
        this.variables = variables;
    }
}
