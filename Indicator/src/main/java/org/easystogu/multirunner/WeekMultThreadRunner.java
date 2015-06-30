package org.easystogu.multirunner;

import org.easystogu.indicator.runner.DailyWeekBollCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekKDJCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekMacdCountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekMai1Mai2CountAndSaveDBRunner;
import org.easystogu.indicator.runner.DailyWeekShenXianCountAndSaveDBRunner;

public class WeekMultThreadRunner extends DayMultThreadRunner {
    @Override
    public void run() {
        //start thread to run ind
        this.startRunner(new DailyWeekMacdCountAndSaveDBRunner(this));
        this.startRunner(new DailyWeekKDJCountAndSaveDBRunner(this));
        this.startRunner(new DailyWeekBollCountAndSaveDBRunner(this));
        this.startRunner(new DailyWeekMai1Mai2CountAndSaveDBRunner(this));
        this.startRunner(new DailyWeekShenXianCountAndSaveDBRunner(this));
        //check all thread completed
        this.checkAllTaskCompleted();
    }

    public static void main(String[] args) {
        WeekMultThreadRunner runner = new WeekMultThreadRunner();
        runner.run();
    }

}
