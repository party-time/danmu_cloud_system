<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${partyName!"null"}的历史弹幕</title>
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
    <div style="background-color:#fff;border:1px solid #eee;min-height:50px;margin-top:2px;">
        <div style="width:45px;position:absolute;margin-top:2px;margin-left: 2px;">
            <img style="width:45px;height:45px" src="${obj.url!"123123123"}" />
        </div>
        <div style="margin-left:50px;">
            <div style="background-color:#f5f8fd;font-size:13px;color:#aaa;margin-top:2px;">
                <span>发送于:${obj.createTime?string("yyyy-MM-dd HH:mm:ss")}</span>
            </div>
            <div style="word-wrap:break-word;margin-top:2px;">
                <span>${obj.msg}</span>
            </div>
        </div>
    </div>
    </#list>
    <#if total gt 20>
    <div style="text-align:center;margin-top: 5px;">
        <span style="<#if pageNumber==0>color:#aaa</#if>"><a <#if pageNumber gt 0>onclick="last()"</#if>><前</a></span>
        <span style="margin-left:20px;<#if pageNumber==lastPage-1>color:#aaa</#if>"><a <#if pageNumber lt lastPage-1>onclick="next()"</#if>>后></a></span>
    </div>
    </#if>
    <#else>
        <div style="text-align:center">目前没有弹幕</div>
    </#if>
<script>
  function next(){
        window.location.href="/wechat/historyDM?openId=${openId}&pageNumber=${pageNumber+2}";
  }
  function last(){
          window.location.href="/wechat/historyDM?openId=${openId}&pageNumber=${pageNumber-1}";
    }

</script>
</body>
</html>
