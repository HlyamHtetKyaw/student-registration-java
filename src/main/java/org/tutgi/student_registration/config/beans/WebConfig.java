package org.tutgi.student_registration.config.beans;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve all files under upload/** (including subfolders)
        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:upload/");
    }
}

