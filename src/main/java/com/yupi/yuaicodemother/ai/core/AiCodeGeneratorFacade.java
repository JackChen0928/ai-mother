package com.yupi.yuaicodemother.ai.core;

import com.yupi.yuaicodemother.ai.core.Parser.CodeParserExecutor;
import com.yupi.yuaicodemother.ai.core.Saver.CodeFileSaverExecutor;
import com.yupi.yuaicodemother.ai.core.Saver.CodeFileSaverTemplate;
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
     * 通用流式代码处理方法
     * @param codeStream 代码流
     * @param codeGenTypeEnum 代码生成类型
     * @return
     */
    private Flux<String> processCodeStream(Flux<String> codeStream,CodeGenTypeEnum codeGenTypeEnum){
        StringBuilder stringBuilder = new StringBuilder();
        return codeStream.doOnNext(chunk ->{
            //实时收集代码片段
            stringBuilder.append(chunk);
        }).doOnComplete(()-> {
            //doOnComplete(正常完成时，对Flux对象操作)
            //流式返回完成后保存代码
            try{
                String completeCode = stringBuilder.toString();
                //调用解析执行器解析代码
                Object parsedCode = CodeParserExecutor.executeParser(completeCode, codeGenTypeEnum);
                //调用保存执行器保存代码
                File savedDir = CodeFileSaverExecutor.executorSaver(parsedCode, codeGenTypeEnum);
                log.info("代码保存成功:{}",savedDir.getAbsolutePath());
            }catch (Exception e){
                log.error("保存代码失败:{}",e.getMessage());
            }
        });
    }

    /**
     * 统一入口1.生成并保存代码(根据两种类型的生成模式，调用不一样的生成和保存方法)非流式，一次性输出，需要等待
     * @param userMessage 用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return 保存的目录
     */
    public File generateAndSaveCode(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");

        return switch (codeGenTypeEnum) {
            case HTML -> {
                HtmlCodeResult htmlCodeResult = aiCodeGeneratorService.generateHtmlCode(userMessage);
                yield CodeFileSaverExecutor.executorSaver(htmlCodeResult, CodeGenTypeEnum.HTML);
            }
            case MULTI_FILE -> {
                MultiFileCodeResult multiFileCodeResult = aiCodeGeneratorService.generateMultiFileCode(userMessage);
                yield CodeFileSaverExecutor.executorSaver(multiFileCodeResult, CodeGenTypeEnum.MULTI_FILE);
            }

            default -> {
                String errorMessage = "不支持的生成类型" + codeGenTypeEnum.getValue();
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, errorMessage);
            }
        };
    }

    /**
     * 统一入口2.流式生成并保存代码(根据两种类型的生成模式，调用不一样的生成和保存方法)流式输出
     * @param userMessage 用户提示词
     * @param codeGenTypeEnum 生成类型
     * @return
     */
    public Flux<String> generateAndSaveCodeStream(String userMessage, CodeGenTypeEnum codeGenTypeEnum) {
        if (codeGenTypeEnum == null)
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "生成类型为空");
        return switch (codeGenTypeEnum) {
            case HTML -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateHtmlCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.HTML);
            }
            case MULTI_FILE -> {
                Flux<String> codeStream = aiCodeGeneratorService.generateMultiFileCodeStream(userMessage);
                yield processCodeStream(codeStream, CodeGenTypeEnum.MULTI_FILE);
            }
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
