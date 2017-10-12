package org.easystogu.runner.dynamic.taskIF;

import java.lang.reflect.Method;

import org.easystogu.compiler.DynamicCompiler;
import org.easystogu.file.FileReaderAndWriter;

public class DynamicRunner {
    public static void main(String[] args) {
        try {
            //step 1:put all project related jar to comile lib path, just for dynamic compiler
            //step 2:put all lib under ear to jdk/jre/lib/ext, just for workaround to classloader (will cause ear deploy failure!!!)
            //note: this solution will cause ear deploy failure!!!
            String compileLibPath = "C:/Users/eyaweiw/Downloads/DynamicCompiler/";
            DynamicCompiler dynaCompiler = new DynamicCompiler(compileLibPath);
            String content = FileReaderAndWriter.readFromFile(compileLibPath + "TaskExample.java");
            Object obj = dynaCompiler.buildRequest("org.easystogu.runner.dynamic.taskIF.TaskExample", content);
            Method method = obj.getClass().getMethod("run", null);
            method.invoke(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
