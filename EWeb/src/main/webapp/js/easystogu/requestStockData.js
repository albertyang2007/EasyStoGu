/**
 * Load ShenXian and StockPrice
 * 
 * @returns {undefined}
 */
function loadShenXian(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_h1 = [], data_h2 = [], data_h3 = [];
	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
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
			createChart_ShenXian(stockId, date_price, volume, data_h1, data_h2,
					data_h3);
		}
	});

	/**
	 * Load ShenXian Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version
			+ "/shenxian/" + stockId + "/" + dateFrom + "_" + dateTo;
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
		if (seriesCounter === 2) {
			createChart_ShenXian(stockId, date_price, volume, data_h1, data_h2,
					data_h3);
		}
	});
}

/**
 * Load ShenXian Sell Point and StockPrice, H3 is replaced by HC5
 * 
 * @returns {undefined}
 */
function loadShenXianSell(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], ddx = [], data_h1 = [], data_h2 = [], data_hc5 = [], data_hc6 = [], 
	buy_flags = [], sell_flags = [], duo_flags = [], suo_flags = [];
	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_price, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			date_price.push([ dateD.getTime(), data[i]['open'],
					data[i]['high'], data[i]['low'], data[i]['close'] ]);

			// replace to ddx
			// volume.push([ dateD.getTime(), data[i]['volume'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart_ShenXianSell(stockId, date_price, ddx, data_h1,
					data_h2, data_hc5, data_hc6, buy_flags, sell_flags,
					duo_flags, suo_flags);
		}
	});

	/**
	 * Load DDX Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/indv1/ddx/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			ddx.push([ dateD.getTime(), data[i]['ddx'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart_ShenXianSell(stockId, date_price, ddx, data_h1,
					data_h2, data_hc5, data_hc6, buy_flags, sell_flags,
					duo_flags, suo_flags);
		}
	});

	/**
	 * Load ShenXian Sell Point Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version
			+ "/shenxianSell/" + stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			data_h1.push([ dateD.getTime(), data[i]['h1'] ]);

			data_h2.push([ dateD.getTime(), data[i]['h2'] ]);

			data_hc5.push([ dateD.getTime(), data[i]['hc5'] ]);

			data_hc6.push([ dateD.getTime(), data[i]['hc6'] ]);

			if (data[i]['buyFlagsTitle'] !== null
					&& data[i]['buyFlagsTitle'].length > 0) {
				var flagData = {
					"x" : dateD.getTime(),
					"title" : data[i]['buyFlagsTitle'],
					"text" : data[i]['buyFlagsText']
				};
				buy_flags.push(flagData);
			}

			if (data[i]['sellFlagsTitle'] !== null
					&& data[i]['sellFlagsTitle'].length > 0) {
				var flagData = {
					"x" : dateD.getTime(),
					"title" : data[i]['sellFlagsTitle'],
					"text" : data[i]['sellFlagsText']
				};
				sell_flags.push(flagData);
			}

			if (data[i]['duoFlagsTitle'] !== null
					&& data[i]['duoFlagsTitle'].length > 0) {
				var flagData = {
					"x" : dateD.getTime(),
					"title" : data[i]['duoFlagsTitle'],
					"text" : data[i]['duoFlagsText']
				};
				duo_flags.push(flagData);
			}
			
			if (data[i]['suoFlagsTitle'] !== null
					&& data[i]['suoFlagsTitle'].length > 0) {
				var flagData = {
					"x" : dateD.getTime(),
					"title" : data[i]['suoFlagsTitle'],
					"text" : data[i]['suoFlagsText']
				};
				suo_flags.push(flagData);
			}
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart_ShenXianSell(stockId, date_price, ddx, data_h1,
					data_h2, data_hc5, data_hc6, buy_flags, sell_flags,
					duo_flags, suo_flags);
		}
	});
}

/**
 * Load LuZao and StockPrice
 * 
 * @returns {undefined}
 */
function loadLuZao(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_ma19 = [], data_ma43 = [], data_ma86 = [];
	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
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
			createChart_LuZao(stockId, date_price, volume, data_ma19,
					data_ma43, data_ma86);
		}
	});

	/**
	 * Load luzao Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/luzao/"
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
		if (seriesCounter === 2) {
			createChart_LuZao(stockId, date_price, volume, data_ma19,
					data_ma43, data_ma86);
		}
	});
}

/**
 * Load Boll and StockPrice
 * 
 * @returns {undefined}
 */
function loadBoll(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_mb = [], data_up = [], data_dn = [];
	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
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
			createChart_Boll(stockId, date_price, volume, data_mb, data_up,
					data_dn);
		}
	});

	/**
	 * Load boll Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/boll/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			data_mb.push([ dateD.getTime(), data[i]['mb'] ]);

			data_up.push([ dateD.getTime(), data[i]['up'] ]);

			data_dn.push([ dateD.getTime(), data[i]['dn'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 2) {
			createChart_Boll(stockId, date_price, volume, data_mb, data_up,
					data_dn);
		}
	});
}

/**
 * Load Macd and StockPrice
 * 
 * @returns {undefined}
 */
function loadMacd(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_dif = [], data_dea = [], data_macd = [];
	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
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
			createChart_Macd(stockId, date_price, volume, data_dif, data_dea,
					data_macd);
		}
	});

	/**
	 * Load macd Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/macd/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			data_dif.push([ dateD.getTime(), data[i]['dif'] ]);

			data_dea.push([ dateD.getTime(), data[i]['dea'] ]);

			data_macd.push([ dateD.getTime(), data[i]['macd'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 2) {
			createChart_Macd(stockId, date_price, volume, data_dif, data_dea,
					data_macd);
		}
	});
}

/**
 * Load QSDD and StockPrice
 * 
 * @returns {undefined}
 */
function loadQSDD(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_lonTerm = [], data_midTerm = [], data_shoTerm = [];
	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
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
			createChart_Qsdd(stockId, date_price, volume, data_lonTerm,
					data_midTerm, data_shoTerm);
		}
	});

	/**
	 * Load qsdd Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/qsdd/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			data_lonTerm.push([ dateD.getTime(), data[i]['lonTerm'] ]);

			data_midTerm.push([ dateD.getTime(), data[i]['midTerm'] ]);

			data_shoTerm.push([ dateD.getTime(), data[i]['shoTerm'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 2) {
			createChart_Qsdd(stockId, date_price, volume, data_lonTerm,
					data_midTerm, data_shoTerm);
		}
	});
}

/**
 * Load WR and StockPrice
 * 
 * @returns {undefined}
 */
function loadWR(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_lonTerm = [], data_midTerm = [], data_shoTerm = [];
	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
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
			createChart_Qsdd(stockId, date_price, volume, data_lonTerm,
					data_midTerm, data_shoTerm);
		}
	});

	/**
	 * Load wr Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/wr/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			data_lonTerm.push([ dateD.getTime(), data[i]['lonTerm'] ]);

			data_midTerm.push([ dateD.getTime(), data[i]['midTerm'] ]);

			data_shoTerm.push([ dateD.getTime(), data[i]['shoTerm'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 2) {
			createChart_WR(stockId, date_price, volume, data_lonTerm,
					data_midTerm, data_shoTerm);
		}
	});
}