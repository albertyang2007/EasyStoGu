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
    @Path("/{stockId}/{date}")
    @Produces("application/json")
    public List<StockPriceVO> queryDayPriceById(@PathParam("stockId")
    String stockIdParm, @PathParam("date")
    String dateParm) {
        if (Pattern.matches(fromToRegex, dateParm)) {
            String date1 = dateParm.split("_")[0];
            String date2 = dateParm.split("_")[1];
            return stockPriceTable.getStockPriceByIdAndBetweenDate(stockIdParm, date1, date2);
        }
        if (Pattern.matches(dateRegex, dateParm) || Strings.isEmpty(dateParm)) {
            List<StockPriceVO> list = new ArrayList<StockPriceVO>();
            list.add(stockPriceTable.getStockPriceByIdAndDate(stockIdParm, dateParm));
            return list;
        }
        return new ArrayList<StockPriceVO>();
    }
}
