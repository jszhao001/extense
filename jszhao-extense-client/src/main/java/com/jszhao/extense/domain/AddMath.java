package com.jszhao.extense.domain;

import com.jszhao.extense.annotation.Extension;
import com.jszhao.extense.model.sub.MathOperationDTO;
import com.jszhao.extense.point.sub.MathOperation;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Extension(bizCode = "add")
@Slf4j
public class AddMath implements MathOperation<MathOperationDTO> {

    public AddMath() {
        init();
    }

    @Override
    public void init() {

    }

    @Override
    public MathOperationDTO execute(MathOperationDTO mathOperationDTO) {
        List<Long> params = mathOperationDTO.getParams();

        Long sum = 0L;

        for (int i = 0; i < params.size(); i++) {
            sum += params.get(i);
        }

        mathOperationDTO.setResult(sum);

        return mathOperationDTO;
    }
}
