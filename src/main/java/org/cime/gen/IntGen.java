/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.cime.db.ColumnDesc;

public class IntGen implements DataGen<Integer> {

    private AtomicInteger index;

    private ColumnDesc columnDesc;

    private Integer minValue;

    private Integer maxValue;

    @Override
    public void intial(ColumnDesc columnDesc,Long startFlag) {
        this.columnDesc = columnDesc;
        String columnType = columnDesc.getColumnTypeName();
        int precision = columnDesc.getPrecision();

        if (("TINYINT".equals(columnType) || "TINYINT UNSIGNED".equals(columnType)) && precision >= 3) {
            maxValue = (int) Byte.MAX_VALUE;
            minValue = (int) Byte.MIN_VALUE;
        } else if (("SMALLINT".equals(columnType) || "SMALLINT UNSIGNED".equals(columnType)) && precision >= 5) {
            maxValue = (int) Short.MAX_VALUE;
            minValue = (int) Short.MIN_VALUE;
        } else if (("MEDIUMINT".equals(columnType) || "MEDIUMINT UNSIGNED".equals(columnType)) && precision >= 7) {
            maxValue = (int) Math.pow(2, 23) - 1;
            minValue = (int) -Math.pow(2, 23);
        } else if (("INT".equals(columnType) || "INT UNSIGNED".equals(columnType)) && precision >= 10) {
            maxValue = Integer.MAX_VALUE;
            minValue = Integer.MIN_VALUE;
        } else {
            StringBuilder sbuilder = new StringBuilder();
            for (int i = 0; i < precision; i++) {
                sbuilder.append(9);
            }
            maxValue = Integer.valueOf(sbuilder.toString());
            minValue = -Integer.valueOf(sbuilder.toString());
        }

        if (columnDesc.isUnsigned()) {
            minValue = 0;
        }

        if (columnDesc.isUnique()) {
            index = new AtomicInteger(minValue);
        }
    }

    @Override
    public Integer generate() {
        if (columnDesc.isUnique()) {
            return getUnique();
        } else {
            Integer value = getRandom();
            return value;
        }
    }

    private Integer getUnique() {
        return index.incrementAndGet();
    }

    private Integer getRandom() {
        return ThreadLocalRandom.current().nextInt(minValue, maxValue);
    }
}
