
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@EnableWebMvc
@Configuration
@Import(MyViewConfig.class)
public class MyWebConfig {

    @Bean
    public FormLogControllerTest userController () {
        return new FormLogControllerTest();
    }

/*    @Bean
    public UserService userService(){
        return new InMemoryUserService();
    }*/
}