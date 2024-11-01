package com.caixy.onlineJudge.models.vo.user;

import com.caixy.onlineJudge.constants.regex.RegexPatternConstants;
import com.caixy.onlineJudge.models.convertor.user.UserConvertor;
import com.caixy.onlineJudge.models.entity.User;
import com.caixy.onlineJudge.models.enums.user.UserRoleEnum;
import com.caixy.onlineJudge.models.enums.user.UserStateEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;

/**
 * 关于我个人信息VO
 *
 * @name: com.caixy.adminSystem.model.vo.user.AboutMeVO
 * @author: CAIXYPROMISE
 * @since: 2024-04-14 20:39
 **/
@Data
public class AboutMeVO implements Serializable
{
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 用户头像
     */
    private String userAvatar;
    
    /**
     * 用户性别
     */
    private Integer userGender;
    

    /**
     * 用户角色：user/admin/ban
     */
    private UserRoleEnum userRole;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 状态
     *
     * @see UserStateEnum
     */
    private UserStateEnum userActive;

    public static AboutMeVO of(User currentUser)
    {
        AboutMeVO aboutMeVO = UserConvertor.INSTANCE.convertToAboutMe(currentUser);
        // 脱密处理
        aboutMeVO.setUserEmail(
                currentUser.getUserEmail().replaceAll(RegexPatternConstants.EMAIL_ENCRYPT_REGEX, "$1****$2"));
        return aboutMeVO;
    }


    private static final long serialVersionUID = 1L;
}
