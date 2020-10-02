package org.easystogu.indicator;

import java.util.List;

import org.easystogu.db.access.table.QianFuQuanStockPriceTableHelper;
import org.easystogu.db.access.table.StockPriceTableHelper;
import org.easystogu.db.vo.table.StockPriceVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/*
 *主力进出指标 
 Var1:=(CLOSE*2+HIGH+LOW)/4;
 Var2:=EMA(Var1,13)-EMA(Var1,34);
 Var3:=EMA(Var2,5);
 强弱分界: 0, CIRCLEDOT;
 空方主力: (-2)*(Var2-Var3)*3.8, LINESTICK;
 多方主力: 2*(Var2-Var3)*3.8, LINESTICK;
 多方金叉: IF(CROSS(多方主力, 空方主力), 1, 0);
 * */

@Component
public class ZhuliJinChuHelper {
	@Autowired
	TALIBWraper talibHelper;

	public double[][] getZhuliJinChuList(double[] var1) {
		int length = var1.length;
		double[][] zhuliJinChu = new double[2][length];

		double[] var21 = talibHelper.getEma(var1, 13);
		double[] var22 = talibHelper.getEma(var1, 34);
		double[] var2 = new double[length];

		for (int i = 0; i < var1.length; i++) {
			var2[i] = var21[i] - var22[i];
		}

		double[] var3 = talibHelper.getEma(var2, 5);
		// System.out.println("var2=" + var2[length - 1]);
		// System.out.println("var3=" + var3[length - 1]);

		for (int i = 1; i < length; i++) {
			zhuliJinChu[0][i] = 2 * (var2[i] - var3[i]) * 3.8;
			zhuliJinChu[1][i] = -1 * zhuliJinChu[0][i];
		}

		//System.out.println("duofang=" + zhuliJinChu[0][length - 1]);
		//System.out.println("kongfang=" + zhuliJinChu[1][length - 1]);

		return zhuliJinChu;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		StockPriceTableHelper stockPriceTable = QianFuQuanStockPriceTableHelper.getInstance();
		List<StockPriceVO> list = stockPriceTable.getStockPriceById("601318");
		ZhuliJinChuHelper ins = new ZhuliJinChuHelper();

		// list is order by date
		int length = list.size();
		double[] var1 = new double[length];
		int index = 0;
		for (StockPriceVO vo : list) {
			var1[index++] = (2 * vo.close + vo.high + vo.low) / 4;
		}

		ins.getZhuliJinChuList(var1);
	}
}
