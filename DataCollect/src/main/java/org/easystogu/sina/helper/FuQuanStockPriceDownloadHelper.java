package org.easystogu.sina.helper;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.sina.common.SinaQuoteStockPriceVO;
import org.easystogu.utils.Strings;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

//get fuquan stock price from sina
public class FuQuanStockPriceDownloadHelper {
	private static String baseUrl = "http://vip.stock.finance.sina.com.cn/api/json_v2.php/BasicStockSrv.getStockFuQuanData?symbol=stockId&type=hfq";
	private static FileConfigurationService configure = FileConfigurationService.getInstance();	

	public List<SinaQuoteStockPriceVO> fetchFuQuanStockPriceFromWeb(List<String> stockIds) {
		List<SinaQuoteStockPriceVO> list = new ArrayList<SinaQuoteStockPriceVO>();
		for (String stockId : stockIds) {
			list.addAll(this.fetchFuQuanStockPriceFromWeb(stockId));
		}
		return list;
	}

	private List<SinaQuoteStockPriceVO> fetchFuQuanStockPriceFromWeb(String stockId) {
		List<SinaQuoteStockPriceVO> spList = new ArrayList<SinaQuoteStockPriceVO>();
		try {

			String url = baseUrl.replaceFirst("stockId", stockId);
			System.out.print("Fetch Sina FuQuan Data for " + stockId);

			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setConnectTimeout(10000);
			requestFactory.setReadTimeout(10000);

			if (Strings.isNotEmpty(configure.getString(Constants.httpProxyServer))) {
				Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(
						configure.getString(Constants.httpProxyServer), configure.getInt(Constants.httpProxyPort)));
				requestFactory.setProxy(proxy);
			}

			RestTemplate restTemplate = new RestTemplate(requestFactory);

			// System.out.println("url=" + urlStr.toString());
			String contents = restTemplate.getForObject(url.toString(), String.class);

			if (Strings.isEmpty(contents)) {
				System.out.println("Contents is empty");
				return spList;
			}

			// convert json to vo list
			// remove the outside ()
			contents = contents.substring(1, contents.length() - 1);
			// extract only data value
			contents = contents.split("data:")[1];
			// remove outside {}
			contents = contents.substring(1, contents.length() - 1);
			System.out.println(contents);

			// for (int i = 0; i < list.data.size(); i++) {
			// System.out.println(list.data.get(i));
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}
		return spList;
	}

	public static void main(String[] args) {
		FuQuanStockPriceDownloadHelper ins = new FuQuanStockPriceDownloadHelper();
		List<SinaQuoteStockPriceVO> list = ins.fetchFuQuanStockPriceFromWeb("002609");
	}
}
