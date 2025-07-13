package cafe.ideation;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class IdeationApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(IdeationApplication.class)
                .headless(false)
                .run(args);
    }

}
