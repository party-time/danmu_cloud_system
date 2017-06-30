package cn.partytime.common.util;

import java.util.Random;

/**
 * Created by dm on 2017/4/26.
 */
public enum ColorEnmu {
    WHITE_COLOR(1,"#ffffff"),
    YELLOW_COLOR(2,"#ffd926"),
    ORANGE_COLOR(3,"#ff4f11"),
    PURPLE_COLOR(4,"#fc33de"),
    GRAY_COLOR(5,"#aaaaaa"),
    CYAN_COLOR(6,"#42deff"),
    BLUE_COLOR(7,"#61a0ff"),
    GREEN_COLOR(8,"#46f8bb");

    private int code;
    private String value;

    ColorEnmu(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public static String getValue(int index) {


        if(index==0){
            Random random = new Random();
            index = random.nextInt(10);
        }
        for (ColorEnmu colorEnmu : ColorEnmu.values()) {

            if(colorEnmu.getCode() == index){
                return colorEnmu.getValue();
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}