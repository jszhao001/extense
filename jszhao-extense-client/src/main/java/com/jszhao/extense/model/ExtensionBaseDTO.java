package com.jszhao.extense.model;

import lombok.Data;

@Data
public class ExtensionBaseDTO {
    /**
     * 业务编码:如add
     */
    private String bizCode;

    /**
     * 业务类类型：如MathOperation.class
     *
     * @see com.jszhao.extense.point.sub.MathOperation
     */
    private Class bizClass;
}
