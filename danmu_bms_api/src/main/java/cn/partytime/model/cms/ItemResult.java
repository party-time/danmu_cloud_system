package cn.partytime.model.cms;

import cn.partytime.model.manager.ResourceFile;
import cn.partytime.model.shop.Item;

import java.util.List;

/**
 * Created by administrator on 2017/7/4.
 */
public class ItemResult {

    private Item item;

    private ResourceFile coverImg;

    private List<ResourceFile> imgList;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public ResourceFile getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(ResourceFile coverImg) {
        this.coverImg = coverImg;
    }

    public List<ResourceFile> getImgList() {
        return imgList;
    }

    public void setImgList(List<ResourceFile> imgList) {
        this.imgList = imgList;
    }
}
