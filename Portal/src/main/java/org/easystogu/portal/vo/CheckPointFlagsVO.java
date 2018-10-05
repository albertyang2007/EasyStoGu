package org.easystogu.portal.vo;

public class CheckPointFlagsVO {
	public StringBuffer weekGordonTitle = new StringBuffer();
	public StringBuffer weekGordonText = new StringBuffer();

	public StringBuffer ziJinLiuRuTitle = new StringBuffer();
	public StringBuffer ziJinLiuRuText = new StringBuffer();

	public StringBuffer bottomAreaTitle = new StringBuffer();
	public StringBuffer bottomAreaText = new StringBuffer();

	public StringBuffer bottomGordonTitle = new StringBuffer();
	public StringBuffer bottomGordonText = new StringBuffer();

	public StringBuffer wbottomMacdTwiceGordonTitle = new StringBuffer();
	public StringBuffer wbottomMacdTwiceGordonText = new StringBuffer();

	@Override
	public String toString() {
		return weekGordonText.toString() + ";" + this.ziJinLiuRuText.toString() + ";" + this.bottomAreaText.toString()
				+ ";" + this.bottomGordonText.toString() + ";" + this.wbottomMacdTwiceGordonText.toString();
	}
}
