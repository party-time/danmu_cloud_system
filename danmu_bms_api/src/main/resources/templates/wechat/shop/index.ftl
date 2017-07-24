<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <title>首页</title>
    <link href="../css/shop/amazeui.css" rel="stylesheet" type="text/css" />
    <link href="../css/shop/admin.css" rel="stylesheet" type="text/css" />
    <link href="../css/shop/demo.css" rel="stylesheet" type="text/css" />
    <link href="../css/shop/hmstyle.css" rel="stylesheet" type="text/css" />
    <script src="../plug/jquery/jquery.min.js"></script>
    <script src="../js/bussiness/shop/amazeui.min.js"></script>
</head>
<body>

<b class="line"></b>
<div class="shopMainbg">
    <div class="shopMain" id="shopmain">
        <#if pageColumn??>
        <#list pageColumn.columnObjectList as columnObj>
        <div class="f${columnObj_index}">
            <!--甜点-->
            <div class="am-container " >
                <div class="shopTitle ">
                    <#if columnObj.column??>
                    <h4 class="floor-title">${columnObj.column.title}</h4>
                    </#if>
                </div>
            </div>
            <div class="am-g am-g-fixed floodSix ">
                <#if columnObj.objectList??>
                <#list columnObj.objectList as object>
                    <#if object_index ==0>
                    <div class="am-u-sm-5 am-u-md-3 text-one list">
                        <a href="${baseUrl}/wechat/item?itemId=${object.item.id}">
                            <img src="${imgUrl}${object.coverImg.fileUrl}" />
                            <div class="outer-con ">
                                <div class="title ">
                                    ${object.item.name}
                                </div>
                                <div class="sub-title ">
                                    ${object.item.title}
                                </div>
                            </div>
                        </a>
                        <div class="triangle-topright"></div>
                    </div>
                    </#if>
                    <#if object_index gte 1 >
                    <li>
                        <div class="am-u-md-2 am-u-lg-2 text-three">
                            <div class="boxLi"></div>
                            <div class="outer-con ">
                                <div class="title ">
                                    ${object.item.name}
                                </div>
                                <div class="sub-title ">
                                    ¥${(object.item.showPrice/100)?string.number}
                                </div>

                            </div>
                            <a href="${baseUrl}/wechat/item?itemId=${object.item.id}"><img src="${imgUrl}${object.coverImg.fileUrl}" /></a>
                        </div>
                    </li>
                    </#if>

                </#list>
                </#if>
            </div>
            <div class="clear "></div>
        </div>
        </#list>
        </#if>
    </div>
</div>
</body>
</html>