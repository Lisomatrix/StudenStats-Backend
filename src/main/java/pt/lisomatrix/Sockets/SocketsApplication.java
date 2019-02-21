package pt.lisomatrix.Sockets;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import pt.lisomatrix.Sockets.models.Hour;
import pt.lisomatrix.Sockets.redis.models.RedisToken;
import pt.lisomatrix.Sockets.redis.repositories.RedisTokenRepository;
import pt.lisomatrix.Sockets.repositories.HoursRepository;
import pt.lisomatrix.Sockets.repositories.ModuleGradesRepository;
import pt.lisomatrix.Sockets.storage.FileStorageProperties;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
@EnableConfigurationProperties({
		FileStorageProperties.class
})
public class SocketsApplication {

    /***
     * Hours Repository to get info
     */
    @Autowired
    private HoursRepository hoursRepository;

    /***
     * Helper to generate Hours for Database from hours.json file in resources
     *
     */
    @PostConstruct
    public void generateHours() {

        try {

            List<Hour> currentHours = hoursRepository.findAll();

            if(currentHours.size() < 1) {

                ObjectMapper mapper = new ObjectMapper();

                List<Hour> hours = mapper.readValue(new ClassPathResource("hours.json").getFile(),
                        new TypeReference<List<Hour>>(){});

                hoursRepository.saveAll(hours);
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @Bean
    public MethodInvokingFactoryBean methodInvokingFactoryBean() {
        MethodInvokingFactoryBean methodInvokingFactoryBean = new MethodInvokingFactoryBean();
        methodInvokingFactoryBean.setTargetClass(SecurityContextHolder.class);
        methodInvokingFactoryBean.setTargetMethod("setStrategyName");
        methodInvokingFactoryBean.setArguments(new String[]{SecurityContextHolder.MODE_INHERITABLETHREADLOCAL});
        return methodInvokingFactoryBean;
    }

	public static void main(String[] args) {
		SpringApplication.run(SocketsApplication.class, args);
	}
}
