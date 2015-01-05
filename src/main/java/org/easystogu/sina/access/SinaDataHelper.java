package org.easystogu.sina.access;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;

import org.easystogu.config.FileConfigurationService;
import org.easystogu.sina.common.RealTimePriceVO;
import org.easystogu.utils.Strings;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class SinaDataHelper {
    private static final String baseUrl = "http://hq.sinajs.cn/list=";
    private static FileConfigurationService configure = FileConfigurationService.getInstance();

    public void fetchDataFromWeb(String stockList) {
        String[] codeNumbers = stockList.split(",");
        this.fetchDataFromWeb(codeNumbers);
    }

    public void fetchDataFromWeb(String... codeNumbers) {
        StringBuffer urlStr = new StringBuffer(baseUrl);
        for (String codeNumber : codeNumbers) {
            urlStr.append(codeNumber + ",");
        }

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(1000);
        requestFactory.setReadTimeout(1000);

        if (Strings.isNotEmpty(configure.getString("http.proxy.server"))) {
            Proxy proxy = new Proxy(Type.HTTP, new InetSocketAddress(configure.getString("http.proxy.server"),
                    configure.getInt("http.proxy.port")));
            requestFactory.setProxy(proxy);
        }

        RestTemplate restTemplate = new RestTemplate(requestFactory);

        String contents = restTemplate.getForObject(urlStr.toString(), String.class);

        if (Strings.isEmpty(contents)) {
            System.out.println("Contents is empty");
            return;
        }

        String[] content = contents.trim().split("\n");
        for (int index = 0; index < content.length; index++) {
            String[] items = content[index].trim().split("\"");
            //System.out.println(items[1]);
            RealTimePriceVO vo = new RealTimePriceVO(codeNumbers[index], items[1]);
            System.out.println(vo);
        }
    }

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        SinaDataHelper ins = new SinaDataHelper();
        ins.fetchDataFromWeb(configure.getString("stock.list"));
    }

}
