package com.caixy.onlineJudge.common.base.utils;

import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.xml.bind.ValidationException;
import java.util.Set;

/**
 * 参数校验器
 *
 * @Author COMPROMISE
 * @name com.caixy.onlineJudge.common.base.utils.BeanValidator
 * @since 2024/7/26 上午1:52
 */
public class BeanValidator
{

    private static final Validator validator = Validation.byProvider(HibernateValidator.class).configure().failFast(
                                                                 true)
                                                         .buildValidatorFactory().getValidator();

    /**
     * @param object object
     * @param groups groups
     */
    public static void validateObject(Object object, Class<?>... groups) throws ValidationException
    {
        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(object, groups);
        if (constraintViolations.stream().findFirst().isPresent())
        {
            throw new ValidationException(constraintViolations.stream().findFirst().get().getMessage());
        }
    }
}