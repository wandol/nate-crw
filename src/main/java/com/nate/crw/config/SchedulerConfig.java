package com.nate.crw.config;

import com.nate.crw.dto.ArticleCate;
import com.nate.crw.schedule.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
public class SchedulerConfig{

    private static Map<ArticleCate, SchedulerInterface> schduledJobsMap = new HashMap<>();

    @Value("${thread.pool.size}")
    private int POOL_SIZE;

    @Autowired
    private ScheduleHeadLine scheduleHeadLine;

    @Autowired
    private ScheduleOpinion scheduleOpinion;

    @Autowired
    private SchedulePolitics schedulePolitics;

    @Autowired
    private ScheduleSocial scheduleSocial;


    /**
     *  스프링 기동시 스케줄 실행.
     */
    @PostConstruct
    public void initScheduler() {
        schduledJobsMap.put(ArticleCate.HEADLINE, scheduleHeadLine);
        schduledJobsMap.put(ArticleCate.POLITICS, schedulePolitics);
        schduledJobsMap.put(ArticleCate.SOCIAL, scheduleSocial);
        schduledJobsMap.put(ArticleCate.OPINION, scheduleOpinion);
        startAll();
    }

    public void startAll() {
        for (SchedulerInterface schedulerObjectInterface : schduledJobsMap.values()) {
            schedulerObjectInterface.start();
        }
    }
    public void restart(String job) {
        stop(job);
        start(job);
    }

    public void stop(String job) {
        schduledJobsMap.get(job).stop();
    }

    public void start(String job) {
        schduledJobsMap.get(job).start();
    }

    /**
     *  스케줄 thread pool 개수 지정.
     */
    @Bean
    public TaskScheduler scheduler() {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(POOL_SIZE);
        threadPoolTaskScheduler.setThreadNamePrefix("SCHEDULE-T-");
        return threadPoolTaskScheduler;
    }
}