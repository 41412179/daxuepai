package com.daxuepai.gaoxiao.controller;

import com.alibaba.fastjson.JSON;
import com.daxuepai.gaoxiao.model.School;
import com.daxuepai.gaoxiao.service.ImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.List;

@Controller
public class ImportController {

    @Autowired
    ImportService importService;

    @RequestMapping("/import")
    public String importSchoolData(){
        String path = "D:\\Downloads\\gaoxiao\\daxuepai\\gaoxiao\\data\\gaoxiaomingdan.json";
        String result = ReadFile(path);
        List<School> schools = JSON.parseArray(result, School.class);
        for (School school:
             schools) {
            importService.importSchool(school);
        }

        return "test";
    }

    // 读文件，返回字符串
    public String ReadFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                laststr = laststr + tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return laststr;
    }
}
