{
    "touser":"${openId}",
    "template_id":"${templateId}",
    "url":"${url!""}",
    "topcolor":"#FF0000",
    "data":{
        "first":{"value":"登录验证码:${code}","color":"#173177"},
        "keyword1":{"value":"登录中","color":"#173177"},
        "keyword2":{"value":"${sendDate?string("yyyy-MM-dd HH:mm:ss")}","color":"#173177"},
        "remark":{"value":"5分钟后过期,请尽快完成登录","color":"#173177"}
    }
}