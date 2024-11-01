package com.caixy.onlineJudge.business.admin.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.caixy.onlineJudge.common.exception.BusinessException;
import com.caixy.onlineJudge.common.exception.ThrowUtils;
import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.models.common.DeleteRequest;
import com.caixy.onlineJudge.models.dto.user.UserQueryRequest;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.vo.user.UserAdminVO;
import com.caixy.serviceclient.service.user.UserAdminFacadeService;
import com.caixy.serviceclient.service.user.UserFacadeService;
import com.caixy.serviceclient.service.user.request.UserPageQueryAdminFacadeRequest;
import com.caixy.serviceclient.service.user.request.UserQueryFacadeRequest;
import com.caixy.serviceclient.service.user.request.condition.UserIdQueryCondition;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.caixy.serviceclient.service.user.response.UserQueryResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 用户管理操作接口控制器
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.admin.controller.UserAdminController
 * @since 2024/9/10 上午1:14
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserAdminController
{
    @Resource
    private UserFacadeService userFacadeService;
    @Resource
    private UserAdminFacadeService userAdminFacadeService;

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteUserById(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request)
    {
        if (deleteRequest.getId() == null || deleteRequest.getId() <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserOperatorResponse userOperatorResponse = userAdminFacadeService.deleteUserById(deleteRequest.getId());
        return ResultUtils.success(userOperatorResponse.isSucceed());
    }


    /**
     * 根据 id 获取用户（仅管理员）
     *
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get")
    public BaseResponse<UserAdminVO> getUserById(long id, HttpServletRequest request)
    {
        if (id <= 0)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserQueryFacadeRequest userQueryFacadeRequest = UserQueryFacadeRequest.build(new UserIdQueryCondition(id));
        UserQueryResponse<UserAdminVO> userAdminResponse = userAdminFacadeService.query(userQueryFacadeRequest);
        ThrowUtils.throwIf(!userAdminResponse.isSucceed(), ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(userAdminResponse.getData());
    }


    @PostMapping("/list/page")
    public BaseResponse<Page<UserAdminVO>> listUserByPage(@RequestBody
                                                          UserQueryRequest userQueryRequest,
                                                          HttpServletRequest request)
    {
        int current = userQueryRequest.getCurrent();
        int size = userQueryRequest.getPageSize();
        UserPageQueryAdminFacadeRequest pageQueryAdminFacadeRequest = new UserPageQueryAdminFacadeRequest();
        pageQueryAdminFacadeRequest.setPageSize(size);
        pageQueryAdminFacadeRequest.setCurrent(current);
        UserQueryResponse<Page<UserAdminVO>> queryResponse = userAdminFacadeService.page(pageQueryAdminFacadeRequest);
        Page<UserAdminVO> userPage = queryResponse.getData();
        return ResultUtils.success(userPage);
    }
}
