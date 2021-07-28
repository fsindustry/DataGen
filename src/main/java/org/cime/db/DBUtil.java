/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.db;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.cime.cli.Args;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import lombok.extern.slf4j.Slf4j;

/**
 * <h1>数据库工具类</h1>
 */
@Slf4j
public class DBUtil {

    /**
     * 数据库url
     */
    private static final String MYSQL_URL_PATTERN =
            "jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=%s&yearIsDateType=false&useSSL=false";

    /**
     * 标识数据表中unique key
     */
    private static final String UNIKEY_FLAG = "UNI";

    /**
     * 标识数据表中的primary key
     */
    private static final String PRIMARY_FLAG = "PRI";

    /**
     * 标识表中复合索引或者非唯一索引
     */
    private static final String MUTIPLE_FLAG = "MUL";

    private static DataSource ds;

    public static void initial(Args args) throws Exception {

        Map<String, String> configMap = new HashMap<>();
        String url = String.format(MYSQL_URL_PATTERN, args.getIp(), args.getPort(), args.getDb(), args.getDbEncoding());
        configMap.put("driverClassName", "com.mysql.jdbc.Driver");
        configMap.put("url", url);
        configMap.put("username", args.getUsername());
        configMap.put("password", args.getPassword());

        Integer threadCount = args.getThreadCount();
        String poolSize;
        if (threadCount <= 1) {
            poolSize = "2";
        } else {
            poolSize = threadCount.toString();
        }
        configMap.put("initialSize", poolSize);
        configMap.put("maxActive", poolSize);
        configMap.put("minIdel", poolSize);

        ds = DruidDataSourceFactory.createDataSource(configMap);
    }

    public static Connection getConn() throws SQLException {

        if (null == ds) {
            throw new SQLException("DataSource is not initial , call DBUtil.initial() first.");
        }

        return ds.getConnection();
    }

    public static Set<String> getUniKeys(Connection conn, String dbName, String tbName) throws SQLException {

        Set<String> uniFieldList = new HashSet<>();
        PreparedStatement pst = null;
        ResultSet ukSet = null;
        ResultSet pkSet = null;
        try {

            pst = conn.prepareStatement(String.format("desc %s.%s", dbName, tbName));
            ukSet = pst.executeQuery();

            //查找主键&唯一键
            while (ukSet.next()) {
                String keyName = ukSet.getString("Key");
                if (UNIKEY_FLAG.equals(keyName) || PRIMARY_FLAG.equals(keyName) || MUTIPLE_FLAG.equals(keyName)) {
                    uniFieldList.add(ukSet.getString("Field"));
                }
            }

            DatabaseMetaData dbMeta = conn.getMetaData();
            pkSet = dbMeta.getPrimaryKeys("", dbName, tbName);
            while (pkSet.next()) {
                uniFieldList.add(pkSet.getString("COLUMN_NAME"));
            }

        } catch (Exception e) {
            log.error("close unique key set failed!", e);
            throw new SQLException("get unique key failed!");
        } finally {

            if (null != ukSet) {
                try {
                    ukSet.close();
                } catch (SQLException e) {
                    log.error("close unique key set failed!", e);
                }

            }

            if (null != pkSet) {
                try {
                    pkSet.close();
                } catch (SQLException e) {
                    log.error("close primary key set failed!", e);
                }
            }

            if (null != pst) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    log.error("close prepared statement failed!", e);
                }
            }
        }

        return uniFieldList;
    }
}
