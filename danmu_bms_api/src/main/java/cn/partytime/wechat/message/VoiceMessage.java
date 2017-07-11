package cn.partytime.wechat.message;

/**
 * Created by administrator on 2017/4/12.
 */
public class VoiceMessage extends BaseMessage {

    private Voice Voice;

    public Voice getVoice() {
        return Voice;
    }

    public void setVoice(Voice voice) {
        Voice = voice;
    }
}
