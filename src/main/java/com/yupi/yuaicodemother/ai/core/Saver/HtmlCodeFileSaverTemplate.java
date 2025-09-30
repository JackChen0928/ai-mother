package com.yupi.yuaicodemother.ai.core.Saver;

import com.yupi.yuaicodemother.ai.model.HtmlCodeResult;
import com.yupi.yuaicodemother.ai.model.enums.CodeGenTypeEnum;
import com.yupi.yuaicodemother.exception.BusinessException;
import com.yupi.yuaicodemother.exception.ErrorCode;

public class HtmlCodeFileSaverTemplate extends CodeFileSaverTemplate<HtmlCodeResult>{

    @Override
    protected CodeGenTypeEnum getCodeType() {
        return CodeGenTypeEnum.HTML;
    }

    @Override
    protected void saveFiles(HtmlCodeResult codeResult, String baseDirPath) {
        writeToFile(baseDirPath, "index.html", codeResult.getHtmlCode());
    }

    @Override
    protected void validateInput(HtmlCodeResult codeResult) {
        super.validateInput(codeResult);
        //html代码不能未空
        if (codeResult.getHtmlCode() == null || codeResult.getHtmlCode().isEmpty())
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"html代码内容不能未空");
    }
}
