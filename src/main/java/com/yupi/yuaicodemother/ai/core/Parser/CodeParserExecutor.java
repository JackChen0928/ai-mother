package com.yupi.yuaicodemother.ai.core.Parser;


import ch.qos.logback.classic.spi.EventArgUtil;
import com.yupi.yuaicodemother.ai.model.enums.CodeGenTypeEnum;
import com.yupi.yuaicodemother.exception.BusinessException;
import com.yupi.yuaicodemother.exception.ErrorCode;

/**
 * 代码解析执行器
 * 根据代码生成类型执行相应的解析逻辑
 */
public class CodeParserExecutor {

     private static final HtmlCodeParser htmlCodeParser= new HtmlCodeParser();

     private static final MultiFileCodeParser multiFileCodeParser= new MultiFileCodeParser();

    /**
     * 执行代码解析逻辑
     */
    public static Object executeParser(String codeContent, CodeGenTypeEnum codeGenTypeEnum){
        return switch (codeGenTypeEnum) {
            case HTML -> htmlCodeParser.parseCode(codeContent);
            case MULTI_FILE -> multiFileCodeParser.parseCode(codeContent);
            default -> throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的代码生成类型："+codeGenTypeEnum);
        };

    }


}
