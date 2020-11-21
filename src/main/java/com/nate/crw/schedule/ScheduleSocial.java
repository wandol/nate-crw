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
public class ScheduleSocial implements SchedulerInterface {

    @Autowired
    private SrcRepository srcRepository;

    private ScheduledFuture<?> future;

    private TaskScheduler scheduler;
    
    @Autowired
    public ScheduleSocial(@Lazy TaskScheduler scheduler) {
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
                    /**
                     *  네이트 사회 홈 페이징 부분 하루치 기사 수집.
                     * @throws EmptySourceInfoException
                     * @throws CrwErrorException
                     */

                    log.info("NATE SOCIAL PAGING AREA schedule START =================================================================");
                    long beforeTime = System.currentTimeMillis();
                    crwMain.startSocPageCrw();
                    long afterTime = System.currentTimeMillis();
                    log.info("SOCIAL 소요 시간  :: {} 초" , (afterTime - beforeTime)/1000 );
                    log.info("NATE SOCIAL PAGING AREA schedule END =================================================================");

                } catch (EmptySourceInfoException e) {
                    e.printStackTrace();
                } catch (CrwErrorException e) {
                    e.printStackTrace();
                }
            }
        }, new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                log.info("SELECT SOCIAL SCHEDULE VALUE =================================================================");
                Source src  = srcRepository.findBySiteNmAndArticleCategoryAndUseYn(SiteName.NATE.name(), ArticleCate.SOCIAL.name(), "Y");
                if(src != null){
                    String cron = src.getCrwCycle();
                    log.info("update SOCIAL cron value :: {}" , cron);
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
