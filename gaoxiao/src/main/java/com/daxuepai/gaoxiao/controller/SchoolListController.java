package com.daxuepai.gaoxiao.controller;

import com.alibaba.fastjson.JSON;
import com.daxuepai.gaoxiao.model.Result;
import com.daxuepai.gaoxiao.model.ResultStatus;
import com.daxuepai.gaoxiao.result.SchoolResult;
import com.daxuepai.gaoxiao.service.SchoolService;
import com.daxuepai.gaoxiao.util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class SchoolListController {

    @Autowired
    SchoolService schoolService;

    @RequestMapping(value = "/school/list", method = RequestMethod.GET)
    @ResponseBody
    public Result getSchoolList(){
        Result result = null;
        List<SchoolResult> results;
        try {
            results = schoolService.getList();
        }catch (Exception e){
            e.printStackTrace();
            result = new Result(ErrorCode.GET_SCHOOL_LIST_FAILED);
            return result;
        }
        result = new Result(ErrorCode.SUCCESS);
        result.setData("schoolList", results);
        return result;
    }
}
