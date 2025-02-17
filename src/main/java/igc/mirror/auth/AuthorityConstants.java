package igc.mirror.auth;

public class AuthorityConstants {
    /**
     * Базовая роль
     */
    public static final String ROLE_BASE = "ROLE_BASE";
    /**
     * Конфигурационные параметры чтение
     */
    public static final String CONFIG_VALUE_READ = "CONFIG_VALUE.READ";

    /**
     * Конфигурационные параметры изменение
     */
    public static final String CONFIG_VALUE_CHANGE = "CONFIG_VALUE.CHANGE";

    public static class PreAuthorize {
        public static final String CONFIG_VALUE_READ = "hasAuthority('CONFIG_VALUE.READ')";
        public static final String CONFIG_VALUE_CHANGE = "hasAuthority('CONFIG_VALUE.CHANGE')";
        private PreAuthorize(){}
    }

    private AuthorityConstants(){}
}
