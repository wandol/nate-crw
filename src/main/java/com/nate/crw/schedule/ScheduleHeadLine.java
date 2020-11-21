package com.nate.crw.schedule;

import com.nate.crw.domain.Source;
import com.nate.crw.dto.ArticleCate;
import com.nate.crw.dto.SiteName;
import com.nate.crw.exception.CrwErrorException;
import com.nate.crw.exception.EmptySourceInfoException;
import com.nate.crw.main.CrwMain;
import com.nate.crw.repository.SrcRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ScheduledFuture;

/**
 * User: wandol<br/>
 * Date: 2020/11/10<br/>
 * Time: 9:08 오후<br/>
 * Desc:    스케줄이 실행 될때 실행될 메소드 설정 및 다음 스케줄 주기 변경된 부분 체크 및 적용.
 */
@Component
@Slf4j
public class ScheduleHeadLine implements SchedulerInterface {

    @Autowired
    private SrcRepository srcRepository;

    private ScheduledFuture<?> future;

    private TaskScheduler scheduler;
    
    @Autowired
    public ScheduleHeadLine(@Lazy TaskScheduler scheduler) {
        this.scheduler = scheduler;
    }
    
    @Autowired
    private CrwMain crwMain;

    @Override
    public void start() {
        future = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("NATE NEWS HOME HEDLINE AREA schedule START =================================================================");
                    long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
                    crwMain.startHeadLineCrw();
                    long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
                    log.info("HEADLINE 소요 시간  :: {} 초" , (afterTime - beforeTime)/1000 );
                    log.info("NATE NEWS HOME HEDLINE AREA schedule END =================================================================");
                } catch (EmptySourceInfoException e) {
                    e.printStackTrace();
                } catch (CrwErrorException e) {
                    e.printStackTrace();
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                log.info("SELECT HEADLINE SCHEDULE VALUE =================================================================");
                Source src  = srcRepository.findBySiteNmAndArticleCategoryAndUseYn(SiteName.NATE.name(), ArticleCate.HEADLINE.name(), "Y");
                if(src != null){
                    String cron = src.getCrwCycle();
                    log.info("update HEADLINE cron value :: {}" , cron);
                    log.info("========================================================================================");
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }else{
                    log.info("========================================================================================");
                    return null;
                }
            }
        });
    }

    @Override
    public void stop() {
        future.cancel(false);
    }


}
