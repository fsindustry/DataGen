/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.cime.db.ColumnDesc;

public class BigIntGen implements DataGen<BigInteger> {

    private static final String template = "1234567890";

    private ColumnDesc columnDesc;

    private AtomicLong index;

    @Override
    public BigInteger generate() {
        if (columnDesc.isUnique()) {
            return getUnique();
        } else {
            return getRandom();
        }
    }

    @Override
    public void intial(ColumnDesc columnDesc, Long startFlag) {
        this.columnDesc = columnDesc;

        if (startFlag > 0) {
            this.index = new AtomicLong(startFlag);
        } else {
            this.index = new AtomicLong(0);
        }
    }

    private BigInteger getUnique() {
        return new BigInteger(index.incrementAndGet() + "");
    }

    private BigInteger getRandom() {
        String digtStr = getDigtStr(ThreadLocalRandom.current().nextInt(1, columnDesc.getPrecision()));
        return new BigInteger(digtStr);
    }

    private String getDigtStr(int length) {

        StringBuilder sbuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {

            int index = ThreadLocalRandom.current().nextInt(template.length());
            sbuilder.append(template.charAt(index));
        }

        return sbuilder.toString();
    }
}
