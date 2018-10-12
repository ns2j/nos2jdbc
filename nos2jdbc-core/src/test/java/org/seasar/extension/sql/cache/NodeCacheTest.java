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
package org.seasar.extension.sql.cache;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import org.seasar.extension.sql.Node;
import org.seasar.extension.sql.context.SqlContextImpl;

/**
 * @author higa
 * 
 */
class NodeCacheTest {

    
    @AfterEach
    void tearDown() throws Exception {
        NodeCache.clear();
    }

    String getPath() {
        return getClass().getName().replace('.', '/') + ".sql";
    }

    /**
     * 
     */
    @Test
    void testGetNode() {
        Node node = NodeCache.getNode(getPath(), null);
        assertNotNull(node);
        SqlContextImpl ctx = new SqlContextImpl();
        node.accept(ctx);
        assertEquals("standard", ctx.getSql());
        assertSame(node, NodeCache.getNode(getPath(), null));
    }

    /**
     * 
     */
    @Test
    void testGetNode_dbmsName() {
        Node node = NodeCache.getNode(getPath(), "oracle");
        assertNotNull(node);
        SqlContextImpl ctx = new SqlContextImpl();
        node.accept(ctx);
        assertEquals("oracle", ctx.getSql());
        assertSame(node, NodeCache.getNode(getPath(), "oracle"));
    }

    /**
     * 
     */
    @Test
    void testGetNode_disallowVariableSql() {
        Node variableNode = NodeCache.getNode(getPath(), null);
        assertNotNull(variableNode);
        assertSame(variableNode, NodeCache.getNode(getPath(), null));

        Node notVariableNode = NodeCache.getNode(getPath(), null, false);
        assertNotSame(variableNode, notVariableNode);
        assertSame(notVariableNode, NodeCache.getNode(getPath(), null, false));

        Node notVariableNodeWithDbms = NodeCache.getNode(getPath(), "oracle",
                false);
        assertNotSame(variableNode, notVariableNodeWithDbms);
        assertNotSame(notVariableNode, notVariableNodeWithDbms);
        assertSame(notVariableNodeWithDbms, NodeCache.getNode(getPath(),
                "oracle", false));
    }

    /**
     * 
     */
    @Test
    void testGetNode_dbmsName_notFound() {
        Node node = NodeCache.getNode(getPath(), "xxx");
        assertNotNull(node);
        SqlContextImpl ctx = new SqlContextImpl();
        node.accept(ctx);
        assertEquals("standard", ctx.getSql());
        assertSame(node, NodeCache.getNode(getPath(), "xxx"));
    }

    /**
     * 
     */
    @Test
    void testGetNode_notFound() {
        assertNull(NodeCache.getNode("notFound", null));
    }

    /**
     * 
     */
    @Test
    void testGetNode_withBom() {
        Node node = NodeCache.getNode(getPath(), "bom");
        assertNotNull(node);
        SqlContextImpl ctx = new SqlContextImpl();
        node.accept(ctx);
        assertEquals("with BOM", ctx.getSql());
    }
}
