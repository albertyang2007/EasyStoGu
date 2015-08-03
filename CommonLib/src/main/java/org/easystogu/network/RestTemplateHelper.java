package org.easystogu.network;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.utils.Strings;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class RestTemplateHelper {
	private static FileConfigurationService configure = FileConfigurationService.getInstance();
	private RestTemplate restTemplate = null;

	public RestTemplateHelper() {
		this.initRestTemplate();
	}

	private void initRestTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(10000);
		requestFactory.setReadTimeout(10000);

		if (Strings.isNotEmpty(configure.getString(Constants.httpProxyServer))) {
			Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(configure.getString(Constants.httpProxyServer),
					configure.getInt(Constants.httpProxyPort)));
			requestFactory.setProxy(proxy);
		}

		restTemplate = new RestTemplate(requestFactory);
	}

	public String fetchDataFromWeb(String url) {
		StringBuffer urlStr = new StringBuffer(url);

		try {
			String contents = restTemplate.getForObject(urlStr.toString(), String.class);
			// System.out.println(contents);
			return contents;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		RestTemplateHelper runner = new RestTemplateHelper();
		runner.fetchDataFromWeb("http://data.eastmoney.com/zjlx/detail.html?cmd=C._A");
	}
}
