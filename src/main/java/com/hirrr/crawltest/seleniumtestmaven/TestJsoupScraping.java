package com.hirrr.crawltest.seleniumtestmaven;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.io.File;
import java.io.FileReader;

import org.apache.poi.hslf.record.Sound;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import com.mysql.jdbc.PreparedStatement;

public class TestJsoupScraping {

	private static Set<String> set = new HashSet<>();
	private static Set<String> set1 = new HashSet<>();
	private static StringBuffer dbinsertionSet = new StringBuffer();
	private static String url = "";
	private static Document mainDoc = null;
	private static int c = 0;
	private static int j = 0;
	private static ArrayList<String> list = new ArrayList<>();

	public static void main(String[] args) throws IOException, SQLException {

		TestJsoupScraping hidden = new TestJsoupScraping();
		File file = new File("/home/vigil/Desktop/output/hidden_Data_Test_run");
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String value = "";
		long startTime;
		while ((value = reader.readLine()) != null) {
			try {
				startTime=System.currentTimeMillis();
				url = value;
				System.out.println(url);
				mainDoc = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
						.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:44.0) Gecko/20100101 Firefox/44.0")
						.maxBodySize(0).timeout(60000).get();
				mainDoc = hidden.mediaChecker(mainDoc);

			} catch (Exception e) {

				j++;
				dbinsertionSet.append("Exception");
				insertIntoDB();
				continue;
			}

			ArrayList<String> idArr = new ArrayList<>();
			ArrayList<String> classArr = new ArrayList<>();

			// System.out.println(doc.select("style"));
			hidden.regexMatcherElements(mainDoc);
			if (mainDoc == null)
				continue;
			// regexMatcher(doc);
			Elements elem = null;
			// try {
			elem = mainDoc.select("link");
			// } catch (Exception e) {
			//
			// hidden.seleniumRun();
			// elem = mainDoc.select("link");
			// }
			// System.out.println(elem.size());
			String strarr = url.replaceAll("(https?://[^\\/]+)\\/.*", "$1");

			for (Element el : elem) {

				try {

					String addr = el.attr("href");
					addr = hidden.badWordCheck(addr);
					if (!addr.startsWith("http")) {
						if (!addr.startsWith("/")) {
							addr = strarr + "/" + addr;
						} else
							addr = strarr + addr;
					}
					Document doc1 = Jsoup.connect(addr).header("Accept-Encoding", "gzip, deflate")
							.userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:44.0) Gecko/20100101 Firefox/44.0")
							.maxBodySize(0).timeout(60000).get();

					doc1 = hidden.mediaChecker(doc1);
					hidden.regexMatcher(doc1);

				} catch (Exception e) {
					continue;
				}
			}

			// set.removeAll(list);
			// set1.removeAll(list);

			idArr.addAll(set);
			classArr.addAll(set1);

			System.out.println(idArr);
			System.out.println(classArr);

			set.clear();
			set1.clear();
			// list.clear();

			StringBuffer strBuff = new StringBuffer();

			Elements elms = null;
			for (int i = 0; i < idArr.size(); i++) {
				String elementId = idArr.get(i).trim();
				if (!elementId.contains(" ") && !elementId.contains(">")) {

					elms = mainDoc.select("body").select("*");
					hidden.exactClassIdSelector(elms, elementId);
				} else if (!elementId.contains(">")) {

					hidden.selectElement(elementId);
				} else {
					elms = mainDoc.select(elementId);
					if (!elms.isEmpty()) {
						dbinsertionSet.append(elementId + "****");
					}
				}
			}

			c++;

			elms = null;
			for (int i = 0; i < classArr.size(); i++) {
				String elementId = classArr.get(i).trim();
				if (!elementId.contains(" ") && !elementId.contains(">")) {
					elms = mainDoc.select("body").select("*");
					hidden.exactClassIdSelector(elms, elementId);
					elms.clear();
				} else if (!elementId.contains(">")) {

					hidden.selectElement(elementId);
				} else {
					elms = mainDoc.select(elementId);
					if (!elms.isEmpty()) {
						dbinsertionSet.append(elementId + "****");
					}
				}

			}

			insertIntoDB();
			++j;
			System.out.println("Inserted----" + j);
			long timeConsumed=System.currentTimeMillis()-startTime;
			System.out.println("\n ----------------------------------------- ");
			System.err.println("TOTAL TIME CONSUMED --- "+timeConsumed+"\n");
			System.out.println(" ----------------------------------------- \n");
		}

	}

	private void regexMatcher(Document doc1) {

		try {
			String reg = "((\\.|\\#)([ \\w:>_,.-]+))+ *\\{[^\\}]*(display|visibility): *(none|hidden)[^\\}]*\\}";
			Pattern p = Pattern.compile(reg);
			Matcher m = null;
			String beautifiedCss = CssFormater.cssBeautifier(doc1.toString());
			m = p.matcher(beautifiedCss);
			// System.out.println(doc1.select("style"));
			while (m.find()) {

				seperateClassndDiv(m.group());
			}
		} catch (Exception e) {
			System.out.println(e);
		}

	}

	private void regexMatcherElements(Document doc) {

		try {
			String reg = "((\\.|\\#)([ \\w:>_,.-]+))+ *\\{[^\\}]*(display: *none|visibility: *hidden)[^\\}]*\\}";

			String content = "";
			Pattern p = Pattern.compile(reg);
			Matcher m = null;
			if (doc.select("style").size() < 3) {
				seleniumRun();

				content = mainDoc.toString();
				// writeToFile(content);
			} else {
				content = doc.toString();
			}
			m = p.matcher(content);
			while (m.find()) {
				seperateClassndDiv(m.group());
			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void seperateClassndDiv(String string) {

		String reg = "\\{[^\\}]+\\}";
		try {
			Pattern p = Pattern.compile(reg);
			Matcher m = null;
			m = p.matcher(string);

			if (m.find()) {

				string = string.replaceAll(reg, "");
				if (string.contains(",")) {
					String[] arr = string.split(",");
					for (int i = 0; i < arr.length; i++) {
						if (arr[i].contains(":")) {
							continue;
						} else {
							if (arr[i].contains("#")) {
								// arr[i] = spaceCheck(arr[i]);
								set.add(arr[i].trim());
							} else if (arr[i].contains(".")) {
								// arr[i] = spaceCheck(arr[i]);
								set1.add(arr[i].trim());
							}
						}
					}
				} else {

					if (string.contains("#") && !string.contains(":")) {
						// string = spaceCheck(string);
						set.add(string.trim());
					} else if (string.contains(".") && !string.contains(":")) {
						// string = spaceCheck(string);
						set1.add(string.trim());
					}

				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private void seleniumRun() throws IOException {
		File file = new File("/home/vigil/Desktop/output/test.txt");
		FileWriter writer = null;
		WebDriver driver = new FirefoxDriver(/* firefoxBinary, null */);
		Logger log = Logger.getLogger(SeleniumIssuescrap.class.toString());
		try {
			writer = new FileWriter(file);
			driver.get(url);
			Thread.sleep(3000);

			log.info("reached");
			mainDoc = Jsoup.parse(driver.getPageSource());
			writer.write(driver.getPageSource());
			writer.flush();
		} catch (Exception e) {
			log.info(e.toString());
			log.info("exception");
			driver.quit();
		} finally {
			driver.quit();
		}

	}

	private String spaceCheck(String element) {

		String returnVal = "";
		int counter = 0;

		for (int i = 0; i < element.length(); i++) {
			if (element.charAt(i) == '.' || element.charAt(i) == '#') {
				counter++;
				if (counter > 1) {
					returnVal = element.substring(0, i).trim() + "*>*" + element.substring(i, element.length()).trim();
				}
			}
		}

		if (!returnVal.isEmpty() && returnVal.contains(" ")) {
			returnVal = returnVal.trim().replace(" ", " > ");
		} else if (element.contains(" ") && returnVal.isEmpty())
			element = element.trim().replace(" ", " > ");

		return (returnVal.trim().equals("")) ? element : returnVal.replace("*", " ");
	}

	private void exactClassIdSelector(Elements eles, String elementId) {

		for (Element element : eles) {
			if (elementId.startsWith(".")) {
				if (element.hasAttr("class") && ("." + element.className().trim()).equals(elementId.trim())) {

					dbinsertionSet.append(elementId + "****");
				}
			} else {
				if (element.hasAttr("id") && ("#" + element.id().trim()).equals(elementId.trim())) {

					dbinsertionSet.append(elementId + "****");

				}
			}
		}
	}

	private void selectElement(String value) {

		String[] splitArr = value.split(" ");
		Elements eles = mainDoc.select(splitArr[0]);
		Elements elements = new Elements();
		for (Element element : eles) {
			if (splitArr[0].startsWith("#") && element.hasAttr("id")) {
				String id = "#" + element.attr("id");
				if (id.equals(splitArr[0])) {
					elements.add(element);
				}
			} else if (splitArr[0].startsWith(".") && element.hasAttr("class")) {
				String className = "." + element.attr("class").replaceAll("\\s+", ".");
				if (className.equals(splitArr[0])) {
					elements.add(element);
				}
			}
		}

		int h=0;
		eles.clear();
		eles = elements;
		for (int i = 1; i < splitArr.length; i++) {
			for (Element element : elements) {
				// elements = element.select(splitArr[i]);
				if (splitArr[i].startsWith(".")) {
					String className = "." + element.select(splitArr[i]).attr("class").replaceAll("\\s+", ".");
					if (splitArr[i].equals(className))
						elements = element.select(splitArr[i]);
				} else if (splitArr[i].startsWith("#") && element.select(splitArr[i]).hasAttr("id")) {
					String id = "#" + element.select(splitArr[i]).attr("id");
					if (splitArr[i].equals(id))
						elements = element.select(splitArr[i]);
				}else {
					if(h==0) {
					elements = element.select(splitArr[i]);
					h++;
					}
				}

			}
		}
		if (!elements.isEmpty() && elements != eles) {
			dbinsertionSet.append(value + "****");
		}
	}

	private String badWordCheck(String addr) {

		if (addr.startsWith("../")) {
			return addr.replace("../", "");
		}

		return addr;
	}

	private void writeToFile(String elms) {
		try {
			File file = null;
			if (c == 0)
				file = new File("/home/vigil/Desktop/output/hidden.txt");
			else
				file = new File("/home/vigil/Desktop/output/hiddenclass.txt");
			FileWriter writer = null;
			writer = new FileWriter(file);
			writer.write(elms);
			writer.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Document mediaChecker(Document doc) {

		String docContent = doc.toString();
		String reg = "@media[^@]+\\}\\s\\}";
		Pattern p = Pattern.compile(reg);
		Matcher m = null;
		m = p.matcher(doc.toString());
		while (m.find()) {
//			System.out.println(m.group());
			try {
				docContent = docContent.replace(m.group(), "");
			} catch (Exception e) {
				return doc;
			}
		}
		// System.out.println(Jsoup.parse(docContent));
		return Jsoup.parse(docContent);
	}

	private static void insertIntoDB() throws SQLException {

		Connection con = null;
		String status = "";
		try {

			if (!dbinsertionSet.toString().isEmpty() && !dbinsertionSet.toString().equals("Exception"))
				status = "YES";
			else if (!dbinsertionSet.toString().equals("Exception"))
				status = "NO";
			Class.forName("com.mysql.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/hiddenData", "root", "careersnow@123");
			String sql = "INSERT INTO `hiddenData`.`hidden`(`url`,`status`,`attr`)VALUES(?,?,?)";
			PreparedStatement pr = (PreparedStatement) con.prepareStatement(sql);
			pr.setString(1, url);
			pr.setString(2, status);
			pr.setString(3, dbinsertionSet.toString());
			int i = pr.executeUpdate();
			if (i > 0) {
				System.out.println(url + "---url content added to db");
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			dbinsertionSet.setLength(0);
			con.close();
		}

	}

}
