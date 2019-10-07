package com.jszhao.extense.invoke;

import com.jszhao.extense.model.ExtensionBaseDTO;
import com.jszhao.extense.point.ExtensionPoints;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

@Slf4j
public class ExtensionsInvoker<Point extends ExtensionPoints, Dto extends ExtensionBaseDTO> {

    public Dto execute(Dto dto, ExtensionsCallback<Point, Dto> callback) {
        String bizCode = dto.getBizCode();
        Class<Point> bizClass = dto.getBizClass();

        if (null == bizClass) {
            bizClass = ExtensionsBuilder.getInstance().getExtPoint(dto.getClass());
            dto.setBizClass(bizClass);
        }

        return executeWithPoint(dto, callback);
    }

    private Dto executeWithPoint(Dto dto, ExtensionsCallback<Point, Dto> callback) {
        String bizCode = dto.getBizCode();
        Class<Point> bizClass = dto.getBizClass();

        if (StringUtils.isBlank(bizCode) || null == bizClass) {
            log.debug("bizCode or bizClass is null");
            return dto;
        }

        // TODO: 2019/10/7 这里为什么要强转？？？
        Point extPoint = (Point) ExtensionsBuilder.getInstance().getExtPoint(bizClass, bizCode);
        if (Objects.isNull(extPoint)) {
            log.debug("target plugin not find");
            return dto;
        }

        return callback.Callback(extPoint);
    }
}
