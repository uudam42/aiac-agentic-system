package com.aiac.agentic.model;

public class OpaRequestPayload {

    private final AccessRequest input;

    public OpaRequestPayload(AccessRequest input) {
        this.input = input;
    }

    public AccessRequest getInput() {
        return input;
    }
}
