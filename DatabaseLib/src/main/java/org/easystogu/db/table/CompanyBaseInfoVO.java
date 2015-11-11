package org.easystogu.db.table;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompanyBaseInfoVO {
	public String stockId;
	public String name;
	public String updateTime;
	public String totalGuBen;
	public long liuTongAGu;
	public long liuTongShiZhi;// =liuTongAGu * currentPrice

	public CompanyBaseInfoVO(String line) {
		// line: 1 000001 平安银行 2015/09/30 143亿 118亿 3.30万 1.27 10.98 16.06 712亿
		// 30.19 233亿 30.8亿 233亿 177亿 13.04 594亿 4.15 0.00 2.60万亿0 44.1亿 49.7亿
		// 2.44万亿0 0 93.95 1571亿 6.05 593亿 4.15 0 0 1991/04/03
		// replace many
		Pattern p = Pattern.compile("\\s+");
		Matcher m = p.matcher(line);
		String nLine = m.replaceAll(" ");
		System.out.println(nLine);
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("CompanyBaseInfoVO: {");
		sb.append("stockId:" + stockId);
		sb.append(", name:" + name);
		sb.append(", totalGuBen:" + totalGuBen);
		sb.append(", updateTime:" + updateTime);
		sb.append(", liuTongAGu:" + liuTongAGu);
		sb.append(", liuTongShiZhi:" + liuTongShiZhi);
		sb.append("}");
		return sb.toString();
	}
}
