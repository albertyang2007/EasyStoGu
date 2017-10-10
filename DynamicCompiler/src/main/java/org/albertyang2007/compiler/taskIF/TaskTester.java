package org.albertyang2007.compiler.taskIF;

import java.lang.reflect.Method;

import org.albertyang2007.compiler.DynamicCompiler;
import org.easystogu.file.FileReaderAndWriter;

public class TaskTester {
    public static void main(String[] args) throws Exception {
        DynamicCompiler dynaCompiler = new DynamicCompiler();
        String content = FileReaderAndWriter.readFromFile("C:/Temp/TaskExample.java");
        Object obj = dynaCompiler.buildRequest("org.albertyang2007.compiler.taskIF.TaskExample", content);
        Method method = obj.getClass().getMethod("runMe", null);
        String rtn = (String) method.invoke(obj, null);
        System.out.println("return result is:" + rtn);
    }
}
