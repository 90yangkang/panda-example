package com.panda.example.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yk
 * @version 1.0
 * @describe Bean Help
 * @date 2020-06-04 17:39
 */
@Slf4j
public class BeanUtils {
    /**
     * 对象copy
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source, Class<T> target){
        T t = null;
        try{
            if (source != null){
                t = target.newInstance();
                BeanCopier bc = BeanCopier.create(source.getClass(), target, false);
                bc.copy(source, t, null);
            }
        }catch (Exception e){
            log.error("对象copy异常", e);
        }
        return t;
    }

    /**
     * List集合复制
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> List<T> copyList(List<?> source, Class<T> target){
        if (null == target || CollectionUtils.isEmpty(source)){
            return null;
        }

        List<T> result = new ArrayList<>();
        for (Object obj : source){
            result.add(copyProperties(obj,target));
        }

        return result;
    }
}
