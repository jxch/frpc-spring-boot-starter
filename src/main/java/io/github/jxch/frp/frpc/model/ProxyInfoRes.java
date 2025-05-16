package io.github.jxch.frp.frpc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyInfoRes {
    private String name;
    private String status;
    private Conf conf;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    @JsonIgnore
    public boolean isOnline() {
        return "online".equals(status);
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Conf getConf() {
        return conf;
    }

    public void setConf(Conf conf) {
        this.conf = conf;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Conf {
        private String type;
        private Integer remotePort;
        private String localIP;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public Integer getRemotePort() {
            return remotePort;
        }

        public void setRemotePort(Integer remotePort) {
            this.remotePort = remotePort;
        }

        public String getLocalIP() {
            return localIP;
        }

        public void setLocalIP(String localIP) {
            this.localIP = localIP;
        }
    }
}
