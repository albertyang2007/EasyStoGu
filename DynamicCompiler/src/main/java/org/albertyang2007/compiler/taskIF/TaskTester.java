package org.albertyang2007.compiler.taskIF;

import java.lang.reflect.Method;

import org.albertyang2007.compiler.DynamicCompiler;
import org.easystogu.file.FileReaderAndWriter;

public class TaskTester {
    public static void main(String[] args) {
        try {
            DynamicCompiler dynaCompiler = new DynamicCompiler();
            String content = FileReaderAndWriter.readFromFile("/home/eyaweiw/TaskExample.java");
            Object obj = dynaCompiler.buildRequest("org.albertyang2007.compiler.taskIF.TaskExample", content);
            Method method = obj.getClass().getMethod("run", null);
            method.invoke(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
