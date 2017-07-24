package cn.partytime.model.manager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2016/12/26.
 */
public class MovieAlias {

    private String name;

    private String value;

    public static List<MovieAlias> movieAliasList = new ArrayList<>();

    static{
        for(int i=1;i<11;i++){
            String a= "enterx-danmu-start-"+i;
            String b= "danmu-start-"+i;
            MovieAlias movieAlias = new MovieAlias();
            movieAlias.setName(a);
            movieAlias.setValue(b);
            movieAliasList.add(movieAlias);
        }
    }

    public static List<MovieAlias> getAll(){
        return movieAliasList;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
