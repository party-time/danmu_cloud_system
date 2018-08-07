package cn.partytime.model;


import lombok.Data;

import java.util.Date;

/**
 * Created by liuwei on 16/6/12.
 * 管理员
 */

@Data
public class AdminUserDto {
    private String id;

    private String userName;

    private String password;

    private String nick;

    private String AdminUserCompanyId;

    private String registIp;

    private String lastLoginIp;

    private Date lastLoginTime;

    private String roleId;

    //微信表的id
    private String wechatId;

    private int checkFlg;

    /**
     * 0:铃声 1:静止
     */
    private int ringFlg;

}
