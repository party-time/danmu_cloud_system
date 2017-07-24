package cn.partytime.model;

import cn.partytime.model.danmu.DanmuLibrary;
import cn.partytime.model.manager.DanmuAddress;
import cn.partytime.model.manager.MovieAlias;
import cn.partytime.model.manager.Party;

import java.util.List;

/**
 * Created by administrator on 2017/6/13.
 */
public class UpdatePartyModel {

    private Party party;

    private List<DanmuAddress> danmuAddressList;

    private String danmuLibraryId;

    private MovieAlias movieAlias;

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public List<DanmuAddress> getDanmuAddressList() {
        return danmuAddressList;
    }

    public void setDanmuAddressList(List<DanmuAddress> danmuAddressList) {
        this.danmuAddressList = danmuAddressList;
    }

    public String getDanmuLibraryId() {
        return danmuLibraryId;
    }

    public void setDanmuLibraryId(String danmuLibraryId) {
        this.danmuLibraryId = danmuLibraryId;
    }

    public MovieAlias getMovieAlias() {
        return movieAlias;
    }

    public void setMovieAlias(MovieAlias movieAlias) {
        this.movieAlias = movieAlias;
    }
}
