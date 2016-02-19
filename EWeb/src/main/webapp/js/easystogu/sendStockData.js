/**
 * post stock price to server for forecast process
 * 
 * @returns {undefined}
 */
function postStockPrice(version, stockId, data) {
	var url_price = "http://localhost:8080/portal/ind" + v + "/shenxian/" + stockId + "/";
	var rtn = "N/A";
	
	var data = [{ "user" : "me!" }, { "user" : "you!" }];
	$.ajax({
	    type: "POST",
	    url: url_price,
	    processData: false,
	    contentType: 'application/json; charset=utf-8',
	    data: JSON.stringify(data),
	    success: function(r) {
	    	alert(r);
	    }
	});
	return rtn;
}