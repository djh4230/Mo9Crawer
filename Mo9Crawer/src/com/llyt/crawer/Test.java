package com.llyt.crawer;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;

/**
 * Created by Administrator on 2015/8/21.
 */
public class Test {
    public static WebDriver driver;
    // li.active>span.next
    public static void main(String[] args) {

        if(args.length<3){
            System.out.println("please input two paremeter!");
            System.exit(0);
        }
        String url=args[0];
        String phantomJsPath=args[1];
        String screenShot=args[2];

        DesiredCapabilities cap = DesiredCapabilities.phantomjs();
        cap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                new String[]{
                        "--webdriver-loglevel=DEBUG",
//			"--proxy=org.openqa.grid.selenium.proxy.DefaultRemoteProxy",
                });
        cap.setCapability(
                PhantomJSDriverService.PHANTOMJS_PAGE_CUSTOMHEADERS_PREFIX
                        + "Accept-Language", "zh_CN");
        //cap.setCapability("phantomjs.page.settings.userAgent","Mozilla/5.0 (Macintosh; Intel Mac OS X 10.9; rv:25.0) Gecko/20100101 Firefox/25.0");
        //"C:\\Users\\Administrator\\Desktop\\phantomjs-2.0.0-windows\\bin\\phantomjs.exe"
        cap.setCapability("phantomjs.binary.path", phantomJsPath);
        cap.setJavascriptEnabled(true);
        driver = new PhantomJSDriver(cap);

        WebDriverWait wait=null;
        wait = new WebDriverWait(driver, 60);
        driver.get(url);
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        screenShot((TakesScreenshot) driver, screenShot);
    }
    static void screenShot(TakesScreenshot drivername, String filename) {
        File scrFile = drivername.getScreenshotAs(OutputType.FILE);
        //System.out.println("save snapshot path is:c:\\" + filename);
        try {
            FileUtils.copyFile(scrFile, new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("screen shot finished");
        }
    }
}
