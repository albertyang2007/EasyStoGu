/**
 * Load LuZao and Trend_Phase Satistics
 * 
 * @returns {undefined}
 */
function loadLuZaoStatistics(version, stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_ma19 = [], data_ma43 = [], data_ma86 = [], count_I = [], count_II = [], count_III = [], count_VI = [];
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
				name : 'I_GuanCha',
				data : count_I,
				yAxis : 1
			}, {
				name : 'II_JianCang',
				data : count_II,
				yAxis : 1
			}, {
				name : 'III_ChiGu',
				data : count_III,
				yAxis : 1
			}, {
				name : 'VI_JianCang',
				data : count_VI,
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
	var url_ind = "http://localhost:8080/portal/statistics/luzao_trend_phase/"
			+ dateFrom + "_" + dateTo;
	$.getJSON(url_ind, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			var dateStr = data[i]['date'] + " 15:00:00";
			var dateD = new Date(Date.parse(dateStr.replace(/-/g, "/")));
			count_I.push([ dateD.getTime(), data[i]['count_I_GuanCha'] ]);
			count_II.push([ dateD.getTime(), data[i]['count_II_JianCang'] ]);
			count_III.push([ dateD.getTime(), data[i]['count_III_ChiGu'] ]);
			count_VI.push([ dateD.getTime(), data[i]['count_VI_JianCang'] ]);
		}

		seriesCounter += 1;
		if (seriesCounter === 3) {
			createChart();
		}
	});
}