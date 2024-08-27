package igc.mirror.ldapuser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LdapGroup {
    private String name;
    private String description;

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public String getName() { return name; }
    public String getDescription() { return description; }
}