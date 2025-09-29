package com.yupi.yuaicodemother.ai.model;

import jdk.jfr.Description;
import lombok.Data;

@Description("生成多代码文件的结果")
@Data
public class MultiFileCodeResult {
    @Description("html代码")
    private String htmlCode;
    @Description("css代码")
    private String cssCode;
    @Description("js代码")
    private String jsCode;
    @Description("生成代码描述")
    private String discription;
}
