package diameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.easystogu.utils.Strings;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class DiameterAvpWarmer {
	public static String basedUrl = "https://www.developingsolutions.com/DiaDict/Dictionary/";
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

	public static String html2Text(String html) {
		Document doc = Jsoup.parse(html.trim());
		return doc.body().text().trim();
	}

	public static RestTemplate getRestTemplate() {
		CloseableHttpClient httpClient = HttpClients.custom().setSSLHostnameVerifier(new NoopHostnameVerifier())
				.build();
		HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
		requestFactory.setConnectTimeout(20000);
		requestFactory.setReadTimeout(20000);
		requestFactory.setHttpClient(httpClient);
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
		if (!file.exists() || file.length() == 0) {
			try {
				// String contents = getWebPageContent(url);
				ResponseEntity<String> response = getRestTemplate().getForEntity(url, String.class);// .getForObject(url,
				String contents = response.getBody();
				if (Strings.isNotEmpty(contents)) {
					writeToFile(contents, fullFilePath);
					System.out.println("INFO: Save file successfully for avp:" + avpName);
				} else {
					System.out.println("ERROR: Definition not found for avp:" + avpName);
				}
			} catch (Exception e) {
				System.out.println("Exception for avp:" + avpName);
				e.printStackTrace();
			}
		}
	}

	public static Avp extractAvpFromFile(String avpName) {
		Avp avp = new Avp(avpName);
		String fullFilePath = saveBasedPath + avpName + ".html";
		String contents = readFromFile(fullFilePath);
		String[] lines = contents.split("\n");
		boolean startSubAvps = false;
		for (int index = 0; index < lines.length; index++) {
			String line = lines[index].trim();
			if (Strings.isEmpty(line)) {
				continue;
			}
			// search avp code, line: Service-Information AVP ( 873 )
			if (line.contains("<div id=\"main_container\">")) {
				// search next tag that end with </h1>
				StringBuffer avpNameCodeSb = new StringBuffer(line);
				while (true) {
					line = lines[++index].trim();
					avpNameCodeSb.append(line);
					if (line.contains("</h1>")) {
						break;
					}
				}
				String avpNameCode = html2Text(avpNameCodeSb.toString());

				if (avpNameCode.contains(avpName)) {
					// System.out.println("avpNameCode=" + avpNameCode);
					// avpNameCode=Subscription-ID AVP (443)

					String avpCodeStr = avpNameCode.split("AVP")[1].trim();
					String avpCode = avpCodeStr.substring(2, avpCodeStr.length() - 1);
					avp.code = Integer.parseInt(avpCode);
				}
			}
			// search avp type,mandatory bit,vendor and length info
			// search type
			if (line.contains("Type:")) {
				// search next tag that end with <br />
				StringBuffer avpTypeSb = new StringBuffer(line);
				while (true) {
					line = lines[++index].trim();
					avpTypeSb.append(line);
					if (line.contains("<br />")) {
						break;
					}
				}
				String avpType = html2Text(avpTypeSb.toString()).split("Type:")[1];
				avp.type = trimAllSpace(avpType);
			}

			// search mandatory
			if (line.contains("Mandatory Bit:")) {
				// search next tag that end with <br />
				StringBuffer avpTypeSb = new StringBuffer(line);
				while (true) {
					line = lines[++index].trim();
					avpTypeSb.append(line);
					if (line.contains("<br />")) {
						break;
					}
				}
				String mandatory = html2Text(avpTypeSb.toString()).split("Mandatory Bit:")[1];
				avp.mandatory = "MUST".equalsIgnoreCase(trimAllSpace(mandatory)) ? true : false;
			}

			// search Protection
			if (line.contains("Protection Bit:")) {
				// search next tag that end with <br />
				StringBuffer avpTypeSb = new StringBuffer(line);
				while (true) {
					line = lines[++index].trim();
					avpTypeSb.append(line);
					if (line.contains("<br />")) {
						break;
					}
				}
				String protection = html2Text(avpTypeSb.toString()).split("Protection Bit:")[1];
				avp.protection = "MUST".equalsIgnoreCase(trimAllSpace(protection)) ? true : false;
			}

			// search VendorId
			if (line.contains("Vendor ID:")) {
				// search next tag that end with <br />
				StringBuffer avpTypeSb = new StringBuffer(line);
				while (true) {
					line = lines[++index].trim();
					avpTypeSb.append(line);
					if (line.contains("<br />")) {
						break;
					}
				}
				String vendorId = html2Text(avpTypeSb.toString()).split("Vendor ID:")[1];
				avp.vendorId = Integer.parseInt(trimAllSpace(vendorId));
			}

			// search value length
			if (line.contains("Value Length:")) {
				// search next tag that end with <br />
				StringBuffer avpTypeSb = new StringBuffer(line);
				while (true) {
					line = lines[++index].trim();
					avpTypeSb.append(line);
					if (line.contains("<br />")) {
						break;
					}
				}
				String valueLen = html2Text(avpTypeSb.toString()).split("Value Length:")[1];
				avp.valueLength = trimAllSpace(valueLen);
			}

			// search Contained AVPs (sub Avps)
			if (!startSubAvps && line.contains("Contained AVPs") && line.contains("table_separator")) {
				startSubAvps = true;
			}

			if (startSubAvps && line.contains("Containing Messages") && line.contains("table_separator")) {
				startSubAvps = false;
			}

			if (startSubAvps) {
				// one sub avp occurs
				// search Name
				if (line.contains("<td align='left'>") && line.contains("href=") && line.contains(".html")
						&& !line.contains("target='help'")) {
					// search next tag that end with </td>
					StringBuffer subAvpSb = new StringBuffer(line);
					while (true) {
						line = lines[++index].trim();
						subAvpSb.append(line);
						if (line.contains("</td>")) {
							break;
						}
					}
					String subAvpName = html2Text(subAvpSb.toString());

					Avp subAvp = new Avp(trimAllSpace(subAvpName));
					avp.subAvp.add(subAvp);

					// search Occurrences
					line = lines[++index].trim();
					if (line.contains("<td align='center'>")) {
						// search next tag that end with </td>
						subAvpSb = new StringBuffer(line);
						String subAvpOccurs = html2Text(subAvpSb.toString());
						subAvp.occurs = trimAllSpace(subAvpOccurs);
						// System.out.println("subAvpOccurs="+subAvpOccurs);
					}

					// download subAvp definition
					downloadAvpDefinition(subAvpName);
					Avp subAvpWithDetails = extractAvpFromFile(subAvpName);
					subAvp.copy(subAvpWithDetails);
				}
			}
		}
		// System.out.println(avp);
		return avp;
	}

	public static String trimAllSpace(String str) {
		if (Strings.isNotEmpty(str)) {
			StringBuffer sb = new StringBuffer();
			char[] ch = str.toCharArray();
			for (char c : ch) {
				if (c != ' ' && c != '\t' && c != '\n' && c != '\r' && (int) c != 160) {
					sb.append(c);
					// System.out.println("ch=" + (int) c);
				}
			}
			return sb.toString();
		}
		return StringUtils.trim(str);
	}

	public static void main(String[] args) {
		String avpName = "Service-Information";
		downloadAvpDefinition(avpName);
		Avp avp = extractAvpFromFile(avpName);
		writeToFile(avp.toAvpCommandXML(), "C:/Temp/avp-command.xml");
		writeToFile(avp.toAvpDictionaryXML(), "C:/Temp/avp-dictionary.xml");
	}
}
