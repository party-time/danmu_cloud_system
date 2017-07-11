package cn.partytime.wechat.message;


/**
 * 音乐model
 * Created by Administrator on 2014/12/21.
 */

public class MusicMessage extends BaseMessage {
    // 音乐
    private cn.partytime.wechat.message.Music Music;

    public cn.partytime.wechat.message.Music getMusic() {
        return Music;
    }

    public void setMusic(cn.partytime.wechat.message.Music music) {
        Music = music;
    }
}
