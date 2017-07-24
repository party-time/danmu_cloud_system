<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title></title>
  <base href="/">
  <meta name="viewport" content="initial-scale=1, maximum-scale=1, minimum-scale=1, width=device-width, user-scalable=no">
      <meta name="apple-mobile-web-app-capable" content="yes">
      <meta name="apple-touch-fullscreen" content="yes">
      <meta name="apple-mobile-web-app-status-bar-style" content="black">
      <meta name="screen-orientation" content="portrait">
      <meta name="x5-orientation" content="portrait">
      <link rel="stylesheet" href="./css/wechat/dmSend_bhblw.css"/>

  </head>
  <body>
      <div class="cont">
           <#if background??>
          <img class="bg" src="${baseUrl}${background.fileUrl}"/>
          </#if>
         <div id="danmukuWrap">
              <div id='danmuku' class='container'></div>
          </div>
          <div class="content" style="top:45%">
              <form id="danmuForm">
              <textarea name="message" id="danmuText" placeholder="请输入您要发送的弹幕内容。。"></textarea>
              <span class="textNum" id="textNum">40</span>
               <#if colors??>
              <button class="color" id="colorBtn" type="button">
<!--                  <span>A</span>-->
              </button>

              <div class="color-box">
                  <img src="images/senddm/jiao.png" class="jiao"/>
                  <ul>
                      <#list colors as color>
                      <li>
                          <p ac-color="${color}" style="background:${color}">

                          </p>
                      </li>
                      </#list>
                  </ul>
              </div>
              </#if>
              <#if expressions??>
              <input class="expression" id="expressionBtn" type="button"/>
              <div class="expression-box">
                  <img src="images/senddm/jiao.png" class="jiao"/>
                  <ul>
                      <#list expressions as e>
                      <#if e.smallFileUrl??>
                      <li>
                          <img ac-img="${e.id}" src="${baseUrl}${e.smallFileUrl}"/>
                      </li>
                      </#if>
                      </#list>
                  </ul>
              </div>
              </#if>
	      <div class="danmucontrol" flag="on">
                  关闭弹幕
              </div>
              <input class="butt" id="danmuBtn" type="button"/>
              <input type="hidden" id="partyId" name="partyId" value="${partyId}">
              <input type="hidden" id="addressId" name="addressId" value="${addressId}">
              <input type="hidden" id="templateId" name="templateId" value="591d1f3a0cf29b664a4fe1ac">
              <input type="hidden" id="colorHidden" name="color" >
              <input type="hidden" id="exHidden" name="idd" >
              </form>
          </div>
<!--          <p class="txt">乱发不和谐内容会被猴子淦掉哦</p>-->
          <div class="success">
              <img src="./images/senddm/success02.png">
          </div>
          <div class="remind">
              请输入弹幕内容
          </div>
          <div class="mask"></div>
          <div class="mask1"></div>
          <div class="networkWarning">
             <!-- <img src="./images/senddm/ac.png" alt=""/> -->
              <div class="networkWarning-con">
                  <h2>好像发生了神马，请稍后再试...</h2>
                  <h1 id="reload">确定</h1>
              </div>
          </div>
          <div class="confirm">
              <!--<img src="./images/senddm/confirm.png" alt=""/>-->
              <div class="confirm-con">
                  <h2>表情！发射！</h2>
                  <div class="btnbox">
                      <button class="cancel">不来</button>
                      <button class="determine">来一发</button>
                  </div>
              </div>
          </div>
          
      </div>
      <input type="hidden" id="openId"  value="${openId}"/>
<script src="./js/wechat/commentCoreLibrary.js"></script>
<script src="./js/wechat/jquery.min.js"></script>
<script src="./js/wechat/reconnecting-websocket.min.js"></script>
<script src="./js/wechat/dmSend_wwyx.js?v=1.2"></script>
</body>
</html>
