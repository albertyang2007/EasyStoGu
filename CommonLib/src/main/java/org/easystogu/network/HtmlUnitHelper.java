package org.easystogu.network;

import java.net.URL;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

@Component
public class HtmlUnitHelper {
	@Autowired
	private FileConfigurationService configure;

	public WebClient getWebClient() {
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
		webClient.setTimeout(30000);
		webClient.setThrowExceptionOnScriptError(false);
		webClient.waitForBackgroundJavaScript(1000 * 5L);
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
}
