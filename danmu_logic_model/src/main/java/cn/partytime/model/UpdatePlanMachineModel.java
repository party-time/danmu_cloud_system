package cn.partytime.model;

import lombok.Data;

@Data
public class UpdatePlanMachineModel {
    //左边墙壁为1  右边墙壁为2
    private String machineNum;

    //更新状态 0 未更新  1更新成功  2更新失败  3开始更新,4：更新成功但是不符合业务要求
    private Integer status =0;

}
