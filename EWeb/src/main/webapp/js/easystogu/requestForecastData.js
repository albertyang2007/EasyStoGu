/**
 * load luzao with forecast trendmode parms
 * 
 * @returns {undefined}
 */
function loadLuZaoWithReqParms(stockId, dateFrom, dateTo, reqParms) {
	var seriesCounter = 0, date_price = [], volume = [], data_ma19 = [], data_ma43 = [], data_ma86 = [];
	var version = "v3";

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	// post forecast stock price data and fetch back with full data
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_price,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
			date_price = convert2Candlestick(data);
			volume = convert2Volume(data);

			seriesCounter += 1;
			if (seriesCounter === 2) {
				createChart_LuZao(stockId, date_price, volume, data_ma19,
						data_ma43, data_ma86);
			}
		}
	});

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/luzao/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_ind,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
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
		}
	});
}

/**
 * load shenxian with forecast trendmode parms
 * 
 * @returns {undefined}
 */
function loadShenXianWithReqParms(stockId, dateFrom, dateTo, reqParms) {
	var seriesCounter = 0, date_price = [], volume = [], data_h1 = [], data_h2 = [], data_h3 = [];
	var version = "v3";

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	// post forecast stock price data and fetch back with full data
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_price,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
			date_price = convert2Candlestick(data);
			volume = convert2Volume(data);

			seriesCounter += 1;
			if (seriesCounter === 2) {
				createChart_ShenXian(stockId, date_price, volume, data_h1,
						data_h2, data_h3);
			}
		}
	});

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/shenxian/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_ind,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
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
				createChart_ShenXian(stockId, date_price, volume, data_h1,
						data_h2, data_h3);
			}
		}
	});
}

/**
 * load shenxianSell with forecast trendmode parms
 * 
 * @returns {undefined}
 */
function loadShenXianSellWithReqParms(stockId, dateFrom, dateTo, reqParms) {
	var seriesCounter = 0, date_price = [], volume = [], data_h1 = [], data_h2 = [], data_hc5 = [], data_hc6 = [];
	var version = "v3";

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	// post forecast stock price data and fetch back with full data
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_price,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
			date_price = convert2Candlestick(data);
			volume = convert2Volume(data);

			seriesCounter += 1;
			if (seriesCounter === 2) {
				createChart_ShenXianSell(stockId, date_price, volume, data_h1,
						data_h2, data_hc5, data_hc6);
			}
		}
	});

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/shenxianSell/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_ind,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
			i = 0;
			for (i; i < data.length; i += 1) {
				var dateStr = data[i]['date'] + " 15:00:00";
				var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
				data_h1.push([ dateD.getTime(), data[i]['h1'] ]);

				data_h2.push([ dateD.getTime(), data[i]['h2'] ]);

				data_hc5.push([ dateD.getTime(), data[i]['hc5'] ]);
				
				data_hc6.push([ dateD.getTime(), data[i]['hc6'] ]);
			}

			seriesCounter += 1;
			if (seriesCounter === 2) {
				createChart_ShenXianSell(stockId, date_price, volume, data_h1,
						data_h2, data_hc5, data_hc6);
			}
		}
	});
}

/**
 * load boll with forecast trendmode parms
 * 
 * @returns {undefined}
 */
function loadBollWithReqParms(stockId, dateFrom, dateTo, reqParms) {
	var seriesCounter = 0, date_price = [], volume = [], data_mb = [], data_up = [], data_dn = [];
	var version = "v3";

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	// post forecast stock price data and fetch back with full data
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_price,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
			date_price = convert2Candlestick(data);
			volume = convert2Volume(data);

			seriesCounter += 1;
			if (seriesCounter === 2) {
				createChart_Boll(stockId, date_price, volume, data_mb, data_up,
						data_dn);
			}
		}
	});

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/boll/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_ind,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
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
		}
	});
}

/**
 * load macd with forecast trendmode parms
 * 
 * @returns {undefined}
 */
function loadMacdWithReqParms(stockId, dateFrom, dateTo, reqParms) {
	var seriesCounter = 0, date_price = [], volume = [], data_dif = [], data_dea = [], data_macd = [];
	var version = "v3";

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	// post forecast stock price data and fetch back with full data
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_price,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
			date_price = convert2Candlestick(data);
			volume = convert2Volume(data);

			seriesCounter += 1;
			if (seriesCounter === 2) {
				createChart_Macd(stockId, date_price, volume, data_dif,
						data_dea, data_macd);
			}
		}
	});

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/macd/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_ind,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
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
				createChart_Macd(stockId, date_price, volume, data_dif,
						data_dea, data_macd);
			}
		}
	});
}

/**
 * load qsdd with forecast trendmode parms
 * 
 * @returns {undefined}
 */
function loadQsddWithReqParms(stockId, dateFrom, dateTo, reqParms) {
	var seriesCounter = 0, date_price = [], volume = [], data_lonTerm = [], data_midTerm = [], data_shoTerm = [];
	var version = "v3";

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	// post forecast stock price data and fetch back with full data
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_price,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
			date_price = convert2Candlestick(data);
			volume = convert2Volume(data);

			seriesCounter += 1;
			if (seriesCounter === 2) {
				createChart_Qsdd(stockId, date_price, volume, data_lonTerm,
						data_midTerm, data_shoTerm);
			}
		}
	});

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	var url_ind = getEasyStoGuServerUrl() + "/portal/ind" + version + "/qsdd/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_ind,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(reqParms),
		success : function(data) {
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
		}
	});
}