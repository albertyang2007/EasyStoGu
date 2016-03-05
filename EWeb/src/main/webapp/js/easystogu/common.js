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
	var url_trend = "http://localhost:8080/portal/trendmode/query/" + name;
	var prices = "[]";
	$.getJSON(url_trend, function(data) {
		prices = data;
	});
	return prices;
}

/**
 * Load all TrendMode names
 * 
 * @returns {undefined}
 */
function getAllTrendModeNames() {
	var url_names = "http://localhost:8080/portal/trendmode/listnames";
	var names = "[]";
	$.getJSON(url_names, function(data) {
		names = data;
	});
	return names;
}

/*
 * get the query parameters from http GET request, for example
 * http://localhost:8080/query?name=value
 * return value
 */
function getQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

/**
 * Load TrendMode Price
 * 
 * @returns {undefined}
 */
function loadStockPrice(stockId, dateFrom, dateTo) {
	var url_price = "http://localhost:8080/portal/price/" + stockId + "/"
			+ dateFrom + "_" + dateTo;
	var data_price = [];
	$.getJSON(url_price, function(data) {
		data_price = data;
	});
	return data_price;
}

/**
 * Create the StockPrice and luzao chart
 * 
 * @returns {undefined}
 */
function createChart_LuZao(stockId, date_price, volume, data_ma19, data_ma43,
		data_ma86) {
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

	chart = $('#container').highcharts();
}

/**
 * Create the StockPrice and shenxian chart
 * 
 * @returns {undefined}
 */
function createChart_ShenXian(stockId, date_price, volume, data_h1, data_h2,
		data_h3) {
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
 * Create the StockPrice and boll chart
 * 
 * @returns {undefined}
 */
function createChart_Boll(stockId, date_price, volume, data_mb, data_up,
		data_dn) {
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
 * Create the StockPrice and macd chart
 * 
 * @returns {undefined}
 */
function createChart_Macd(stockId, date_price, volume, data_dif, data_dea,
		data_macd) {
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
 * Create the StockPrice and qsdd chart
 * 
 * @returns {undefined}
 */
function createChart_Qsdd(stockId, date_price, volume, data_lonTerm,
		data_midTerm, data_shoTerm) {
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
 * Create the StockPrice Candlestick chart
 * 
 * @returns {undefined}
 */
function createChart_Candlestick(stockId, date_price, volume) {
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
			type : 'column',
			name : 'Volume',
			data : volume,
			yAxis : 1
		} ]
	});

	chart = $('#container').highcharts();
}

/*
 * convert from data to candlestick data
 */
function convert2Candlestick(data) {
	// push data to price candlestick
	var date_price = [];
	i = 0;
	for (i; i < data.length; i += 1) {
		var dateStr = data[i]['date'] + " 15:00:00";
		var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
		date_price.push([ dateD.getTime(), data[i]['open'], data[i]['high'],
				data[i]['low'], data[i]['close'] ]);
	}
	return date_price;
}

/*
 * convert from data to volumn data
 */
function convert2Volume(data) {
	// push data to price candlestick
	var volume = [];
	i = 0;
	for (i; i < data.length; i += 1) {
		var dateStr = data[i]['date'] + " 15:00:00";
		var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
		volume.push([ dateD.getTime(), data[i]['volume'] ]);
	}
	return volume;
}