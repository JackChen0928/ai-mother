package com.yupi.yuaicodemother.ai.core.Saver;

import com.yupi.yuaicodemother.ai.model.HtmlCodeResult;
import com.yupi.yuaicodemother.ai.model.MultiFileCodeResult;
import com.yupi.yuaicodemother.ai.model.enums.CodeGenTypeEnum;
import com.yupi.yuaicodemother.exception.BusinessException;
import com.yupi.yuaicodemother.exception.ErrorCode;

import java.io.File;

public class CodeFileSaverExecutor {

    private static final HtmlCodeFileSaverTemplate htmlCodeFileSaverTemplate =new HtmlCodeFileSaverTemplate();

    private static final MultiFileCodeFileSaverTemplate multiFileCodeFileSaverTemplate =new MultiFileCodeFileSaverTemplate();

    public static File executorSaver(Object codeResult, CodeGenTypeEnum codeGenType, Long appId){
        switch (codeGenType){
            case HTML:
                return htmlCodeFileSaverTemplate.saveCode((HtmlCodeResult) codeResult,appId);
            case MULTI_FILE:
                return multiFileCodeFileSaverTemplate.saveCode((MultiFileCodeResult) codeResult,appId);
            default:
                throw new BusinessException(ErrorCode.SYSTEM_ERROR,"不支持的代码生成类型："+ codeGenType);
        }
    }

}
