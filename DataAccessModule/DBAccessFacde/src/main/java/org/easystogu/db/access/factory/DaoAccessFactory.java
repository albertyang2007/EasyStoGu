package org.easystogu.db.access.factory;

import org.easystogu.config.Constants;
import org.easystogu.db.access.facde.DBAccessFacdeFactory;
import org.easystogu.db.helper.IF.IndicatorDBHelperIF;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DaoAccessFactory {
	@Autowired
	protected DBAccessFacdeFactory dBAccessFacdeFactory;

	@Bean("macdTable")
	public IndicatorDBHelperIF macdTable() {
		return dBAccessFacdeFactory.getInstance(Constants.indMacd);
	}

	@Bean("kdjTable")
	public IndicatorDBHelperIF kdjTable() {
		return dBAccessFacdeFactory.getInstance(Constants.indKDJ);
	}

	@Bean("bollTable")
	public IndicatorDBHelperIF bollTable() {
		return dBAccessFacdeFactory.getInstance(Constants.indBoll);
	}

	@Bean("shenXianTable")
	public IndicatorDBHelperIF shenXianTable() {
		return dBAccessFacdeFactory.getInstance(Constants.indShenXian);
	}

	@Bean("qsddTable")
	public IndicatorDBHelperIF qsddTable() {
		return dBAccessFacdeFactory.getInstance(Constants.indQSDD);
	}

	@Bean("wrTable")
	public IndicatorDBHelperIF wrTable() {
		return dBAccessFacdeFactory.getInstance(Constants.indWR);
	}
	
	@Bean("weekMacdTable")
	public IndicatorDBHelperIF weekMacdTable() {
		return dBAccessFacdeFactory.getInstance(Constants.indWeekMacd);
	}
	
	@Bean("weekKdjTable")
	public IndicatorDBHelperIF weekKdjTable() {
		return dBAccessFacdeFactory.getInstance(Constants.indWeekKDJ);
	}
}
