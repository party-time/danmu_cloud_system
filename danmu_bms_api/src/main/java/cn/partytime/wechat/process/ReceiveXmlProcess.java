package cn.partytime.wechat.process;

import cn.partytime.wechat.entity.PayNotifyXmlEntity;
import cn.partytime.wechat.entity.ReceiveGroupRedPackXmlEntity;
import cn.partytime.wechat.entity.ReceiveUnifiedOrderXmlEntity;
import cn.partytime.wechat.entity.ReceiveXmlEntity;
import cn.partytime.wechat.pojo.UnifiedorderResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Iterator;

/**
 * 解析接收到的微信xml，返回消息对象
 *
 */
@Slf4j
public class ReceiveXmlProcess {

	/**
	 * 解析微信xml消息
	 * @param request
	 * @return
	 */
//	public ReceiveXmlEntity getMsgEntity(String strXml){
	public ReceiveXmlEntity getMsgEntity(HttpServletRequest request){

		ReceiveXmlEntity msg = null;
		try {
            // 从request中取得输入流
            InputStream inputStream = request.getInputStream();

            // 读取输入流
            SAXReader reader = new SAXReader();
            Document document = reader.read(inputStream);
//			if (strXml.length() <= 0 || strXml == null)
//				return null;
			 
			// 将字符串转化为XML文档对象
//			Document document = DocumentHelper.parseText(strXml);


			// 获得文档的根节点
			Element root = document.getRootElement();
			// 遍历根节点下所有子节点
			Iterator<?> iter = root.elementIterator();
			
			// 遍历所有结点
			msg = new ReceiveXmlEntity();
			//利用反射机制，调用set方法
			//获取该实体的元类型
			Class<?> c = Class.forName("cn.partytime.wechat.entity.ReceiveXmlEntity");
			msg = (ReceiveXmlEntity)c.newInstance();//创建这个实体的对象
			
			while(iter.hasNext()){
				Element ele = (Element)iter.next();
				//获取set方法中的参数字段（实体类的属性）
				Field field = c.getDeclaredField(ele.getName());
				//获取set方法，field.getType())获取它的参数数据类型
				Method method = c.getDeclaredMethod("set"+ele.getName(), field.getType());
				log.info(ele.getName()+"======="+ele.getText());
				//调用set方法
				method.invoke(msg, ele.getText());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
            log.error(e.getMessage());
		}
		return msg;
	}



	public ReceiveGroupRedPackXmlEntity getGroupRedPackEntity(String entityStr){

		ReceiveGroupRedPackXmlEntity entity = null;
		try {

			Document document =  DocumentHelper.parseText(entityStr);
//			if (strXml.length() <= 0 || strXml == null)
//				return null;

			// 将字符串转化为XML文档对象
//			Document document = DocumentHelper.parseText(strXml);

			// 获得文档的根节点
			Element root = document.getRootElement();
			// 遍历根节点下所有子节点
			Iterator<?> iter = root.elementIterator();

			// 遍历所有结点
			entity = new ReceiveGroupRedPackXmlEntity();
			//利用反射机制，调用set方法
			//获取该实体的元类型
			Class<?> c = Class.forName("cn.partytime.wechat.entity.ReceiveGroupRedPackXmlEntity");
			entity = (ReceiveGroupRedPackXmlEntity)c.newInstance();//创建这个实体的对象

			while(iter.hasNext()){
				Element ele = (Element)iter.next();
				//获取set方法中的参数字段（实体类的属性）
				Field field = c.getDeclaredField(ele.getName());
				//获取set方法，field.getType())获取它的参数数据类型
				Method method = c.getDeclaredMethod("set"+ele.getName(), new Class[] {field.getType()});
				log.debug("set"+ele.getName(), field.getType());
				//调用set方法
				method.invoke(entity, ele.getText());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return entity;
	}

	public ReceiveUnifiedOrderXmlEntity getUnifiedOrderEntity(String entityStr){

		ReceiveUnifiedOrderXmlEntity entity = null;
		try {

			Document document =  DocumentHelper.parseText(entityStr);
//			if (strXml.length() <= 0 || strXml == null)
//				return null;

			// 将字符串转化为XML文档对象
//			Document document = DocumentHelper.parseText(strXml);

			// 获得文档的根节点
			Element root = document.getRootElement();
			// 遍历根节点下所有子节点
			Iterator<?> iter = root.elementIterator();

			// 遍历所有结点
			entity = new ReceiveUnifiedOrderXmlEntity();
			//利用反射机制，调用set方法
			//获取该实体的元类型
			Class<?> c = Class.forName("cn.partytime.wechat.entity.ReceiveUnifiedOrderXmlEntity");
			entity = (ReceiveUnifiedOrderXmlEntity)c.newInstance();//创建这个实体的对象

			while(iter.hasNext()){
				Element ele = (Element)iter.next();
				//获取set方法中的参数字段（实体类的属性）
				Field field = c.getDeclaredField(ele.getName());
				//获取set方法，field.getType())获取它的参数数据类型
				String name = ele.getName().substring(0, 1).toUpperCase() +  ele.getName().substring(1);
				Method method = c.getDeclaredMethod("set"+name, new Class[] {field.getType()});
				log.debug("set"+ele.getName(), field.getType());
				//调用set方法
				method.invoke(entity, ele.getText());
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		return entity;
	}

	public PayNotifyXmlEntity getWeChatPayEntity(HttpServletRequest request){

		PayNotifyXmlEntity msg = null;
		try {
			// 从request中取得输入流
			InputStream inputStream = request.getInputStream();

			// 读取输入流
			SAXReader reader = new SAXReader();
			Document document = reader.read(inputStream);
//			if (strXml.length() <= 0 || strXml == null)
//				return null;

			// 将字符串转化为XML文档对象
//			Document document = DocumentHelper.parseText(strXml);


			// 获得文档的根节点
			Element root = document.getRootElement();
			// 遍历根节点下所有子节点
			Iterator<?> iter = root.elementIterator();

			// 遍历所有结点
			msg = new PayNotifyXmlEntity();
			//利用反射机制，调用set方法
			//获取该实体的元类型
			Class<?> c = Class.forName("cn.partytime.wechat.entity.PayNotifyXmlEntity");
			msg = (PayNotifyXmlEntity)c.newInstance();//创建这个实体的对象

			while(iter.hasNext()){
				Element ele = (Element)iter.next();
				//获取set方法中的参数字段（实体类的属性）
				Field field = c.getDeclaredField(ele.getName());
				//获取set方法，field.getType())获取它的参数数据类型
				Method method = c.getDeclaredMethod("set"+ele.getName().substring(0,1).toUpperCase()+ele.getName().substring(1,ele.getName().length()), field.getType());
				//调用set方法
				method.invoke(msg, ele.getText());
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			log.error("",e);
		}
		return msg;
	}

}
