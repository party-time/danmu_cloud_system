package cn.partytime.listener;

import cn.partytime.annotation.CommandAnnotation;
import cn.partytime.util.ClassUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Set;


@Slf4j
@Component("commandListener")
public class CommandListener implements MessageListener {

    @Override
    public void onMessage(Message message, byte[] bytes) {
        if(message!=null){
            String command = JSON.parseObject(String.valueOf(message),String.class).replace("'","");
            log.info("command:"+command);
            //BlockKeyQeueueModel blockKeyQeueueModel = JSON.parseObject(blockKeyQeueueModelStr, BlockKeyQeueueModel.class);
            //cacheDataRepository.updateCacheOne(blockKeyQeueueModel);
            String packageName = "cn.partytime.scheduler";
            Set<String> classNames = ClassUtils.getClassName(packageName, false);
            if (classNames != null) {
                log.info("classNames:"+JSON.toJSONString(classNames));
                for (String className : classNames) {
                    log.info("classNames",command);
                    show(className,command);
                }
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