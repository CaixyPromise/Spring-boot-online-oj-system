package com.caixy.onlineJudge.business.user.constant;

/**
 * 用户服务区域内常量
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.user.constant.UserServiceConstant
 * @since 2024/9/5 下午10:21
 */
public interface UserServiceConstant
{
    String USER_ACTIVE_QUEUE_NAME = "Delay-userActiveQueue";
    String USER_ACTIVE_DEAD_QUEUE_NAME = "X-DeadLetter-Delay-userActive-Queue";

}
