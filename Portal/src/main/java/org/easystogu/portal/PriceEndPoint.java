package org.easystogu.portal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

import org.easystogu.db.access.StockPriceTableHelper;
import org.easystogu.db.table.StockPriceVO;
import org.easystogu.utils.Strings;

public class PriceEndPoint {
    private StockPriceTableHelper stockPriceTable = StockPriceTableHelper.getInstance();
    private String dateRegex = "[0-9]{4}-[0-9]{2}-[0-9]{2}";
    private String fromToRegex = dateRegex + "_" + dateRegex;

    @GET
    @Path("/{stockid}/{date}")
    @Produces("application/json")
    public List<StockPriceVO> queryDayPriceById(@PathParam("stockid")
    String stockid, @PathParam("date")
    String date) {
        if (Pattern.matches(fromToRegex, date)) {
            String date1 = date.split("_")[0];
            String date2 = date.split("_")[1];
            return stockPriceTable.getStockPriceByIdAndBetweenDate(stockid, date1, date2);
        }
        if (Pattern.matches(dateRegex, date) || Strings.isEmpty(date)) {
            List<StockPriceVO> list = new ArrayList<StockPriceVO>();
            list.add(stockPriceTable.getStockPriceByIdAndDate(stockid, date));
            return list;

        }
        return null;
    }
}
