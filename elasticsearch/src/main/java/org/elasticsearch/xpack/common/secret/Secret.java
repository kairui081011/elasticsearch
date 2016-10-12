/*
 * Copyright Elasticsearch B.V. and/or licensed to Elasticsearch B.V. under one
 * or more contributor license agreements. Licensed under the Elastic License;
 * you may not use this file except in compliance with the Elastic License.
 */
package org.elasticsearch.xpack.common.secret;

import java.io.IOException;
import java.util.Arrays;

import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.xpack.security.crypto.CryptoService;

public class Secret implements ToXContent {

    protected final char[] text;

    public Secret(char[] text) {
        this.text = text;
    }

    public char[] text(CryptoService service) {
        if (service == null) {
            return text;
        }
        return service.decrypt(text);
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        return builder.value(new String(text));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Secret secret = (Secret) o;

        return Arrays.equals(text, secret.text);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(text);
    }

}
