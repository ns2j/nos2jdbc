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

import java.util.List;

import jakarta.persistence.Basic;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

/**
 * @author higa
 * 
 */
@Entity
public class Bbb {

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
	public Integer cccId;

	/**
	 * 
	 */
	@OneToOne
	public Ccc ccc;

	/**
	 * 
	 */
	@OneToOne(mappedBy = "bbb")
	public Aaa aaa;

	/**
	 * 
	 */
	@OneToMany(mappedBy = "bbb")
	public List<Ddd> ddds;

    /**
     * 
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    public String lazyName;
}
