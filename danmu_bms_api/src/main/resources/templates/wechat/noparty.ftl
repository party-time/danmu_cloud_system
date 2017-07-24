<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>聚时代-活动不存在</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait">
    <script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
</head>
<body>
<div style="position:absolute;left:0;right:0;top:0;bottom:0;">
    <button onclick="wxGetLocation()">test${canGetLocation}</button>
    <image src="/images/wechat/noparty/noparty.png" style="position: absolute;height:100%;width: 100%;overflow: hidden;"/>
</div>

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
       wx.ready(function () {
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