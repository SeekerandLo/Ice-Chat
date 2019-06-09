package com.liy.chat.utils;

import com.alibaba.fastjson.JSON;
import com.liy.chat.netty.pojo.DataContent;

/**
 * data: 2019/6/5 19:18
 **/

public class MsgUtils {

    // 转换为传输结构
    public static DataContent toDataContent(String jsonString){
        return JSON.parseObject(jsonString,DataContent.class);
    }

}
