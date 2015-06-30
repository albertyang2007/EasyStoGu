package org.easystogu.multirunner;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.easystogu.indicator.runner.DailyBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyMai1Mai2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyShenXianCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyXueShi2CountAndSaveDBRunner;

public class DayMultThreadRunner {

    private Map<String, TaskInfo> taskInfoMap = new ConcurrentHashMap<String, TaskInfo>();

    public void run() {
        //start thread to run ind
        this.startRunner(new DailyMacdCountAndSaveDBRunner(this));
        this.startRunner(new DailyKDJCountAndSaveDBRunner(this));
        this.startRunner(new DailyBollCountAndSaveDBRunner(this));
        this.startRunner(new DailyMai1Mai2CountAndSaveDBRunner(this));
        this.startRunner(new DailyShenXianCountAndSaveDBRunner(this));
        this.startRunner(new DailyXueShi2CountAndSaveDBRunner(this));
        //check all thread completed
        this.checkAllTaskCompleted();
    }

    public void checkAllTaskCompleted() {
        while (true) {
            Set<String> taskNamesSet = this.taskInfoMap.keySet();
            Iterator<String> taskNames = taskNamesSet.iterator();
            int totalCompleted = 0;
            while (taskNames.hasNext()) {
                String taskName = taskNames.next();
                TaskInfo taskInfo = this.taskInfoMap.get(taskName);
                if (!taskInfo.completed) {
                    try {
                        System.out.println(taskName + " is not completed, sleep 20 sec.");
                        Thread.sleep(20000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    totalCompleted++;
                    System.out.println(taskName + " completed.");
                    if (totalCompleted == this.taskInfoMap.size()) {
                        System.out.println("All task completed.");
                        return;
                    }
                }
            }
        }
    }

    public void startRunner(Runnable runner) {
        Thread t = new Thread(runner);
        t.start();
    }

    public void startTaskInfo(String taskName) {
        TaskInfo taskInfo = this.taskInfoMap.get(taskName);
        if (taskInfo != null) {
            taskInfo.startTime = System.currentTimeMillis();
        }
    }

    public void stopTaskInfo(String taskName) {
        TaskInfo taskInfo = this.taskInfoMap.get(taskName);
        if (taskInfo != null) {
            taskInfo.stopTime = System.currentTimeMillis();
            taskInfo.completed = true;
        }
    }

    public void newTaskInfo(String taskName) {
        if (!this.taskInfoMap.containsKey(taskName)) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.taskName = taskName;
            taskInfo.completed = false;
            this.taskInfoMap.put(new String(taskName), taskInfo);
        }
    }

    public static void main(String[] args) {
        DayMultThreadRunner runner = new DayMultThreadRunner();
        runner.run();
    }
}
