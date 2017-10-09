package com.hirrr.crawltest.seleniumtestmaven;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

public class JBrowser {
	public static void main(String[] args) {
		
		
		JBrowserDriver driver = new JBrowserDriver(Settings.builder().timezone(Timezone.AMERICA_NEWYORK).build());
		driver.get("http://www.fcg-india.net");
		System.out.println(driver.getStatusCode());
		System.out.println(driver.getPageSource());
		driver.quit();
	}
}