<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>聚时代-历史弹幕</title>
    <base href="/">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, user-scalable=no">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <meta name="apple-touch-fullscreen" content="yes">
  <meta name="apple-mobile-web-app-status-bar-style" content="black">
  <meta name="screen-orientation" content="portrait">
  <meta name="x5-orientation" content="portrait">
</head>
<body>
    <div style="text-align:center">
        <h1>${partyName!"null"}的历史弹幕</h1>
    </div>
    <#if dataList??>
    <#list dataList as obj>
    <div style="background-color:#fff;border:1px solid #eee;min-height:55px">
        <div style="width:19%;float:left;margin-top:2px;margin-left: 2px;">
            <img style="width:45px;height:45px" src="${obj.url!"123123123"}" />
        </div>
        <div style="width:80%;float:left;">
            <div style="background-color:#f5f8fd;font-size:13px;color:#aaa">
                <span>发送于:${obj.createTime?string("yyyy-MM-dd HH:mm:ss")}</span>
            </div>
            <div style="word-wrap:break-word">
                <span>${obj.msg}</span>
            </div>
        </div>
    </div>
    </#list>
    <#else>
        <div style="text-align:center">目前没有弹幕</div>
    </#if>
</body>
</html>