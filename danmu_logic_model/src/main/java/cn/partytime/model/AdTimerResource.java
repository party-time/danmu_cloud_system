package cn.partytime.model;

import lombok.Data;

import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */

@Data
public class AdTimerResource {

    List<TimerDanmuFileModel> timerDanmuFileLogicModels;


    List<ResourceFileModel> resourceFileList;

}
