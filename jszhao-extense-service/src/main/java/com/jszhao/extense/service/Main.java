package com.jszhao.extense.service;

import com.jszhao.extense.invoke.ExtensionsInvoker;
import com.jszhao.extense.model.sub.MathOperationDTO;
import com.jszhao.extense.point.sub.MathOperation;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        MathOperationDTO dto = new MathOperationDTO();
        dto.setBizCode("add");
        dto.setParams(Arrays.asList(1L, 2L, 3L, 4L));

        ExtensionsInvoker<MathOperation, MathOperationDTO> invoker = new ExtensionsInvoker();
        MathOperationDTO execute = invoker.execute(dto, p -> (MathOperationDTO) p.execute(dto));

        System.out.println(execute.getResult());
    }
}
