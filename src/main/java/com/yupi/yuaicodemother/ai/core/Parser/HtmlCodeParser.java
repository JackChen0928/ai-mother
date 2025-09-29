package com.yupi.yuaicodemother.ai.core.Parser;

import com.yupi.yuaicodemother.ai.model.HtmlCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * HTML单文件代码解析器
 */
public class HtmlCodeParser implements CodeParser<HtmlCodeResult>{

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```",Pattern.CASE_INSENSITIVE);


    @Override
    public HtmlCodeResult parseCode(String codeContent) {
        HtmlCodeResult htmlCodeResult = new HtmlCodeResult();
        //提取HTML代码
        String htmlCode = extractHtmlCode(codeContent);
        if (htmlCode != null && !htmlCode.trim().isEmpty()){
            htmlCodeResult.setHtmlCode(htmlCode.trim());
        }else {
            //如果没有找到代码块，将整个内容作为HTML
            htmlCodeResult.setHtmlCode(codeContent.trim());
        }
        return htmlCodeResult;
    }

    /**
     * 提取HTML代码内容
     * @param codeContent 原始内容
     * @return  提取的Html代码
     */
    private String extractHtmlCode(String codeContent) {
        Matcher matcher = HTML_CODE_PATTERN.matcher(codeContent);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return null;
    }
}
