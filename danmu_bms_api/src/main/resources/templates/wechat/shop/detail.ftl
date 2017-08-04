<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <style type="text/css" id="alertifyCSS">/* style.css */</style>
    <title>${itemResult.item.name}</title>
    <meta name="renderer" content="webkit">
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimal-ui">
    <link rel="stylesheet" href="../css/shop/metinfo.css">
    <style type="text/css"></style>
    <script src="../js/wechat/jquery.min.js"></script>
    <script src="../js/shop/detail.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.1.0.js"></script>
</head>
<body class="met-white-lightGallery">
<div class="met-position  pattern-show">
    <div class="container">
        <div class="row">
            <ol class="breadcrumb">
                <li>
                    <a href="${baseUrl}/wechat/buy" title="首页">
                        返回首页
                    </a>
                </li>
            </ol>
        </div>
    </div>
</div>
<!---->
<div class="page met-showproduct pagetype1 animsition">
    <!---->
    <div class="met-showproduct-head">
        <div class="container">
            <div class="row">
                <div class="col-md-7">
                    <div id="gallery" class="ad-gallery" data-x="400" data-y="400">
                        <div class="ad-image-wrapper" style="height:340px;">
                            <div class="ad-image" style="width: 400px;top:0px;left:126.5px;position:relative;z-index:1">
                                <img id="showImg" src="${imgUrl}${firstImg.fileUrl}" width="400px" style="position:absolute;">
                            </div>
                            <div style="position:absolute;z-index:2;top:290px;">
                                <span>${itemResult.item.title!}</span>
                                <span style="color:red;font-size:20px">${(itemResult.item.showPrice/100)?string.number}元</span>
                                <p style="font-size:10px;text-align:left;color:#888888">${itemResult.item.name}</p>
                            </div>
                        </div>
                        <div class="ad-nav">
                            <div class="ad-thumbs">
                                <ul class="ad-thumb-list" id="lightgallery" style="width: 653px;">
                                    <#if itemResult?? && itemResult.imgList??>
                                    <#list itemResult.imgList as itemImg>
                                    <li >
                                        <a class="ad-active" onclick="drawImg('${imgUrl}${itemImg.fileUrl}')">
                                            <img src="${imgUrl}${itemImg.fileUrl}" class="img-responsive" style="opacity: 1;">
                                        </a>
                                    </li>
                                    </#list>
                                    </#if>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="col-md-5 product-intro">
                    <div class="shop-product-intro grey-500">
                        <div class="form-group margin-top-15" style="float:left;">
                            <label class="control-label font-weight-unset">数量</label>
                            <div class="width-150">
                                <div class="input-group bootstrap-touchspin">
                                    <span class="input-group-btn">
                                        <button class="btn btn-default bootstrap-touchspin-down" type="button" onclick="subNum()">-</button></span>
                                    <span class="input-group-addon bootstrap-touchspin-prefix" style="display: none;"></span><input id="itemNum" class="form-control text-center" data-min="1" data-max="999" data-plugin="touchSpin" name="buynum" id="buynum" autocomplete="off" value="1" style="display: block;" type="text"><span class="input-group-addon bootstrap-touchspin-postfix" style="display: none;"></span><span class="input-group-btn"><button class="btn btn-default bootstrap-touchspin-up" type="button" onclick="addNum()">+</button></span></div>
                            </div>
                        </div>
                        <div class="form-group margin-top-30 purchase-btn" style="float:left;margin-left:30px;">
                            <a href="#" class="btn btn-lg btn-squared btn-danger margin-right-20 product-buynow" onclick="buy()">立即购买</a>
                            <div class="visible-xs-block height-10"></div>
                        </div>
                    </div>
                    <!---->
                </div>
            </div>
        </div>
    </div>
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
            'chooseWXPay'
          ]
    });
    var wechatPay = function(){
        // 注意：此 Demo 使用 2.7 版本支付接口实现，建议使用此接口时参考微信支付相关最新文档。
          $.ajax({
              url: "/wechat/buyItem?nonceStr=${wxJsConfig.nonceStr}&openId=${opendId}&timestamp=${wxJsConfig.timestamp}&itemId=${itemResult.item.id}&num="+$('#itemNum').val(),
              type: "get"
          }).done(function (data) {
                  wx.chooseWXPay({
                        timestamp: data.timeStamp,
                        nonceStr: data.nonceStr,
                        package: data.packageStr,
                        signType: data.signType, // 注意：新版支付接口使用 MD5 加密
                        paySign: data.paySign,
                        success: function (res) {
                            window.location.href="https://open.weixin.qq.com/connect/oauth2/authorize?appid=${wxJsConfig.appId}&redirect_uri=http://www.party-time.cn/wechat/buy&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect";
                            alert('支付成功');
                        }
                  });
            });
    }
    var buy = function () {
        wechatPay();
    };
</script>
</body></html>