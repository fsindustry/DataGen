/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.cli;

import lombok.Data;

/**
 * <h1>命令行参数对象</h1>
 * <p>
 * 通过反射解析命令行参数,并设置给该对象;
 * </p>
 */
@Data
public class Args {

    /**
     * 数据库IP地址
     */
    private String ip;

    /**
     * 数据库端口
     */
    private Integer port;

    /**
     * 数据库名
     */
    private String db;

    /**
     * 表名
     */
    private String table;

    /**
     * 线程数
     */
    private Integer threadCount;

    /**
     * 批处理数量
     */
    private Integer batchSize;

    /**
     * 写入总记录数
     */
    private Long count;

    /**
     * 数据库用户名
     */
    private String username;

    /**
     * 数据库密码
     */
    private String password;

    /**
     * 数据编码
     */
    private String encoding;

    /**
     * 数据库编码
     */
    private String dbEncoding;

    /**
     * 数据开始标签
     */
    private Long startFlag;

}
