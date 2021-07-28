/*
 * Copyright (C) 2016 Baidu, Inc. All Rights Reserved.
 */
package org.cime.gen;

import org.cime.db.ColumnDesc;

public interface DataGen<T> {

    void intial(ColumnDesc columnDesc, Long startFlag);

    T generate();
}
