/*
 * *****************************************************************************
 *      Cloud Foundry
 *      Copyright (c) [2009-2015] Pivotal Software, Inc. All Rights Reserved.
 *      This product is licensed to you under the Apache License, Version 2.0 (the "License").
 *      You may not use this product except in compliance with the License.
 *
 *      This product includes a number of subcomponents with
 *      separate copyright notices and license terms. Your use of these
 *      subcomponents is subject to the terms and conditions of the
 *      subcomponent's license, as noted in the LICENSE file.
 * *****************************************************************************
 */

package org.cloudfoundry.identity.uaa.config;

public class SamlConfig {
    private boolean requestSigned = false;
    private boolean wantAssertionSigned = false;
    private boolean wantAuthnRequestSigned = false;
    private String certificate;
    private String privateKey;

    public boolean isRequestSigned() {
        return requestSigned;
    }

    public void setRequestSigned(boolean requestSigned) {
        this.requestSigned = requestSigned;
    }

    public boolean isWantAssertionSigned() {
        return wantAssertionSigned;
    }

    public void setWantAssertionSigned(boolean wantAssertionSigned) {
        this.wantAssertionSigned = wantAssertionSigned;
    }

    public boolean isWantAuthnRequestSigned() {
        return wantAuthnRequestSigned;
    }

    public void setWantAuthnRequestSigned(boolean wantAuthnRequestSigned) {
        this.wantAuthnRequestSigned = wantAuthnRequestSigned;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getPrivateKey() {
        return privateKey;
    }
}
