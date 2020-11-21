package com.nate.crw.main;


import com.nate.crw.domain.Source;
import com.nate.crw.dto.ArticleArea;
import com.nate.crw.dto.ArticleCate;
import com.nate.crw.dto.ErrorDto;
import com.nate.crw.dto.SiteName;
import com.nate.crw.exception.CrwErrorException;
import com.nate.crw.exception.EmptySourceInfoException;
import com.nate.crw.module.HeadLineModule;
import com.nate.crw.module.PageModule;
import com.nate.crw.service.HeadLineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class CrwMain {

    @Autowired
    private HeadLineService headLineService;

    @Autowired
    private HeadLineModule headLineModule;

    @Autowired
    private PageModule pageModule;

    /**
     *  네이트 메인 홈 뉴스 3개 영역 수집.
     *  영역이 ajax으로 불러 오기에 2번 클릭 적용.
     *
     * @throws EmptySourceInfoException
     * @throws CrwErrorException
     */
    public void startHeadLineCrw() throws EmptySourceInfoException, CrwErrorException {

        //  TODO 수집원 설정 값 DB에서 가져오기
        Source src = headLineService.findBySiteNmAndArticleCategoryAndUseYn(SiteName.NATE.name(), ArticleCate.HEADLINE.name(),"Y");

        if (src == null) {
            throw new EmptySourceInfoException(ErrorDto.builder()
                    .errorArticleCate(ArticleCate.HEADLINE.name())
                    .errorMsg("수집원을 찾지 못했습니다.")
                    .errorSiteNm(SiteName.NATE.name())
                    .build());
        }
        log.info("NATE HEADLINE SOURCE INFO :: {}" , src);

        //  TODO 다음 뉴스 홈 헤드라인,정치,사회 영역 link 수집
        List<String> newsHomHeadLineLinkList = headLineModule.getHeadLineLink(src);
        newsHomHeadLineLinkList.forEach(url -> log.info("nate news home headline url list :: {}",url));

        //  todo 다음 뉴스 홈 urls 상세 기사 수집. 게시 시간 수집.
        headLineModule.getDetailArticle(src, newsHomHeadLineLinkList, true , ArticleArea.HOMEHEADLINE);

    }

    /**
     *  정치 홈 페이징 부분 수집.
     *
     * @throws EmptySourceInfoException
     * @throws CrwErrorException
     */
    public void startPolPageCrw() throws EmptySourceInfoException, CrwErrorException {

        //  TODO 수집원 설정 값 DB에서 가져오기음
        Source src = headLineService.findBySiteNmAndArticleCategoryAndUseYn(SiteName.NATE.name(), ArticleCate.POLITICS.name(), "Y");

        if (src == null) {
            throw new EmptySourceInfoException(ErrorDto.builder()
                    .errorArticleCate(ArticleCate.POLITICS.name())
                    .errorMsg("수집원을 찾지 못했습니다.")
                    .errorSiteNm(SiteName.NATE.name())
                    .build());
        }
        log.info("NATE POLITICS SOURCE INFO :: {}" , src.toString());

        //  todo 오늘 날짜에 생성된 기사만 페이징 처리하여 link 수집.
        List<String> urlList = pageModule.getPagingParamDateGetUrl(src);
        urlList.forEach(url -> log.info("nate politics home paging url list :: {} ",url));

        //  todo 해당 url list로 상세 기사 수집하여  중복체크 및 DB 저장.
        headLineModule.getDetailArticle(src, urlList, false, ArticleArea.POLPAGING);

    }

    /**
     *  사회 홈 페이징 부분 수집 .
     *
     * @throws EmptySourceInfoException
     * @throws CrwErrorException
     */
    public void startSocPageCrw() throws EmptySourceInfoException, CrwErrorException {

        //  TODO 수집원 설정 값 DB에서 가져오기
        Source src = headLineService.findBySiteNmAndArticleCategoryAndUseYn(SiteName.NATE.name(), ArticleCate.SOCIAL.name(), "Y");

        if (src == null) {
            throw new EmptySourceInfoException(ErrorDto.builder()
                    .errorArticleCate(ArticleCate.SOCIAL.name())
                    .errorMsg("수집원을 찾지 못했습니다.")
                    .errorSiteNm(SiteName.NATE.name())
                    .build());
        }
        log.info("NATE SOCIAL SOURCE INFO :: {}" , src.toString());

        //  todo 어제 날짜에 생성된 기사만 페이징 처리하여 link 수집.
        List<String> urlList = pageModule.getPagingParamDateGetUrl(src);
        urlList.forEach(url -> log.info("nate social home paging url list :: {} ",url));

        //  todo 해당 url list로 상세 기사 수집하여  중복체크 및 DB 저장.
        headLineModule.getDetailArticle(src, urlList, false, ArticleArea.SOCPAGING);
    }

    /**
     *  전체 칼럼. -> 속보  페이징 부분 수집 .
     *
     * @throws EmptySourceInfoException
     * @throws CrwErrorException
     */
    public void startOpiPageCrw() throws EmptySourceInfoException, CrwErrorException {

        //  TODO 수집원 설정 값 DB에서 가져오기
        Source src = headLineService.findBySiteNmAndArticleCategoryAndUseYn(SiteName.NATE.name(), ArticleCate.OPINION.name(), "Y");

        if (src == null) {
            throw new EmptySourceInfoException(ErrorDto.builder()
                    .errorArticleCate(ArticleCate.OPINION.name())
                    .errorMsg("수집원을 찾지 못했습니다.")
                    .errorSiteNm(SiteName.NATE.name())
                    .build());
        }
        log.info("NATE OPINION SOURCE INFO :: {}" , src.toString());

        //  todo 오피니언 속보 영역 페이징 부분 페이징 처리 하여 LINK 수집
        List<String> urlList = pageModule.getPagingParamDateGetUrl(src);
        urlList.forEach(url -> log.info("nate opinion paging url list :: {} ",url));

        //  todo 해당 url list로 상세 기사 수집하여  중복체크 및 DB 저장.
        headLineModule.getDetailArticle(src, urlList, false, ArticleArea.OPINIONPAGING);
    }
}
