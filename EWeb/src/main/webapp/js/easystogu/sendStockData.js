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
		$('#container').highcharts('StockChart', {		
	        
			chart: {
	        },
	        
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
				data : date_price,
				events: {
                    click: function (event) {
                        alert(this.name + "; dateTime=" + event.point.x + "; category=" + event.point.category + "; chart.series.length=" + chart.series.length);
                        
                        for(var i = 0;i < chart.series.length;i++){
                            if(this.name == chart.series[i].name){
                              alert(JSON.stringify(chart.series[i].data));
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
		
		chart =  $('#container').highcharts();
	}

	var forecastData = [ {
		"stockId" : stockId,
		"date" : "2016-02-22",
		"open" : "19.99",
		"close" : "20.50",
		"low" : "19.70",
		"high" : "20.80",
		"volume" : "44000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-02-23",
		"open" : "20.80",
		"close" : "20.70",
		"low" : "20.05",
		"high" : "21.10",
		"volume" : "45000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-02-24",
		"open" : "20.50",
		"close" : "21.80",
		"low" : "20.50",
		"high" : "22.00",
		"volume" : "46000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-02-25",
		"open" : "21.80",
		"close" : "20.90",
		"low" : "20.70",
		"high" : "22.20",
		"volume" : "54000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-02-26",
		"open" : "20.70",
		"close" : "22.00",
		"low" : "20.50",
		"high" : "22.00",
		"volume" : "64000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-02-29",
		"open" : "22.20",
		"close" : "22.50",
		"low" : "21.70",
		"high" : "22.80",
		"volume" : "84000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-01",
		"open" : "23.00",
		"close" : "23.50",
		"low" : "22.10",
		"high" : "23.99",
		"volume" : "78000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-02",
		"open" : "22.90",
		"close" : "22.00",
		"low" : "22.01",
		"high" : "23.30",
		"volume" : "83000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-03",
		"open" : "22.01",
		"close" : "21.05",
		"low" : "21.05",
		"high" : "22.30",
		"volume" : "74000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-04",
		"open" : "21.50",
		"close" : "21.20",
		"low" : "20.80",
		"high" : "22.00",
		"volume" : "78000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-07",
		"open" : "21.20",
		"close" : "22.50",
		"low" : "21.20",
		"high" : "22.80",
		"volume" : "84000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-08",
		"open" : "22.40",
		"close" : "23.50",
		"low" : "22.10",
		"high" : "23.99",
		"volume" : "78000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-09",
		"open" : "24.00",
		"close" : "24.99",
		"low" : "23.51",
		"high" : "25.10",
		"volume" : "83000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-10",
		"open" : "26.60",
		"close" : "26.70",
		"low" : "25.95",
		"high" : "27.00",
		"volume" : "74000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-11",
		"open" : "26.50",
		"close" : "26.00",
		"low" : "25.90",
		"high" : "27.00",
		"volume" : "78000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-14",
		"open" : "25.80",
		"close" : "25.50",
		"low" : "25.20",
		"high" : "25.99",
		"volume" : "84000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-15",
		"open" : "25.99",
		"close" : "26.99",
		"low" : "24.10",
		"high" : "27.99",
		"volume" : "78000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-16",
		"open" : "27.30",
		"close" : "26.80",
		"low" : "26.78",
		"high" : "27.50",
		"volume" : "83000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-17",
		"open" : "26.60",
		"close" : "27.20",
		"low" : "26.50",
		"high" : "27.99",
		"volume" : "74000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-18",
		"open" : "28.50",
		"close" : "29.00",
		"low" : "27.20",
		"high" : "30.00",
		"volume" : "78000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-21",
		"open" : "30.00",
		"close" : "29.50",
		"low" : "25.20",
		"high" : "30.00",
		"volume" : "84000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-22",
		"open" : "29.40",
		"close" : "30.90",
		"low" : "29.40",
		"high" : "31.00",
		"volume" : "78000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-23",
		"open" : "31.30",
		"close" : "32.90",
		"low" : "30.80",
		"high" : "33.50",
		"volume" : "83000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-24",
		"open" : "33.01",
		"close" : "34.20",
		"low" : "32.50",
		"high" : "35.24",
		"volume" : "74000000"
	}, {
		"stockId" : stockId,
		"date" : "2016-03-25",
		"open" : "35.00",
		"close" : "36.50",
		"low" : "35.00",
		"high" : "37.00",
		"volume" : "78000000"
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