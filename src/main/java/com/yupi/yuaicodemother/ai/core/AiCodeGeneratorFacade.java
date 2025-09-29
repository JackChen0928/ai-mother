package com.yupi.yuaicodemother.ai.core;

import com.yupi.yuaicodemother.ai.model.HtmlCodeResult;
import com.yupi.yuaicodemother.ai.model.MultiFileCodeResult;
import com.yupi.yuaicodemother.ai.model.enums.CodeGenTypeEnum;
import com.yupi.yuaicodemother.ai.service.AiCodeGeneratorService;
import com.yupi.yuaicodemother.exception.BusinessException;
import com.yupi.yuaicodemother.exception.ErrorCode;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

@Service
public class AiCodeGeneratorFacade {

    private static final Logger log = LoggerFactory.getLogger(AiCodeGeneratorFacade.class);
    @Resource
    private AiCodeGeneratorService aiCodeGeneratorService;

    /**
     * 1.生成并保存代码(根据两种类型的生成模式，调用不一样的生成和保存方法)
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        return switch (codeGenTypeEnum) {
            case HTML -> generateAndSaveHtmlCode(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCode(userMessage);
            default -> {
                String errorMessage = "不支持的生成类型" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 2.流式生成并保存代码(根据两种类型的生成模式，调用不一样的生成和保存方法)
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        return switch (codeGenTypeEnum) {
            case HTML -> generateAndSaveHtmlCodeStream(userMessage);
            case MULTI_FILE -> generateAndSaveMultiFileCodeStream(userMessage);
            default -> {
                String errorMessage = "不支持的生成类型" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);

            }
        };
    }


    /**
     * 1.1生成并保存Html代码
     */
    private File generateAndSaveHtmlCode(String userMessage) {
        //生成
        HtmlCodeResult result = aiCodeGeneratorService.generateHtmlCode(userMessage);
        //保存
        return CodeFileSaver.saveHtmlCodeResult(result);
    }

    /**
     * 1.2生成并保存多文件代码
     */
    private File generateAndSaveMultiFileCode(String userMessage) {
        //生成
        MultiFileCodeResult result = aiCodeGeneratorService.generateMultiFileCode(userMessage);
        //保存
        return CodeFileSaver.saveMultiFileCodeResult(result);
    }

    /**
     * 2.1生成并保存Html代码(流式)
     */
    private Flux<String> generateAndSaveHtmlCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
        //流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        //doOnNext(每个元素发出时，对Flux对象的操作)
        return result.doOnNext(chunk ->{
            //实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(()-> {
            //doOnComplete(正常完成时，对Flux对象的操作)
            //流式返回完成后保存代码
            try{
                String completeHtmlCode = codeBuilder.toString();
                HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(completeHtmlCode);
                //保存代码到文件
                File savedDir = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                log.info("代码保存成功:{}",savedDir.getAbsolutePath());
            }catch (Exception e){
                log.error("保存代码失败:{}",e.getMessage());
            }
        });
    }

    /**
     * 2.2生成并保存多文件代码(流式)
     */
    private Flux<String> generateAndSaveMultiFileCodeStream(String userMessage) {
        Flux<String> result = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
        //流式返回生成代码完成后，再保存代码
        StringBuilder codeBuilder = new StringBuilder();
        //doOnNext(每个元素发出时，对Flux对象的操作)
        return result.doOnNext(chunk ->{
            //实时收集代码片段
            codeBuilder.append(chunk);
        }).doOnComplete(()-> {
            //doOnComplete(正常完成时，对Flux对象的操作)
            //流式返回完成后保存代码
            try{
                String completeMultiFileCode = codeBuilder.toString();
                MultiFileCodeResult multiFileCodeResult = CodeParser.parseMultiFileCode(completeMultiFileCode);
                //保存代码到文件
                File savedDir = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
                log.info("代码保存成功:{}",savedDir.getAbsolutePath());
            }catch (Exception e){
                log.error("保存代码失败:{}",e.getMessage());
            }
        });
    }

}
