/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.util.concurrent.ThreadLocalRandom;

import org.cime.db.ColumnDesc;

class YearGen implements DataGen<Integer> {
    @Override
    public void intial(ColumnDesc columnDesc, Long startFlag) {

    }

    @Override
    public Integer generate() {
        return ThreadLocalRandom.current().nextInt(1901, 2155);
    }
}
