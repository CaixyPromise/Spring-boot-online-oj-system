package com.caixy.onlineJudge.models.enums.sandbox;

import com.caixy.onlineJudge.models.vo.common.OptionVO;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 系统所支持的语言
 *
 * @Author CAIXYPROMISE
 * @name com.caixy.onlineJudge.models.enums.sandbox.LanguageProviderEnum
 * @since 2024/9/11 上午2:21
 */
@Getter
public enum LanguageProviderEnum
{
    PYTHON_2("PYTHON_2", "Python_2"),
    PYTHON_3("PYTHON_3", "Python_3"),
    JAVA("JAVA", "Java"),
    CPP("CPP", "C++"),
    C_LANGUAGE("C_LANGUAGE", "C语言");

    private final String value;
    private final String text;

    LanguageProviderEnum(String value, String text)
    {
        this.value = value;
        this.text = text;
    }

    public static LanguageProviderEnum getProviderEnumByValue(String value)
    {
        if (StringUtils.isBlank(value))
        {
            return null;
        }
        for (LanguageProviderEnum languageProviderEnum : LanguageProviderEnum.values())
        {
            if (languageProviderEnum.getValue().equals(value))
            {
                return languageProviderEnum;
            }
        }
        return null;
    }

    public static List<OptionVO<String>> getOptionVO()
    {
        List<OptionVO<String>> optionVOList = new ArrayList<>();
        for (LanguageProviderEnum languageProviderEnum : LanguageProviderEnum.values())
        {
            OptionVO<String> objectOptionVO = new OptionVO<>();
            objectOptionVO.setValue(languageProviderEnum.getValue());
            objectOptionVO.setLabel(languageProviderEnum.getText());
            optionVOList.add(objectOptionVO);
        }
        return optionVOList;
    }
}
