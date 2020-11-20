package com.nate.crw.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Data
@Slf4j
@Entity
@Table(name = "potal_source_tb")
public class Source {

    @Id
    @GeneratedValue
    private int id;

    //  naver, daum, nate
    private String siteNm;

    //   politics, social, opinion
    private String articleCategory;

    //  수집주기. 크론탭으로 표시.
    private String crwCycle;

    //	수집 스케줄 실행 여부.
    private String useYn = "Y";

    //  현재 수집 상태   DONE, ERROR, NOTHING
    private String crwStatus;

    //  현재 수집 상태 메세지
    private String crwStatusMsg;

    //  작성일
    private LocalDateTime regDt;

    //  수정일
    private LocalDateTime upDt;

    //  시작 URL
    private String startUrl;

    // 뉴스 홈 헤드라인 영역 이미지 링크 xpath 필요 없으면 null
    private String homeHeadlineImgLinkXpth;

    // 뉴스 홈 헤드라인 영역 목록 링크 xpath 필요 없으면 null
    private String homeHeadlineListLinkXpth;

    //  뉴스 홈 정치 영역 이미지 링크 xpath 필요 없으면 null
    private String homePoliticsImgLinkXpth;

    //  뉴스 홈 정치 영역 목록 링크 xpath 필요 없으면 null
    private String homePoliticsListLinkXpth;

    //  뉴스 홈 사회 영역 이미지 링크 xpath 필요 없으면 null
    private String homeSocialImgLinkXpth;

    //  뉴스 홈 사회 영역 목록 링크 xpath 필요 없으면 null
    private String homeSocialListLinkXpth;

    //  정치,사회,칼럼 카테고리 헤드라인 목록 xpath  필요 없으면 null
    private String cateHeadlineListXpath;

    //  정치,사회,칼럼 카테고리 paging 목록 xpath 필요 없으면 null
    private String catePagingListXpath;

    //  정치,사회,칼럼 카테고리 paging 목록 날짜 xpath 필요 없으면 null
    private String catePagingListDateXpath;

    //  기사 상세 관련

    //  기사 상세 카데고리 xpath
    private String articleCateXpth;

    //  기사 상세 카데고리 xpath
    private String articleMediaNmXpth;

    //  기사 상세 제목 xpath
    private String articleTitleXpth;

    //  기사 상세 내용 xpath
    private String articleContXpth;

    // 	기사 이미지 내용 xpath
    private String articleImgContXpth;

    // 	기사 작성자 xpath
    private String articleWriterXpth;

    //  기사 작성일 xpath
    private String articleWriteDtXpth;

}
