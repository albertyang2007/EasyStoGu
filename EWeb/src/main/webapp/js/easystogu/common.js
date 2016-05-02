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
 * http://localhost:8080/query?name=value return value
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
function loadStockPrice(version, stockId, dateFrom, dateTo) {
	var url_price = "http://localhost:8080/portal/price" + version + "/"
			+ stockId + "/" + dateFrom + "_" + dateTo;
	var data_price = [];
	$.getJSON(url_price, function(data) {
		data_price = data;
	});
	return data_price;
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