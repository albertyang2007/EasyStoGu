package org.easystogu.runner.dynamic.taskIF;

import java.lang.reflect.Method;

import org.easystogu.compiler.DynamicCompiler;
import org.easystogu.file.FileReaderAndWriter;

public class DynamicRunner {
    public static void main(String[] args) {
        try {
            DynamicCompiler dynaCompiler = new DynamicCompiler();
            String content = FileReaderAndWriter.readFromFile("C:/temp/TaskExample.java");
            Object obj = dynaCompiler.buildRequest("org.easystogu.runner.dynamic.taskIF.TaskExample", content);
            Method method = obj.getClass().getMethod("run", null);
            method.invoke(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
