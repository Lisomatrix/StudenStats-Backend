package pt.lisomatrix.Sockets.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig extends AsyncConfigurerSupport {

    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        /*executor.setCorePoolSize(10);
        executor.setMaxPoolSize(25);
        executor.setQueueCapacity(500);*/
        executor.setCorePoolSize(200);
        executor.setMaxPoolSize(5000);
        executor.setThreadGroupName("taskExecutor");
        executor.setThreadNamePrefix("asyncRequest");
        executor.initialize();

        return executor;
    }
}