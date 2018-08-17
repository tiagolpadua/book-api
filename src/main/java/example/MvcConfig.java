package example;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

// https://springfox.github.io/springfox/docs/current/
// https://springframework.guru/spring-boot-restful-api-documentation-with-swagger-2/

@Configuration
@EnableSwagger2
public class MvcConfig implements WebMvcConfigurer {
}
