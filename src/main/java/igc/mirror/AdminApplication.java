package igc.mirror;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class AdminApplication extends SpringBootServletInitializer {
    static final Logger logger = LoggerFactory.getLogger(AdminApplication.class);
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        logger.info("Run from servlet");
        return super.configure(builder);
    }

    public static void main(String[] args) {
        logger.info("Run from main");
        SpringApplication.run(AdminApplication.class, args);
    }
}
