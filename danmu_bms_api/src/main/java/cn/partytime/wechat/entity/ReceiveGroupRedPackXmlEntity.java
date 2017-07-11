package cn.partytime.wechat.entity;

/**
 * Created by liuwei on 2016/10/9.
 */
public class ReceiveGroupRedPackXmlEntity {

    //状态码  SUCCESS/FAIL
    private String return_code;

    //返回信息
    private String return_msg;

    //签名
    private String sign;

    //业务结果 SUCCESS/FAIL
    private String result_code;

    //错误代码
    private String err_code;

    //错误代码描述
    private String err_code_des;

    //商户订单号
    private String mch_billno;

    //商户号
    private String mch_id;

    //公众账号appid
    private String wxappid;

    //用户openid
    private String re_openid;

    //总金额
    private String total_amount;

    //微信单号
    private String send_listid;

    public String getReturn_code() {
        return return_code;
    }

    public void setreturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_msg() {
        return return_msg;
    }

    public void setreturn_msg(String return_msg) {
        this.return_msg = return_msg;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setresult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getErr_code() {
        return err_code;
    }

    public void seterr_code(String err_code) {
        this.err_code = err_code;
    }

    public String getErr_code_des() {
        return err_code_des;
    }

    public void seterr_code_des(String err_code_des) {
        this.err_code_des = err_code_des;
    }

    public String getMch_billno() {
        return mch_billno;
    }

    public void setmch_billno(String mch_billno) {
        this.mch_billno = mch_billno;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setmch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getWxappid() {
        return wxappid;
    }

    public void setwxappid(String wxappid) {
        this.wxappid = wxappid;
    }

    public String getRe_openid() {
        return re_openid;
    }

    public void setre_openid(String re_openid) {
        this.re_openid = re_openid;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void settotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSend_listid() {
        return send_listid;
    }

    public void setsend_listid(String send_listid) {
        this.send_listid = send_listid;
    }
}
