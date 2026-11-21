package br.edu.atitus.productapi.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.nio.file.Paths;

@Configuration
public class ImageResourceConfig implements WebMvcConfigurer {
    @Value("${app.images.location:/data/images}")
    private String imageLocation;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        String location = Paths.get(imageLocation).toAbsolutePath().normalize().toUri().toString();
        registry.addResourceHandler("/images/**").addResourceLocations(location);
    }
}
