package cn.partytime.model;

import java.util.List;

/**
 * Created by administrator on 2017/9/5.
 */
public class PartyJson {

    private String partyId;

    private String name;

    private String movieAlias;

    private List<ResourceJson> expressions;

    private List<ResourceJson> specialEffects;

    private List<VideoResourceJson> localVideoUrl;

    private List<ResourceJson> timerDanmuUrl;

    private String adTimerDanmuUrl;

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMovieAlias() {
        return movieAlias;
    }

    public void setMovieAlias(String movieAlias) {
        this.movieAlias = movieAlias;
    }

    public List<ResourceJson> getExpressions() {
        return expressions;
    }

    public void setExpressions(List<ResourceJson> expressions) {
        this.expressions = expressions;
    }

    public List<ResourceJson> getSpecialEffects() {
        return specialEffects;
    }

    public void setSpecialEffects(List<ResourceJson> specialEffects) {
        this.specialEffects = specialEffects;
    }

    public List<VideoResourceJson> getLocalVideoUrl() {
        return localVideoUrl;
    }

    public void setLocalVideoUrl(List<VideoResourceJson> localVideoUrl) {
        this.localVideoUrl = localVideoUrl;
    }

    public List<ResourceJson> getTimerDanmuUrl() {
        return timerDanmuUrl;
    }

    public void setTimerDanmuUrl(List<ResourceJson> timerDanmuUrl) {
        this.timerDanmuUrl = timerDanmuUrl;
    }

    public String getAdTimerDanmuUrl() {
        return adTimerDanmuUrl;
    }

    public void setAdTimerDanmuUrl(String adTimerDanmuUrl) {
        this.adTimerDanmuUrl = adTimerDanmuUrl;
    }
}
