<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>聚时代-语音</title>
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="screen-orientation" content="portrait">
    <meta name="x5-orientation" content="portrait">
    <script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
    <script src="/js/wechat/jquery.min.js"></script>
</head>
<body>
<div style="position:absolute;left:0;right:0;top:0;bottom:0;">
    <button onclick="startRecord()">开始录音</button>
    <button onclick="stopRecode()">结束录音</button>
    <button onclick="uploadRecode()">上传录音</button>
    <div id="showServerId"></div>
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
             'startRecord',
             'stopRecord',
             'uploadVoice'
           ]
       });
       var localId = '';
       var serverId = '';
       var startRecord = function(){
            wx.startRecord();
       }
       var stopRecode = function(){
            wx.stopRecord({
                success: function (res) {
                    localId = res.localId;
                }
            });
       }
       var uploadRecode = function(){
           wx.uploadVoice({
               localId: localId, // 需要上传的音频的本地ID，由stopRecord接口获得
               isShowProgressTips: 1, // 默认为1，显示进度提示
                   success: function (res) {
                   serverId = res.serverId; // 返回音频的服务器端ID
                   $.ajax({
                      url: "/wechat/getVoiceId?serverId="+serverId,
                      type: "get",
                  }).done(function (data) {
                     alert(serverId);
                  });
               }
           });
       }

</script>

</body>
</html>