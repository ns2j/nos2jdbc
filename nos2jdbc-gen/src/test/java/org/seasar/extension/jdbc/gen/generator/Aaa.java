/*
 * Copyright 2004-2015 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.extension.jdbc.gen.generator;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import jakarta.persistence.Version;

/**
 * @author taedium
 * 
 */
@Entity
class Aaa {

    /** */
    @Id
    @Column(nullable = false)
    public Integer id;

    /** */
    @Column(nullable = false)
    public String name;

    /** */
    @Lob
    @Column(nullable = false)
    public byte[] lob;

    /** */
    @Temporal(TemporalType.DATE)
    public Date date;

    /** */
    @Transient
    public String temp;

    /** */
    @Version
    @Column(nullable = false)
    public Integer version;

    /** */
    @Column(nullable = false)
    public Integer bbbId;

    /** */
    @ManyToOne
    public Bbb bbb;
}
