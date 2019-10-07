package com.jszhao.extense.point;

import com.jszhao.extense.model.ExtensionBaseDTO;

public interface ExtensionPoints<T extends ExtensionBaseDTO> {
    /**
     * 执行的方法
     */
    T execute(T t);

    /**
     * 初始化方法
     */
    void init();
}
