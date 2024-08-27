package igc.mirror.ldapuser.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LdapUser {
    private String login;
    private String fullName;
    private String description;
    private LocalDateTime accountExpires;
    private boolean isDisabled;
    private List<LdapGroup> memberof;

    public void setLogin(String login) { this.login = login; }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public void setDescription(String description) { this.description = description; }
    public void setIsDisabled(boolean isDisabled) { this.isDisabled = isDisabled; }
    public void setAccountExpires(LocalDateTime accountExpires) { this.accountExpires = accountExpires; }
    public void setMemberof(List<LdapGroup> memberof) { this.memberof = memberof; }
    public List<LdapGroup> getMemberof() { return memberof; }
}
