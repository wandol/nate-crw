package com.nate.crw.module;

import com.nate.crw.domain.Source;
import com.nate.crw.dto.ErrorDto;
import com.nate.crw.dto.SiteName;
import com.nate.crw.exception.CrwErrorException;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * User: wandol<br/>
 * Date: 2020/10/30<br/>
 * Time: 6:54 오후<br/>
 * Desc:    페이징 으로 된 수집원 수집하는 모듈.
 */
@Slf4j
@Component
public class PageModule {

    @Value("${chrome.driver.path}")
    private String chromeDirverPath;

    @Value("${article.write.date.default}")
    private String articleWriteDateDefault;

    @Value("${crw.waiting.sec}")
    private int crwWaitingSec;

    public List<String> getPagingParamDateGetUrl(Source src) throws CrwErrorException {

        List<String> result = new ArrayList<>();
        System.setProperty("webdriver.chrome.driver", chromeDirverPath );
        
        ChromeOptions options = new ChromeOptions();
        options.addArguments("headless");
        
        WebDriver wb = null;

        //  어제 날짜 'yyyyMMdd' 형식
        String yesterday = LocalDate.now(ZoneId.of("Asia/Seoul")).minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String addDateParam  = "&date=" + yesterday;
        String crwUrl = src.getStartUrl();

        //  어제 날짜를 구해 'yyyymmdd' url 파라미터로 추가한다.
        try {
            //  today에 해당 되는 것만 수집하기 위한 flag
            boolean stopFlag = true;
            int page = 1;

            while(stopFlag){
                //  수집 url make
                String startUrl = crwUrl + page + addDateParam;

                wb = new ChromeDriver(options);
                wb.navigate().to(startUrl);

                List<WebElement> pageLinkList = wb.findElements(By.xpath(src.getCatePagingListXpath()));

                //  pageLinkList  사이즈가 20 이 아니면 수집 종료
                if(pageLinkList.size() != 20) stopFlag = false;
                //  해당 url에 목록이 없으면 수집종료.
                if(pageLinkList.size() == 0) break;

                for (WebElement webElement : pageLinkList) {
                    result.add(webElement.getAttribute("href"));
                }

                //  page 증가.
                page++;
                //  2초간 페이징 이동 간격
                Thread.sleep(crwWaitingSec);

                //  브라우져 종료.
                wb.quit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            wb.quit();
            throw new CrwErrorException(ErrorDto.builder()
                    .errorMsg(e.getMessage())
                    .errorArticleCate(src.getArticleCategory())
                    .errorSiteNm(SiteName.NATE.name()).build());
        }finally {
            wb.quit();
        }

        return result;
    }
}
