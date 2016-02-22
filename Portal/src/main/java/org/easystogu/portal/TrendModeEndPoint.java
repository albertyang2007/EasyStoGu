package org.easystogu.portal;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import net.sf.json.JSONArray;

import org.easystogu.trendmode.generator.ModeExtractor;
import org.easystogu.trendmode.vo.TrendModeVO;

public class TrendModeEndPoint {
	@GET
	@Path("/{name}")
	@Produces("application/json")
	public String queryByName(@PathParam("name") String name) {
		// just for debug. will load data from json file in trendmode project
		ModeExtractor ins = new ModeExtractor();
		TrendModeVO tmVO = ins.generateTrendMode("TestName", "Test Description", "999999", "2016-01-20", "2016-02-22");
		return JSONArray.fromObject(tmVO.prices).toString();
	}
}
