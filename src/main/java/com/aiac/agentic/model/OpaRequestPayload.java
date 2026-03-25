package com.aiac.agentic.model;

public class OpaRequestPayload {

    private final OpaInput input;

    public OpaRequestPayload(OpaInput input) {
        this.input = input;
    }

    public OpaInput getInput() {
        return input;
    }
}
