/**
 * post stock price to server for forecast process
 * 
 * @returns {undefined}
 */
function postStockPrice(stockId, dateFrom, dateTo, trendModePrices) {
	var seriesCounter = 0, date_price = [], volume = [], data_ma19 = [], data_ma43 = [], data_ma86 = [];
	var v = "3";
	/**
	 * Create the chart when all data is loaded
	 * 
	 * @returns {undefined}
	 */
	function createChart() {
		$('#container').highcharts(
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
										alert(this.name + "; dateTime="
												+ event.point.x + "; category="
												+ event.point.category
												+ "; chart.series.length="
												+ chart.series.length);
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
		data : JSON.stringify(trendModePrices),
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
		data : JSON.stringify(trendModePrices),
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