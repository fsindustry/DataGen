/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.cime.db.ColumnDesc;

public class DoubleGen implements DataGen<Double> {

    private AtomicLong index;

    private ColumnDesc columnDesc;

    private Double minValue;

    @Override
    public void intial(ColumnDesc columnDesc, Long startFlag) {
        this.columnDesc = columnDesc;

        if (columnDesc.isUnsigned()) {
            minValue = 0d;
        }

        if (startFlag > 0) {
            this.index = new AtomicLong(startFlag);
        } else {
            this.index = new AtomicLong(0);
        }
    }

    @Override
    public Double generate() {
        if (columnDesc.isUnique()) {
            return getUnique();
        } else {
            return getRandom();
        }
    }

    private Double getUnique() {
        return Double.longBitsToDouble(index.incrementAndGet());
    }

    private Double getRandom() {
        return ThreadLocalRandom.current().nextDouble();
    }
}
