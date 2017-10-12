package org.easystogu.compiler;

//to fix  ToolProvider.getSystemJavaCompiler() null pointer issue
//cpoy jdk\lib\tools.jar to jre\lib
public class DynamicCompiler {
    private String libPath;

    public DynamicCompiler(String libPath) {
        this.libPath = libPath;
    }

    public Object buildRequest(String fullName, String javaSrcCode)
            throws IllegalAccessException, InstantiationException {
        //long start = System.currentTimeMillis();
        //System.out.println("Compile and Execute JavaCode:\n" + javaSrcCode);
        DynamicEngine engine = new DynamicEngine(this.libPath);
        Object instance = engine.javaCodeToObject(fullName, javaSrcCode.toString());
        //long end = System.currentTimeMillis();
        //System.out.println("Use Time:" + (end - start) + "ms");
        return instance;
    }
}