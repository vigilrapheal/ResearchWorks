import java.io.IOException;
import java.net.MalformedURLException;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.jaunt.Element;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

public class JauntTest {

	public static void main(String[] args) throws FailingHttpStatusCodeException, MalformedURLException, IOException, InterruptedException {
//		
//		WebClient webclient=new WebClient(BrowserVersion.FIREFOX_38);
//		HtmlPage page=webclient.getPage("http://www.fcg-india.net");
//		System.out.println(page.asXml());
//		webclient.close();
		
		
		try{
			  UserAgent userAgent = new UserAgent();                       
			  
			  //open HTML from a String.
			  
			  userAgent.visit("http://www.fcg-india.net");   
			  Thread.sleep(3000);
//			  userAgent.openContent("<html><body>WebPage <div>Hobbies:<p>beer<p>skiing</div> Copyright 2013</body></html>");
			  System.out.println(userAgent.doc.innerHTML());   
//			  Element body = userAgent.doc.findFirst("<body>");
//			  Element div = body.findFirst("<div>");
//			   
//			  System.out.println("body's text: " + body.getText());         //join child text of body element
//			  System.out.println("body's innerText: " + body.innerText());  //join all text within body element
//			  System.out.println("div's text: " + div.getText());           //join child text of div element
//			  System.out.println("div's innerText: " + div.innerText());    //join all text within the div element
			}
			catch(JauntException e){                         
			  System.err.println(e);         
			}
	}

}

