package org.easystogu.sina.helper;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.sina.common.SinaQuoteStockPriceVO;
import org.easystogu.utils.Strings;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

//get real time stock price from http://vip.stock.finance.sina.com.cn/quotes_service/api/
//it will get all the stockId from the web, including the new on board stockId
public class DailyStockPriceDownloadHelper2 {
	// currently total stock number is less then 3000, if increase, then enlarge
	// the numberPage
	private static final int totalNumberPage = 8;
	private static final int numberPerPage = 400;
	private static final String baseUrl = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=1&num="
			+ numberPerPage + "&sort=symbol&asc=1&node=hs_a";
	private static FileConfigurationService configure = FileConfigurationService.getInstance();

	public List<SinaQuoteStockPriceVO> fetchAllStockPriceFromWeb() {
		List<SinaQuoteStockPriceVO> list = new ArrayList<SinaQuoteStockPriceVO>();
		for (int pageNumber = 1; pageNumber <= totalNumberPage; pageNumber++) {
			list.addAll(this.fetchAPageDataFromWeb(pageNumber));
		}
		return list;
	}

	private List<SinaQuoteStockPriceVO> fetchAPageDataFromWeb(int pageNumber) {
		List<SinaQuoteStockPriceVO> list = new ArrayList<SinaQuoteStockPriceVO>();
		try {

			String url = baseUrl.replaceFirst("page=1", "page=" + pageNumber);
			System.out.print("Fetch Sina Daily Data for page= " + pageNumber);

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
				return list;
			}

			// convert json to vo list
			JSONArray jsonArray = JSONArray.fromObject(contents);
			JSONObject jsonObject;
			Object pojoValue;

			for (int i = 0; i < jsonArray.size(); i++) {
				jsonObject = jsonArray.getJSONObject(i);
				pojoValue = JSONObject.toBean(jsonObject, SinaQuoteStockPriceVO.class);
				list.add((SinaQuoteStockPriceVO) pojoValue);
			}

			System.out.println(", result size= " + jsonArray.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) {
		DailyStockPriceDownloadHelper2 ins = new DailyStockPriceDownloadHelper2();
		List<SinaQuoteStockPriceVO> list = ins.fetchAllStockPriceFromWeb();
		SinaQuoteStockPriceVO vo = list.get(list.size() - 1);
		System.out.println(list.size());
		System.out.println(vo);
	}
}