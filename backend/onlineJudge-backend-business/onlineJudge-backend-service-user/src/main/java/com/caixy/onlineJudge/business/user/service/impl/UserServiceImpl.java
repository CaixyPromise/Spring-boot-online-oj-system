package com.caixy.onlineJudge.business.user.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.caixy.onlineJudge.common.cache.redis.RedisUtils;
import com.caixy.onlineJudge.common.cache.redis.annotation.DistributedLock;
import com.caixy.onlineJudge.common.email.models.active.SendUserActiveDTO;
import com.caixy.onlineJudge.common.email.mq.EmailSenderRabbitMQProducer;
import com.caixy.onlineJudge.common.exception.BusinessException;

import com.caixy.onlineJudge.common.objectMapper.ObjectMapperUtil;
import com.caixy.onlineJudge.common.regex.RegexUtils;
import com.caixy.onlineJudge.constants.code.ErrorCode;
import com.caixy.onlineJudge.constants.common.UserConstant;
import com.caixy.onlineJudge.common.encrypt.EncryptUtils;
import com.caixy.onlineJudge.business.user.service.UserService;
import com.caixy.onlineJudge.business.user.mapper.UserMapper;
import com.caixy.onlineJudge.models.convertor.user.UserConvertor;
import com.caixy.onlineJudge.models.dto.oauth.OAuthResultDTO;
import com.caixy.onlineJudge.models.dto.user.*;
import com.caixy.onlineJudge.models.enums.email.EmailSenderCategoryEnum;
import com.caixy.onlineJudge.models.enums.redis.RDLockKeyEnum;
import com.caixy.onlineJudge.models.enums.redis.RedisKeyEnum;
import com.caixy.onlineJudge.models.enums.user.UserStateEnum;
import com.caixy.onlineJudge.models.vo.user.UserAdminVO;
import com.caixy.serviceclient.service.user.request.UserPageQueryAdminFacadeRequest;
import com.caixy.serviceclient.service.user.request.UserQueryFacadeRequest;
import com.caixy.serviceclient.service.user.request.condition.*;
import com.caixy.serviceclient.service.user.response.UserOperatorResponse;
import com.fasterxml.jackson.databind.type.TypeFactory;
import lombok.extern.slf4j.Slf4j;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import com.caixy.onlineJudge.models.vo.user.LoginUserVO;
import com.caixy.onlineJudge.models.vo.user.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBloomFilter;
import org.redisson.api.RedissonClient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author CAIXYPROMISE
 * @description 针对表【user(用户)】的数据库操作Service实现
 * @createDate 2024-07-11 23:46:33
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService, InitializingBean
{

    private RBloomFilter<String> nickNameBloomFilter;

    private RBloomFilter<String> emailBloomFilter;

    //    @InjectRedissonClient(clientName = "bloom-filter", name = "bloom-filter")
    @Resource
    private RedissonClient redissonClient;

    @Resource
    private EmailSenderRabbitMQProducer<SendUserActiveDTO> emailSenderRabbitMQProducer;

    @Resource
    private UserMapper userMapper;
    @Resource
    private RedisUtils redisUtils;

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @Override
    @Deprecated
    public User getLoginUser(HttpServletRequest request)
    {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null)
        {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        if (currentUser.getUserRole().equals(UserConstant.BAN_ROLE))
        {
            // 被封号的用户，先断开连接
            userLogout(request);
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR, "账号已被封禁");
        }
        return currentUser;
    }

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request)
    {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null)
        {
            return null;
        }
        return currentUser;
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request)
    {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user)
    {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }

    /**
     * 用户注销
     *
     * @param request
     */
    @Override
    public boolean userLogout(HttpServletRequest request)
    {
        if (request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE) == null)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        // 移除登录态
        request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user)
    {
        if (user == null)
        {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user)
    {
        if (user == null)
        {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList)
    {
        if (CollUtil.isEmpty(userList))
        {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest)
    {
        if (userQueryRequest == null)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }

        return new QueryWrapper<>();
    }


    /**
     * 随机生成密码
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/4/26 下午9:42
     */
    @Override
    public String generatePassword()
    {
        // 定义字符集
        String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
        String upperCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String numbers = "0123456789";
        String specialCharacters = "!@#$%&*.?";

        // 确保每种字符至少出现一次
        List<Character> passwordChars = new ArrayList<>();
        passwordChars.add(RandomUtil.randomChar(lowerCaseLetters));
        passwordChars.add(RandomUtil.randomChar(upperCaseLetters));
        passwordChars.add(RandomUtil.randomChar(numbers));
        passwordChars.add(RandomUtil.randomChar(specialCharacters));

        // 随机密码长度
        int length = RandomUtil.randomInt(8, 21);

        // 填充剩余的字符
        String allCharacters = lowerCaseLetters + upperCaseLetters + numbers + specialCharacters;
        for (int i = passwordChars.size(); i < length; i++)
        {
            passwordChars.add(RandomUtil.randomChar(allCharacters));
        }

        // 打乱字符顺序
        Collections.shuffle(passwordChars);

        // 构建最终的密码字符串
        StringBuilder password = new StringBuilder();
        for (Character ch : passwordChars)
        {
            password.append(ch);
        }

        return password.toString();
    }


    @Override
    public Boolean modifyPassword(Long userId, UserModifyPasswordRequest userModifyPasswordRequest)
    {
        String userPassword = userModifyPasswordRequest.getNewPassword();
        if (userPassword.length() < 8)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (userPassword.length() > 20)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过长");
        }
        if (!userPassword.equals(userModifyPasswordRequest.getConfirmPassword()))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入密码不一致");
        }
        // 查询用户
        User currenUser = this.getById(userId);
        if (currenUser == null)
        {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "用户不存在");
        }

        // 校验密码
        boolean matches =
                EncryptUtils.matches(userModifyPasswordRequest.getOldPassword(), currenUser.getUserPassword());
        if (!matches)
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "原密码错误");
        }

        // 加密密码
        String encryptPassword = EncryptUtils.encodePassword(userPassword);
        currenUser.setUserPassword(encryptPassword);
        // 清空登录状态
        StpUtil.logout();
        return this.updateById(currenUser);
    }

    @DistributedLock(lockKeyEnum = RDLockKeyEnum.USER_LOCK, args = "#email")
    @Override
    public UserOperatorResponse preRegisterByEmail(String email, String password)
    {
        if (StringUtils.isAnyBlank(password, email))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (!RegexUtils.isEmail(email) || !RegexUtils.validatePassword(password))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱或密码格式错误");
        }
        // 预注册用户信息
        if (emailIsExist(email))
        {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "邮箱已注册");
        }
        // todo: 增加已经预注册的邮箱不允许重复预注册
        User user = User.preRegister(email, EncryptUtils.encodePassword(password));
        Map<String, Object> convertedMap = ObjectMapperUtil.convertValue(user,
                TypeFactory.defaultInstance().constructMapType(Map.class, String.class, Object.class));

        SendUserActiveDTO userActiveDTO = new SendUserActiveDTO();
        String token = DigestUtil.md5Hex(user.getUserEmail() +
                                         user.getCreateTime() +
                                         RandomUtil.randomNumbers(6) +
                                         System.currentTimeMillis());
        userActiveDTO.setToken(token);
        emailSenderRabbitMQProducer.sendEmail(email, userActiveDTO, EmailSenderCategoryEnum.ACTIVE_USER);
        // 账号数据写入缓存
        redisUtils.setHashMap(RedisKeyEnum.ACTIVE_USER, convertedMap, token);
        // 返回
        return UserOperatorResponse.success(null);
    }

    /**
     * 激活用户
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/6 上午1:25
     */
    @DistributedLock(lockKeyEnum = RDLockKeyEnum.USER_LOCK, args = "#email")
    @Override
    public UserOperatorResponse activeUserAccount(User userInfo, String nickName, Integer userGender)
    {
        String email = userInfo.getUserEmail();

        // 检查有没有重复激活
        if (emailIsExist(email))
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "账号已激活");
        }
        if (nickNameIsExist(nickName))
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "昵称已存在");
        }
//        ThrowUtils.throwIf(nickNameIsExist(nickName), ErrorCode.PARAMS_ERROR, "昵称已存在");

        // 设置用户状态
        userInfo.setIsActive(UserStateEnum.ACTIVE.getCode());
        userInfo.setNickName(nickName);
        userInfo.setUserGender(userGender);
        userInfo.setUserRole(UserRoleEnum.USER.getValue());
        // 保存用户信息
        boolean saved = this.save(userInfo);
        if (!saved)
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "激活失败");
        }
        // 更新过滤器
        addNickName(nickName);
        addEmail(email);
        return UserOperatorResponse.success(null);
    }

    @Override
    public UserOperatorResponse doAuthLogin(OAuthResultDTO resultResponse)
    {
        if (!resultResponse.isSuccess())
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "验证失败");
        }
        UserLoginByOAuthAdapter loginAdapter = resultResponse.getLoginAdapter();
        User oauthUserInfo = loginAdapter.getUserInfo();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(loginAdapter.getUniqueFieldName(), loginAdapter.getUniqueFieldValue());
        User userInfo = this.getOne(queryWrapper);
        log.info("查询到登录用户信息: {}", userInfo);
        boolean isRegister = userInfo == null;
        // 如果未查询到，注册该用户
        if (isRegister)
        {
            userInfo = UserConvertor.INSTANCE.copyAllPropertiesIgnoringId(oauthUserInfo, userInfo);
            userInfo.setUserRole(UserRoleEnum.USER.getValue());
        }
        else // 更新用户信息
        {
            userInfo = UserConvertor.INSTANCE.copyPropertiesWithStrategy(
                    oauthUserInfo,
                    userInfo,
                    new HashSet<>(
                            Arrays.asList("id", "userPassword", "createTime", "updateTime", "isDelete", "userRole",
                                    "isActive")),
                    ((sourceValue, targetValue) -> sourceValue != null && targetValue == null));
        }
        UserOperatorResponse userOperatorResponse = new UserOperatorResponse();

        boolean result = isRegister ? this.save(userInfo) : this.updateById(userInfo);
        if (!result)
        {
            log.warn("用户信息操作失败: {}, 方法: {}", userInfo, isRegister ? "注册" : "登录");
            userOperatorResponse.setCode(ErrorCode.OPERATION_ERROR.getCode());
        }
        else
        {
            userOperatorResponse.setCode(ErrorCode.SUCCESS.getCode());
            UserVO convertedVo = UserConvertor.INSTANCE.convert(userInfo);
            userOperatorResponse.setData(convertedVo);
        }
        return userOperatorResponse;
    }

    @Override
    public void validUserInfo(User user, boolean isAdd)
    {

    }

    /**
     * 根据邮箱查找用户
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/8/31 下午3:59
     */
    @Override
    public User findByEmail(String email)
    {
        return userMapper.findByEmail(email);
    }

    /**
     * 根据用户名查找用户
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/6 上午1:55
     */
    @Override
    public User findByNickName(String nickName)
    {
        return userMapper.findByNickname(nickName);
    }


    /**
     * 根据邮箱判断用户是否存在
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/8/31 下午2:45
     */
    @Override
    public boolean emailIsExist(String email)
    {
        //如果布隆过滤器中存在，再进行数据库二次判断
        if (this.emailBloomFilter != null && this.emailBloomFilter.contains(email))
        {
            return userMapper.findByEmail(email) != null;
        }
        return false;
    }

    /**
     * 根据用户名判断用户是否存在
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/8/31 下午2:45
     */
    @Override
    public boolean nickNameIsExist(String nickName)
    {
        //如果布隆过滤器中存在，再进行数据库二次判断
        if (this.nickNameBloomFilter != null && this.nickNameBloomFilter.contains(nickName))
        {
            return userMapper.findByNickname(nickName) != null;
        }
        return false;
    }

    @Override
    public User findByEmailAndPass(String email, String password)
    {
        User byEmail = userMapper.findByEmail(email);
        if (byEmail != null && EncryptUtils.matches(password, byEmail.getUserPassword()))
        {
            return byEmail;
        }
        return null;
    }

    @Override
    public User findById(Long userId)
    {
        return userMapper.findById(userId);
    }

    private boolean addNickName(String nickName)
    {
        return this.nickNameBloomFilter != null && this.nickNameBloomFilter.add(nickName);
    }

    private boolean addEmail(String email)
    {
        return this.emailBloomFilter != null && this.emailBloomFilter.add(email);
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        this.nickNameBloomFilter = redissonClient.getBloomFilter("nickName");
        if (nickNameBloomFilter != null && !nickNameBloomFilter.isExists())
        {
            this.nickNameBloomFilter.tryInit(100000L, 0.01);
        }
        this.emailBloomFilter = redissonClient.getBloomFilter("email");
        if (emailBloomFilter != null && !emailBloomFilter.isExists())
        {
            this.emailBloomFilter.tryInit(100000L, 0.01);
        }
    }

    /**
     * 查询用户
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/10 上午2:59
     */
    @Override
    public User query(UserQueryFacadeRequest userQueryFacadeRequest)
    {
        User user = null;
        UserQueryCondition condition = userQueryFacadeRequest.getUserQueryCondition();
        if (condition instanceof UserIdQueryCondition)
        {
            UserIdQueryCondition userIdQueryCondition = (UserIdQueryCondition) condition;
            user = findById(userIdQueryCondition.getUserId());
        }
        else if (condition instanceof UserEmailAndPassQueryCondition)
        {
            UserEmailAndPassQueryCondition userPhoneQueryCondition = (UserEmailAndPassQueryCondition) condition;
            user = findByEmail(userPhoneQueryCondition.getEmail());
        }
        else if (condition instanceof UserNickNameQueryCondition)
        {
            UserNickNameQueryCondition userPhoneQueryCondition = (UserNickNameQueryCondition) condition;
            user = findByNickName(userPhoneQueryCondition.getNickName());
        }
        else
        {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "不允许的请求类型");
        }
        return user;
    }

    /**
     * 远程调用给管理员进行用户分页
     *
     * @author CAIXYPROMISE
     * @version 1.0
     * @since 2024/9/10 上午3:16
     */
    @Override
    public Page<UserAdminVO> userAdminVOPage(UserPageQueryAdminFacadeRequest pageQueryAdminFacadeRequest)
    {
        Page<User> userPage = new Page<>(pageQueryAdminFacadeRequest.getCurrent(),
                pageQueryAdminFacadeRequest.getPageSize());
        Page<User> page = this.page(userPage);
        Page<UserAdminVO> userAdminVOPage = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        if (userPage.getTotal() == 0)
        {
            userAdminVOPage.setRecords(Collections.emptyList());
        }
        else
        {
            UserConvertor userConvertor = UserConvertor.INSTANCE;
            userAdminVOPage.setRecords(page.getRecords().stream().map(user ->
            {
                UserAdminVO userAdminVO = new UserAdminVO();
                userConvertor.convertToAdmin(user);
                return userAdminVO;
            }).collect(Collectors.toList()));
        }
        return userAdminVOPage;
    }
}