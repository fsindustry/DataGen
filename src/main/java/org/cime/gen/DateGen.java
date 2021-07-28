/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.cime.db.ColumnDesc;

public class DateGen implements DataGen<String> {

    private SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");

    @Override
    public void intial(ColumnDesc columnDesc,Long startFlag) {
    }

    @Override
    public String generate() {
        return format.format(new Date());
    }
}
