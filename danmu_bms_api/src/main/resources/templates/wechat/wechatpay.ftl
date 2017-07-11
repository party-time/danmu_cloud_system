<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>聚时代-付费</title>
    <base href="/">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, user-scalable=no">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-touch-fullscreen" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="screen-orientation" content="portrait">
  <meta name="x5-orientation" content="portrait">
    <link rel="stylesheet" href="./css/wechat/weui.min.css"/>
</head>
<body>
     <image src="/images/wechat/paybg.png" style="position: absolute;width: 100%;overflow: hidden;"/>
          <image src="/images/wechat/noname.png" style="position: absolute;width: 30%;overflow: hidden;top:42%;margin-left: 8%;" id="chooseNoNameWXPay"/>
          <image src="/images/wechat/pay.png" style="position: absolute;width: 30%;overflow: hidden;top:42%;margin-left: 63%;" id="chooseWXPay"/>

<script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
 <script src="./js/wechat/jquery.min.js"></script>
 <script>
        wx.config({
              debug: false,
              appId: '${wxJsConfig.appId}',
              timestamp: ${wxJsConfig.timestamp},
              nonceStr: '${wxJsConfig.nonceStr}',
              signature: '${wxJsConfig.signature}',
              jsApiList: [
                'checkJsApi',
                'chooseWXPay'
              ]
          });
          var wechatPay = function(attach){
            // 注意：此 Demo 使用 2.7 版本支付接口实现，建议使用此接口时参考微信支付相关最新文档。
              $.ajax({
                  url: "/wechat/pay?nonceStr=${wxJsConfig.nonceStr}&openId=${opendId}&timestamp=${wxJsConfig.timestamp}&h5TempId=592689fd0cf24d415c21be6a&attach="+attach,
                  type: "get"
              }).done(function (data) {
                      wx.chooseWXPay({
                            timestamp: data.timeStamp,
                            nonceStr: data.nonceStr,
                            package: data.packageStr,
                            signType: data.signType, // 注意：新版支付接口使用 MD5 加密
                            paySign: data.paySign,
                            success: function (res) {
                                window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=${wxJsConfig.appId}&redirect_uri=http://www.party-time.cn/wechat/payIndex&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
                                alert('支付成功');
                            }
                      });

                });
          }
          document.querySelector('#chooseWXPay').onclick = function () {
                wechatPay('dmId:592525d30cf26a8f85eeac21,h5Id:592689fd0cf24d415c21be6a,name:name');
            };
            document.querySelector('#chooseNoNameWXPay').onclick = function () {
                 wechatPay('dmId:592525d30cf26a8f85eeac21,h5Id:592689fd0cf24d415c21be6a,name:noname');
            };
    </script>
</body>
</html>