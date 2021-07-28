/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import org.cime.db.ColumnDesc;

public class BooleanGen implements DataGen<Boolean> {

    @Override
    public void intial(ColumnDesc columnDesc,Long startFlag) {

    }

    @Override
    public Boolean generate() {
        return ThreadLocalRandom.current().nextBoolean();
    }
}
