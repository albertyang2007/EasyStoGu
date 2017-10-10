package org.albertyang2007.compiler.taskIF;

public class TaskExample implements org.albertyang2007.compiler.taskIF.SuperTask {
    public String runMe() {
        System.out.println("Hello, this is in runMe!!!");
        return "Task is Running.";
    }
}
