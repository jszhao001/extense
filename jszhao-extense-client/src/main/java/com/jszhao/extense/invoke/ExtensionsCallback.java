package com.jszhao.extense.invoke;

import com.jszhao.extense.model.ExtensionBaseDTO;
import com.jszhao.extense.point.ExtensionPoints;

public interface ExtensionsCallback<Point extends ExtensionPoints, Dto extends ExtensionBaseDTO> {
    Dto Callback(Point t);
}
