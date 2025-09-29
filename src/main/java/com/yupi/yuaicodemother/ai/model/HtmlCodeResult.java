package com.yupi.yuaicodemother.ai.model;

import jdk.jfr.Description;
import lombok.Data;

@Description("生成html代码文件的结果")
@Data
public class HtmlCodeResult {
    @Description("html代码")
    private String htmlCode;
    @Description("生成代码的描述")
    private String discription;
}
