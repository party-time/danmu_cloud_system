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
    <#if dataList??>
    <#list dataList as obj>
    <div>
        <div>
            <img src="${obj.url}" />
        </div>
        <div>
            <div>
                <span>${obj.createTime?string("yyyy-MM-dd HH:mm:ss")}</span>
            </div>
            <div>
                <span>${obj.msg}</span>
            </div>
        </div>
    </div>
    </#list>
    <#else>
        <div>目前没有弹幕</div>
    </#if>
</body>
</html>