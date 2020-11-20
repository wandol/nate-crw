package com.nate.crw.schedule;

/**
 * User: wandol<br/>
 * Date: 2020/11/10<br/>
 * Time: 9:07 오후<br/>
 * Desc:    스케줄 주기를 DB 값으로 동적으로 변경하기 위해.
 */
public interface SchedulerInterface {
    void start();
    void stop();
}
