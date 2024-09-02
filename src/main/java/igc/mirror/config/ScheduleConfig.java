package igc.mirror.config;

import igc.mirror.task.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod="shutdown")
    public Executor taskExecutor() {
        return Executors.newScheduledThreadPool(6);
    }

    @Bean
    public RequestSearchHistoryTask requestSearchHistoryTask() {
        return new RequestSearchHistoryTask();
    }

    @Bean
    public EisContractsTask eisContractsTask() {
        return new EisContractsTask();
    }

    @Bean
    public CommercialOfferNotificationTask commercialOfferNotificationTask() {
        return new CommercialOfferNotificationTask();
    }

    @Bean
    public CommercialOfferStatusTask commercialOfferStatusTask() {
        return new CommercialOfferStatusTask();
    }

    @Bean
    public NotificationServiceTask notificationServiceTask () {
        return new NotificationServiceTask();
    }
//    @Bean
//    public UpdatePricingTask updatePricingTask() {
//        return new UpdatePricingTask();
//    }
}