/**
 * 
 */
package com.hirrr.crawltest.SeleniumTestMaven;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Navigation;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.hirrr.learner.googletranslator.LanguageDetector;

/**
 * @author VIGIL V RAPHEAL
 *
 */
@PrepareForTest({LanguageDetector.class,Jsoup.class})
@RunWith(PowerMockRunner.class)
public class LanguageDetectorTest {

	/**
	 * Test method for {@link com.hirrr.learner.googletranslator.LanguageDetector#mainMethod(java.lang.String)}.
	 */
	private LanguageDetector detector;
	private WebDriver driver;
	private Document doc;
	private FirefoxDriver firefox;
	private Connection conj;
	private Elements eles;
	private WebElement webele;
	private Navigation navi;
	
	
	@Before
	public void initializeObject() {
		
		detector=new LanguageDetector();
		driver=Mockito.mock(WebDriver.class);
		doc=Mockito.mock(Document.class);
		firefox=Mockito.mock(FirefoxDriver.class);
		conj=Mockito.mock(Connection.class);
		eles=Mockito.mock(Elements.class);
		webele=Mockito.mock(WebElement.class);
		navi=Mockito.mock(Navigation.class);
	}
	
	@Test
	public void testLanguageDetector() throws Exception {
		
		PowerMockito.whenNew(FirefoxDriver.class).withNoArguments().thenReturn(firefox);
		driver=firefox;
		PowerMockito.doNothing().when(driver).get(Matchers.anyString());
		
		PowerMockito.mockStatic(Jsoup.class);
		PowerMockito.doReturn(conj).when(Jsoup.class,"connect",Matchers.anyString());
		PowerMockito.doReturn(conj).when(conj).header(Matchers.anyString(),Matchers.anyString());
		PowerMockito.doReturn(conj).when(conj).userAgent(Matchers.anyString());
		PowerMockito.doReturn(conj).when(conj).maxBodySize(Matchers.anyInt());
		PowerMockito.doReturn(conj).when(conj).timeout(Matchers.anyInt());
		PowerMockito.doReturn(doc).when(conj).get();
		
		PowerMockito.doReturn(eles).when(doc).select(Matchers.anyString());
		PowerMockito.doReturn("<html>foo content</html> @").when(eles).toString();
		
		PowerMockito.doReturn(webele).when(driver).findElement(By.id(Matchers.anyString()));
		PowerMockito.doReturn(webele).when(driver).findElement(By.xpath(Matchers.anyString()));
		PowerMockito.doReturn("Thai").when(webele).getText();
		PowerMockito.doReturn(navi).when(driver).navigate();
		PowerMockito.doNothing().when(navi).refresh();
		
		Assert.assertEquals("English", detector.languageDetector(""));
		
		PowerMockito.doReturn("<html>foo content โครงการบ้านเดี่ยว  ทาวน์โฮม ทาวน์เฮ้าส์ </html> @").when(eles).toString();
		
		Assert.assertEquals("Thai", detector.languageDetector(""));
	}

}
