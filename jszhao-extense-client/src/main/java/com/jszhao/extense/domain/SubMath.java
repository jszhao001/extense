package com.jszhao.extense.domain;

import com.jszhao.extense.annotation.Extension;
import com.jszhao.extense.model.sub.MathOperationDTO;
import com.jszhao.extense.point.sub.MathOperation;

@Extension(bizCode = "sub")
public class SubMath implements MathOperation<MathOperationDTO> {

    public SubMath() {
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public MathOperationDTO execute(MathOperationDTO mathOperationDTO) {
        System.out.println("执行了sub方法");
        return mathOperationDTO;
    }
}
