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
	var url_price = "http://localhost:8080/portal/price" + version + "/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
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
		if (seriesCounter === 2) {
			createChart_ShenXian(stockId, date_price, volume, data_h1, data_h2,
					data_h3);
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
	var url_price = "http://localhost:8080/portal/price" + version + "/"+ stockId + "/"
			+ dateFrom + "_" + dateTo;
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
	var url_ind = "http://localhost:8080/portal/ind" + version + "/luzao/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
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
	var url_price = "http://localhost:8080/portal/price" + version + "/"+ stockId + "/"
			+ dateFrom + "_" + dateTo;
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
	var url_ind = "http://localhost:8080/portal/ind" + version + "/boll/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
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
	var url_price = "http://localhost:8080/portal/price" + version + "/"+ stockId + "/"
			+ dateFrom + "_" + dateTo;
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
	var url_ind = "http://localhost:8080/portal/ind" + version + "/macd/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
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
	var url_price = "http://localhost:8080/portal/price" + version + "/"+ stockId + "/"
			+ dateFrom + "_" + dateTo;
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
	var url_ind = "http://localhost:8080/portal/ind" + version + "/qsdd/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
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