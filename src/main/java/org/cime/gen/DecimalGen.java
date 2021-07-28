/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

import org.cime.db.ColumnDesc;

public class DecimalGen implements DataGen<BigDecimal> {

    private static final String template = "1234567890";

    private ColumnDesc columnDesc;

    private AtomicLong index;

    private Integer scale;

    private Integer intPart;

    @Override
    public BigDecimal generate() {
        if (columnDesc.isUnique()) {
            return getUnique();
        } else {
            return getRandom();
        }
    }

    @Override
    public void intial(ColumnDesc columnDesc, Long startFlag) {
        this.columnDesc = columnDesc;
        Integer precision = columnDesc.getPrecision();
        this.scale = columnDesc.getScale();
        this.intPart = precision - scale;
        if (startFlag > 0) {
            this.index = new AtomicLong(startFlag);
        } else {
            this.index = new AtomicLong(0);
        }
    }

    private BigDecimal getUnique() {
        return new BigDecimal(index.incrementAndGet());
    }

    private BigDecimal getRandom() {

        String digtStr = "";
        if (0 == intPart) {
            digtStr += "0";
        } else {
            digtStr = getDigtStr(ThreadLocalRandom.current().nextInt(1, intPart));
        }

        if (scale > 0) {
            digtStr += "." + getDigtStr(ThreadLocalRandom.current().nextInt(1, scale));
        }

        return new BigDecimal(digtStr);
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
