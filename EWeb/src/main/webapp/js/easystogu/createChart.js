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

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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
 * Create the StockPrice and luzao trend phase chart, and luzao statistics (4
 * phase) chart
 * 
 * @returns {undefined}
 */
function createChart_LuZao_Trend_Statistics(stockId, date_price, volume,
		data_ma19, data_ma43, data_ma86, data_1_guancha, data_2_jiancang,
		data_3_chigu, data_4_jiancang) {
	$('#container').highcharts('StockChart', {

		rangeSelector : {
			selected : 1
		},

		title : {
			text : stockId
		},

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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
			name : '观察个股数目',
			data : data_1_guancha,
			yAxis : 1
		}, {
			name : '建仓个股数目',
			data : data_2_jiancang,
			yAxis : 1
		}, {
			name : '持股个股数目',
			data : data_3_chigu,
			yAxis : 1
		}, {
			name : '减仓阶段数目',
			data : data_4_jiancang,
			yAxis : 1
		} ]
	});

	chart = $('#container').highcharts();
}

/**
 * Create the StockPrice and luzao gordon dead chart, and luzao statistics (3
 * gordon and 2 dead ) chart
 * 
 * @returns {undefined}
 */
function createChart_LuZao_Gordon_Statistics(stockId, date_price, volume,
		data_ma19, data_ma43, data_ma86, data_1_gordon0, data_2_gordon1,
		data_3_gordon2, data_4_dead1, data_5_dead2) {
	$('#container').highcharts('StockChart', {

		rangeSelector : {
			selected : 1
		},

		title : {
			text : stockId
		},

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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
			name : '升越良山数目',
			data : data_2_gordon1,
			yAxis : 1
		}, {
			name : '山腰乘凉数目',
			data : data_3_gordon2,
			yAxis : 1
		}, {
			name : '三山重叠数目',
			data : data_4_dead1,
			yAxis : 1
		}, {
			name : '跌到山腰数目',
			data : data_5_dead2,
			yAxis : 1
		}, {
			name : '跌到山脚数目',
			data : data_1_gordon0,
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

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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

	chart = $('#container').highcharts();
}

/**
 * Create the StockPrice and shenxian chart, and statistics chart
 * 
 * @returns {undefined}
 */
function createChart_ShenXian_Statistics(stockId, date_price, volume, data_h1,
		data_h2, data_h3, data_gordon, data_dead) {
	$('#container').highcharts('StockChart', {

		rangeSelector : {
			selected : 1
		},

		title : {
			text : stockId
		},

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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
			name : 'H1',
			data : data_h1
		}, {
			name : 'H2',
			data : data_h2
		}, {
			name : 'H3',
			data : data_h3
		}, {
			name : '金叉个股数目',
			data : data_gordon,
			yAxis : 1
		}, {
			name : '死叉个股数目',
			data : data_dead,
			yAxis : 1
		} ]
	});

	chart = $('#container').highcharts();
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

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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

	chart = $('#container').highcharts();
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

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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

	chart = $('#container').highcharts();
}

/**
 * Create the StockPrice and macd statistics chart
 * 
 * @returns {undefined}
 */
function createChart_Macd_Statistics(stockId, date_price, volume, data_dif,
		data_dea, data_macd, data_gordon, data_dead) {
	$('#container').highcharts('StockChart', {

		rangeSelector : {
			selected : 1
		},

		title : {
			text : stockId
		},

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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
			name : '金叉个股数目',
			data : data_gordon,
			yAxis : 1
		}, {
			name : '死叉个股数目',
			data : data_dead,
			yAxis : 1
		} ]
	});

	chart = $('#container').highcharts();
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

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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

	chart = $('#container').highcharts();
}

/**
 * Create the StockPrice and qsdd chart, and qsdd statistics (Top and Bottom)
 * chart.
 * 
 * @returns {undefined}
 */
function createChart_Qsdd_Statistics(stockId, date_price, volume, data_lonTerm,
		data_midTerm, data_shoTerm, date_topArea, date_bottomArea,
		date_bottomGordon) {
	$('#container').highcharts('StockChart', {

		rangeSelector : {
			selected : 1
		},

		title : {
			text : stockId
		},

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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
			data : date_topArea,
			yAxis : 2
		}, {
			name : '见底个股数目',
			data : date_bottomArea,
			yAxis : 2
		}, {
			name : '金叉个股数目',
			data : date_bottomGordon,
			yAxis : 2
		} ]
	});

	chart = $('#container').highcharts();
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

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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

/**
 * Create the StockPrice and wr chart, and qsdd statistics (Top and Bottom)
 * chart.
 * 
 * @returns {undefined}
 */
function createChart_WR_Statistics(stockId, date_price, volume, data_lonTerm,
		data_midTerm, data_shoTerm, date_topArea, date_bottomArea,
		date_bottomGordon) {
	$('#container').highcharts('StockChart', {

		rangeSelector : {
			selected : 1
		},

		title : {
			text : stockId
		},

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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
				text : 'WR'
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
			data : date_topArea,
			yAxis : 2
		}, {
			name : '见底个股数目',
			data : date_bottomArea,
			yAxis : 2
		}, {
			name : '金叉个股数目',
			data : date_bottomGordon,
			yAxis : 2
		} ]
	});

	chart = $('#container').highcharts();
}

/**
 * Create the StockPrice and qsdd chart
 * 
 * @returns {undefined}
 */
function createChart_WR(stockId, date_price, volume, data_lonTerm,
		data_midTerm, data_shoTerm) {
	$('#container').highcharts('StockChart', {

		rangeSelector : {
			selected : 1
		},

		title : {
			text : stockId
		},

		plotOptions : {
			candlestick : {
				color : '#00ff00',// Green
				upColor : '#ff0000'// Red
			}
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
				text : 'WR'
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

	chart = $('#container').highcharts();
}

