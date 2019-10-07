package com.jszhao.extense.model.sub;

import com.jszhao.extense.annotation.BizInterface;
import com.jszhao.extense.model.ExtensionBaseDTO;
import com.jszhao.extense.point.sub.MathOperation;
import lombok.Data;

import java.util.List;

@Data
@BizInterface(interfaceName = MathOperation.class)
public class MathOperationDTO extends ExtensionBaseDTO {
    private List<Long> params;

    private Long result;
}
