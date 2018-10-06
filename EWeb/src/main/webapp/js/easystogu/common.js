/**
 * Global variable definition
 * 
 * @returns {undefined}
 */
function getEasyStoGuServerUrl() {
	// return "http://www.mawaq.com:18080";
	return "http://www.openstock.cn:18080";
	// return "http://192.168.208.1:8080";
	// return "http://localhost:8080";
}

/**
 * Load CompanyBaseInfo
 * 
 * @returns {undefined}
 */
function loadCompanyInfo(stockId) {
	var url_company = getEasyStoGuServerUrl() + "/portal/company/" + stockId;
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
	var url_company = getEasyStoGuServerUrl() + "/portal/company/name=" + name;
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
	var url_trend = getEasyStoGuServerUrl() + "/portal/trendmode/query/" + name;
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
	var url_names = getEasyStoGuServerUrl() + "/portal/trendmode/listnames";
	var names = "[]";
	$.getJSON(url_names, function(data) {
		names = data;
	});
	return names;
}

/**
 * Load latest N date
 * 
 * @returns {undefined}
 */
function getLatestNDate(limit) {
	var url_dates = getEasyStoGuServerUrl() + "/portal/company/latestndate/"
			+ limit;
	var dates = "[]";
	$.getJSON(url_dates, function(data) {
		dates = data;
	});
	return dates;
}

/**
 * Load all stockIds from View
 * 
 * @returns {undefined}
 */
function getAllStockIdsFromView(viewName) {
	var url = getEasyStoGuServerUrl() + "/portal/view/" + viewName;
	var stockIds = [];
	$.getJSON(url, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			stockIds.push([ data[i]['stockId'], data[i]['name'] ]);
		}
	});
	return stockIds;
}

/**
 * Load all stockIds from View
 * 
 * @returns {undefined}
 */
function getFavoritesStockIds(date) {
	var url = getEasyStoGuServerUrl() + "/portal/favorites?date=" + date;
	var stockIds = [];
	$.getJSON(url, function(data) {
		i = 0;
		for (i; i < data.length; i += 1) {
			stockIds.push([ data[i]['stockId'], data[i]['name'] ]);
		}
	});
	return stockIds;
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
	var url_price = getEasyStoGuServerUrl() + "/portal/price" + version + "/"
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

/*
 * add stockId to favorites
 */
function addToFavorites(stockId) {
	var userId = "admin";
	var url_price = getEasyStoGuServerUrl() + "/portal/favorites/" + userId + "/"+ stockId;
	$.ajax({
		type : "POST",
		url : url_price,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		success : function(data) {
			alert("Success add to favorites");
		}
	});
}

/*
 * delete stockId from favorites
 */
function deleteFromFavorites(stockId) {
	var userId = "admin";
	var url_price = getEasyStoGuServerUrl() + "/portal/favorites/" + userId + "/"+ stockId;
	$.ajax({
		type : "DELETE",
		url : url_price,
		processData : false,
		contentType : 'application/json; charset=utf-8',
		success : function(data) {
			alert("Success delete from favorites");
		}
	});
}

/*
 * get favorites stockId by userId
 */
function getFavoritesStockIds(userId) {
	var url_price = getEasyStoGuServerUrl() + "/portal/favorites/" + userId;
	var data_price = [];
	$.getJSON(url_price, function(data) {
		data_price = data;
	});
	return data_price;
}