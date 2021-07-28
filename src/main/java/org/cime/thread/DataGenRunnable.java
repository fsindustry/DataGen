/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.cime.db.ColumnDesc;
import org.cime.db.DBUtil;
import org.cime.db.TableDesc;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>数据生成线程</h1>
 * <p>用于生成数据表数据</p>
 */
@Slf4j
public class DataGenRunnable implements Runnable {

    /**
     * 是否关闭shutdown接口
     */
    private volatile boolean isShutDown = false;

    /**
     * 线程计数闩
     */
    private CountDownLatch latch;

    /**
     * 数据表描述信息
     */
    private TableDesc tableDesc;

    /**
     * 当前线程需要生成的数据总数
     */
    private long count;

    /**
     * 数据库批处理数量
     */
    private int batchSize;

    /**
     * 用来存放生成的sql语句模板
     */
    private String sqlTemplate;

    public DataGenRunnable(CountDownLatch latch, TableDesc tableDesc, long count, int batchSize) {
        this.latch = latch;
        this.tableDesc = tableDesc;
        this.count = count;
        this.batchSize = batchSize;
        //生成sql语句模板,便于后续使用
        sqlTemplate = generateSqlTemplate(tableDesc);
    }

    @Override
    public void run() {

        if (log.isInfoEnabled()) {
            log.info("start data generate thread...");
        }
        long start = System.currentTimeMillis();

        List<ColumnDesc> columnDescList = tableDesc.getColumnDescList();

        //获取sql模板
        Connection conn = null;
        try {
            //收到退出信号,退出执行
            if (isShutDown) {
                return;
            }

            conn = DBUtil.getConn();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement(sqlTemplate);

            for (int currCount = 1; currCount <= count; currCount++) {

                for (int i = 1; i <= columnDescList.size(); i++) {
                    ColumnDesc columnDesc = columnDescList.get(i - 1);

                    if (!columnDesc.isAutoIncrement()) {
                        Object value = columnDesc.getDataGen().generate();
                        ps.setObject(i, value);
                    } else {
                        ps.setObject(i, null);
                    }
                }

                ps.addBatch();

                if ((currCount % batchSize) == 0) {
                    ps.executeBatch();
                    conn.commit();
                }

                //收到退出信号,完成当次执行后退出执行
                if (isShutDown) {
                    ps.executeBatch();
                    conn.commit();
                    return;
                }
            }

            if (count % batchSize != 0) {
                ps.executeBatch();
                conn.commit();
            }

        } catch (SQLException e) {
            if (null != conn) {
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

            e.printStackTrace();
        } finally {
            //计数器减1
            latch.countDown();
            if (null != conn) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        if (log.isInfoEnabled()) {
            long cost = System.currentTimeMillis() - start;
            log.info("generate finshed,count:" + count + ",cost:" + cost + " ms.");
        }
    }

    /**
     * <h2>通过传入的TableDesc对象生成对应的insert语句模板</h2>
     *
     * @param tableDesc 存放数据表描述信息
     *
     * @return 生成好的insert语句模板
     */
    private String generateSqlTemplate(TableDesc tableDesc) {

        StringBuilder strBuilder = new StringBuilder();
        strBuilder.append("insert into ");
        strBuilder.append(tableDesc.getDbName());
        strBuilder.append(".");
        strBuilder.append(tableDesc.getTbName());
        strBuilder.append("(");

        List<ColumnDesc> columnDescList = tableDesc.getColumnDescList();
        for (int i = 0; i < columnDescList.size(); i++) {
            ColumnDesc columnDesc = columnDescList.get(i);
            strBuilder.append(columnDesc.getColumnName());

            if (i < columnDescList.size() - 1) {
                strBuilder.append(",");
            }
        }

        strBuilder.append(")");
        strBuilder.append("values(");

        for (int i = 0; i < columnDescList.size(); i++) {
            strBuilder.append("?");

            if (i < columnDescList.size() - 1) {
                strBuilder.append(",");
            }
        }

        strBuilder.append(")");

        return strBuilder.toString();
    }

    void setShutDown(boolean shutDown) {
        isShutDown = shutDown;
    }
}
