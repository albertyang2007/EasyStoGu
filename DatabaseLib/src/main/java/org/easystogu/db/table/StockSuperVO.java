package org.easystogu.db.table;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.easystogu.utils.CrossType;

public class StockSuperVO {
	public StockPriceVO priceVO;
	public MacdVO macdVO;
	public KDJVO kdjVO;
	public BollVO bollVO;
	public XueShi2VO xueShi2VO;
	public ShenXianVO shenXianVO;
	public Mai1Mai2VO mai1mai2VO;
	public ZhuliJinChuVO zhuliJinChuVO;
	public YiMengBSVO yiMengBSVO;
	public Map<String, ZiJinLiuVO> ziJinLiuVOMap = new HashMap<String, ZiJinLiuVO>();
	public double volumeIncreasePercent;// äº¤æ˜“çš„volumeè¾ƒå‰�ä¸€æ—¥çš„å¢žé‡�ç™¾åˆ†æ¯”
	public long avgVol5;// avg of 5 days volume
	public CrossType macdCorssType;
	public CrossType kdjCorssType;
	public CrossType rsvCorssType;
	public CrossType bullXueShi2DnCrossType;
	public CrossType shenXianCorssType12;// H1 corss H2
	public CrossType shenXianCorssType13;// H1 corss H3
	public CrossType mai1mai2CrossTypeMai1;// SK cross SD and sk < 0. buy point
	public CrossType mai1mai2CrossTypeMai2;// SK cross SD and sk > 0. buy point
	public CrossType zhuliJinChuCrossType;// duoFang kongFang
	public CrossType yiMengBSCrossType;// YiMengBS X2 cross X3 is buy point
	public boolean[] KLineStatus = new boolean[4];// big/small red, big/small
													// green
	public boolean priceHigherThanNday = false;// 当前价格是否突破平台,比如15天的价格
	public double avgMA5;// 5日均价
	public double avgMA10;// 10日均价
	public double avgMA20;// 20日均价
	public double avgMA30;// 30日均价
	public double avgMA60;// 60日均价
	public double avgMA120;// 120日均价

	public int hengPanWeekLen;// hengPan week length

	public StockSuperVO(StockPriceVO priceVO, MacdVO macdVO, KDJVO kdjVO, BollVO bollVO) {
		this.priceVO = priceVO;
		this.macdVO = macdVO;
		this.kdjVO = kdjVO;
		this.bollVO = bollVO;
		this.macdCorssType = CrossType.UNKNOWN;
		this.kdjCorssType = CrossType.UNKNOWN;
		this.volumeIncreasePercent = 1.0;
	}

	public void setShenXianVO(ShenXianVO vo) {
		this.shenXianVO = vo;
	}

	public void setXueShi2VO(XueShi2VO vo) {
		this.xueShi2VO = vo;
	}

	public void setMai1Mai2VO(Mai1Mai2VO vo) {
		this.mai1mai2VO = vo;
	}

	public void setZhuliJinChuVO(ZhuliJinChuVO vo) {
		this.zhuliJinChuVO = vo;
	}

	public void setYiMengBSVO(YiMengBSVO vo) {
		this.yiMengBSVO = vo;
	}

	@Override
	public String toString() {
		return priceVO.toString() + ";" + macdVO.toString() + ";" + kdjVO.toString() + ";" + bollVO.toString() + ";"
				+ "; macdCross:" + macdCorssType + "; kdjCross:" + kdjCorssType + "; shenXianCross:"
				+ shenXianCorssType12 + "; shenXianCross2:" + shenXianCorssType13 + "; volumeIncFactor="
				+ volumeIncreasePercent;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((priceVO == null) ? 0 : priceVO.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		StockSuperVO other = (StockSuperVO) obj;
		if (priceVO == null) {
			if (other.priceVO != null) {
				return false;
			}
		} else if (!priceVO.equals(other.priceVO)) {
			return false;
		}
		return true;
	}

	// 近几日是否资金流入状态
	// money is incoming in recent days
	public boolean isAllMajorNetPerIn() {
		Set<String> keys = this.ziJinLiuVOMap.keySet();
		Iterator<String> it = keys.iterator();
		while (it.hasNext()) {
			String key = it.next();
			ZiJinLiuVO vo = this.ziJinLiuVOMap.get(key);
			if (vo.majorNetPer < 0)
				return false;
		}
		return true;
	}

	// we have 3 zijinliuvos: currentDay, 3Day, 5Day
	// the first one is currentDay's ziJinLiu
	//
	public double getFirstZiJinLiuVOMajorNetPer() {
		if (this.ziJinLiuVOMap.containsKey(ZiJinLiuVO._1Day)) {
			return this.ziJinLiuVOMap.get(ZiJinLiuVO._1Day).majorNetPer;
		}
		return 0;
	}

	public double getAllZiJinLiuVOMajorNetPer() {
		double sum = 0.0;
		if (this.ziJinLiuVOMap.containsKey(ZiJinLiuVO._1Day)) {
			sum += this.ziJinLiuVOMap.get(ZiJinLiuVO._1Day).majorNetPer;
		}
		if (this.ziJinLiuVOMap.containsKey(ZiJinLiuVO._3Day)) {
			sum += this.ziJinLiuVOMap.get(ZiJinLiuVO._3Day).majorNetPer;
		}
		if (this.ziJinLiuVOMap.containsKey(ZiJinLiuVO._5Day)) {
			sum += this.ziJinLiuVOMap.get(ZiJinLiuVO._5Day).majorNetPer;
		}
		return sum;
	}

	public void putZiJinLiuVO(String whichDay, ZiJinLiuVO ziJinLiuVO) {
		if (ziJinLiuVO == null)
			return;
		if (!this.ziJinLiuVOMap.containsKey(whichDay))
			this.ziJinLiuVOMap.put(whichDay, ziJinLiuVO);
	}

	public String genZiJinLiuInfo() {
		StringBuffer rtn = new StringBuffer();
		if (this.ziJinLiuVOMap.containsKey(ZiJinLiuVO._1Day)) {
			rtn.append(ZiJinLiuVO._1Day + this.ziJinLiuVOMap.get(ZiJinLiuVO._1Day).toNetPerString() + " <br> ");
		}
		if (this.ziJinLiuVOMap.containsKey(ZiJinLiuVO._3Day)) {
			rtn.append(ZiJinLiuVO._3Day + this.ziJinLiuVOMap.get(ZiJinLiuVO._3Day).toNetPerString() + " <br> ");
		}
		if (this.ziJinLiuVOMap.containsKey(ZiJinLiuVO._5Day)) {
			rtn.append(ZiJinLiuVO._5Day + this.ziJinLiuVOMap.get(ZiJinLiuVO._5Day).toNetPerString() + " <br> ");
		}
		return rtn.toString();
	}

}
