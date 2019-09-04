package com.daxuepai.gaoxiao.controller;

import com.daxuepai.gaoxiao.model.Result;
import com.daxuepai.gaoxiao.util.ErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class AdviceController {

    Logger logger = LoggerFactory.getLogger(AdviceController.class);

    @ResponseBody
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Result handleMissingParams(Exception e){
        logger.error(ErrorCode.MISSING_PARAMS_EXCEPTION.toString());
        logger.error("", e);
        Result result = new Result();
        result.setCode(ErrorCode.MISSING_PARAMS_EXCEPTION.getCode());
        result.setMsg(ErrorCode.MISSING_PARAMS_EXCEPTION.getText());
        return  result;
    }


}
