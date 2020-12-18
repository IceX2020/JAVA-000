package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbContextHolder {
    private static Logger log = LoggerFactory.getLogger(DbContextHolder.class);
    public static final String WRITE = "write";
    public static final String READ = "read";

    private static ThreadLocal<String> contextHolder= new ThreadLocal<>();

    public static void setDbType(String dbType) {
        if (dbType == null) {
            log.error("dbType为空");
            throw new NullPointerException();
        }
        log.info("设置dbType为：{}",dbType);
        contextHolder.set(dbType);
    }

    public static String getDbType() {
        return contextHolder.get() == null ? WRITE : contextHolder.get();
    }

    public static void clearDbType() {
        contextHolder.remove();
    }
}
