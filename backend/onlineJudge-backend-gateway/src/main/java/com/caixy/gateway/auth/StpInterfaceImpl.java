package com.caixy.gateway.auth;

import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;

import com.caixy.onlineJudge.models.enums.user.UserPermission;
import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import com.caixy.onlineJudge.models.enums.user.UserStateEnum;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 自定义权限验证接口
 *
 * @author Hollis
 */
@Component
public class StpInterfaceImpl implements StpInterface
{
    @Override
    public List<String> getPermissionList(Object loginId, String loginType)
    {
        UserVO userInfo = (UserVO) StpUtil.getSessionByLoginId(loginId).get((String) loginId);

        if (userInfo.getUserRole() == UserRoleEnum.ADMIN || userInfo.getUserActive().equals(UserStateEnum.ACTIVE))
        {
            return Arrays.asList(UserPermission.BASIC.name(), UserPermission.AUTH.name());
        }

        if (userInfo.getUserActive().equals(UserStateEnum.DISABLED))
        {
            return Collections.singletonList(UserPermission.FROZEN.name());
        }

        return Collections.singletonList(UserPermission.NONE.name());
    }

    @Override
    public List<String> getRoleList(Object loginId, String loginType)
    {
        UserVO userInfo = (UserVO) StpUtil.getSessionByLoginId(loginId).get((String) loginId);
        if (userInfo.getUserRole() == UserRoleEnum.ADMIN)
        {
            return Collections.singletonList(UserRoleEnum.ADMIN.name());
        }
        return Collections.singletonList(UserRoleEnum.USER.name());
    }
}
