/**
 * Load CompanyBaseInfo
 * 
 * @returns {undefined}
 */
function loadCompanyInfo(stockId) {
	var url_company = "http://localhost:8080/portal/company/" + stockId;
	var companyInfo = "N/A";
	$.getJSON(url_company, function(data) {
		alert(data);
		companyInfo = data;
	});
	return companyInfo;
}

/**
 * Load CompanyBaseInfo
 * 
 * @returns {undefined}
 */
function loadCompanyInfoByName(name) {
	var url_company = "http://localhost:8080/portal/company/name=" + name;
	var companyInfo = "N/A";
	$.getJSON(url_company, function(data) {
		companyInfo = data;
	});
	return companyInfo;
}

/**
 * Load TrendMode Price
 * 
 * @returns {undefined}
 */
function loadTrendMode(name) {
	var url_trend = "http://localhost:8080/portal/trendmode/" + name;
	var prices = "[]";
	$.getJSON(url_trend, function(data) {
		prices = data;
	});
	return prices;
}

/**
 * Load ShenXian and StockPrice
 * 
 * @returns {undefined}
 */
function loadShenXian(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_h1 = [], data_h2 = [], data_h3 = [];
    var v = "1";
    if(version == 'v2'){
    	v = "2";
    }
	/**
	 * Create the chart when all data is loaded
	 * 
	 * @returns {undefined}
	 */
	function createChart() {
		$('#container').highcharts('StockChart', {

			rangeSelector : {
				selected : 1
			},

			title : {
				text : stockId
			},

			yAxis : [ {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'Price'
				},
				height : '60%',
				lineWidth : 2
			}, {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'Volume'
				},
				top : '65%',
				height : '35%',
				offset : 0,
				lineWidth : 2
			} ],

			series : [ {
				type : 'candlestick',
				name : 'OHLC',
				data : date_price
			}, {
				name : 'H1',
				data : data_h1
			}, {
				name : 'H2',
				data : data_h2
			}, {
				name : 'H3',
				data : data_h3
			}, {
				type : 'column',
				name : 'Volume',
				data : volume,
				yAxis : 1
			} ]
		});
	}

	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = "http://localhost:8080/portal/price/" + stockId + "/"
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
			createChart();
		}
	});

	/**
	 * Load ShenXian Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + v + "/shenxian/" + stockId + "/"
			+ dateFrom + "_" + dateTo;
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
			createChart();
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
    var v = "1";
    if(version == 'v2'){
    	v = "2";
    }
	/**
	 * Create the chart when all data is loaded
	 * 
	 * @returns {undefined}
	 */
	function createChart() {
		$('#container').highcharts('StockChart', {

			rangeSelector : {
				selected : 1
			},

			title : {
				text : stockId
			},

			yAxis : [ {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'Price'
				},
				height : '60%',
				lineWidth : 2
			}, {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'Volume'
				},
				top : '65%',
				height : '35%',
				offset : 0,
				lineWidth : 2
			} ],

			series : [ {
				type : 'candlestick',
				name : 'OHLC',
				data : date_price
			}, {
				name : 'MA19',
				data : data_ma19
			}, {
				name : 'MA43',
				data : data_ma43
			}, {
				name : 'MA86',
				data : data_ma86
			}, {
				type : 'column',
				name : 'Volume',
				data : volume,
				yAxis : 1
			} ]
		});
	}

	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = "http://localhost:8080/portal/price/" + stockId + "/"
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
			createChart();
		}
	});

	/**
	 * Load ShenXian Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + v + "/luzao/" + stockId + "/"
			+ dateFrom + "_" + dateTo;
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
			createChart();
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
    var v = "1";
    if(version == 'v2'){
    	v = "2";
    }
	/**
	 * Create the chart when all data is loaded
	 * 
	 * @returns {undefined}
	 */
	function createChart() {
		$('#container').highcharts('StockChart', {

			rangeSelector : {
				selected : 1
			},

			title : {
				text : stockId
			},

			yAxis : [ {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'Price'
				},
				height : '60%',
				lineWidth : 2
			}, {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'Volume'
				},
				top : '65%',
				height : '35%',
				offset : 0,
				lineWidth : 2
			} ],

			series : [ {
				type : 'candlestick',
				name : 'OHLC',
				data : date_price
			}, {
				name : 'MB',
				data : data_mb
			}, {
				name : 'UP',
				data : data_up
			}, {
				name : 'DN',
				data : data_dn
			}, {
				type : 'column',
				name : 'Volume',
				data : volume,
				yAxis : 1
			} ]
		});
	}

	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = "http://localhost:8080/portal/price/" + stockId + "/"
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
			createChart();
		}
	});

	/**
	 * Load ShenXian Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + v + "/boll/" + stockId + "/"
			+ dateFrom + "_" + dateTo;
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
			createChart();
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
    var v = "1";
    if(version == 'v2'){
    	v = "2";
    }
	/**
	 * Create the chart when all data is loaded
	 * 
	 * @returns {undefined}
	 */
	function createChart() {
		$('#container').highcharts('StockChart', {

			rangeSelector : {
				selected : 1
			},

			title : {
				text : stockId
			},

			yAxis : [ {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'Price'
				},
				height : '60%',
				lineWidth : 2
			}, {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'MACD'
				},
				top : '65%',
				height : '35%',
				offset : 0,
				lineWidth : 2
			} ],

			series : [ {
				type : 'candlestick',
				name : 'OHLC',
				data : date_price
			}, {
				name : 'DIF',
				data : data_dif,
				yAxis : 1
			}, {
				name : 'DEA',
				data : data_dea,
				yAxis : 1
			}, {
				name : 'MACD',
				data : data_macd,
				yAxis : 1
			} ]
		});
	}

	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = "http://localhost:8080/portal/price/" + stockId + "/"
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
			createChart();
		}
	});

	/**
	 * Load ShenXian Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + v + "/macd/" + stockId + "/"
			+ dateFrom + "_" + dateTo;
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
			createChart();
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
    var v = "1";
    if(version == 'v2'){
    	v = "2";
    }
	/**
	 * Create the chart when all data is loaded
	 * 
	 * @returns {undefined}
	 */
	function createChart() {
		$('#container').highcharts('StockChart', {

			rangeSelector : {
				selected : 1
			},

			title : {
				text : stockId
			},

			yAxis : [ {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'Price'
				},
				height : '60%',
				lineWidth : 2
			}, {
				labels : {
					align : 'right',
					x : -3
				},
				title : {
					text : 'QSDD'
				},
				top : '65%',
				height : '35%',
				offset : 0,
				lineWidth : 2
			} ],

			series : [ {
				type : 'candlestick',
				name : 'OHLC',
				data : date_price
			}, {
				name : 'LonTerm',
				data : data_lonTerm,
				yAxis : 1
			}, {
				name : 'MidTerm',
				data : data_midTerm,
				yAxis : 1
			}, {
				name : 'ShoTerm',
				data : data_shoTerm,
				yAxis : 1
			} ]
		});
	}

	/**
	 * Load StocPrice and display OHLC
	 * 
	 * @returns {undefined}
	 */
	var url_price = "http://localhost:8080/portal/price/" + stockId + "/"
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
			createChart();
		}
	});

	/**
	 * Load ShenXian Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + v + "/qsdd/" + stockId + "/"
			+ dateFrom + "_" + dateTo;
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
			createChart();
		}
	});
}