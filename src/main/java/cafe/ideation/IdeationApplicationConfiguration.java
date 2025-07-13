package cafe.ideation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.rebelcraft.cli.App;

@Configuration
public class IdeationApplicationConfiguration {

    @Bean
    App ideationApp(@Value("${spring.application.name}") String applicationName) {
        return new App.Builder()
                .appName(applicationName)
                .withHomeDirectory()
                .build();
    }

}
