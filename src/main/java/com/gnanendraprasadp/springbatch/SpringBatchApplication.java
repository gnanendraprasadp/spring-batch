package com.gnanendraprasadp.springbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
//@EnableScheduling
public class SpringBatchApplication {

    private static final Logger logger = LoggerFactory.getLogger(SpringBatchApplication.class);
    private static final SimpleDateFormat date_format = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(SpringBatchApplication.class, args);

//        reportTime();
//        reportTime1();
//        reportTime2();
//        reportTime3();
    }

//    @Scheduled(fixedRate = 2000)
//    public static void reportTime() throws InterruptedException {
//        logger.info("Time with fixedRate is {}", date_format.format(new Date()));
//        Thread.sleep(1000);
//    }
//
//    @Scheduled(fixedDelay = 1000)
//    public static void reportTime1() {
//        logger.info("Time with fixedDelay is {}", date_format.format(new Date()));
//    }
//
//    @Scheduled(initialDelay = 2000)
//    public static void reportTime2() {
//        logger.info("Time with initialDelay is {}", date_format.format(new Date()));
//    }
//
//    @Scheduled(cron = "*/2 * * * * *")
//    public static void reportTime3() {
//        logger.info("Time with initialDelay is {}", date_format.format(new Date()));
//    }
//
//    @Scheduled(fixedRateString = "PT2H") // java duration unit
//    public static void reportTime4() {
//        logger.info("Time with initialDelay is {}", date_format.format(new Date()));
//    }
}
