/**
 * Load LuZao and Trend_Phase Satistics
 * 
 * @returns {undefined}
 */
function loadLuZaoStatistics(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_ma19 = [], data_ma43 = [], data_ma86 = [], count1 = [], count2 = [], count3 = [], count4 = [];
	var v = "1";
	if (version == 'v2') {
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
					text : 'Statistics'
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
				name : '观察阶段数目',
				data : count1,
				yAxis : 1
			}, {
				name : '建仓阶段数目',
				data : count2,
				yAxis : 1
			}, {
				name : '持股阶段数目',
				data : count3,
				yAxis : 1
			}, {
				name : '减仓阶段数目',
				data : count4,
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
		if (seriesCounter === 3) {
			createChart();
		}
	});

	/**
	 * Load luzao Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + v + "/luzao/" + stockId
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
		if (seriesCounter === 3) {
			createChart();
		}
	});

	/**
	 * Load luzao trend phase statistics and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/statistics/luzao/" + dateFrom
			+ "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			count1.push([ dateD.getTime(), data[i]['count1'] ]);
			count2.push([ dateD.getTime(), data[i]['count2'] ]);
			count3.push([ dateD.getTime(), data[i]['count3'] ]);
			count4.push([ dateD.getTime(), data[i]['count4'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart();
		}
	});
}

/**
 * Load Qsdd and Top Bottom Satistics
 * 
 * @returns {undefined}
 */
function loadQsddStatistics(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_lonTerm = [], data_midTerm = [], data_shoTerm = [], count1 = [], count2 = [], count3 = [];
	var v = "1";
	if (version == 'v2') {
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
				name : '短期线',
				data : data_shoTerm,
				yAxis : 1
			}, {
				name : '中期线',
				data : data_midTerm,
				yAxis : 1
			}, {
				name : '长期线',
				data : data_lonTerm,
				yAxis : 1
			}, {
				name : '见顶个股数目',
				data : count1,
				yAxis : 2
			}, {
				name : '见底个股数目',
				data : count2,
				yAxis : 2
			}, {
				name : '金叉个股数目',
				data : count3,
				yAxis : 2
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
		if (seriesCounter === 3) {
			createChart();
		}
	});

	/**
	 * Load luzao Indicator and display
	 * 
	 * @returns {undefined}
	 */
	var url_ind = "http://localhost:8080/portal/ind" + v + "/qsdd/" + stockId
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
		if (seriesCounter === 3) {
			createChart();
		}
	});

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
			count1.push([ dateD.getTime(), data[i]['count1'] ]);
			count2.push([ dateD.getTime(), data[i]['count2'] ]);
			count3.push([ dateD.getTime(), data[i]['count3'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart();
		}
	});
}