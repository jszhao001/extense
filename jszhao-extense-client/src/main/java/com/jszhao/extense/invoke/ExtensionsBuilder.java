package com.jszhao.extense.invoke;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.jszhao.extense.annotation.BizInterface;
import com.jszhao.extense.annotation.Extension;
import com.jszhao.extense.point.ExtensionPoints;
import com.jszhao.extense.point.sub.MathOperation;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;


public class ExtensionsBuilder {

    private static Logger log = LoggerFactory.getLogger(ExtensionsBuilder.class);

    /**
     * 默认加载路径
     */
    public final static String DEFAULT_LOADER_CLASS_PATH = "com.jszhao.extense.point";
    public final static String DEFAULT_SUB_LOADER_CLASS_PATH = "com.jszhao.extense.domain";
    public final static String DEFAULT_DTO_LOADER_CLASS_PATH = "com.jszhao.extense.model";

    /**
     * 已经加载的一级路径
     */
    public final static Set<String> LOADED_CLASS_PATHS = Sets.newConcurrentHashSet();

    /**
     * extMap里面存放扩展点信息
     */
    private final static Map<Class, Map<String, Object>> extMap = Maps.newConcurrentMap();

    /**
     * 维护dto和point点的映射关系
     */
    private final static Map<Class, Class> dto2PointMap = Maps.newConcurrentMap();

    /**
     * 单例
     */
    private final static ExtensionsBuilder builder = new ExtensionsBuilder();

    static {
        // 在此处build map
        initExtMap(null, null, null, true);
    }

    public static boolean initExtMap(String classPath, String subClassPath, String dtoClassPath, boolean isRepeatLoad) {
        if (StringUtils.isBlank(classPath) || StringUtils.isBlank(subClassPath) || StringUtils.isBlank(dtoClassPath)) {
            classPath = DEFAULT_LOADER_CLASS_PATH;
            subClassPath = DEFAULT_SUB_LOADER_CLASS_PATH;
            dtoClassPath = DEFAULT_DTO_LOADER_CLASS_PATH;
        }

        synchronized (LOADED_CLASS_PATHS) {
            // 防止多个线程架子啊
            if (!isRepeatLoad && LOADED_CLASS_PATHS.contains(classPath)) {
                log.info(String.format("the classPath {} has load", classPath));
                return true;
            }

            LOADED_CLASS_PATHS.add(classPath);
        }

        // 1. 读取指定目录下的class文件
        Reflections bizInterfaceReflections = new Reflections(classPath);
        Reflections bizImplReflections = new Reflections(subClassPath);
        Reflections dtoReflections = new Reflections(dtoClassPath);

        // 2. 获取到所有业务扩展点的一级接口定义
        Set<Class<? extends ExtensionPoints>> bizInterfaces = bizInterfaceReflections.getSubTypesOf(ExtensionPoints.class);
        Set<Class<?>> bizImpls = bizImplReflections.getTypesAnnotatedWith(Extension.class);
        Set<Class<?>> dtos = dtoReflections.getTypesAnnotatedWith(BizInterface.class);

        // 3. 获取DTO对象和point对象之间的映射关系


        if (CollectionUtils.isEmpty(bizInterfaces)
                || CollectionUtils.isEmpty(bizImpls)
                || CollectionUtils.isEmpty(dtos)) {
            return true;
        }

        bizInterfaces.forEach(bizInterface -> {
            Map<String, Object> bizMap;
            if (extMap.containsKey(bizInterface)) {
                bizMap = extMap.get(bizInterface);
            } else {
                bizMap = Maps.newHashMap();
            }

            bizImpls.forEach(bizImpl -> {
                if (bizInterface.isAssignableFrom(bizImpl)) {
                    // 说明bizImpl 是bizInterface的实现
                    Extension[] annotations = bizImpl.getAnnotationsByType(Extension.class);
                    for (Extension annotation : annotations) {
                        try {
                            // 创建实现类的实例，置于map中
                            Object o = bizImpl.newInstance();
                            Method method = bizImpl.getMethod("init");
                            method.invoke(o);
                            bizMap.put(annotation.bizCode(), o);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            extMap.put(bizInterface, bizMap);
        });

        dtos.forEach(dto -> {
            if (extMap.containsKey(dto)) {
                return;
            }
            BizInterface[] annotations = dto.getAnnotationsByType(BizInterface.class);
            for (BizInterface annotation : annotations) {
                try {
                    dto2PointMap.put(dto, annotation.interfaceName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });

        return true;
    }

    public <Point> Point getExtPoint(Class<Point> point, String bizCode) {
        if (extMap.containsKey(point)) {
            return (Point) extMap.get(point).get(bizCode);
        }

        return null;
    }

    public Class getExtPoint(Class dto) {
        return dto2PointMap.get(dto);
    }

    public static ExtensionsBuilder getInstance() {
        return builder;
    }
}
