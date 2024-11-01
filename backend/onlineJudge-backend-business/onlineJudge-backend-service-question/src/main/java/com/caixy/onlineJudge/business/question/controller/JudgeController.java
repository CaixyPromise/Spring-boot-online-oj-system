package com.caixy.onlineJudge.business.question.controller;

import com.caixy.onlineJudge.common.response.BaseResponse;
import com.caixy.onlineJudge.common.response.ResultUtils;
import com.caixy.onlineJudge.models.enums.sandbox.LanguageProviderEnum;
import com.caixy.onlineJudge.models.vo.common.OptionVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 判题接口控制器
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.business.question.controller.JudgeController
 * @since 2024/9/13 下午5:49
 */
@Slf4j
@RestController
@RequestMapping("/judge")
public class JudgeController
{
    @GetMapping("/lang/provideList")
    public BaseResponse<List<OptionVO<String>>> getLangProvideList()
    {
        return ResultUtils.success(LanguageProviderEnum.getOptionVO());
    }
}
