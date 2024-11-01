package com.caixy.onlineJudge.business.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.caixy.onlineJudge.models.entity.User;


/**
 * @author CAIXYPROMISE
 * @description 针对表【user(用户)】的数据库操作Mapper
 * @createDate 2024-07-11 23:46:33
 * @Entity generator.entity.User
 */
public interface UserMapper extends BaseMapper<User>
{
    /**
     * 根据用户名查找用户
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/5 下午11:25
     */
    User findByNickname(String nickname);

    /**
     * 根据邮箱查找用户数据
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/5 下午11:25
     */
    User findByEmail(String userEmail);


    /**
     * 根据Id查找用户数据
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/5 下午11:25
     */
    User findById(Long userId);
}




