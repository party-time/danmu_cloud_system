<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>聚时代弹幕</title>
    <base href="/">
    <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, user-scalable=no">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-touch-fullscreen" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta name="screen-orientation"content="portrait">
    <meta name="x5-orientation"content="portrait">
    <link rel="stylesheet" href="./css/dmSend.css"/>
</head>
<body>
<div class="cont">
    <img class="bg" src="image/bg.png"/>
    <div class="content">
        <textarea name="" id="danmuText" placeholder="请输入您要发送的弹幕内容"></textarea>
        <span class="textNum" id="textNum">40</span>
        <input class="butt" id="danmuBtn" type="button"/>
    </div>
    <div class="success">
        <img src="image/success.png">
    </div>
    <div class="tip">
        请输入弹幕内容
    </div>
    <div class="mask"></div>
    <div class="networkWarning">
        <div class="networkWarning-con">
            <h2>好像发生了神马，请稍后再试...</h2>
            <h1 id="reload">确定</h1>
        </div>
    </div>
</div>
<script src="./bower_components/jquery/dist/jquery.min.js"></script>
<script src="./js/dmSend.js"></script>
</body>
</html>