/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.cime.db.ColumnDesc;

public class FloatGen implements DataGen<Float> {

    private AtomicInteger index;

    private ColumnDesc columnDesc;

    @Override
    public void intial(ColumnDesc columnDesc,Long startFlag) {
        this.columnDesc = columnDesc;

        Float minValue = Float.MIN_VALUE;

        if (columnDesc.isUnsigned()) {
            minValue = 0f;
        }

        if (columnDesc.isUnique()) {
            index = new AtomicInteger(Float.floatToIntBits(startFlag));
        }
    }

    @Override
    public Float generate() {
        if (columnDesc.isUnique()) {
            return getUnique();
        } else {
            return getRandom();
        }
    }

    private Float getUnique() {
        return Float.intBitsToFloat(index.incrementAndGet());
    }

    private Float getRandom() {
        return ThreadLocalRandom.current().nextFloat();
    }
}
