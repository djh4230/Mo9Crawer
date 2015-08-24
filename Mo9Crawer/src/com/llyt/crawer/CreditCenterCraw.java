package com.llyt.crawer;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CreditCenterCraw {

	private static WebDriver driver;
	private static int c=1;
	private static String URL = "https://www.mo9.com/creditCenter/p/"+c;
    //private static String URL = "https://www.baidu.com";
	private static boolean isLast = false;

	// li.active>span.next
	public static void main(String[] args) {

		if(args.length<2){
			System.out.println("please input two paremeter!");
			System.exit(0);
		}
		String filePath=args[0];
		String phantomJsPath=args[1];

		DesiredCapabilities cap = DesiredCapabilities.phantomjs();
		cap.setCapability(PhantomJSDriverService.PHANTOMJS_CLI_ARGS,
                new String[]{
 //                       "--webdriver-loglevel=DEBUG",
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
		driver.get(URL);
        /*try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        //screenShot((TakesScreenshot) driver, "/home/admin/"+"mo9"+c+".png");
		List<WebElement> nextElement = null;
		ArrayList<String> credits = new ArrayList<String>();

		int prePage = 1;
		int curPage = c;
		BufferedWriter br = null;
		File f = new File(filePath);
		try {
			br = new BufferedWriter(new FileWriter(f, true));
			br.write("mo9账号,真实姓名,身份证号,欠款金额,逾期天数");
			br.write("\r\n");
		} catch (IOException e1) {
			e1.printStackTrace();
		}finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		while (!isLast) {
			List<WebElement> elements =null;
			try {
				elements = wait.until(ExpectedConditions
						.visibilityOfAllElementsLocatedBy(By
								.cssSelector("div.faith-list>dl.fs12")));
				
			} catch (Exception e) {
				e.printStackTrace();
				driver.close();
				driver.quit();
				while(elements==null){
					try {
						
						Thread.currentThread().sleep(720000);
						//c=((curPage/15)*15)+1;
						//curPage=c;
						driver = new PhantomJSDriver(cap);
						//driver=new FirefoxDriver(binary,profile);
						
						wait = new WebDriverWait(driver, 60);
						driver.get("https://www.mo9.com/creditCenter/p/"+curPage);
						elements = wait.until(ExpectedConditions
								.visibilityOfAllElementsLocatedBy(By
										.cssSelector("div.faith-list>dl.fs12")));
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}
			
			System.out.println(curPage);
			for (WebElement element : elements) {
				StringBuffer sb = new StringBuffer();
				String acount = element.findElement(
						By.cssSelector("div.faith-list>dl.fs12>dt.row01"))
						.getText();
				String name = element.findElement(
						By.cssSelector("div.faith-list>dl.fs12>dd.row02"))
						.getText();
				String certiCard = element.findElement(
						By.cssSelector("div.faith-list>dl.fs12>dd.row03"))
						.getText();
				String debt = element.findElement(
						By.cssSelector("div.faith-list>dl.fs12>dd.row04"))
						.getText();
				String overDue = element.findElement(
						By.cssSelector("div.faith-list>dl.fs12>dd.row05"))
						.getText();
				sb.append(acount + "," + name + "," + certiCard + "," + debt
						+ "," + overDue);
				credits.add(sb.toString());
			}
			nextElement = driver.findElements(By.cssSelector("li>a.next"));
			
			if(credits.size()%150==0 || nextElement.size() == 0){
				try {
					br = new BufferedWriter(new FileWriter(f, true));
					for (String str : credits) {
						br.write(str);
						br.write("\r\n");
					}
					credits.clear();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					if (br != null) {
						try {
							br.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				try {
					//Random r=new Random(100);
					//int n=r.nextInt(90);
					Thread.currentThread().sleep(20000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			if (nextElement.size() == 0) {
				isLast = true;
			} else {
				nextElement.get(0).click();
				curPage++;
				try {
					Thread.currentThread().sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			
			
		}

	}
	static void screenShot(TakesScreenshot drivername, String filename){
        File scrFile = drivername.getScreenshotAs(OutputType.FILE);
        System.out.println("save snapshot path is:"+filename);
        try {
            FileUtils.copyFile(scrFile, new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("screen shot finished");
        }
    }
	class Credit {
		private String acount;
		private String name;
		private String certiCard;
		private String debt;
		private String overDue;

		public String getAcount() {
			return acount;
		}

		public void setAcount(String acount) {
			this.acount = acount;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCertiCard() {
			return certiCard;
		}

		public void setCertiCard(String certiCard) {
			this.certiCard = certiCard;
		}

		public String getDebt() {
			return debt;
		}

		public void setDebt(String debt) {
			this.debt = debt;
		}

		public String getOverDue() {
			return overDue;
		}

		public void setOverDue(String overDue) {
			this.overDue = overDue;
		}

	}

}
