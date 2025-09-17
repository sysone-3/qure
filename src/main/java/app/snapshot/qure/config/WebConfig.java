package app.snapshot.qure.config;
//작성자 : 최온유

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InspectorAuthInterceptor inspectorAuthInterceptor;

    public WebConfig(InspectorAuthInterceptor inspectorAuthInterceptor) {
        this.inspectorAuthInterceptor = inspectorAuthInterceptor;
    }

    @Bean(name = "multipartResolver")
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(inspectorAuthInterceptor)
                .addPathPatterns("/mobile/*/checklist", "/mobile/*/checklist/**");
    }
}
