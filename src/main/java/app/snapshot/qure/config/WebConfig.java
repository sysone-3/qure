package app.snapshot.qure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// 대상 경로를 /mobile/*/checklist 로만 한정하여 다른 팀원 페이지에 영향 없음.

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final InspectorAuthInterceptor inspectorAuthInterceptor;

    public WebConfig(InspectorAuthInterceptor inspectorAuthInterceptor) {
        this.inspectorAuthInterceptor = inspectorAuthInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(inspectorAuthInterceptor)
                .addPathPatterns("/mobile/*/checklist", "/mobile/*/checklist/**");
                // 다른 경로는 추가하지 않음.
    }
}
