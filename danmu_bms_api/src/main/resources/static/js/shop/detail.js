var drawImg = function(imgUrl){
    $('#showImg').attr('src',imgUrl);
}

var addNum = function(){
    var num = $('#itemNum').val();
    num = parseInt(num)+1;
    $('#itemNum').val(num);
}

var subNum = function(){
    var num = $('#itemNum').val();
    num = parseInt(num)-1;
    if( num <1){
        num = 1;
    }
    $('#itemNum').val(num);
}