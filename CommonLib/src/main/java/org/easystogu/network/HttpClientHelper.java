package org.easystogu.network;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.utils.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//this http client still have connect timeout
@Component
public class HttpClientHelper {
	@Autowired
	private FileConfigurationService configure;

	String proxy = configure.getString(Constants.httpProxyServer, "");

	int port = configure.getInt(Constants.httpProxyPort, 0);

	String proxyTrue = Strings.isNotEmpty(configure.getString(Constants.httpProxyServer)) ? "true" : "false";

	public void initProxyWithOutUserPwd(String host, int port, final String proxyTrue) {

		Authenticator.setDefault(new Authenticator() {
		});
		System.setProperty("http.proxyType", "4");
		System.setProperty("http.proxyPort", port + "");
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxySet", proxyTrue);
	}

	public void initProxy(String host, int port, final String username, final String password, final String proxyTrue) {

		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, new String(password).toCharArray());
			}
		});
		System.setProperty("http.proxyType", "4");
		System.setProperty("http.proxyPort", port + "");
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxySet", proxyTrue);
	}

	public void getHttpContentFromWeb(String url, String outFile, String encode) {
		try {
			String curLine = "";

			FileOutputStream outPut = new FileOutputStream(outFile);
			OutputStreamWriter osw = new OutputStreamWriter(outPut, encode);

			if (proxyTrue.equals("true"))
				initProxyWithOutUserPwd(proxy, port, proxyTrue);

			URL server = new URL(url);

			HttpURLConnection connection = (HttpURLConnection) server.openConnection();

			connection.connect();

			InputStream inputs = connection.getInputStream();

			BufferedReader reader = new BufferedReader(new InputStreamReader(inputs, encode));

			while ((curLine = reader.readLine()) != null) {
				osw.write(curLine + "\n");
			}
			osw.flush();
			osw.close();
			inputs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String baseUrl = "http://data.eastmoney.com/zjlx/";
		String outFilePath = "F:/Stock/EasyStoGu/EasyMoneyHistoryData/";
		String stockId = "000024";
		HttpClientHelper ins = new HttpClientHelper();
		System.out.println("Process " + stockId);
		String url = baseUrl + stockId + ".html";
		String outfile = outFilePath + stockId + ".html";
		String encode = "gb2312";
		ins.getHttpContentFromWeb(url, outfile, encode);
	}

}
