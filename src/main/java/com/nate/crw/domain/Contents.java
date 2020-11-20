package com.nate.crw.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Slf4j
@Table(name = "potal_contents_tb")
@NoArgsConstructor
public class Contents {

    @Id
    @GeneratedValue
    private long id;

    //  기사 게시한 사이트 명
    //  Naver, Daum, Nate, Media
    private String siteNm;

    //  기사 카테고리
    //  Headline, Politics, Social, Opinion
    private String srcType;

    //  기사 카테고리
    private String articleCategory;

    //  기사 작성한 언론사 명
    private String articleMediaNm;

    //   기사 제목
    private String articleTitle;

    //  성새 기사 내용
    @Lob
    private String articleContents;

    //   기사 작성자
    private String articleWriter;

    //   기사 url
    private String articleUrl;

    //   기사 이미지 내용
    @Column(length = 5000)
    private String articleImgCaption;

    //   기사 작성일
    private LocalDateTime articleWriteDt;

    //  기사 pk ( url,제목 MD5 )
    private String articlePk;

    //  기사 게시 시작 시간.
    private LocalDateTime articlePostStartDt;

    //  기사 게시 끝 시간.
    private LocalDateTime articlePostEndDt;

    //  기사 수집한 시간
    @CreatedDate
    private LocalDateTime articleCrwDt;

    //  컨텐츠 업데이트 시간.
    @LastModifiedDate
    private LocalDateTime upDt;

    //  기사 삭제여부
    private String delYn ;


    @Builder
    public Contents(String siteNm,String srcType, String articleCategory, String articleMediaNm, String articleTitle, String articleContents, String articleWriter, String articleUrl,
                    String articleImgCaption, LocalDateTime articleWriteDt, String articlePk, LocalDateTime articlePostStartDt, LocalDateTime articlePostEndDt,LocalDateTime articleCrwDt, LocalDateTime upDt, String delYn) {
        this.siteNm = siteNm;
        this.srcType = srcType;
        this.articleCategory = articleCategory;
        this.articleMediaNm = articleMediaNm;
        this.articleTitle = articleTitle;
        this.articleContents = articleContents;
        this.articleWriter = articleWriter;
        this.articleUrl = articleUrl;
        this.articleImgCaption = articleImgCaption;
        this.articleWriteDt = articleWriteDt;
        this.articlePk = articlePk;
        this.articlePostStartDt = articlePostStartDt;
        this.articlePostEndDt = articlePostEndDt;
        this.articleCrwDt = articleCrwDt;
        this.upDt = upDt;
        this.delYn = delYn;
    }
}
