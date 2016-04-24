package org.easystogu.sina.helper;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.sina.common.SinaQuotesServiceVO;
import org.easystogu.utils.Strings;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class DailyStockPriceDownloadHelper2 {
	// currently total stock number is less then 3000, if increase, then enlarge
	// the numberPage
	private static final int totalNumberPage = 6;
	private static final int numberPerPage = 500;
	private static final String baseUrl = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=1&num=500&sort=symbol&asc=1&node=hs_a";
	private static FileConfigurationService configure = FileConfigurationService.getInstance();
	private CompanyInfoFileHelper stockConfig = CompanyInfoFileHelper.getInstance();

	private static Map<String, Class> classMap = new HashMap<String, Class>();
	static {
	}

	public List<SinaQuotesServiceVO> fetchAllStockPriceFromWeb() {
		List<SinaQuotesServiceVO> list = new ArrayList<SinaQuotesServiceVO>();
		for (int pageNumber = 1; pageNumber <= totalNumberPage; pageNumber++) {
			list.addAll(this.fetchAPageDataFromWeb(pageNumber));
		}
		return list;
	}

	public List<SinaQuotesServiceVO> fetchAPageDataFromWeb(int pageNumber) {
		List<SinaQuotesServiceVO> list = new ArrayList<SinaQuotesServiceVO>();
		try {

			String url = baseUrl.replaceFirst("page=1", "page=" + pageNumber);

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
				pojoValue = JSONObject.toBean(jsonObject, SinaQuotesServiceVO.class);
				list.add((SinaQuotesServiceVO) pojoValue);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void main(String[] args) {
		DailyStockPriceDownloadHelper2 ins = new DailyStockPriceDownloadHelper2();
		List<SinaQuotesServiceVO> list = ins.fetchAllStockPriceFromWeb();
		SinaQuotesServiceVO vo = list.get(list.size() - 1);
		System.out.println(list.size());
		System.out.println(vo);
	}
}
