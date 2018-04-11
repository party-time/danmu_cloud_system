$(function () {
    var ajaxLock = false,
        damuT = $('#danmuText'),
        openId = $("#openId"),
        timer = null,
        $color = '',
        $expression = '';

    var str = '';
    var now = '';

    filter_time = function () {
        var time = setInterval(filter_staff_from_exist, 100);
        $(this).bind('blur', function () {
            clearInterval(time);
        });
    };
    $('.color-box p').on('click', function () {
        $color = $(this).attr('ac-color');
        $('.color-box').hide();
        $('.mask').hide();

        $(this).parent('li').addClass('active').siblings().removeClass('active');
        $('#colorBtn').css('color', $color)
        $('#danmuText').css('color', $color)
        $('#colorBtn').removeClass('active');
        var colorStr = $color;
        $('#colorHidden').val(colorStr.replace('#','0x'));
    });
    filter_staff_from_exist = function () {
        now = $.trim(damuT.val());
        if (now != '' && now != str) {
            var len = 40 - now.length;
            if (len < 0) {
                damuT.val(now.substr(0, 40));
                ajaxLock = false
            } else {
                ajaxLock = true;
                //console.log(len)
                $('#textNum').text(len);
                $('.remind').hide();
            }
        } else if (now == '') {
            $('#textNum').text(40);
        }
        str = now;
    };

    damuT.bind('focus', filter_time);


    $('#fastDmSelect').change(function(){
        if($("#fastDmSelect").val() == '请选择需要发送的弹幕'){
            return;
        }
        var obj = {
            templateId:"591d1f3a0cf29b664a4fe1ac",
            partyId:$('#partyId').val(),
            addressId:$('#addressId').val(),
            message:$("#fastDmSelect").val(),
            color:'',
            idd:''
        }

        $.ajax({
            url: "/v1/api/danmu",
            type: "post",
            dataType: "json",
            data:obj
        }).done(function (data) {
            if (data.result == 200 || data.result == 403) {
                clearTimeout(timer);
                $('.success,.mask1').show();
                timer = setTimeout(function () {
                    $('.success,.mask1').hide();
                    $('#danmuBtn').removeAttr('disabled');
                }, 2000);
                damuT.val('');
                $('.textNum').html('40');
            } else if (data.result == 400) {
                clearTimeout(timer);
                $('.networkWarning,.mask1').show();
                damuT.val('');
                $('.textNum').html('40');
            } else {
                $('.networkWarning,.mask1').show();
                damuT.val('');
                $('.textNum').html('40');
            }
        }).fail(function () {
            //alert("发送弹幕失败");
            $('.networkWarning,.mask1').show();
            damuT.val('');
            $('.textNum').html('40');
        });
    })

    $('#danmuBtn').on('click', function () {
        if (damuT.val().length != 0) {
            $('.remind').hide();
            $('.butt').attr('disabled', "disabled");
            if (!ajaxLock) {
                $('.remind').show();
                clearTimeout(timer);
                timer = setTimeout(function () {
                    $('.remind').hide();
                }, 3000);
                damuT.val('');
                $('#danmuBtn').removeAttr('disabled');
            } else {
                $('#templateId').val('591d1f3a0cf29b664a4fe1ac');
                $.ajax({
                    url: "/v1/api/danmu",
                    type: "post",
                    dataType: "json",
                    data:$('#danmuForm').serialize()
                }).done(function (data) {
                    if (data.result == 200 || data.result == 403) {
                        clearTimeout(timer);
                        $('.success,.mask1').show();
                        timer = setTimeout(function () {
                            $('.success,.mask1').hide();
                            $('#danmuBtn').removeAttr('disabled');
                        }, 2000);
                        damuT.val('');
                        $('.textNum').html('40');
                    } else if (data.result == 400) {
                        clearTimeout(timer);
                        $('.networkWarning,.mask1').show();

                        damuT.val('');
                        $('.textNum').html('40');
                    } else {
                        $('.networkWarning,.mask1').show();
                        damuT.val('');
                        $('.textNum').html('40');
                    }
                }).fail(function () {
                    //alert("发送弹幕失败");
                    $('.networkWarning,.mask1').show();
                    damuT.val('');
                    $('.textNum').html('40');
                });
                $('#danmuBtn').removeAttr('disabled');
            }
            ;
        } else {
            $('.remind').show();
            clearTimeout(timer);
            timer = setTimeout(function () {
                $('.remind').hide();
            }, 3000)
        }

    });

    $('#reload,.cancel').on('click', function () {
        $('.confirm,.mask1').hide();
        $('.networkWarning,.mask1').hide();
    })

    $('.expression-box img').on('click', function () {
        $('.confirm,.mask1').show();
        $(this).parent('li').addClass('active').siblings().removeClass('active');
        $expression = $(this).attr('ac-img');
        //var name = $(this).attr('src');
        //$suffix = name.substring(name.indexOf("."));
        $('.mask').hide();
        $('.expression-box').hide();
        $('#expressionBtn').removeClass('active');
    });
    $('.determine').on('click', function () {
        $('.confirm,.mask1').hide();
        $('#exHidden').val($expression);
        $('#templateId').val('5925541c0cf29df8028729a0');
        $.ajax({
            url: "/v1/api/expression",
            type: "post",
            dataType: "json",
            data:$('#danmuForm').serialize()
        }).done(function (data) {
            if (data.result == 200) {
                $('.success,.mask1').show();
                timer = setTimeout(function () {
                    $('.success,.mask1').hide();
                    $('#danmuBtn').removeAttr('disabled');
                }, 2000);
            } else {
                $('.networkWarning,.mask1').show();

            }
            ;

        });
    });
    $('#colorBtn').on('click', function () {
        $('.mask').show();
        $('.color-box').show();
        $('#colorBtn').addClass('active');
    });
    $('#expressionBtn').on('click', function () {
        $('.mask').show();
        $('.expression-box').show();
        $('#expressionBtn').addClass('active');
    });
    $('.mask').on('click', function () {
        $('.mask,.expression-box,.color-box').hide();
        $('#expressionBtn,#colorBtn').removeClass('active');
    });
    if (window.innerHeight)
        $('body,.bg,.cont').css('height', window.innerHeight);
    else if ((document.body) && (document.body.clientHeight))
        $('body,.bg,.cont').css('height', document.body.clientHeight);


    //弹幕

    window.settings = {
        danmakuUrl: 'http://www.party-time.cn/wechat/sanae',
        commentLifeTime: 7000
    };
    var CM = window.CM = new CommentManager(document.getElementById('danmuku')),
        timeAxis = 1,
        timer2 = null,
        danmuku = $('#danmukuWrap');

    CM.init(); // 初始化

    //载入弹幕列表
    var danmakuList = [
        {
            "mode": 1,
            "text": "欢迎来看电影",
            "stime": 2,
            "size": 20,
            "color": 16731921
        },
    ];
    CM.load(danmakuList);

    //// 插入弹幕
    //var someDanmakuAObj = {
    //    "mode":1,
    //    "text":"Hello CommentCoreLibrary",
    //    "stime":5,
    //    "size":10,
    //    "color":0xff0000
    //};
    //
    //CM.insert(someDanmakuAObj);

    // 启动播放弹幕
    CM.startTimer();

    timer2 = setInterval(function () {
        CM.time(timeAxis++);
    }, 1000);


    //弹幕开关
    $('.danmucontrol').on('click', function () {
        var _this = $(this),
            slide = _this.find('.slide');
        flag = _this.attr('flag');
        if (flag == 'on') {
            console.log(1)
            _this.attr('flag', 'off');
            _this.text('开启弹幕');
            danmuku.hide()
        } else {
            console.log(2)
            _this.attr('flag', 'on');
            _this.text('关闭弹幕');
            danmuku.show();
        }
    });

    var wsUri = '';
    var userLogin = function () {
        $.ajax({
            url: "/distribute/mobile/client/login/" + openId.val(),
            type: "get"
        }).done(function (data) {
            data = JSON.parse(data);
            if (data.code == 200) {
                wsUri = 'ws://' + data.serverInfo.ip + ':' + data.serverInfo.port + '/ws?code='+openId.val()+'&clientType=1';
                var websocket = window.aaawebsocket = new ReconnectingWebSocket(wsUri);
                websocket.onopen = function (evt) {
                    //websocket.send(JSON.stringify({type: 'login', "code": openId.val(), "clientType": "1"}));
                    console.log(evt)
                };
                websocket.onclose = function (evt) {
                    console.log(evt);
                };
                websocket.onmessage = function (evt) {
                    //console.log(evt);
                    var object = JSON.parse(evt.data);

                    var content = object.data;
                    if (object.type == 'pDanmu') {
                        CM.insert({
                            "mode": 1,
                            "text": content.message,
                            "stime": (timeAxis + 1),
                            "size": 20,
                            "color": parseInt(content.color)
                        });
                    }
                };
            } else {
                console.log(data);
            }
            ;
        });
    }

    userLogin();

    //websocket.onerror = function(evt) {
    //};

    //$('.restart').on('click',function(){
    //    websocket.close();
    //    websocket = null;
    //    websocket = new WebSocket(wsUri);
    //});
});
