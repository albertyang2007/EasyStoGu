package org.easystogu.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogHelper {
    public static Logger getLogger(Class<?> myClass) {
        return LoggerFactory.getLogger(myClass.getSimpleName());
    }
}
