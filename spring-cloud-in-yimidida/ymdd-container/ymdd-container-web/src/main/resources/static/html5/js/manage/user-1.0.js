/**
 * 提交注册信息
 */
$("#submitBtn").bind('click', function() {
	var mobile = $.trim($("#mobile").val());
	if (mobile == '') {
		layer.msg("手机号不能为空", {
			time : 1000
		});
		return false;
	}
	var mobileRegex = /^1[34578]\d{9}$/; // 判断手机号正则表达式
	if (!mobileRegex.exec(mobile)) {
		layer.msg("手机号码格式有误!", {
			time : 1000
		});
		return false;
	}
	var name = $.trim($("#name").val());
	if (name == '') {
		layer.msg("姓名不能为空", {
			time : 1000
		});
		return false;
	}
	var password = $.trim($("#password").val());
	var pwd = $.trim($("#pwd").val());
	if (password == '' || pwd == '') {
		layer.msg("密码不能为空", {
			time : 1000
		});
		return false;
	}
	if (password != pwd) {
		layer.msg("两次密码不一致", {
			time : 1000
		});
		return false;
	}
	var smsCode = $.trim($("#smsCode").val());
	if (smsCode == '') {
		layer.msg("验证码不能为空", {
			time : 1000
		});
		return false;
	}
	$.ajax({
		url : "/app/web/register",
		data : JSON.stringify({
			"data" : {
				"name" : name,
				"mobile" : mobile,
				"password" : password,
				"code" : $("#code").val(),
				"smsCode" : smsCode
			}
		}),
		dataType : "JSON",
		contentType : "application/json",
		type : "POST",
		success : function(d) {
			if (d.code == '200') {
				$("#register").hide();
				$("#download").show();
			} else {
				layer.msg(d.message, {
					time : 1000
				});
			}
		}
	});
});


$("#submitDownloadBtn").bind('click', function() {
    var mobile = $.trim($("#mobile").val());
    if (mobile == '') {
        layer.msg("手机号不能为空", {
            time : 1000
        });
        return false;
    }
    var mobileRegex = /^1[34578]\d{9}$/; // 判断手机号正则表达式
    if (!mobileRegex.exec(mobile)) {
        layer.msg("手机号码格式有误!", {
            time : 1000
        });
        return false;
    }
    var smsCode = $.trim($("#smsCode").val());
    if (smsCode == '') {
        layer.msg("验证码不能为空", {
            time : 1000
        });
        return false;
    }
    $.ajax({
        url : "/app/web/version/userDownload",
        data : JSON.stringify({
            "data" : {
                "name" : name,
                "mobile" : mobile,
                "code" : $("#code").val(),
                "smsCode" : smsCode
            }
        }),
        dataType : "JSON",
        contentType : "application/json",
        type : "POST",
        success : function(d) {
            if (d.code == '200') {
                // $("#register").hide();
                // $("#download").show();
                var app = navigator.userAgent;
                if (app.indexOf('Android') > -1 || app.indexOf('Linux') > -1) {
                    $.ajax({
                        type : "POST",
                        dataType : "JSON",
                        contentType : "application/json",
                        url : "/app/web/html5/downlink",
                        data : JSON.stringify({
                            "data" : $.trim($("#mobile").val())
                        }),
                        success : function(d) {
                            if (d.code == '200') {
                                window.location.href = d.data;
                            } else {
                                layer.msg(d.message, {
                                    time : 1000
                                });
                            }
                        }
                    });
                    return;
                }
                if (app.indexOf('iPhone') > -1) {
                    layer.alert("很抱歉, 暂不支持苹果版!", {
                        time : 1800
                    });
                    return;
                }
            } else {
                layer.msg(d.message, {
                    time : 1000
                });
            }
        }
    });
});
/**
 * 立即下载 1、只有在不同域名或同域不同端口, 微信H5才会弹出外部浏览器下载 2、微信内置浏览器不支持内置流式下载
 */
$("#downlink").bind('click', function() {
	var app = navigator.userAgent;
	if (app.indexOf('Android') > -1 || app.indexOf('Linux') > -1) {
		$.ajax({
			type : "POST",
			dataType : "JSON",
			contentType : "application/json",
			url : "/app/web/html5/downlink",
			data : JSON.stringify({
				"data" : $.trim($("#mobile").val())
			}),
			success : function(d) {
				if (d.code == '200') {
					window.location.href = d.data;
				} else {
					layer.msg(d.message, {
						time : 1000
					});
				}
			}
		});
		return;
	}
	if (app.indexOf('iPhone') > -1) {
		layer.alert("很抱歉, 暂不支持苹果版!", {
			time : 1800
		});
		return;
	}
});