package com.yupi.yuaicodemother.ai.core.Saver;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.yupi.yuaicodemother.ai.model.enums.CodeGenTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * 抽象类，定义了保存代码的模板方法
 */
public abstract class CodeFileSaverTemplate<T> {

    protected static final String FILE_SAVE_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 模版方法：保存代码的标准流程
     * @param codeResult
     * @return
     */
    public final File saveCode(T codeResult){
        //1.验证输入
        validateInput(codeResult);
        //2.构建生成唯一目录
        String baseDirPath = buildUniqueDir();
        //3.保存文件（具体实现由子类提供）
        saveFiles(codeResult, baseDirPath);
        //4.返回目录文件对象
        return new File(baseDirPath);
    }

    /**
     * 1.校验输入参数（可由子类覆盖）
     * @param codeResult
     */
    protected void  validateInput(T codeResult){
        if (codeResult == null){
            throw new IllegalArgumentException("输入参数不能为空");
        }
    }

    /**
     * 2.构建生成唯一目录（可由子类覆盖）
     */
    protected final String buildUniqueDir(){
        String codeType = getCodeType().getValue();
        //生成唯一目录名称(类型+唯一雪花id)
        String uniqueDirName = StrUtil.format("{}_{}", codeType, IdUtil.getSnowflakeNextIdStr());
        String dirPath = FILE_SAVE_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 获取代码类型枚举
     * @return
     */
    protected abstract CodeGenTypeEnum getCodeType();

    /**
     * 写入单个文件的工具方法
     * @param dirPath,filename,content
     */
    protected final void writeToFile(String dirPath,String filename, String content){
        if(StrUtil.isNotBlank(content)){
            String filePath = dirPath + File.separator + filename;
            FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
        }
    }

    /**
     * 3.保存文件（由子类实现）
     * @param codeResult 代码结果
     * @param baseDirPath 基础目录路径
     */
    protected abstract void saveFiles(T codeResult, String baseDirPath);

}
