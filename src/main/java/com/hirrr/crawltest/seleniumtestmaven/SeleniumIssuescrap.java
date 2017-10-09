package com.hirrr.crawltest.seleniumtestmaven;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

public class SeleniumIssuescrap {

	public static void main(String[] args) throws InterruptedException {
		// String Xport = System.getProperty("lmportal.xvfb.id", ":1");
		// FirefoxBinary firefoxBinary = new FirefoxBinary();
		// firefoxBinary.setEnvironmentProperty("DISPLAY", Xport);
		WebDriver driver = new FirefoxDriver(/* firefoxBinary, null */);
		Set<String> set = new HashSet<>();
		List<String> javasc = new ArrayList<>();
		List<WebElement> copyLis = new ArrayList<>();
		Logger log = Logger.getLogger(SeleniumIssuescrap.class.toString());
		try {

			driver.get("https://www.amrutanjan.com/careers.html");
			Thread.sleep(3000);

			log.info("reached");
		} catch (Exception e) {
			log.info(e.toString());
			log.info("exception");
		}

		List<WebElement> list = driver.findElements(By.tagName("a"));
		Thread.sleep(3000);
		System.out.println(list.size());
		// for(int i=0;i<list.size();i++)
		// System.out.println(list.get(i).getAttribute("href"));

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getAttribute("href") != null
					/*&& (list.get(i).getAttribute("href").startsWith("http://")
							|| list.get(i).getAttribute("href").startsWith("https://"))*/
					&& (list.get(i).getAttribute("href").contains("career")
							|| list.get(i).getAttribute("href").contains("careers")
							|| list.get(i).getAttribute("href").contains("Vacancies")
							|| list.get(i).getAttribute("href").contains("current-openings")
							|| list.get(i).getAttribute("href").contains("job"))) {

				if (alreadyStored(set, list.get(i).getAttribute("href"))) {

					copyLis.add(list.get(i));
				}
				set.add(list.get(i).getAttribute("href"));
			}
		}

		javasc.clear();
		javasc.addAll(set);

		for (int i = 0; i < copyLis.size(); i++)
			System.out.println(copyLis.get(i).getAttribute("href") + "-------" + i);

		for (int i = 0; i < copyLis.size(); i++) {

			// WebDriver driver1 = new FirefoxDriver(/* firefoxBinary, null */);
			// driver1.get(javasc.get(i));
			// Thread.sleep(10000);
			// Hiddenwebelements.hiddenCheck(driver);
			// driver.navigate().back();
			// Thread.sleep(10000);
			try {
				// list.get(i).click();
				// WebElement
				// webe=driver.findElement(By.cssSelector("a[href='"+javasc.get(i)+"']"));

				// String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL,Keys.RETURN);
				// driver.switchTo().window(list.get(i).click());
				// Actions newTab = new Actions(driver);
				// newTab.keyDown(Keys.SHIFT).click(copyLis.get(i)).keyUp(Keys.SHIFT).build().perform();

				if (copyLis.get(i).getAttribute("href").contains("#")) {
					copyLis.get(i).click();
					Thread.sleep(5000);
				} else {
					String selectLinkOpeninNewTab = Keys.chord(Keys.CONTROL, "t");
					copyLis.get(i).sendKeys(selectLinkOpeninNewTab);
				}
				Thread.sleep(5000);
//				Hiddenwebelements.hiddenCheck(driver);
				System.out.println(copyLis.get(i).getAttribute("href"));
				System.out.println();
				System.out.println("Link----" + copyLis.get(i).getAttribute("href") + "---" + i);
				try {
					if (copyLis.get(i).getAttribute("href").contains("#")) {
						copyLis.get(i).click();
						Thread.sleep(5000);
					}
				} catch (Exception e) {
					driver.navigate().back();
					Thread.sleep(10000);
				}
				driver.navigate().back();
				Thread.sleep(10000);

			} catch (Exception e) {
				// e.printStackTrace();
				System.out.println("--------------------------------" + i + "=Exception=" + copyLis.get(i));

				driver.navigate().back();
				Thread.sleep(10000);

				continue;
			}
			//
			System.out.println("-----------------------------------------" + i
					+ "--------------------------------------------------");
			System.out.println(
					"==============================================================================================");
			System.out.println(
					"==============================================================================================");
			System.out.println(
					"===========================================END================================================");
		}

		// List<WebElement> foundElements =
		// driver.findElements(By.xpath("//*[contains(text(), 'hidden')]"));
		// for(int i=0;i<foundElements.size();i++) {
		// System.out.println(foundElements.get(i).getAttribute("innerHTML"));
		// System.out.println("========================================================================================");
		// }
		// System.out.println(foundElements.size());

		// List<WebElement> webele = driver.findElements(By.tagName("script"));
		// set.addAll(webele);
		// webele.clear();
		// webele.addAll(set);
		// for (int i = 0; i < webele.size(); i++) {
		// if (webele.get(i).getAttribute("src").trim().isEmpty())
		// continue;
		//// WebDriver driver1 = new FirefoxDriver(/* firefoxBinary, null */);
		// System.out.println(webele.get(i).getAttribute("src").trim());
		//// driver1.get(webele.get(i).getAttribute("src").trim());
		//// javasc.add(driver1.getPageSource());
		// }
		//
		// System.out.println(javasc);
		// System.out.println(driver.findElement(By.linkText("About
		// Us")).getCssValue("background-color"));
		// webele.sendKeys("");
		// System.out.println(webele.getAttribute("outerHTML"));
		// System.out.println(driver.getPageSource());

		// WebElement gs = driver.findElement(By.cssSelector("#pc_hidden"));
		// System.out.println(gs.getAttribute("innerHTML"));

		// driver.findElement(By.className("gsfi")).sendKeys("http://www.beisselneedles.com");
		// Thread.sleep(6000);
		// driver.findElement(By.name("btnK")).click();
		// Thread.sleep(3000);
		// driver.findElement(By.xpath(
		// "/html/body/div[6]/div[4]/div[10]/div[1]/div[2]/div/div[2]/div[2]/div/div/div/div[1]/div/div[1]/div/div/h3/a")).click();
		// Thread.sleep(6000);
		// WebElement webele=driver.findElement(By.tagName("html"));
		//
		// System.out.println(webele.getAttribute("outerHTML"));
		// driver.findElement(arg0)

		// WebElement webele=driver.findElement(By.tagName("a"));

		// List<WebElement> list = driver.findElements(By.tagName("a"));
		// List<String> listString=new ArrayList<>();
		// //
		// Thread.sleep(2000);
		// System.out.println(list.size());
		// Set<String> set=new HashSet<>();
		// //
		// for(int i=0;i<list.size();i++) {
		// set.add(list.get(i).getAttribute("class"));
		// }
		// listString.addAll(set);
		//
		// System.out.println(listString);
		// //
		// for (int i = 0; i < listString.size(); i++) {
		// try {
		// if(!(listString.isEmpty()||listString.size()==0)) {
		// if(listString.get(i).isEmpty() || listString.get(i)==null) {
		// continue;
		// }

		// System.out.println(list.get(i).getAttribute("href")+"---"+i);
		// list.get(i).click();
		// Thread.sleep(10000);
		// System.out.println(i);
		// driver.navigate().back();
		// Thread.sleep(10000);
		// driver.navigate().refresh();
		// Thread.sleep(10000);
		// System.out.println(list.size());
		// -----------------------------------------------------------------
		// driver.findElement(By.className(listString.get(i))).click();
		// Thread.sleep(10000);
		// driver.navigate().back();
		// Thread.sleep(10000);
		// }
		// else {
		// System.out.println(list.get(i).getAttribute("href")+"---"+i);
		// list.get(i).click();
		// Thread.sleep(10000);
		// System.out.println(i);
		// driver.navigate().back();
		// Thread.sleep(10000);
		// driver.navigate().refresh();
		// Thread.sleep(10000);
		// System.out.println(list.size());
		// }
		// }
		// catch (Exception e) {
		// continue;
		// }

		// }

		// System.out.println(webele.getAttribute("outerHTML"));
		// webele.click();

		// System.out.println(webele.getAttribute("outerHTML").length());
		// System.out.println(webele.getAttribute("outerHTML"));

		// /html/body/div[6]/div[4]/div[10]/div[1]/div[2]/div/div[2]/div[2]/div/div/div/div[1]/div/div[1]/div/div/h3/a

		// /html/body/div[3]/form/div[2]/div[2]/div[1]/div[1]/div[2]/div/div/div[2]/div/input[3]
	}

	private static boolean alreadyStored(Set<String> set, String url) {

		Iterator<String> it = set.iterator();

		while (it.hasNext()) {
			if (it.next().equals(url)) {
				return false;
			}
		}

		return true;
	}

}
