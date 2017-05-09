package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.easystogu.cache.ConfigurationServiceCache;
import org.easystogu.cache.XXXYuanStockStatisticsCache;
import org.easystogu.db.vo.view.StatisticsViewVO;
import org.easystogu.portal.util.MergeNDaysStatisticsHelper;
import org.easystogu.portal.vo.StatisticsVO;

public class XXXYuanStockStatisticsEndPoint {
	private ConfigurationServiceCache config = ConfigurationServiceCache.getInstance();
	private String accessControlAllowOrgin = config.getString("Access-Control-Allow-Origin", "");
	private XXXYuanStockStatisticsCache stockStatisticsCache = XXXYuanStockStatisticsCache.getInstance();

	@GET
	@Path("/{howMuchYuan}")
	@Produces("application/json")
	public List<StatisticsVO> getLatestDate(@PathParam("howMuchYuan") String howMuchYuan,
			@Context HttpServletResponse response) {
		List<StatisticsVO> rtnList = new ArrayList<StatisticsVO>();
		response.addHeader("Access-Control-Allow-Origin", accessControlAllowOrgin);
		List<StatisticsViewVO> list = this.stockStatisticsCache.get(howMuchYuan);

		// should merge into a Week or Month based
		for (StatisticsViewVO svvo : list) {
			StatisticsVO svo = new StatisticsVO();
			svo.date = svvo.date;
			svo.count1 = svvo.count;
			rtnList.add(svo);
		}
		return MergeNDaysStatisticsHelper.mergeToMonthBased(rtnList);
	}
}
