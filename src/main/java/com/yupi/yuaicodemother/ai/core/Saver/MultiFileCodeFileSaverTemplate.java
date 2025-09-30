package com.yupi.yuaicodemother.ai.core.Saver;

import cn.hutool.core.util.StrUtil;
import com.yupi.yuaicodemother.ai.model.MultiFileCodeResult;
import com.yupi.yuaicodemother.ai.model.enums.CodeGenTypeEnum;
import com.yupi.yuaicodemother.exception.BusinessException;
import com.yupi.yuaicodemother.exception.ErrorCode;

public class MultiFileCodeFileSaverTemplate extends CodeFileSaverTemplate<MultiFileCodeResult>{

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.MULTI_FILE;
    }

    @Override
    protected void saveFiles(MultiFileCodeResult codeResult, String baseDirPath) {
        //保存html代码
        writeToFile(baseDirPath, "index.html", codeResult.getHtmlCode());
        //保存css代码
        writeToFile(baseDirPath, "style.css", codeResult.getCssCode());
        //保存js代码
        writeToFile(baseDirPath, "script.js", codeResult.getJsCode());
    }

    @Override
    protected void validateInput(MultiFileCodeResult codeResult){
        super.validateInput(codeResult);
        if(StrUtil.isBlank(codeResult.getHtmlCode())){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "html代码内容不能为空");
        }
    }
}
