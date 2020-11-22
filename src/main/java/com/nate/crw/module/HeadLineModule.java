package com.nate.crw.module;

import com.nate.crw.domain.Contents;
import com.nate.crw.domain.Source;
import com.nate.crw.dto.ArticleArea;
import com.nate.crw.dto.ErrorDto;
import com.nate.crw.dto.SiteName;
import com.nate.crw.exception.CrwErrorException;
import com.nate.crw.service.HeadLineService;
import com.nate.crw.util.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Component
public class HeadLineModule {

    @Value("${chrome.driver.path}")
    private String chromeDirverPath;

    @Value("${article.write.date.default}")
    private String articleWriteDateDefault;

    @Autowired
    private HeadLineService headLineService;

    /**
     *
     *  네이트 홈 에 있는 뉴스 영역 3가지 수집.
     *  탭 클릭 이벤트 추가.
     *  각 탭별로 link 수집.
     *
     * @param src
     * @return
     * @throws CrwErrorException
     */
    public List<String> getHeadLineLink(Source src) throws CrwErrorException {

        List<String> result = new ArrayList<>();
        //	크롬 드라이버 설정.
        System.setProperty("webdriver.chrome.driver", chromeDirverPath);
        // 크롬 브라우져 열기.
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");

        WebDriver driver = new ChromeDriver(options);
        driver.navigate().to(src.getStartUrl());

        try {

            WebElement tabCnt = driver.findElement(By.xpath("//*[@id='newsMoreArea']/em"));
            log.info("tabCnt :: {}", tabCnt.getText());

            if("2".equals(tabCnt.getText())){
                driver.findElement(By.xpath("//*[@id='newsOptBtn']/button[2]")).click();
                Thread.sleep(1000);
            }else if("3".equals(tabCnt.getText())){
                driver.findElement(By.xpath("//*[@id='newsOptBtn']/button[2]")).click();
                Thread.sleep(1000);
                driver.findElement(By.xpath("//*[@id='newsOptBtn']/button[2]")).click();
                Thread.sleep(1000);
            }

            WebElement startCnt = driver.findElement(By.xpath("//*[@id='newsMoreArea']/em"));
            log.info("startCnt :: {}", startCnt.getText());

            List<WebElement> linkList = driver.findElements(By.xpath(src.getHomeHeadlineListLinkXpth()));
            linkList.forEach(v -> result.add(v.getAttribute("href")));

            driver.findElement(By.xpath("//*[@id='newsOptBtn']/button[3]")).click();
            Thread.sleep(1000);

            List<WebElement> linkList2 = driver.findElements(By.xpath(src.getHomeHeadlineListLinkXpth()));
            linkList2.forEach(v -> result.add(v.getAttribute("href")));

            driver.findElement(By.xpath("//*[@id='newsOptBtn']/button[3]")).click();
            Thread.sleep(1000);

            List<WebElement> linkList3 = driver.findElements(By.xpath(src.getHomeHeadlineListLinkXpth()));
            linkList3.forEach(v -> result.add(v.getAttribute("href")));

        } catch (Exception e) {
            e.printStackTrace();
            driver.quit();
            throw new CrwErrorException(ErrorDto.builder()
                    .errorMsg(e.getMessage())
                    .errorArticleCate(src.getArticleCategory())
                    .errorSiteNm(SiteName.NATE.name()).build());
        }finally {
            driver.quit();
        }

        return result;
    }

    /**
     *
     *  url를 받아 기사 상세 수집.
     *
     * @param src
     * @param list
     * @param postFlag
     * @param area
     * @throws CrwErrorException
     */
    public void getDetailArticle(Source src, List<String> list, boolean postFlag, ArticleArea area) throws CrwErrorException {

        List<Contents> result = new ArrayList<>();
        List<String> pks = new ArrayList<>();
        Document doc;

        try {
            for (String url : list) {
                doc = Jsoup.connect(url).get();

                Element checkContNull = doc.getElementById(src.getArticleContXpth());
                String imgCont = new CommonUtil().getImgCont(doc,src.getArticleImgContXpth());
                //  내용이 널이 아닌 데이터 수집.
                //  헤드라인에 정치,사회 아닌 연예,사진,동영상 컨텐츠도 같이 있음.
                if(checkContNull != null){
                    //  PK 구하기
                    String pk_v = new CommonUtil().getEncMD5(url + doc.getElementsByAttributeValue("property", src.getArticleTitleXpth() ).attr("content"));

                    //  pk 리스트
                    //  게시 되어 있는지 안되어 있는지 판단하는 기준이 되는 pk를 list로 가지고 있는다.
                    pks.add(pk_v);

                    //  중복체크 및 게시 시간을 구하기 위해. 해당 pk로 먼저 수집원 데이터 체크.
                    Contents contents = headLineService.findFirstBySiteNmAndArticlePkAndDelYn(SiteName.NATE.toString(), pk_v,"N");

                    //  중복된 contents는 담지 않음.
                    if(contents == null){
                        Contents cont = Contents.builder()
                                .articleCategory(doc.select(src.getArticleCateXpth()).text())
                                .articleContents(doc.getElementById(src.getArticleContXpth()).text())
                                .articleImgCaption(imgCont)
                                .articleMediaNm(doc.select(src.getArticleMediaNmXpth()).text())
                                // 해당 제목과 url의 텍스트를 합쳐서 md5를 구하고 pk로 함.
                                .articlePk(pk_v)
                                .articleTitle(doc.getElementsByAttributeValue("property",src.getArticleTitleXpth()).attr("content"))
                                .articleUrl(url)
                                .articleWriteDt(new CommonUtil().checkDate(doc.select(src.getArticleWriteDtXpth()).text(),"yyyy-MM-dd HH:mm"))
                                .articleWriter(doc.select(src.getArticleWriterXpth()).text())
                                .siteNm(SiteName.NATE.name())
                                .srcType(area.name())
                                .delYn("N")
                                .articlePostStartDt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                                .articleCrwDt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                                .upDt(LocalDateTime.now(ZoneId.of("Asia/Seoul")))
                                .build();

                        result.add(cont);
                    }
                }
            }

            //  PK list
            pks.forEach(log::debug);

            //  상세 기사 데이터 DB 저장.
            if(result.size() > 0){
                List<Contents> saveCont = headLineService.saveAll(result);
                saveCont.forEach(vo -> log.debug("get Detail article save data :: {}" ,vo.toString()));
            }

            //  todo 해당 기사 게시 체크 ( 게시 되어 있는지 내려갔는지 체크 하는 로직. )
            //  한 헤드라인 수집 사이클에서 가져온 pks 들을 가지고  기존 contents_tb 에서  not in  pks   ..  and post_end_dt 가 null 인 데이터에
            //  post_end_dt 칼럼에 현재시각을 update 한다.
            if(postFlag && pks.size() > 0 ){
                int updatePostEndDt  = headLineService.updatePostEndDt(pks,ArticleArea.HOMEHEADLINE.name(),SiteName.NATE.name());
                log.info("update count end date :: {}" , updatePostEndDt);
            }

            //  정상완료되면 해당 수집원에 정상 처리 update
            headLineService.updateSuccessComplate(SiteName.NATE.name(),src.getArticleCategory());

        } catch (Exception e) {
            e.printStackTrace();
            throw new CrwErrorException(ErrorDto.builder()
                    .errorMsg(e.getMessage())
                    .errorArticleCate(src.getArticleCategory())
                    .errorSiteNm(SiteName.NATE.name()).build());
        }
    }
}
