function loadStockData(stockId, dateFrom, dateTo) {
    var seriesOptions = [],
        seriesCounter = 0,
        date_price = [],
        volume = [],
        data_h1 = [],
        data_h2 = [],
        data_h3 = [];  
		
    /**
     * Create the chart when all data is loaded
     * @returns {undefined}
     */
    function createChart() {
        $('#container').highcharts('StockChart', {

                rangeSelector: {
		                selected: 1
		            },
		
		            title: {
		                text: 'OHLC'
		            },
		
		            yAxis: [{
		                labels: {
		                    align: 'right',
		                    x: -3
		                },
		                title: {
		                    text: 'Price'
		                },
		                height: '60%',
		                lineWidth: 2
		            }, {
		                labels: {
		                    align: 'right',
		                    x: -3
		                },
		                title: {
		                    text: 'Volume'
		                },
		                top: '65%',
		                height: '35%',
		                offset: 0,
		                lineWidth: 2
		            }],
		
		            series: [{
		                type: 'candlestick',
		                name: 'OHLC',
		                data: date_price
		            }, 
		            {
		                name: 'H1',
		                data: data_h1
		            }, 
		            {
		                name: 'H2',
		                data: data_h2
		            }, 
		            {
		                name: 'H3',
		                data: data_h3
		            },                         
		               {
		                type: 'column',
		                name: 'Volume',
		                data: volume,
		                yAxis: 1
		            }]
        });
    }


    var url_price = "http://localhost:8080/portal/price/" + stockId + "/" + dateFrom + "_" + dateTo;
    $.getJSON(url_price,    function (data) {
        i = 0;
        for (i; i < data.length; i += 1) {
        	var dateStr = data[i]['date'] + " 15:00:00";
            var dateD = new Date(Date.parse(dateStr.replace(/-/g,   "/")));
            date_price.push([
                dateD.getTime(),
                data[i]['open'],
                data[i]['high'],
                data[i]['low'],
                data[i]['close']
            ]);
            
            volume.push([
                dateD.getTime(),
                data[i]['volume']
            ]);  
        }
        
        seriesCounter += 1;
        if (seriesCounter === 2) {
            createChart();
        }
    });

    var url_shenxian = "http://localhost:8080/portal/ind/shenxian/" + stockId + "/" + dateFrom + "_" + dateTo;
    $.getJSON(url_shenxian,    function (data) {
        i = 0;
        for (i; i < data.length; i += 1) {
        	var dateStr = data[i]['date'] + " 15:00:00";
            var dateD = new Date(Date.parse(dateStr.replace(/-/g,   "/")));
            data_h1.push([
                dateD.getTime(),
                data[i]['h1']
            ]);

            data_h2.push([
                dateD.getTime(),
                data[i]['h2']
            ]);
            
            data_h3.push([
                dateD.getTime(),
                data[i]['h3']
            ]);
        }
        
        seriesCounter += 1;
        if (seriesCounter === 2) {
            createChart();
        }
    });
}