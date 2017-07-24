{"button": [
    {"name": "发弹幕",
     "sub_button":[
            {
                "type":"view",
                "name":"H5弹幕",
                "url":"https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=http://test.party-time.cn/wechat/sendDM&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect"
            },
            {
                "type":"click",
                "name":"语音弹幕",
                "key":"VOICE_DAN_MU"
            },
            {
                "name": "来打赏",
                "type":"view",
                "url":"https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=http://test.party-time.cn/wechat/payIndex&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect"
            },
            {
                "type":"view",
                "url":"http://www.party-time.cn/htm/contact.html",
                "name":"商务合作"
            }
     ]
    },
    {"name": "表白",
     "type":"view",
     "url":"http://test.party-time.cn/upload/h5html/love.html"
    },
    {"name": "买买买",
     "type":"view",
     "url":"https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appId}&redirect_uri=http://test.party-time.cn/wechat/buy&response_type=code&scope=snsapi_base&state=STATE#wechat_redirect"
    }
]}
