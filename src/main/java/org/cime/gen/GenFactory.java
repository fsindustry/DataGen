/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.cime.db.ColumnDesc;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GenFactory {

    /**
     * 根据传入字段描述,生成相符的数据产生器
     *
     * @param columnDesc 字段描述对象
     *
     * @return 自增字段, 返回NULL;其它字段返回相应的数据生成器;
     */
    public static DataGen getDataGen(ColumnDesc columnDesc, Long startFlag) {

        //1.获取字段类型
        DataGen<?> dataGen = null;
        try {

            //如果是自增键,则返回null,不需要数据生成器
            if (columnDesc.isAutoIncrement()) {
                return null;
            }

            //若为BLOB类型,则返回字符串生成器
            String columnClassName = columnDesc.getColumnClassName();
            if ("[B".equals(columnClassName)) {
                dataGen = new StringGen();
            }

            Class<?> columnClass = Class.forName(columnClassName);

            if (columnClass.isAssignableFrom(Boolean.class)) {
                dataGen = new BooleanGen();
            } else if (columnClass.isAssignableFrom(Integer.class)) {
                dataGen = new IntGen();
            } else if (columnClass.isAssignableFrom(Long.class)) {
                dataGen = new LongGen();
            } else if (columnClass.isAssignableFrom(Float.class)) {
                dataGen = new FloatGen();
            } else if (columnClass.isAssignableFrom(Double.class)) {
                dataGen = new DoubleGen();
            } else if (columnClass.isAssignableFrom(BigDecimal.class)) {
                dataGen = new DecimalGen();
            } else if (columnClass.isAssignableFrom(BigInteger.class)) {
                dataGen = new BigIntGen();
            } else if (columnClass.isAssignableFrom(String.class)) {
                dataGen = new StringGen();
            } else if (columnClass.isAssignableFrom(java.sql.Time.class) |
                    columnClass.isAssignableFrom(java.sql.Date.class) |
                    columnClass.isAssignableFrom(java.sql.Timestamp.class)) {
                dataGen = new DateGen();
            } else if (columnClass.isAssignableFrom(Short.class)) {
                dataGen = new YearGen();
            }

            if (dataGen != null) {
                dataGen.intial(columnDesc, startFlag);
            }

        } catch (ClassNotFoundException e) {
            String errMsg = "get data generator failed , column info : " + columnDesc + " !";
            log.error(errMsg, e);
        }

        return dataGen;
    }
}
