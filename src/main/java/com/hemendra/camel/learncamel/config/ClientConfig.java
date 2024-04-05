package com.hemendra.camel.learncamel.config;

import java.util.List;

public class ClientConfig {
    private String clientName;
    private String baseUrl;
    private List<Resource> resources;
    private Auth auth;

    // getters and setters

    public static class Resource {
        private String name;
        private List<Endpoint> endpoints;

        public static class Endpoint {
            private String name;
            private String method;
            private String path;

            public String getName() {
                return name;
            }
            public void setName(String name) {
                this.name = name;
            }
            public String getMethod() {
                return method;
            }
            public void setMethod(String method) {
                this.method = method;
            }
            public String getPath() {
                return path;
            }
            public void setPath(String path) {
                this.path = path;
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<Endpoint> getEndpoints() {
            return endpoints;
        }

        public void setEndpoints(List<Endpoint> endpoints) {
            this.endpoints = endpoints;
        }
    }

    public static class Auth {
        private String type;
        private String token;
        private String tokenLocation;
        private String tokenName;

        public String getType() {
            return type;
        }
        public void setType(String type) {
            this.type = type;
        }
        public String getToken() {
            return token;
        }
        public void setToken(String token) {
            this.token = token;
        }
        public String getTokenLocation() {
            return tokenLocation;
        }
        public void setTokenLocation(String tokenLocation) {
            this.tokenLocation = tokenLocation;
        }
        public String getTokenName() {
            return tokenName;
        }
        public void setTokenName(String tokenName) {
            this.tokenName = tokenName;
        }

    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public Auth getAuth() {
        return auth;
    }

    public void setAuth(Auth auth) {
        this.auth = auth;
    }

    
}
