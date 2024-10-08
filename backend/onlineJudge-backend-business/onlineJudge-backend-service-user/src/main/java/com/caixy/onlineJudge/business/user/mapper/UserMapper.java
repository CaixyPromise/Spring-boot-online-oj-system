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
    User findByNickname(String nickname);

    User findByEmail(String userEmail);
}




