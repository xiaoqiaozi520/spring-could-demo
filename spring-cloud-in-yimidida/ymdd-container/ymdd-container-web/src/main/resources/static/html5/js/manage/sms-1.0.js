var clock = '';
var nums = 60;
var btn;
function sendCode(thisBtn , type) {
	var mobile = $.trim($("#mobile").val());
	if (mobile == '') {
		layer.msg("手机号不能为空!", {
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

	btn = thisBtn;
	btn.disabled = true; // 将按钮置为不可点击
	btn.value = nums + 's';
	clock = setInterval(doLoop, 1000); // 一秒执行一次
	$.ajax({
		url : "/app/web/sendSms",
		data : JSON.stringify({
			"data" : {
				"mobile" : mobile,
				"type" : type
			}
		}),
		dataType : "JSON",
		contentType : "application/json",
		type : "POST",
		success : function(d) {
			if (d.code == '200') {
				layer.msg("验证码已发送, 请查收短信!", {
					time : 1000
				});
			} else if (d.code == "10004") {
				window.clearTimeout(clock);
				nums = 60;
				$(".msgButton").removeAttr("disabled");
				$(".msgButton").val("获取验证码");
				$("#register").hide();
				$("#download").show();
				layer.msg(mobile + ',已经注册过', {
					time : 1000
				});
			} else {
				window.clearTimeout(clock);
				nums = 60;
				$(".msgButton").removeAttr("disabled");
				$(".msgButton").val("获取验证码");
				layer.msg(d.message, {
					time : 1000
				});
			}
		}
	});
}



function doLoop() {
	nums--;
	if (nums > 0) {
		btn.value = nums + 's';
	} else {
		clearInterval(clock); // 清除js定时器
		btn.disabled = false;
		btn.value = '获取验证码';
		nums = 60; // 重置时间
	}
}