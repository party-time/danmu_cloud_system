var wechatpay = function(timestamp,nonceStr,paySign,package){
    wx.chooseWXPay({
        timestamp: timestamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
        nonceStr: nonceStr, // 支付签名随机串，不长于 32 位
        package: package, // 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***）
        signType: 'SHA1', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
        paySign: paySign, // 支付签名
        success: function (res) {
            // 支付成功后的回调函数
            alert('success');
        }
    });
}