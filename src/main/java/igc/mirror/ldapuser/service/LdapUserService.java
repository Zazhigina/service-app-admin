package igc.mirror.ldapuser.service;

import igc.mirror.auth.UserDetails;
import igc.mirror.ldapuser.model.LdapGroup;
import igc.mirror.ldapuser.model.LdapUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.OrFilter;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchControls;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

@Service
@Validated
public class LdapUserService {
    static final Logger logger = LoggerFactory.getLogger(LdapUserService.class);
    @Autowired
    UserDetails userDetails;

    @Value("${mirror.ldap.url}")
    private String ldapUrl;
    @Value("${mirror.ldap.base}")
    private String ldapBase;
    @Value("${mirror.ldap.filter}")
    private String ldapFilter;
    @Value("${mirror.ldap.username}")
    private String ldapUsername;
    @Value("${mirror.ldap.password}")
    private String ldapPassword;
    @Value("${mirror.ldap.svc-users}")
    private String[] svcUsers;

    public LdapUser getLdapUser(String username) {
        logger.info("Получение ролей пользователя {}, запущено пользователем {}", username, userDetails.getUsername());

        LdapUser result = null;

        try {
            LdapTemplate ldapTemplate = ldapTemplate();

            var controls = new SearchControls();
            controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
            controls.setReturningAttributes(new String[]{ "sAMAccountName", "cn", "memberOf", "description", "accountexpires", "useraccountcontrol" });

            AndFilter filter = new AndFilter();
            filter.and(new EqualsFilter("objectclass", "person"));
            filter.and(new EqualsFilter("sAMAccountName", username));
            if(!ldapFilter.isEmpty()) {
                filter.and(new EqualsFilter("memberOf", ldapFilter));
            }

            List<LdapUser> results = ldapTemplate.search("", filter.encode(), controls, new LdapUserAttributesMapper());
            if(!results.isEmpty()) {
                result = results.get(0);

                var groups = result.getMemberof();
                for (int i = 0; i < groups.size(); i++) {
                    var currentGroup = groups.get(i);
                    try {
                        var group = ldapTemplate.lookup("CN=" + currentGroup.getName(), new LdapGroupAttributesMapper());
                        currentGroup.setName(group.getName());
                        currentGroup.setDescription(group.getDescription());
                    }
                    catch (Exception e) {
                    }
                }
            }

        } catch (Exception e) {
            logger.info("Ошибка при получении ролей пользователя {}: {}", username, e.getMessage());
            throw new RuntimeException(e);
        }
        return result;
    }

    public List<LdapUser> getLdapUserSvc() {
        logger.info("Получение ролей СУЗ, запущено пользователем {}", userDetails.getUsername());

        List<LdapUser> ldapUsers = new ArrayList<>();

        if(svcUsers.length > 0) {
            try {
                LdapTemplate ldapTemplate = ldapTemplate();

                var controls = new SearchControls();
                controls.setSearchScope(SearchControls.SUBTREE_SCOPE);
                controls.setReturningAttributes(new String[]{"sAMAccountName", "cn", "memberOf", "description", "accountexpires", "useraccountcontrol"});

                AndFilter filter = new AndFilter();
                filter.and(new EqualsFilter("objectclass", "person"));
                OrFilter svc_names = new OrFilter();
                for (int i = 0; i < svcUsers.length; i++) {
                    svc_names.or(new EqualsFilter("sAMAccountName", svcUsers[i]));
                }
                filter.and(svc_names);
                if(!ldapFilter.isEmpty()) {
                    filter.and(new EqualsFilter("memberOf", ldapFilter));
                }

                var users = ldapTemplate.search("", filter.encode(), controls, new LdapUserAttributesMapper());
                ldapUsers.addAll(users);

            } catch (Exception e) {
                logger.info("Ошибка при получении ролей СУЗ: {}", e.getMessage());
                throw new RuntimeException(e);
            }
        }
        return ldapUsers;
    }

    public LdapContextSource getContextSource() throws Exception{
        LdapContextSource ldapContextSource = new LdapContextSource();
        ldapContextSource.setUrl(ldapUrl);
        ldapContextSource.setBase(ldapBase);
        ldapContextSource.setUserDn(ldapUsername);
        ldapContextSource.setPassword(ldapPassword);
        ldapContextSource.afterPropertiesSet();
        return ldapContextSource;
    }

    public LdapTemplate ldapTemplate() throws Exception{
        LdapTemplate ldapTemplate = new LdapTemplate(getContextSource());
        ldapTemplate.setIgnorePartialResultException(true);
        return ldapTemplate;
    }

    private static class LdapUserAttributesMapper implements AttributesMapper<LdapUser> {
        public LdapUser mapFromAttributes(Attributes attrs) throws NamingException {
            LdapUser ldapUser = new LdapUser();
            ldapUser.setLogin((String)attrs.get("samaccountname").get());
            ldapUser.setFullName((String)attrs.get("cn").get());

            if(attrs.get("description") != null) {
                ldapUser.setDescription((String)attrs.get("description").get());
            }
            if(attrs.get("accountexpires") != null) {
                long adDate = Long.parseLong((String)attrs.get("accountexpires").get());
                long milliseconds = (adDate / 10000L) -  11644473600000L;
                ldapUser.setAccountExpires(new Date(milliseconds).toInstant()
                        .atZone(ZoneId.systemDefault())
                        .toLocalDateTime());
            }
            if(attrs.get("useraccountcontrol") != null) {
                ldapUser.setIsDisabled(((String)attrs.get("useraccountcontrol").get()).equals("514"));
            }

            List<LdapGroup> memberOf = new ArrayList<>();
            if(attrs.get("memberOf") != null) {
                for (Enumeration vals = attrs.get("memberOf").getAll(); vals.hasMoreElements(); ) {
                    String groupName = (String) vals.nextElement();
                    if (groupName.toLowerCase().startsWith("cn=")) {
                        groupName = groupName.substring(3);
                    }
                    if (groupName.toLowerCase().startsWith("mirror")) {
                        groupName = groupName.substring(0, groupName.indexOf(","));
                        LdapGroup group = new LdapGroup();
                        group.setName(groupName);
                        memberOf.add(group);
                    }
                }
            }
            ldapUser.setMemberof(memberOf);
            return ldapUser;
        }
    }

    private static class LdapGroupAttributesMapper implements AttributesMapper<LdapGroup> {
        public LdapGroup mapFromAttributes(Attributes attrs) throws NamingException {
            LdapGroup ldapGroup = new LdapGroup();
            ldapGroup.setName((String)attrs.get("cn").get());
            if(attrs.get("description") != null) {
                ldapGroup.setDescription((String)attrs.get("description").get());
            }
            return ldapGroup;
        }
    }
}