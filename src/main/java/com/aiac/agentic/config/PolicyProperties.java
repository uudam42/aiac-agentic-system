package com.aiac.agentic.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "policy")
public class PolicyProperties {

    private String provider = "local";
    private Opa opa = new Opa();

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public Opa getOpa() {
        return opa;
    }

    public void setOpa(Opa opa) {
        this.opa = opa;
    }

    public static class Opa {
        private String url = "http://localhost:8181/v1/data/aiac/allow";

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
