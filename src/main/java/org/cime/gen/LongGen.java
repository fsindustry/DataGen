/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.cime.db.ColumnDesc;

public class LongGen implements DataGen<Long> {

    private AtomicLong index;

    private Long minValue;

    private Long maxValue;

    private ColumnDesc columnDesc;

    @Override
    public void intial(ColumnDesc columnDesc,Long startFlag) {
        this.columnDesc = columnDesc;
        String columnType = columnDesc.getColumnTypeName();
        int precision = columnDesc.getPrecision();

        if ("BIGINT".equals(columnType) && precision >= 19) {
            maxValue = Long.MAX_VALUE;
            minValue = Long.MIN_VALUE;
        } else {
            StringBuilder sbuilder = new StringBuilder();
            for (int i = 0; i < precision; i++) {
                sbuilder.append(9);
            }
            maxValue = Long.valueOf(sbuilder.toString());
            minValue = -Long.valueOf(sbuilder.toString());
        }

        if (columnDesc.isUnsigned()) {
            minValue = 0L;
        }

        if (columnDesc.isUnique()) {
            index = new AtomicLong(startFlag);
        }
    }

    @Override
    public Long generate() {
        if (columnDesc.isUnique()) {
            return getUnique();
        } else {
            return getRandom();
        }
    }

    private Long getUnique() {
        return index.incrementAndGet();
    }

    private Long getRandom() {
        return ThreadLocalRandom.current().nextLong(minValue, maxValue);
    }
}
