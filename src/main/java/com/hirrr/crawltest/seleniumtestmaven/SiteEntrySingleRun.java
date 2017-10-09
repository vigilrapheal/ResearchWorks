package com.hirrr.crawltest.seleniumtestmaven;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.SocketException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.whois.WhoisClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.mysql.jdbc.PreparedStatement;
import com.sleepycat.je.utilint.Stat;

public class SiteEntrySingleRun {

	public static void main(String[] args) throws IOException, SQLException {

		SiteEntrySingleRun site = new SiteEntrySingleRun();
		HashSet<String> hrefSet = new HashSet<>();
		String url = "";
		String value = "";
		ArrayList<String> hrefList = new ArrayList<>();

			url="http://www.propertyguru.com.sg";
			Document doc1 = new Document(url);
			hrefList = new ArrayList<>();
			
			try {
				doc1 = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
						.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:44.0) Gecko/20100101 Firefox/44.0")
						.maxBodySize(0).timeout(60000).get();
			} catch (Exception e) {
				e.printStackTrace();
			}

			Elements elesA = doc1.select("a");
			Elements elesButton = doc1.select("button");
			Elements elesInput = doc1.select("input");
			
			if(elesA.isEmpty() && elesButton.isEmpty() && elesInput.isEmpty()) {
				Document doc=site.seleniumRun(url);
				elesA = doc.select("a");
				elesButton = doc.select("button");
				elesInput = doc.select("input");
			}
			
			hrefSet=site.extractURLFromElements(elesA, elesInput, elesButton, url);

			hrefList.addAll(hrefSet);
			site.dbInsertion(url, hrefList);
		

	}
	
	private HashSet<String> extractURLFromElements(Elements elesA,Elements elesInput,Elements elesButton ,String url){
		
		HashSet<String> hrefSet = new HashSet<>();
		
		for (Element element : elesA) {
			String href = element.attr("href");
			if (!href.trim().isEmpty() && !href.trim().equals(url.trim()) && !"#".equals(href)/* && !"/".equals(href)*/) {

				hrefSet.add(href + " -----ANCHOR");
			}
		}
		
		for (Element element : elesInput) {
			String type = element.attr("type");
			if (type.equals("submit") || type.equals("button")) {
				String onclick = element.attr("onclick");
				if (!onclick.trim().isEmpty() && !onclick.trim().equals(url.trim()) && !"#".equals(onclick)
						/*&& !"/".equals(onclick)*/)
					hrefSet.add(onclick + " -----INPUT");
			}
		}

		
		for (Element element : elesButton) {
			String type = element.attr("type");
			if (type.equals("submit")) {
				String onclick = element.attr("onclick");
				if (!onclick.trim().isEmpty() && !onclick.trim().equals(url.trim()) && !"#".equals(onclick)
						/*&& !"/".equals(onclick)*/)
					hrefSet.add(onclick + " -----BUTTON");

			}
		}
		
		return hrefSet;
	}

	private boolean requiredURLChecker(String url, String returnURL) {

		if (returnURL.toUpperCase().contains("HOME") || returnURL.toUpperCase().contains("INDEX")
				|| (returnURL.contains(domainNameExtractor(url)) && badWordCheck(returnURL))) {
			return true;
		}

		return false;
	}

	private boolean badWordCheck(String returnURL) {

		String value = returnURL.toUpperCase();

		return (value.contains("FORGOT") || value.contains("MAILTO") || value.contains("INSTAGRAM")
				|| value.contains("TWITTER") || value.contains("VIMEO") || value.contains("YOUTUBE")
				|| value.contains("WHATSAPP") || value.contains("REGISTER") || value.contains("TEL")
				|| value.contains("JAVASCRIPT")) ? false : true;
	}

	private String urlCorrector(String url, String scrappedURL) {

		String returnURL = scrappedURL;
		if (!scrappedURL.trim().startsWith("http")) {
			if (!scrappedURL.startsWith("/") && !url.endsWith("/")) {
				returnURL = url + "/" + scrappedURL;
			} else {
				returnURL = url + scrappedURL;
			}

		}
		if (requiredURLChecker(url, returnURL)) {
			return returnURL;
		}

		return "";
	}

	private String domainNameExtractor(String url) {

		String reg = "https?\\:\\/\\/((www|[^.]+)\\.|)?([^.]+)(\\.(ac|ad|ae|af|ag|ai|al|am|an|ao|aq|ar|as|at|au|aw|ax|az|ba|bb|bd|be|bf|bg|bh|bi|bj|bl|bm|bn|bo|bq|br|bs|bt|bv|bw|by|bz|ca|cc|cd|cf|cg|ch|ci|ck|cl|cm|cn|co|cr|cs|cu|cv|cw|cx|cy|cz|de|dj|dk|dm|do|dz|ec|ee|eg|eh|er|es|et|eu|fi|fj|fk|fm|fo|fr|ga|gb|uk|gd|ge|gf|gg|gh|gi|gl|gm|gn|gp|gq|gr|gs|gt|gu|gw|gy|hk|hm|hn|hr|ht|hu|id|ie|il|im|in|io|iq|ir|is|it|je|jm|jo|jp|ke|kg|kh|ki|km|kn|kp|kr|kw|ky|kz|la|lb|lc|li|lk|lr|ls|lt|lu|lv|ly|ma|mc|md|me|mf|mg|mh|mk|ml|mm|mn|mo|mp|mq|mr|ms|mt|mu|mv|mw|mx|my|mz|na|nc|ne|nf|ng|ni|nl|no|np|nr|nu|nz|om|pa|pe|pf|pg|ph|pk|pl|pm|pn|pr|ps|pt|pw|py|qa|re|ro|rs|ru|rw|sa|sb|sc|sd|se|sg|sh|si|sj|sk|sl|sm|sn|so|sr|ss|st|su|sv|sx|sy|sz|tc|td|tf|tg|th|tj|tk|tl|tm|tn|to|tp|tr|tt|tv|tw|tz|ua|ug|uk|um|us|uy|uz|va|vc|ve|vg|vi|vn|vu|wf|ws|ye|yt|yu|za|zm|zr|zw|yu|academy|accountant|accountants|active|actor|ads|adult|aero|agency|airforce|apartments|app|archi|army|art|associates|attorney|auction|audible|audio|author|auto|autos|aws|baby|band|bar|barefoot|bargains|baseball|beauty|beer|best|bestbuy|bet|bid|bike|bingo|bio|biz|black|blackfriday|blockbuster|blog|blue|boo|book|bot|boutique|box|broker|build|builders|business|buy|buzz|cab|cafe|call|cam|camera|camp|cancerresearch|capital|cards|care|career|careers|cars|case|cash|casino|catering|catholic|center|ceo|cfd|channel|chat|cheap|christmas|church|circle|city|claims|cleaning|click|clinic|clothing|cloud|club|coach|codes|coffee|college|community|company|computer|condos|construction|consulting|contact|contractors|cooking|cool|coop|country|coupon|coupons|courses|credit|creditcard|cricket|cruises|dad|dance|date|dating|day|deal|deals|degree|delivery|democrat|dental|dentist|design|dev|diamonds|diet|digital|direct|directory|discount|diy|doctor|dog|domains|download|duck|earth|eat|eco|education|email|energy|engineer|engineering|equipment|esq|estate|events|exchange|expert|exposed|express|fail|faith|family|fan|fans|farm|fashion|fast|feedback|film|final|finance|financial|fire|fish|fishing|fit|fitness|flights|florist|flowers|fly|foo|food|foodnetwork|football|forsale|forum|foundation|free|frontdoor|fun|fund|furniture|fyi|gallery|game|games|garden|gift|gifts|gives|glass|global|gold|golf|gop|graphics|green|gripe|group|guide|guitars|guru|hair|hangout|health|healthcare|help|here|hiphop|hiv|hockey|holdings|holiday|homegoods|homes|homesense|horse|host|hosting|hot|house|how|ice|info|ing|ink|institute|insurance|insure|international|investments|jewelry|jobs|joy|kim|kitchen|land|latino|law|lawyer|lease|legal|lgbt|life|lifeinsurance|lighting|like|limited|limo|link|live|living|loan|loans|locker|lol|lotto|love|luxe|luxury|makeup|management|market|marketing|markets|mba|media|meet|meme|memorial|men|menu|mint|mobi|mobily|moe|money|mortgage|motorcycles|mov|movie|museum|name|navy|network|new|news|ngo|ninja|now|off|one|ong|onl|online|ooo|open|organic|origins|page|partners|parts|party|pay|pet|pharmacy|photo|photography|photos|physio|pics|pictures|pid|pin|pink|pizza|place|plumbing|plus|poker|porn|post|press|prime|pro|productions|prof|promo|properties|property|protection|qpon|racing|radio|read|realestate|realty|recipes|red|rehab|ren|rent|rentals|repair|report|republican|rest|review|reviews|rich|rip|rocks|rodeo|room|rsvp|run|safe|sale|save|scholarships|school|science|secure|security|select|services|sex|sexy|shoes|shop|shopping|show|showtime|silk|singles|site|ski|skin|sky|smile|soccer|social|software|solar|solutions|song|space|spot|spreadbetting|store|stream|studio|study|style|sucks|supplies|supply|support|surf|surgery|systems|talk|tattoo|tax|taxi|team|tech|technology|tel|tennis|theater|theatre|tickets|tips|tires|today|tools|top|tours|town|toys|trade|trading|training|travel|travelersinsurance|trust|tube|tunes|university|vacations|vet|video|villas|vip|vision|vodka|vote|voting|voyage|wang|watch|watches|weather|webcam|website|wed|wedding|whoswho|wiki|win|wine|winners|work|works|world|wow|wtf|xxx|xyz|yoga|you|zero|zone|com|org|net|int|edu|gov|mil|govt|nic|asia))+\\/?(\\s|$)";

		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(url);
		String domainName = "";
		if (m.find()) {
			domainName = url.replaceAll(reg, "$3");
		}

		return domainName;
	}

	private String onClickURLExtractor(String url, String scrappedURL) {

		String href = "";
		if (scrappedURL.toUpperCase().contains("HREF")) {
			href = scrappedURL.split("href")[1];
			int start = 0;
			int end = 0;
			int count = 0;
			for (int i = 0; i < href.length(); i++) {
				if (href.charAt(i) == '\'' || href.charAt(i) == '\"') {
					if (count == 0) {
						count++;
						start = i;
					} else if (count == 1) {
						end = i;
						break;
					}

				}
			}
			href = href.substring(start + 1, end);
			href = urlCorrector(url, href);
		}
		return href;
	}
	
	private Document seleniumRun(String url) {
	
		Document doc=new Document(url);
		WebDriver driver = new FirefoxDriver(/* firefoxBinary, null */);
		Logger log = Logger.getLogger(SeleniumIssuescrap.class.toString());
		try {
			driver.get(url);
			Thread.sleep(3000);

			log.info("reached");
			doc = Jsoup.parse(driver.getPageSource());
		} catch (Exception e) {
			log.info(e.toString());
			log.info("exception");
			driver.quit();
			return doc;
		} finally {
			driver.quit();
		}
		
		return doc;

	}

	private void dbInsertion(String url, ArrayList<String> arrList) throws SQLException {

		StringBuilder anchor = new StringBuilder();
		StringBuilder input = new StringBuilder();
		StringBuilder button = new StringBuilder();
		StringBuilder allUrl = new StringBuilder();

		
		boolean a = true;
		boolean in = true;
		boolean b = true;

		for (int i = 0; i < arrList.size(); i++) {

			allUrl.append(arrList.get(i).split(" -----")[0] + "\n");

			if (arrList.get(i).contains(" -----ANCHOR")) {

				String val = arrList.get(i).split(" -----ANCHOR")[0];
				val = urlCorrector(url, val);
				System.out.println(val);
				if (!val.isEmpty() && a) {
					a = false;
					anchor.append(val + "\n");
				}
			} else if (arrList.get(i).contains(" -----INPUT")) {
				String val = arrList.get(i).split(" -----INPUT")[0];
				val = onClickURLExtractor(url, val);
				System.out.println(val);
				if (!val.isEmpty() && in) {
					in = false;
					input.append(val + "\n");
				}
			} else {

				String val = arrList.get(i).split(" -----BUTTON")[0];
				val = onClickURLExtractor(url, val);
				System.out.println(val);
				if (!val.isEmpty() && b) {
					b = false;
					button.append(val + "\n");
				}
			}
		}

		System.out.println("anchor tag content------"+anchor);
		System.out.println("input tag content------"+input);
		System.out.println("button tag content------"+button);
		/*Connection con = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hiddenData", "root", "careersnow@123");
			String sql = "INSERT INTO `hiddenData`.`siteEntryWords`(`url`,`anchor`,`input`,`button`,`obtainedURL`)VALUES(?,?,?,?,?)";
			PreparedStatement pr = (PreparedStatement) con.prepareStatement(sql);
			pr.setString(1, url);
			pr.setString(2, anchor.toString());
			pr.setString(3, input.toString());
			pr.setString(4, button.toString());
			pr.setString(5, allUrl.toString());
			int i = pr.executeUpdate();
			if (i > 0) {
				System.out.println(url + "---url content added to db");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			con.close();
		}*/
	}

}
