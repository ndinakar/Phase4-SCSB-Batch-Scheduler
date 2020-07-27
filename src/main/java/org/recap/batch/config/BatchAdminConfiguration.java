package org.recap.batch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.filter.ShallowEtagHeaderFilter;
import javax.servlet.Filter;

/**
 * Created by rajeshbabuk on 23/3/17.
 */

@Configuration
@ImportResource({"classpath*:/org/springframework/batch/admin/web/resources/servlet-config.xml",
                 "classpath*:/org/springframework/batch/admin/web/resources/webapp-config.xml",
                 "classpath*:/META-INF/integration-context.xml"})
public class BatchAdminConfiguration {
    @Bean
    public Filter shallowEtagHeaderFilter() {
        return new ShallowEtagHeaderFilter();
    }

    @Bean
    public Filter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}