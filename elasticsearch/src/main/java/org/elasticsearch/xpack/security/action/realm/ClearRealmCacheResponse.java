/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.security.action.realm;

import org.elasticsearch.action.FailedNodeException;
import org.elasticsearch.action.support.nodes.BaseNodeResponse;
import org.elasticsearch.action.support.nodes.BaseNodesResponse;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.List;

public class ClearRealmCacheResponse extends BaseNodesResponse<ClearRealmCacheResponse.Node> implements ToXContent {

    public ClearRealmCacheResponse() {
    }

    public ClearRealmCacheResponse(ClusterName clusterName, List<Node> nodes, List<FailedNodeException> failures) {
        super(clusterName, nodes, failures);
    }

    @Override
    protected List<ClearRealmCacheResponse.Node> readNodesFrom(StreamInput in) throws IOException {
        return in.readList(Node::readNodeResponse);
    }

    @Override
    protected void writeNodesTo(StreamOutput out, List<ClearRealmCacheResponse.Node> nodes) throws IOException {
        out.writeStreamableList(nodes);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        builder.startObject("nodes");
        for (ClearRealmCacheResponse.Node node : getNodes()) {
            builder.startObject(node.getNode().getId());
            builder.field("name", node.getNode().getName());
            builder.endObject();
        }
        builder.endObject();

        return builder;
    }

    @Override
    public String toString() {
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder().prettyPrint();
            builder.startObject();
            toXContent(builder, EMPTY_PARAMS);
            builder.endObject();
            return builder.string();
        } catch (IOException e) {
            return "{ \"error\" : \"" + e.getMessage() + "\"}";
        }
    }

    public static class Node extends BaseNodeResponse {

        Node() {
        }

        Node(DiscoveryNode node) {
            super(node);
        }

        public static Node readNodeResponse(StreamInput in) throws IOException {
            Node node = new Node();
            node.readFrom(in);
            return node;
        }
    }

}
