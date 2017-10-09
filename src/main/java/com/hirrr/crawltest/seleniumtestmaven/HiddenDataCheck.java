package com.hirrr.crawltest.seleniumtestmaven;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Logger;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class HiddenDataCheck {

	public static void main(String[] args) throws IOException {

		WebDriver driver = new FirefoxDriver(/* firefoxBinary, null */);

		Logger log = Logger.getLogger(SeleniumIssuescrap.class.toString());
		try {

			driver.get("http://www.capitalfirst.com/careers");
			Thread.sleep(3000);

			log.info("reached");
		} catch (Exception e) {
			log.info(e.toString());
			log.info("exception");
		}
		// WebElement
		// weele=driver.findElement(By.xpath("//div[contains(@style,'display:none')]"));

		BufferedWriter bw = null;
//		FileWriter fw = null;

		JavascriptExecutor js = (JavascriptExecutor) driver;
		try (FileWriter fw = new FileWriter("/home/vigil/Desktop/output/test.txt")){
			
			@SuppressWarnings("unchecked")
			ArrayList<String> mainURL=(ArrayList<String>) js.executeScript("var links=[];"
					+ "for(i=0;i<document.getElementsByTagName(\"div\").length;i++){"
					+ "if(document.getElementsByTagName(\"div\")[i].style.display=\"none\"){"
					+ "links.push(document.getElementsByTagName(\"div\")[i].outerHTML);"
					+ "}"
					+ "}return(links);");
			for(int i=0;i<mainURL.size();i++) {
				bw = new BufferedWriter(fw);
				bw.write(mainURL.get(i));
				bw.write("=====================================END====================");
			System.out.println(mainURL.get(i)+"---------value");
			}
			System.out.println(mainURL.size());
		}
		catch (Exception e) {
			e.printStackTrace();
		}finally {
			bw.close();
		}
			
	}
}
