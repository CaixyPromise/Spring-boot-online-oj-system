package com.caixy.onlineJudge.business.user.facade;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.onlineJudge.business.user.service.UserService;
import com.caixy.onlineJudge.common.rpc.facade.Facade;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.convertor.user.UserConvertor;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.vo.user.UserAdminVO;
import com.caixy.serviceclient.service.user.UserAdminFacadeService;
import com.caixy.serviceclient.service.user.request.UserPageQueryAdminFacadeRequest;
import com.caixy.serviceclient.service.user.request.UserQueryFacadeRequest;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

/**
 * 管理员用户远程调用接口
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.user.facade.UserAdminFacadeServiceImpl
 * @since 2024/9/10 上午2:56
 */
@DubboService(version = "1.0.0")
public class UserAdminFacadeServiceImpl implements UserAdminFacadeService
{

    @Resource
    private UserService userService;

    @Facade
    @Override
    public UserOperatorResponse deleteUserById(Long id)
    {
        UserOperatorResponse response = new UserOperatorResponse();
        boolean removeById = userService.removeById(id);
        if (removeById) {
            response.setCode(ErrorCode.SUCCESS.getCode());
        }
        else {
            response.setCode(ErrorCode.NOT_FOUND_ERROR.getCode());
        }
        response.setData(null);
        return response;
    }


    /**
     * 管理员远程调用查询
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/10 上午3:17
     */
    @Override
    public UserQueryResponse<UserAdminVO> query(UserQueryFacadeRequest userQueryFacadeRequest)
    {
        User queriedResult = userService.query(userQueryFacadeRequest);
        UserQueryResponse<UserAdminVO> response = new UserQueryResponse<>();

        if (queriedResult != null)
        {
            response.setCode(ErrorCode.SUCCESS.getCode());
            UserAdminVO userInfo = UserConvertor.INSTANCE.convertToAdmin(queriedResult);
            response.setData(userInfo);
        }
        else {
            response.setCode(ErrorCode.NOT_FOUND_ERROR.getCode());
            response.setData(null);
        }
        return response;
    }

    /**
     * 管理员远程调用分页查询
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/10 上午3:17
     */
    @Facade
    @Override
    public UserQueryResponse<Page<UserAdminVO>> page(UserPageQueryAdminFacadeRequest pageQueryAdminFacadeRequest)
    {
        Page<UserAdminVO> userAdminVOPage = userService.userAdminVOPage(pageQueryAdminFacadeRequest);
        UserQueryResponse<Page<UserAdminVO>> response = new UserQueryResponse<>();
        response.setData(userAdminVOPage);
        if (userAdminVOPage.getTotal() != 0)
        {
            response.setCode(ErrorCode.SUCCESS.getCode());
        }
        else {
            response.setCode(ErrorCode.NOT_FOUND_ERROR.getCode());
        }
        return response;
    }
}
