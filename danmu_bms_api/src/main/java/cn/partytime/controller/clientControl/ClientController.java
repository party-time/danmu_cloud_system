package cn.partytime.controller.clientControl;

import cn.partytime.controller.base.BaseAdminController;
import cn.partytime.model.RestResultModel;
import cn.partytime.service.BmsClientWechatSendService;
import cn.partytime.service.client.ClientService;
import cn.partytime.service.movie.MovieService;
import cn.partytime.util.PartyTimeConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by administrator on 2017/3/22.
 */

@RestController
@RequestMapping(value = "/v1/api/admin/clientControl")
@Slf4j
public class ClientController extends BaseAdminController{

    @Autowired
    private BmsClientWechatSendService bmsClientWechatSendService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private PartyTimeConfig partyTimeConfig;


    @Autowired
    private MovieService movieService;


    /**
     * projectStart 开启投影  projectClose 关闭投影  projectChange 投影切白
     * appRestart app重启  appStart app开启  appClose app关闭
     * flashUpdate flash更新  flashRollBack  flash回滚  javaUpdate java升级  javaRollBack java回滚
     * videoDown 视频下载   expressionDown 表情下载  specialImgDown 特效图片下载  timerDmDown 定时弹幕下载 adDmDown 广告弹幕下载
     * configCreate 生成配置表
     * teamViewStart1,teamViewStart2 开启teamView  teamViewClose1,teamViewClose2 关闭teamView screenPic1,screenPic2 截屏
     *
     * danmu-start-1 movie-start movie-close
     * @param cmd
     * @return
     */
    @RequestMapping(value = "/control", method = RequestMethod.GET)
    public RestResultModel controls(String danmuStart , String cmd, String addressId){
        RestResultModel restResultModel = new RestResultModel();
        String callback = "";
        if(cmd.equals("screenPic1")){
            callback = partyTimeConfig.getUrl()+"/v1/api/printScreen/sendScreenPic?adminId="+getAdminUser().getId()+"&addressId="+addressId+"&num=1";
        }else if( cmd.equals("screenPic2")){
            callback = partyTimeConfig.getUrl()+"/v1/api/printScreen/sendScreenPic?adminId="+getAdminUser().getId()+"&addressId="+addressId+"&num=2";
        }

        if(cmd.startsWith("danmu-")){
            clientService.sendCommand(danmuStart,addressId,callback);
        }else{
            clientService.sendCommand(cmd,addressId,callback);
        }


        /*
        clientService.sendCommand(cmd,addressId,callback);

        if(cmd.startsWith("danmu-") || cmd.startsWith("movie-")){
            movieService.movieHandler(danmuStart,cmd,addressId);
        }*/




        restResultModel.setResult(200);
        return restResultModel;
    }



}
