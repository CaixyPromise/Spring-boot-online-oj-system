package com.caixy.onlineJudge.models.dto.user;


import com.caixy.onlineJudge.models.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;



import java.io.Serializable;

/**
 * 用户查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable
{
    private String email;

    private static final long serialVersionUID = 1L;
}