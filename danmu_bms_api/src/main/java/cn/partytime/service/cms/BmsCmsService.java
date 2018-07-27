package cn.partytime.service.cms;

import cn.partytime.model.RestResultModel;
import cn.partytime.model.cms.*;
import cn.partytime.model.manager.ResourceFile;
import cn.partytime.model.shop.Item;
import cn.partytime.service.ResourceFileService;
import cn.partytime.service.shop.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by administrator on 2017/6/29.
 */

@Service
public class BmsCmsService {

    @Autowired
    private PageService pageService;

    @Autowired
    private ColumnService columnService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ResourceFileService resourceFileService;

    public void createColumn(String url, String title , Integer objectType,String addressId){
        if(StringUtils.isEmpty(url) && StringUtils.isEmpty(addressId)){
            return;
        }
        Column column = new Column();
        column.setTitle(title);
        column.setObjectType(objectType);
        column.setAddressId(addressId);
        column = columnService.save(column);
        if(!StringUtils.isEmpty(url)){
            Page page = pageService.findByUrl(url);
            if( null == page.getColumnIdList() ){
                page.setColumnIdList(new ArrayList<>());
            }
            page.getColumnIdList().add(column.getId());
            pageService.update(page);
        }
    }

    public void delColumn(String url,String columnId){
        Page page = pageService.findByUrl(url);
        Column column = columnService.findById(columnId);
        if( null != page && null != page.getColumnIdList()){
            page.getColumnIdList().remove(column);
            pageService.update(page);
        }
        if(null != column){
            columnService.delById(columnId);
        }

    }

    public PageColumn findByPageUrl(String pageUrl){
        PageColumn pageColumn = new PageColumn();
        Page page = pageService.findByUrl(pageUrl);
        if( null == page){
            page = new Page();
            page.setTitle("电商首页");
            page.setUrl(pageUrl);
            page = pageService.save(page);
        }
        pageColumn.setPage(page);
        if( null != page.getColumnIdList()){
            List<ColumnObject> columnObjectList = new ArrayList<>();
            List<Column> columnList = columnService.findByIds(page.getColumnIdList());
            if( null != columnList && columnList.size() > 0){

                for( Column column : columnList){
                    ColumnObject<Item> columnObject = new ColumnObject();
                    columnObject.setColumn(column);
                    if( null != column.getObjectIdList()){
                        List<Item> itemList = itemService.findByIds(column.getObjectIdList());
                        columnObject.setObjectList(itemList);
                    }
                    columnObjectList.add(columnObject);
                }
            }
            pageColumn.setColumnObjectList(columnObjectList);
        }
        return pageColumn;
    }


    public String selectItem(String itemId,String columnId){
        Column column = columnService.findById(columnId);
        if( null != column ){
            List<String> objectIdList = column.getObjectIdList();
            if( null == objectIdList){
                objectIdList = new ArrayList<>();
            }else{
                List<Item> itemList = new ArrayList<>();
                for(String objectId:objectIdList){
                    if( objectId.equals(itemId)){
                        return "不能添加相同的商品";
                    }
                    Item item = itemService.findById(objectId);
                    itemList.add(item);
                }
                if(itemList.size() > 4){
                    return "最多只能添加5个商品";
                }
            }
            objectIdList.add(itemId);
            column.setObjectIdList(objectIdList);
            columnService.update(column);
            return null;
        }
        return "栏目不存在";
    }

    public void delItem(String itemId,String columnId){
        Column column = columnService.findById(columnId);
        if( null != column ){
            List<String> objectIdList = column.getObjectIdList();
            if( null != objectIdList){
                objectIdList.remove(itemId);
                column.setObjectIdList(objectIdList);
            }
            columnService.update(column);
        }

    }

    public PageColumn findByAddressId(String addressId){
        PageColumn pageColumn = new PageColumn();
        List<ColumnObject> columnObjectList = new ArrayList<>();
        List<Column> columnList = columnService.findByAddressId(addressId);
        if( null != columnList && columnList.size() > 0){
            for( Column column : columnList){
                ColumnObject<Item> columnObject = new ColumnObject();
                columnObject.setColumn(column);
                if( null != column.getObjectIdList()){
                    List<Item> itemList = itemService.findByIds(column.getObjectIdList());
                    columnObject.setObjectList(itemList);
                }
                columnObjectList.add(columnObject);
            }
        }
        pageColumn.setColumnObjectList(columnObjectList);

        return pageColumn;
    }

    public PageColumn findItemByAddressId(String addressId){
        PageColumn pageColumn = new PageColumn();
        List<ColumnObject> columnObjectList = new ArrayList<>();
        List<Column> columnList = columnService.findByAddressId(addressId);
        if( null != columnList && columnList.size() > 0){
            for( Column column : columnList){
                ColumnObject<ItemResult> columnObject = new ColumnObject();
                columnObject.setColumn(column);
                if( null != column.getObjectIdList()){
                    List<Item> itemList = itemService.findByIds(column.getObjectIdList());
                    if( null != itemList ){
                        List<ItemResult> itemResultList = new ArrayList<>();
                        for(Item item : itemList){
                            ItemResult itemResult = new ItemResult();
                            itemResult.setItem(item);
                            ResourceFile coverImg = resourceFileService.findById(item.getCoverImgId());
                            itemResult.setCoverImg(coverImg);
                            itemResultList.add(itemResult);
                        }
                        columnObject.setObjectList(itemResultList);
                    }
                }
                columnObjectList.add(columnObject);
            }
        }
        pageColumn.setColumnObjectList(columnObjectList);

        return pageColumn;
    }


    public ItemResult findByItemId(String itemId){
        Item item = itemService.findById(itemId);
        if( null != item){
            ItemResult itemResult = new ItemResult();
            itemResult.setItem(item);
            if( null != item.getCoverImgId()){
                ResourceFile coverImg = resourceFileService.findById(item.getCoverImgId());
                itemResult.setCoverImg(coverImg);
            }
            if( null != item.getImageIds()){
                List<ResourceFile> resourceFileList = resourceFileService.findByIds(item.getImageIds());
                itemResult.setImgList(resourceFileList);
            }
            return itemResult;
        }else{
            return null;
        }
    }





}
