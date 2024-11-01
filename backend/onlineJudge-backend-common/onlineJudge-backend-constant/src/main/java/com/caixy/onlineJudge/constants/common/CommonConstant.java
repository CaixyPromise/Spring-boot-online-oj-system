package com.caixy.onlineJudge.constants.common;

/**
 * 通用常量接口配置
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.constants.common.CommonConstants
 * @since 2024/7/22 下午9:20
 */
public interface CommonConstant
{
    /**
     * 升序
     */
    String SORT_ORDER_ASC = "ascend";

    /**
     * 降序
     */
    String SORT_ORDER_DESC = " descend";

    /**
     * 系统消息身份Id
     */
    Long SYSTEM_ID_CODE = 0L;

    String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    String HEADER_SERVICE_GATEWAY_VALUE = "Service-Gateway";

    String CAPTCHA_SIGN = "captcha_sign";
    String FRONTED_URL = "http://localhost:8000";

}
