package cn.partytime.listener;

import cn.partytime.annotation.CommandAnnotation;
import cn.partytime.scheduler.BaseScheduler;
import cn.partytime.util.ClassUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;


@Slf4j
@Component("commandListener")
public class CommandListener implements MessageListener {

    @Autowired
    private ServletContext servletContext;
    @Override
    public void onMessage(Message message, byte[] bytes) {
        if(message!=null){
            String command = JSON.parseObject(String.valueOf(message),String.class).replace("'","");
            log.info("command:"+command);
            //BlockKeyQeueueModel blockKeyQeueueModel = JSON.parseObject(blockKeyQeueueModelStr, BlockKeyQeueueModel.class);
            //cacheDataRepository.updateCacheOne(blockKeyQeueueModel);
            /*String packageName = "cn.partytime.scheduler";
            Set<String> classNames = ClassUtils.getClassName(packageName, false);
            log.info("classNames:"+JSON.toJSONString(classNames));
            if (classNames != null) {
                for (String className : classNames) {
                    log.info("classNames",command);
                    show(className,command);
                }
            }*/

            try {
                ApplicationContext ac1 = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);
                BaseScheduler baseScheduler = (BaseScheduler)ac1.getBean(command);
                baseScheduler.execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void show(String path,String command){
        try {
            //Method[] methods =  AnnotationParsing.class.getClassLoader().loadClass("annotation.AnnotationExample").getMethods();
            Method[] methods =  CommandListener.class.getClassLoader().loadClass(path).getMethods();
            Class<?> clazz = Class.forName(path);
            for (Method method : methods) {
                if (method.isAnnotationPresent(CommandAnnotation.class)) {
                    try {
                        CommandAnnotation  commandAnnotation = method.getAnnotation(CommandAnnotation.class);
                        if(command.equals(commandAnnotation.command())){
                            log.info("methodName:"+method.getName());
                            Method method2 = clazz.getMethod(method.getName());
                            method2.invoke(clazz.newInstance());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}