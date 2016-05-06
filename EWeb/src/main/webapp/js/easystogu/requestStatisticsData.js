/**
 * Load LuZao and Trend_Phase Satistics
 * 
 * @returns {undefined}
 */
function loadLuZaoTrendStatistics(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_ma19 = [], data_ma43 = [], data_ma86 = [], data_1_guancha = [], data_2_jiancang = [], data_3_chigu = [], data_4_jiancang = [];

	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = "http://localhost:8080/portal/price" + version + "/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_price, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			date_price.push([ dateD.getTime(), data[i]['open'],
					data[i]['high'], data[i]['low'], data[i]['close'] ]);

			volume.push([ dateD.getTime(), data[i]['volume'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart_LuZao_Trend_Statistics(stockId, date_price, volume,
					data_ma19, data_ma43, data_ma86, data_1_guancha,
					data_2_jiancang, data_3_chigu, data_4_jiancang);
		}
	});

	/**
	 * Load luzao Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + version + "/luzao/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			data_ma19.push([ dateD.getTime(), data[i]['ma19'] ]);

			data_ma43.push([ dateD.getTime(), data[i]['ma43'] ]);

			data_ma86.push([ dateD.getTime(), data[i]['ma86'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart_LuZao_Trend_Statistics(stockId, date_price, volume,
					data_ma19, data_ma43, data_ma86, data_1_guancha,
					data_2_jiancang, data_3_chigu, data_4_jiancang);
		}
	});

	/**
	 * Load luzao trend phase statistics and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/statistics/luzao/trend/"
			+ dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			data_1_guancha.push([ dateD.getTime(), data[i]['count1'] ]);
			data_2_jiancang.push([ dateD.getTime(), data[i]['count2'] ]);
			data_3_chigu.push([ dateD.getTime(), data[i]['count3'] ]);
			data_4_jiancang.push([ dateD.getTime(), data[i]['count4'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart_LuZao_Trend_Statistics(stockId, date_price, volume,
					data_ma19, data_ma43, data_ma86, data_1_guancha,
					data_2_jiancang, data_3_chigu, data_4_jiancang);
		}
	});
}

/**
 * Load LuZao and Gordon_Dead Satistics
 * 
 * @returns {undefined}
 */
function loadLuZaoGordonStatistics(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_ma19 = [], data_ma43 = [], data_ma86 = [], data_1_gordon0 = [], data_2_gordon1 = [], data_3_gordon2 = [], data_4_dead1 = [], data_5_dead2 = [];

	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = "http://localhost:8080/portal/price" + version + "/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$
			.getJSON(
					url_price,
					function(data) {
						i = 0;
						for (i; i < data.length; i += 1) {
							var dateStr = data[i]['date'] + " 15:00:00";
							var dateD = new Date(Date.parse(dateStr.replace(
									/-/g, "/")));
							date_price.push([ dateD.getTime(), data[i]['open'],
									data[i]['high'], data[i]['low'],
									data[i]['close'] ]);

							volume.push([ dateD.getTime(), data[i]['volume'] ]);
						}

						seriesCounter += 1;
						if (seriesCounter === 3) {
							createChart_LuZao_Gordon_Statistics(stockId,
									date_price, volume, data_ma19, data_ma43,
									data_ma86, data_1_gordon0, data_2_gordon1,
									data_3_gordon2, data_4_dead1, data_5_dead2);
						}
					});

	/**
	 * Load luzao Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + version + "/luzao/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$
			.getJSON(
					url_ind,
					function(data) {
						i = 0;
						for (i; i < data.length; i += 1) {
							var dateStr = data[i]['date'] + " 15:00:00";
							var dateD = new Date(Date.parse(dateStr.replace(
									/-/g, "/")));
							data_ma19
									.push([ dateD.getTime(), data[i]['ma19'] ]);

							data_ma43
									.push([ dateD.getTime(), data[i]['ma43'] ]);

							data_ma86
									.push([ dateD.getTime(), data[i]['ma86'] ]);
						}

						seriesCounter += 1;
						if (seriesCounter === 3) {
							createChart_LuZao_Gordon_Statistics(stockId,
									date_price, volume, data_ma19, data_ma43,
									data_ma86, data_1_gordon0, data_2_gordon1,
									data_3_gordon2, data_4_dead1, data_5_dead2);
						}
					});

	/**
	 * Load luzao trend phase statistics and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/statistics/luzao/gordon/"
			+ dateFrom + "_" + dateTo;
	$
			.getJSON(
					url_ind,
					function(data) {
						i = 0;
						for (i; i < data.length; i += 1) {
							var dateStr = data[i]['date'] + " 15:00:00";
							var dateD = new Date(Date.parse(dateStr.replace(
									/-/g, "/")));
							data_1_gordon0.push([ dateD.getTime(),
									data[i]['count1'] ]);
							data_2_gordon1.push([ dateD.getTime(),
									data[i]['count2'] ]);
							data_3_gordon2.push([ dateD.getTime(),
									data[i]['count3'] ]);
							data_4_dead1.push([ dateD.getTime(),
									data[i]['count4'] ]);
							data_5_dead2.push([ dateD.getTime(),
									data[i]['count5'] ]);
						}

						seriesCounter += 1;
						if (seriesCounter === 3) {
							createChart_LuZao_Gordon_Statistics(stockId,
									date_price, volume, data_ma19, data_ma43,
									data_ma86, data_1_gordon0, data_2_gordon1,
									data_3_gordon2, data_4_dead1, data_5_dead2);
						}
					});
}

/**
 * Load Qsdd and Top Bottom Satistics
 * 
 * @returns {undefined}
 */
function loadQsddStatistics(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_lonTerm = [], data_midTerm = [], data_shoTerm = [], date_topArea = [], date_bottomArea = [], data_bottomGordon = [];

	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = "http://localhost:8080/portal/price" + version + "/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_price, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			date_price.push([ dateD.getTime(), data[i]['open'],
					data[i]['high'], data[i]['low'], data[i]['close'] ]);

			volume.push([ dateD.getTime(), data[i]['volume'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 2) {
			createChart_Qsdd_Statistics(stockId, date_price, volume,
					data_lonTerm, data_midTerm, data_shoTerm, date_topArea,
					date_bottomArea, data_bottomGordon);
		}
	});

	/**
	 * Load luzao Indicator and display
	 * 
	 * @returns {undefined}
	 */
	/*
	 * var url_ind = "http://localhost:8080/portal/ind" + version + "/qsdd/" +
	 * stockId + "/" + dateFrom + "_" + dateTo; $.getJSON(url_ind,
	 * function(data) { i = 0; for (i; i < data.length; i += 1) { var dateStr =
	 * data[i]['date'] + " 15:00:00"; var dateD = new
	 * Date(Date.parse(dateStr.replace(/-/g, "/"))); data_lonTerm.push([
	 * dateD.getTime(), data[i]['lonTerm'] ]);
	 * 
	 * data_midTerm.push([ dateD.getTime(), data[i]['midTerm'] ]);
	 * 
	 * data_shoTerm.push([ dateD.getTime(), data[i]['shoTerm'] ]); }
	 * 
	 * seriesCounter += 1; if (seriesCounter === 3) {
	 * createChart_Qsdd_Statistics(stockId, date_price, volume, data_lonTerm,
	 * data_midTerm, data_shoTerm, date_topArea, date_bottomArea,
	 * data_bottomGordon); } });
	 */

	/**
	 * Load qsdd top bottom statistics and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/statistics/qsdd/" + dateFrom
			+ "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			date_topArea.push([ dateD.getTime(), data[i]['count1'] ]);
			date_bottomArea.push([ dateD.getTime(), data[i]['count2'] ]);
			data_bottomGordon.push([ dateD.getTime(), data[i]['count3'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 2) {
			createChart_Qsdd_Statistics(stockId, date_price, volume,
					data_lonTerm, data_midTerm, data_shoTerm, date_topArea,
					date_bottomArea, data_bottomGordon);
		}
	});
}

/**
 * Load ShenXian and Gordon, Dead Statistics data
 * 
 * @returns {undefined}
 */
function loadShenXianStatistics(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_h1 = [], data_h2 = [], data_h3 = [], data_gordon = [], data_dead = [];
	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = "http://localhost:8080/portal/price" + version + "/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_price, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			date_price.push([ dateD.getTime(), data[i]['open'],
					data[i]['high'], data[i]['low'], data[i]['close'] ]);

			volume.push([ dateD.getTime(), data[i]['volume'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart_ShenXian_Statistics(stockId, date_price, volume,
					data_h1, data_h2, data_h3, data_gordon, data_dead);
		}
	});

	/**
	 * Load ShenXian Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + version + "/shenxian/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			data_h1.push([ dateD.getTime(), data[i]['h1'] ]);

			data_h2.push([ dateD.getTime(), data[i]['h2'] ]);

			data_h3.push([ dateD.getTime(), data[i]['h3'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart_ShenXian_Statistics(stockId, date_price, volume,
					data_h1, data_h2, data_h3, data_gordon, data_dead);
		}
	});

	/**
	 * Load shenxian gordon and dead statistics and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/statistics/shenxian/"
			+ dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			data_gordon.push([ dateD.getTime(), data[i]['count1'] ]);
			data_dead.push([ dateD.getTime(), data[i]['count2'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart_ShenXian_Statistics(stockId, date_price, volume,
					data_h1, data_h2, data_h3, data_gordon, data_dead);
		}
	});
}