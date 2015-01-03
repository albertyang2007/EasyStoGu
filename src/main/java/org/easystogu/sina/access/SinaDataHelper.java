package org.easystogu.sina.access;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


public class SinaDataHelper {
	private static final String baseUrl = "http://hq.sinajs.cn/list=";

	public void fetchDataFromWeb(String... codeNumbers) {
		StringBuffer urlStr = new StringBuffer(baseUrl);
		for (String codeNumber : codeNumbers) {
			urlStr.append(codeNumber + ",");
		}
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);

        RestTemplate restTemplate = new RestTemplate(requestFactory);
        
        String content = restTemplate.getForObject(urlStr.toString(), String.class);
        System.out.println(content);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		SinaDataHelper ins = new SinaDataHelper();
		String[] codeNumbers = { "sh601318", "sz000830" };
		ins.fetchDataFromWeb(codeNumbers);
	}

}
