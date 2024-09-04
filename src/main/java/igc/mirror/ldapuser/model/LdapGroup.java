package igc.mirror.ldapuser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String distinguishedName;

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public void setDistinguishedName(String distinguishedName) { this.distinguishedName = distinguishedName; }
    public String getDistinguishedName() { return distinguishedName; }
}