package com.example.util;

import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 
 * @author zac
 * @version
 * @since 2.0.0
 * 
 * 取得 Bean里 打@NotBlank @NotNull 等标签的结果的共通方法
 */
@Component
public class ZacValidator {
    private static final Logger log = LoggerFactory.getLogger(ZacValidator.class);

    private static Validator validator;

    static {
        if (validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
    }
    
    public <T> boolean listValidator(List<T> list){
        boolean validatorFlg = true;
        for(T t:list){
            if(!beanValidator(t)){
                validatorFlg = false;
            }
        }
        return validatorFlg;
    }
    
    public <T> boolean beanValidator(T t) {
        Set<ConstraintViolation<T>> constraintViolations =  validator.validate(t);
        if (CollectionUtils.size(constraintViolations) > 0) {
            for (ConstraintViolation<T> ConstraintViolation : constraintViolations) {
                log.warn("Validation check failed :" + " " + ConstraintViolation.getMessage());
            }
            
            return false;
        }
        return true;
    }
}
