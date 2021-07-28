/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.cime.db.ColumnDesc;

public class StringGen implements DataGen<String> {

    /**
     * 字符表
     */
    private static final String template = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890_";

    private AtomicLong index;

    private ColumnDesc columnDesc;

    private Integer length;

    @Override
    public void intial(ColumnDesc columnDesc, Long startFlag) {
        this.columnDesc = columnDesc;
        this.length = columnDesc.getPrecision();
        if (length > 100) {
            length = 100;
        }

        if (startFlag > 0) {
            this.index = new AtomicLong(startFlag);
        } else {
            this.index = new AtomicLong(0);
        }
    }

    @Override
    public String generate() {

        if (columnDesc.isUnique()) {
            return getUnique();
        } else {
            return getRandom();
        }
    }

    private String getUnique() {
        return index.incrementAndGet() + "";
    }

    private String getRandom() {
        StringBuilder sbuider = new StringBuilder();

        int randomLen = ThreadLocalRandom.current().nextInt(length);
        for (int i = 0; i < randomLen; i++) {

            int index = ThreadLocalRandom.current().nextInt(template.length());
            sbuider.append(template.charAt(index));
        }

        return sbuider.toString();
    }
}
