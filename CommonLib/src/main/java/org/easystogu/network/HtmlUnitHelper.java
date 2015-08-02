package org.easystogu.network;

import java.net.URL;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.utils.Strings;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.RefreshHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlDivision;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;

public class HtmlUnitHelper {
	private static FileConfigurationService configure = FileConfigurationService.getInstance();

	public static WebClient getWebClient() {
		WebClient webClient = null;
		if (Strings.isNotEmpty(configure.getString(Constants.httpProxyServer))) {
			webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8,
					configure.getString(Constants.httpProxyServer), configure.getInt(Constants.httpProxyPort));
		} else {
			webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_8);
		}
		webClient.setJavaScriptEnabled(true);
		webClient.setCssEnabled(true);
		webClient.setAjaxController(new NicelyResynchronizingAjaxController());
		webClient.setTimeout(50000);
		webClient.setThrowExceptionOnScriptError(false);
		webClient.waitForBackgroundJavaScript(1000 * 10L);
		webClient.setJavaScriptTimeout(0);
		webClient.setRedirectEnabled(true);
		JavaScriptEngine engine = new JavaScriptEngine(webClient);
		webClient.setJavaScriptEngine(engine);

		RefreshHandler rh = new RefreshHandler() {
			public void handleRefresh(final Page page, final URL url, final int seconds) {
			}
		};

		webClient.setRefreshHandler(rh);

		return webClient;
	}

	public static void main(String[] args) {
		try {
			WebClient webClient = HtmlUnitHelper.getWebClient();
			HtmlPage htmlpage = webClient.getPage("http://data.eastmoney.com/zjlx/detail.html");
			// System.out.println(htmlpage.asText());
			final HtmlDivision div = (HtmlDivision) htmlpage.getElementById("PageCont");
			final HtmlTextInput input = div.getElementById("gopage");
			input.setValueAttribute("59");
			List<?> links = div.getByXPath("a");
			HtmlAnchor anchor = (HtmlAnchor) links.get(links.size() - 1);

			HtmlPage page = (HtmlPage) anchor.click();
			webClient.waitForBackgroundJavaScript(1000 * 5L);
			// System.out.println(page.asXml());
			final HtmlTable tabContent = (HtmlTable) page.getElementById("dt_1");
			System.out.println(tabContent.asText());

			webClient.closeAllWindows();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
