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
package org.seasar.extension.jdbc.entity;

import java.sql.Timestamp;
import java.time.OffsetDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Version;
import nos2jdbc.annotation.CreatedAt;
import nos2jdbc.annotation.UpdatedAt;

/**
 * @author koichik
 */
@Entity
public class Eee {

    /**
     * 
     */
    @Id
    public Integer id;

    /**
     * 
     */
    public String name;

    /**
     * 
     */
    @Lob
    public String longText;

    /**
     * 
     */
    public Integer fffId;

    /**
     * 
     */
    @OneToOne
    public Fff fff;

    /**
     * 
     */
    @Version
    public Long version = 0L;
    
    @CreatedAt
    public OffsetDateTime createAt;
    @UpdatedAt
    public OffsetDateTime updateAt;

    /**
     * 
     */
    @Column(insertable = false, updatable = false)
    public Timestamp lastUpdated;

    /**
     * 
     */
    public Eee() {
    }

    /**
     * @param id
     * @param name
     */
    public Eee(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * @param id
     * @param name
     * @param longText
     */
    public Eee(Integer id, String name, String longText) {
        super();
        this.id = id;
        this.name = name;
        this.longText = longText;
    }
    
}
