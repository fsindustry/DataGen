/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.cime.cli.Args;
import org.cime.cli.ArgsUtil;
import org.cime.db.ColumnDesc;
import org.cime.db.DBUtil;
import org.cime.db.TableDesc;
import org.cime.gen.DataGen;
import org.cime.gen.GenFactory;
import org.cime.thread.DataGenRunnable;
import org.cime.thread.ShutDownHook;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>程序入口</h1>
 */
@Slf4j
public class Main {

    public static void main(String[] args) {

        log.info("DataGen start...");

        //1.解析命令行参数
        Args argsObj = null;
        try {
            argsObj = (Args) ArgsUtil.parseArgs(args);
            log.info(argsObj.toString());
        } catch (Exception e) {
            log.error("parse cmd args error, system will exit(1).", e);
            System.exit(1);
        }

        //2.初始化数据库连接池
        try {
            DBUtil.initial(argsObj);
        } catch (Exception e) {
            log.error("initial datasource error, system will exit(1).", e);
            System.exit(1);
        }

        //3.获取数据表信息
        TableDesc tableDesc = null;
        try {
            Connection conn = DBUtil.getConn();
            conn.setAutoCommit(true);

            PreparedStatement pstmt = conn.prepareStatement(String.format("select * from %s limit 0", argsObj.getTable
                    ()));
            ResultSet res = pstmt.executeQuery();
            ResultSetMetaData tbMeta = res.getMetaData();

            tableDesc = new TableDesc();
            tableDesc.setDbName(argsObj.getDb());
            tableDesc.setTbName(argsObj.getTable());
            List<ColumnDesc> colDescList = new ArrayList<>();
            Set<String> uniField = DBUtil.getUniKeys(conn, argsObj.getDb(), argsObj.getTable());

            int colCount = tbMeta.getColumnCount();
            for (int i = 1; i <= colCount; i++) {

                ColumnDesc columnDesc = new ColumnDesc();
                columnDesc.setColumnClassName(tbMeta.getColumnClassName(i));
                columnDesc.setColumnName(tbMeta.getColumnName(i));
                columnDesc.setColumnType(tbMeta.getColumnType(i));
                columnDesc.setColumnTypeName(tbMeta.getColumnTypeName(i));
                columnDesc.setPrecision(tbMeta.getPrecision(i));
                columnDesc.setScale(tbMeta.getScale(i));
                columnDesc.setAutoIncrement(tbMeta.isAutoIncrement(i));
                columnDesc.setUnsigned(!tbMeta.isSigned(i));

                //判断当前字段是否是唯一键
                if (uniField.contains(columnDesc.getColumnName())) {
                    columnDesc.setUnique(true);
                }

                //创建并注入数据生成器
                DataGen dataGen = GenFactory.getDataGen(columnDesc, argsObj.getStartFlag());
                columnDesc.setDataGen(dataGen);

                colDescList.add(columnDesc);
                tableDesc.setColumnDescList(colDescList);
            }

            log.info("tableDesc:" + tableDesc);
        } catch (SQLException e) {
            log.error("get db conn failed, system will exit(1).", e);
            System.exit(1);
        }

        //4.开启数据生成线程
        CountDownLatch latch = new CountDownLatch(argsObj.getThreadCount());

        //划分每个线程需要生成的记录数
        long totalCount = argsObj.getCount();
        long threadCount = argsObj.getThreadCount();
        long avgCount = totalCount / threadCount;
        long leftCount = totalCount % threadCount;
        int batchSize = argsObj.getBatchSize();

        //启动线程
        List<DataGenRunnable> runList = new ArrayList<>();
        DataGenRunnable dataRun;
        for (int i = 0; i < argsObj.getThreadCount(); i++) {
            if (i < leftCount) {
                dataRun = new DataGenRunnable(latch, tableDesc, avgCount + 1, batchSize);
            } else {
                dataRun = new DataGenRunnable(latch, tableDesc, avgCount, batchSize);
            }
            runList.add(dataRun);
            new Thread(dataRun).start();
        }

        //注册关闭钩子
        Runtime.getRuntime().addShutdownHook(new ShutDownHook(runList));

        //5.等待子线程完成
        try {
            latch.await();
        } catch (InterruptedException e) {
            log.error("latch.await error, system will exit(1).", e);
            System.exit(1);
        }

        log.info("DataGen stop...");
    }
}

