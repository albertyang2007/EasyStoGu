package diameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.easystogu.utils.Strings;
import org.jsoup.Jsoup;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class DiameterAvpWarmer {
	public static String basedUrl = "http://www.developingsolutions.com/DiaDict/Dictionary/";
	public static String saveBasedPath = "C:/temp/";

	public static String readFromFile(String filePath) {
		StringBuffer content = new StringBuffer();
		try {
			String encoding = "UTF-8";
			File file = new File(filePath);
			if (file.isFile() && file.exists()) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					content.append(lineTxt + "\n");
				}
				read.close();
			} else {
				System.out.println("file not found:" + filePath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content.toString();
	}

	public static void writeToFile(String content, String filePath) {
		try {
			File f = new File(filePath);
			FileWriter fw;
			fw = new FileWriter(f);
			fw.write(content);
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RestTemplate getRestTemplate() {
		SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
		requestFactory.setConnectTimeout(20000);
		requestFactory.setReadTimeout(20000);
		return new RestTemplate(requestFactory);
	}

	public static String getWebPageContent(String url) {
		try {
			CloseableHttpClient httpClient = HttpClients.createDefault();
			HttpGet httpGet = new HttpGet(url);
			CloseableHttpResponse httpResponse = null;
			RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000)
					.setConnectionRequestTimeout(2000).build();
			httpGet.setConfig(requestConfig);
			httpResponse = httpClient.execute(httpGet);
			InputStream inputStream = null;
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				HttpEntity httpResponseEntity = httpResponse.getEntity();
				inputStream = httpResponseEntity.getContent();
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
				String line = null;
				StringBuffer stringBuffer = new StringBuffer();
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line + "\n");
				}
				return String.valueOf(stringBuffer);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public static void downloadAvpDefinition(String avpName) {
		String url = basedUrl + avpName + ".html";
		String fullFilePath = saveBasedPath + avpName + ".html";
		File file = new File(fullFilePath);
		if (!file.exists()) {
			try {
				String contents = getWebPageContent(url);
				if (Strings.isNotEmpty(contents)) {
					writeToFile(contents, fullFilePath);
					System.out.println("save file successfully " + fullFilePath);
				} else {
					System.out.println("Definition not found for avp " + avpName);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void extractAvpFromFile(String avpName) {
		String fullFilePath = saveBasedPath + avpName + ".html";
		String contents = readFromFile(fullFilePath);
		System.out.println(StringEscapeUtils.unescapeHtml(contents));
	}

	public static void main(String[] args) {
		String avpName = "Service-Information";
		downloadAvpDefinition(avpName);
		extractAvpFromFile(avpName);
	}
}
