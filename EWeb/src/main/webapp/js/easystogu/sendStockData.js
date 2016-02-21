/**
 * post stock price to server for forecast process
 * 
 * @returns {undefined}
 */
function postStockPrice(stockId, dateFrom, dateTo) {
	var seriesCounter = 0, date_price = [], volume = [], data_ma19 = [], data_ma43 = [], data_ma86 = [];
	var v = "3";
	/**
	 * Create the chart when all data is loaded
	 * 
	 * @returns {undefined}
	 */
	function createChart() {
		$('#container')
				.highcharts(
						'StockChart',
						{

							chart : {},

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

							series : [
									{
										type : 'candlestick',
										name : 'OHLC',
										data : date_price,
										events : {
											click : function(event) {
												alert(this.name
														+ "; dateTime="
														+ event.point.x
														+ "; category="
														+ event.point.category
														+ "; chart.series.length="
														+ chart.series.length);

												for (var i = 0; i < chart.series.length; i++) {
													if (this.name == chart.series[i].name) {
														alert(chart.series[i].data);
														break;
													}
												}
											}
										}
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

	var forecastData = [ {
		"stockId" : stockId,
		"open" : "0.0",
		"close" : "2.50",
		"low" : "-2.67",
		"high" : "4.56",
		"volume" : "1.3"
	}, {
		"stockId" : stockId,
		"open" : "1.89",
		"close" : "1.5",
		"low" : "-2.67",
		"high" : "4.76",
		"volume" : "1.1"
	}, {
		"stockId" : stockId,
		"open" : "1.50",
		"close" : "5.0",
		"low" : "-2.89",
		"high" : "4.0",
		"volume" : "0.9"
	}, {
		"stockId" : stockId,
		"open" : "-0.91",
		"close" : "-0.10",
		"low" : "-2.55",
		"high" : "4.45",
		"volume" : "1.1"
	}, {
		"stockId" : stockId,
		"open" : "1.12",
		"close" : "2.0",
		"low" : "-4.50",
		"high" : "5.90",
		"volume" : "0.89"
	}, {
		"stockId" : stockId,
		"open" : "-0.50",
		"close" : "4.56",
		"low" : "-1.10",
		"high" : "8.90",
		"volume" : "1.4"
	}, {
		"stockId" : stockId,
		"open" : "2.01",
		"close" : "3.59",
		"low" : "0.01",
		"high" : "4.01",
		"volume" : "0.87"
	}, {
		"stockId" : stockId,
		"open" : "-2.0",
		"close" : "-4.55",
		"low" : "-6.90",
		"high" : "0.50",
		"volume" : "1.1"
	}, {
		"stockId" : stockId,
		"open" : "0.10",
		"close" : "-3.5",
		"low" : "-7.90",
		"high" : "2.11",
		"volume" : "0.9"
	}, {
		"stockId" : stockId,
		"open" : "-1.10",
		"close" : "2.90",
		"low" : "-4.91",
		"high" : "4.01",
		"volume" : "1.10"
	}, {
		"stockId" : stockId,
		"open" : "1.01",
		"close" : "2.0",
		"low" : "-2.01",
		"high" : "2.59",
		"volume" : "0.9"
	}, {
		"stockId" : stockId,
		"open" : "-0.56",
		"close" : "2.45",
		"low" : "-3.90",
		"high" : "5.91",
		"volume" : "1.0"
	}, {
		"stockId" : stockId,
		"open" : "-1.50",
		"close" : "-4.21",
		"low" : "-10.0",
		"high" : "-1.50",
		"volume" : "0.7"
	}, {
		"stockId" : stockId,
		"open" : "1.19",
		"close" : "1.19",
		"low" : "-2.10",
		"high" : "3.09",
		"volume" : "0.75"
	}, {
		"stockId" : stockId,
		"open" : "3.68",
		"close" : "4.0",
		"low" : "-1.01",
		"high" : "4.0",
		"volume" : "1.4"
	}, {
		"stockId" : stockId,
		"open" : "-2.45",
		"close" : "-3.45",
		"low" : "-6.90",
		"high" : "-2.45",
		"volume" : "0.7"
	}, {
		"stockId" : stockId,
		"open" : "-5.90",
		"close" : "0.56",
		"low" : "-5.90",
		"high" : "1.03",
		"volume" : "1.0"
	}, {
		"stockId" : stockId,
		"open" : "0.90",
		"close" : "1.92",
		"low" : "-2.60",
		"high" : "3.56",
		"volume" : "1.1"
	}, {
		"stockId" : stockId,
		"open" : "-2.11",
		"close" : "3.56",
		"low" : "-4.17",
		"high" : "4.44",
		"volume" : "0.9"
	}, {
		"stockId" : stockId,
		"open" : "3.0",
		"close" : "2.11",
		"low" : "-4.10",
		"high" : "7.19",
		"volume" : "0.9"
	}, {
		"stockId" : stockId,
		"open" : "4.01",
		"close" : "3.19",
		"low" : "-1.10",
		"high" : "5.20",
		"volume" : "1.13"
	}, {
		"stockId" : stockId,
		"open" : "-2.91",
		"close" : "-1.90",
		"low" : "-6.19",
		"high" : "0.01",
		"volume" : "0.7"
	}, {
		"stockId" : stockId,
		"open" : "4.12",
		"close" : "5.12",
		"low" : "-3.89",
		"high" : "9.89",
		"volume" : "1.4"
	}, {
		"stockId" : stockId,
		"open" : "-0.10",
		"close" : "1.10",
		"low" : "-3.55",
		"high" : "3.11",
		"volume" : "0.8"
	}, {
		"stockId" : stockId,
		"open" : "3.0",
		"close" : "7.67",
		"low" : "0.56",
		"high" : "10.00",
		"volume" : "1.6"
	} ];

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	// post forecast stock price data and fetch back with full data
	var url_price = "http://localhost:8080/portal/price/forecast/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_price,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(forecastData),
		success : function(data) {
			// push data to price candlestick
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
		}
	});

	/*
	 * POST forecast sotck price and fetch back full price data
	 */
	var url_ind = "http://localhost:8080/portal/ind" + v + "/luzao/" + stockId
			+ "/" + dateFrom + "_" + dateTo;
	$.ajax({
		type : "POST",
		url : url_ind,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		data : JSON.stringify(forecastData),
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
				createChart();
			}
		}
	});
}