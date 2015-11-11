package org.easystogu.db.table;

import org.easystogu.utils.Strings;

public class CompanyInfoVO {
	public String stockId;
	public String name;
	public String updateTime;
	public double totalGuBen;
	public double liuTongAGu;
	public double liuTongShiZhi;// =liuTongAGu * currentPrice

	public CompanyInfoVO(String line) {
		// 1074,000002,万
		// 科Ａ,2015/09/30,111亿,97.2亿,2.92万,0.62,8.05,7.69,796亿,26.06,124亿,
		// 14.8亿,125亿,68.5亿,6.14,433亿,3.92,29.06,5708亿,5200亿,40.4亿,10.0亿,4497亿,4021亿,476亿,
		// 78.78,889亿,15.58,83.4亿,0.75,0,13.1亿,1991/01/29
		try {
			String[] items = line.trim().split(",");
			if (items.length < 1) {
				System.out.println("Bad format for CompanyInfoVO line: " + line);
				return;
			}

			// skip the first line (header)
			if (!Strings.isNumeric(items[0]) || !Strings.isNumeric(items[1])) {
				return;
			}

			this.stockId = items[1];
			this.name = items[2];
			this.updateTime = items[3];
			this.totalGuBen = convert2Double(items[4]);
			this.liuTongAGu = convert2Double(items[5]);

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(line);
		}
	}

	public double convert2Double(String item) {
		if (item == null || "".equals(item) || "0".equals(item)) {
			return 0;
		}
		// item is like: 1.2亿 or 5600万, 返回单位为亿
		if (item.contains("亿")) {
			return Double.parseDouble(item.substring(0, item.length() - 1));
		} else if (item.contains("万")) {
			return Double.parseDouble(item.substring(0, item.length() - 1)) / 10000;
		} else {
			return Double.parseDouble(item.substring(0, item.length() - 1)) / (10000 * 10000);
		}
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("CompanyInfoVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", updateTime:" + updateTime);
		sb.append(", totalGuBen:" + totalGuBen);
		sb.append(", liuTongAGu:" + liuTongAGu);
		sb.append(", liuTongShiZhi:" + liuTongShiZhi);
		sb.append("}");
		return sb.toString();
	}
}
