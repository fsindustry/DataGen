/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.cli;

import org.apache.commons.cli.Option;

/**
 * <h1>封装命令行选项信息</h1>
 * <p>重写commons-cli option类,添加校验表达式,属性名,默认值</p>
 */
public class PatternOption extends Option {

    /**
     * 属性名
     */
    private String colName;

    /**
     * 校验正则表达式
     */
    private String pattern;

    /**
     * 默认值
     */
    private String defaultValue;

    public PatternOption(String opt, String longOpt, boolean hasArg, String description)
            throws IllegalArgumentException {
        super(opt, longOpt, hasArg, description);
    }

    public String getColName() {
        return colName;
    }

    public void setColName(String colName) {
        this.colName = colName;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    @Override
    public String toString() {
        return "PatternOption{" +
                "colName='" + colName + '\'' +
                ", pattern='" + pattern + '\'' +
                ", defaultValue='" + defaultValue + '\'' +
                '}';
    }
}
