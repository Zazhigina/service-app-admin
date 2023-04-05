package igc.mirror.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestResponseLoggingFilter implements Filter {
    static final String USER_AGENT_HEADER = "User-Agent";
    static final String X_REQUEST_ID_HEADER = "X-Request-ID";
    static final String USER_AGENT_KEY = "http_user_agent";
    static final String X_REQUEST_ID_KEY = "http_request_id";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        if (httpRequest.getHeader(USER_AGENT_HEADER) != null) {
            MDC.put(USER_AGENT_KEY, httpRequest.getHeader(USER_AGENT_HEADER));
        }

        if (httpRequest.getHeader(X_REQUEST_ID_HEADER) != null) {
            MDC.put(X_REQUEST_ID_KEY, httpRequest.getHeader(X_REQUEST_ID_HEADER));
        } else {
            MDC.put(X_REQUEST_ID_KEY, UUID.randomUUID().toString());
        }

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }

}
