package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.XXXYuanStockStatisticsCache;
import org.easystogu.db.vo.view.StatisticsViewVO;
import org.easystogu.portal.util.MergeNDaysStatisticsHelper;
import org.easystogu.portal.vo.StatisticsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

@RestController
@RequestMapping(value = "/xxxyuan")
public class XXXYuanStockStatisticsEndPoint {
	@Autowired
	private ConfigurationServiceCache config;
	@Autowired
	private XXXYuanStockStatisticsCache stockStatisticsCache;

	private Gson gson = new Gson();

	@GetMapping("/{howMuchYuan}")
	@Produces("application/json")
	public String getLatestDate(@PathParam("howMuchYuan") String howMuchYuan, @Context HttpServletResponse response) {
		List<StatisticsVO> rtnList = new ArrayList<StatisticsVO>();
		String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsViewVO> list = this.stockStatisticsCache.get(howMuchYuan);

		// should merge into a Week or Month based
		for (StatisticsViewVO svvo : list) {
			StatisticsVO svo = new StatisticsVO();
			svo.date = svvo.date;
			svo.count1 = svvo.count;
			rtnList.add(svo);
		}
		return gson.toJson(MergeNDaysStatisticsHelper.mergeToMonthBased(rtnList));
	}
}
