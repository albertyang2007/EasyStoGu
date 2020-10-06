package org.easystogu.sina.helper;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.easystogu.config.Constants;
import org.easystogu.config.FileConfigurationService;
import org.easystogu.file.access.CompanyInfoFileHelper;
import org.easystogu.log.LogHelper;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.utils.Strings;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

//get real time stock price from http://hq.sinajs.cn/list=
//it need sotckIds as parameter
@Component
public class DailyStockPriceDownloadHelper {
	private static Logger logger = LogHelper.getLogger(DailyStockPriceDownloadHelper.class);
	private static final String baseUrl = "http://hq.sinajs.cn/list=";
	@Autowired
	private FileConfigurationService configure;
	@Autowired
	private CompanyInfoFileHelper stockConfig;

	// stockList is like: sh000001,sh601318
	// has prefix
	public List<RealTimePriceVO> fetchDataFromWeb(String stockList) {
		String[] stockIds = stockList.split(",");
		return this.fetchDataFromWeb(Arrays.asList(stockIds));
	}

	public List<RealTimePriceVO> fetchDataFromWeb(List<String> stockIds) {
		List<RealTimePriceVO> list = new ArrayList<RealTimePriceVO>();
		try {

			StringBuffer urlStr = new StringBuffer(baseUrl);
			for (String stockId : stockIds) {
				urlStr.append(stockId + ",");
			}

			SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
			requestFactory.setConnectTimeout(10000);
			requestFactory.setReadTimeout(10000);

			if (Strings.isNotEmpty(configure.getString(Constants.httpProxyServer))) {
				Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(
						configure.getString(Constants.httpProxyServer), configure.getInt(Constants.httpProxyPort)));
				requestFactory.setProxy(proxy);
			}

			RestTemplate restTemplate = new RestTemplate(requestFactory);

			// logger.debug("url=" + urlStr.toString());
			String contents = restTemplate.getForObject(urlStr.toString(), String.class);

			if (Strings.isEmpty(contents)) {
				logger.debug("Contents is empty");
				return list;
			}

			String[] content = contents.trim().split("\n");
			for (int index = 0; index < content.length; index++) {
				String[] items = content[index].trim().split("\"");
				if (items.length <= 1) {
					continue;
				}
				// logger.debug(items[1]);
				String realStockId = stockConfig.getStockIdMapping(stockIds.get(index));
				RealTimePriceVO vo = new RealTimePriceVO(realStockId, items[1]);
				if (vo.isValidated()) {
					list.add(vo);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
}
