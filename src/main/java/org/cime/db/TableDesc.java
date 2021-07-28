/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.db;

import java.util.List;

/**
 * <h1>数据表描述信息实体</h1>
 */
public class TableDesc {

    /**
     * 数据库名称
     */
    private String dbName;

    /**
     * 数据表名称
     */
    private String tbName;

    /**
     * 数据表字段描述信息
     */
    private List<ColumnDesc> columnDescList;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTbName() {
        return tbName;
    }

    public void setTbName(String tbName) {
        this.tbName = tbName;
    }

    public List<ColumnDesc> getColumnDescList() {
        return columnDescList;
    }

    public void setColumnDescList(List<ColumnDesc> columnDescList) {
        this.columnDescList = columnDescList;
    }

    @Override
    public String toString() {
        return "TableDesc{" +
                "dbName='" + dbName + '\'' +
                ", tbName='" + tbName + '\'' +
                ", columnDescList=" + columnDescList +
                '}';
    }
}
