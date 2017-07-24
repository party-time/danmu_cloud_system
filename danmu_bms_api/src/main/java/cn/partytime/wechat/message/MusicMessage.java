package cn.partytime.wechat.message;


/**
 * 音乐model
 * Created by Administrator on 2014/12/21.
 */

public class MusicMessage extends BaseMessage {
    // 音乐
    private Music Music;

    public Music getMusic() {
        return Music;
    }

    public void setMusic(Music music) {
        Music = music;
    }
}
