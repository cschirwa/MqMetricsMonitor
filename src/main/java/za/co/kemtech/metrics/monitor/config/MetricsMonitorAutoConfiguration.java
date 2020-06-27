package za.co.kemtech.metrics.monitor.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import sun.plugin.services.BrowserService;

import java.util.List;

@Slf4j
@EnableScheduling
@Configuration
@EnableCaching
@EnableConfigurationProperties(MessageBrowserProperties.class)
public class MetricsMonitorAutoConfiguration {

    @Autowired
    private MessageBrowserProperties messageBrowserProperties;

    @Autowired
    private ConnectionRepository connectionRepository;

    @Autowired
    private TaskScheduler taskScheduler;

    @Autowired
    private BrowserService browserService;

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        final ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(5);
        threadPoolTaskScheduler.setThreadNamePrefix("ThreadPoolTaskScheduler");
        return threadPoolTaskScheduler;
    }

    @Bean
    public SmartInitializingSingleton initialize() {
        return this::doAtStartUp;
    }

    public void doAtStartUp() {
        if (connectionRepository.findAll().isEmpty()) {
            final List<ConnectionData> connections = messageBrowserProperties.getMonitored().stream().map(ConnectionData::new).collect(Collectors.toList());
            connections.forEach(connection -> {
                connectionRepository.addConnectionData(connection);
                if (!connection.getProperties().getMonitorFrequency().equalsIgnoreCase("none")) {
                    log.info("scheduling at {}", connection.getProperties().getMonitorFrequency());
                    taskScheduler.schedule(() -> browserService.fetchQueues(connection.getProperties().getHostname(), connection.getProperties().getQueueManager()), new CronTrigger(connection
                            .getProperties().getMonitorFrequency()));
                }
            });
        }

    }

}

