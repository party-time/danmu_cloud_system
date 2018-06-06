<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>获取地理位置</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, user-scalable=no">
      <meta name="apple-mobile-web-app-capable" content="yes">
      <meta name="apple-touch-fullscreen" content="yes">
      <meta name="apple-mobile-web-app-status-bar-style" content="black">
      <meta name="screen-orientation" content="portrait">
      <meta name="x5-orientation" content="portrait">
    <link rel="stylesheet" href="./css/weui.min.css"/>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>

</head>
<body>
    <div style="position:fixed;bottom:10%;right:15%;z-index:50">
        <img src="/images/wechat/dkan.png" style="width:100%" id="getLocation">
    </div>
    <img src="/images/wechat/dksm.png" style="width:100%;">

     <script>
             wx.config({
                   debug: false,
                   appId: '${wxJsConfig.appId}',
                   timestamp: ${wxJsConfig.timestamp},
                   nonceStr: '${wxJsConfig.nonceStr}',
                   signature: '${wxJsConfig.signature}',
                   jsApiList: [
                     'checkJsApi',
                     'getLocation'
                   ]
               });
                document.querySelector('#getLocation').onclick = function () {
                   wx.getLocation({
                     success: function (res) {
                        window.location.href="/wechat/setLocation?openId=${openId}&longitude="+res.longitude+"&latitude="+res.latitude;
                     },
                     cancel: function (res) {
                        alert("老司机定位失败，请打开“提供位置信息”");
                        window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=${url}/wechat/sendDM&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
                     }
                   });
                 };
         </script>
</body>
</html>