package igc.mirror.utils.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.config.SpringDataWebConfiguration;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WebServiceUtil {

    @Autowired
    private SpringDataWebProperties webProperties;
    @Autowired
    SpringDataWebConfiguration webConfiguration;

    public  String buildUriByPageableProperties(String uri, Pageable pageable) {

        UriComponentsBuilder urlBuilder = UriComponentsBuilder.fromUriString(uri);
        if (pageable != null) {
            if (pageable.isPaged()) {
                urlBuilder
                        .queryParam(webProperties.getPageable().getPageParameter(), pageable.getPageNumber())
                        .queryParam(webProperties.getPageable().getSizeParameter(), pageable.getPageSize());
            }
            if (pageable.getSort() != null)
                urlBuilder
                        .queryParam(webProperties.getSort().getSortParameter(), pageable.getSort().stream()
                                .map(order -> order.getProperty() + webConfiguration.sortResolver().getPropertyDelimiter() + order.getDirection().name().toLowerCase())
                                .toArray());
        }

        return urlBuilder.encode().toUriString();
    }
}
