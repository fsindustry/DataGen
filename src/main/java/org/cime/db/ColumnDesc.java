/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.db;

import org.cime.gen.DataGen;

/**
 * <h1>数据表字段实体</h1>
 */
public class ColumnDesc {

    /**
     * 字段名
     */
    private String columnName;

    /**
     * 字段类型
     */
    private Integer columnType;

    /**
     * 字段类型名称
     */
    private String columnTypeName;

    /**
     * 映射JAVA类
     */
    private String columnClassName;

    /**
     * 精度
     */
    private Integer precision;

    /**
     * 小数位数
     */
    private Integer scale;

    /**
     * 是否是唯一键
     */
    private boolean isUnique;

    /**
     * 是否自增
     */
    private boolean isAutoIncrement;

    /**
     * 是否是无符号型的
     */
    private boolean isUnsigned;

    /**
     * 对应的数据生成器
     */
    private DataGen dataGen;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getColumnType() {
        return columnType;
    }

    public void setColumnType(Integer columnType) {
        this.columnType = columnType;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public String getColumnClassName() {
        return columnClassName;
    }

    public void setColumnClassName(String columnClassName) {
        this.columnClassName = columnClassName;
    }

    public Integer getPrecision() {
        return precision;
    }

    public void setPrecision(Integer precision) {
        this.precision = precision;
    }

    public DataGen getDataGen() {
        return dataGen;
    }

    public void setDataGen(DataGen dataGen) {
        this.dataGen = dataGen;
    }

    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        isAutoIncrement = autoIncrement;
    }

    public boolean isUnsigned() {
        return isUnsigned;
    }

    public void setUnsigned(boolean unsigned) {
        isUnsigned = unsigned;
    }

    public Integer getScale() {
        return scale;
    }

    public void setScale(Integer scale) {
        this.scale = scale;
    }

    @Override
    public String toString() {
        return "ColumnDesc{" +
                "columnName='" + columnName + '\'' +
                ", columnType=" + columnType +
                ", columnTypeName='" + columnTypeName + '\'' +
                ", columnClassName='" + columnClassName + '\'' +
                ", precision=" + precision +
                ", scale=" + scale +
                ", isUnique=" + isUnique +
                ", isAutoIncrement=" + isAutoIncrement +
                ", isUnsigned=" + isUnsigned +
                ", dataGen=" + dataGen +
                '}';
    }
}
