package com.yupi.yuaicodemother.ai.core.Parser;

import com.yupi.yuaicodemother.ai.model.MultiFileCodeResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 多文件(html,css,js)代码解析器
 */
public class MultiFileCodeParser implements CodeParser<MultiFileCodeResult>{

    private static final Pattern HTML_CODE_PATTERN = Pattern.compile("```html\\s*\\n([\\s\\S]*?)```",Pattern.CASE_INSENSITIVE);
    private static final Pattern CSS_CODE_PATTERN = Pattern.compile("```css\\s*\\n([\\s\\S]*?)```",Pattern.CASE_INSENSITIVE);
    private static final Pattern JS_CODE_PATTERN = Pattern.compile("```(?:js|javascript)\\s*\\n([\\s\\S]*?)```",Pattern.CASE_INSENSITIVE);

    @Override
    public MultiFileCodeResult parseCode(String codeContent) {
        MultiFileCodeResult multiFileCodeResult = new MultiFileCodeResult();
        //提取各个部分的代码
        String htmlCode = extractCodeByPattern(codeContent,HTML_CODE_PATTERN);
        String cssCode = extractCodeByPattern(codeContent,CSS_CODE_PATTERN);
        String jsCode = extractCodeByPattern(codeContent,JS_CODE_PATTERN);
        //设置html 代码块
        if (htmlCode != null && !htmlCode.trim().isEmpty()){
            multiFileCodeResult.setHtmlCode(htmlCode.trim());
        }
        //设置css 代码块
        if (cssCode != null && !cssCode.trim().isEmpty()){
            multiFileCodeResult.setCssCode(cssCode.trim());
        }
        //设置js 代码块
        if (jsCode != null && !jsCode.trim().isEmpty()){
            multiFileCodeResult.setJsCode(jsCode.trim());
        }
        return multiFileCodeResult;
    }

    /**
     * 根据正则模式提取代码
     * @param codeContent 原始内容 pattern 正则模式
     * @return  提取的代码
     */
    private String extractCodeByPattern(String codeContent,Pattern pattern) {
        Matcher matcher = HTML_CODE_PATTERN.matcher(codeContent);
        if (matcher.find())
        {
            return matcher.group(1);
        }
        return null;
    }
}
