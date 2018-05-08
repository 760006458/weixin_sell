package com.example.util;

import com.example.dto.CartDTO;
import com.example.enums.CodeEnum;
import com.example.enums.OrderStatusEnum;

/**d
 * @author xuan
 * @create 2018-04-13 14:47
 **/
public class EnumUtil {
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass) {
        for (T t : enumClass.getEnumConstants()) {
            //因为<T extends CodeEnum>的缘故，所以可以调用T的getCode()
            if (code == t.getCode()) {
                return t;
            }
        }
        return null;
    }
}
