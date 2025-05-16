package io.github.jxch.frp.frpc.config;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;
import java.util.UUID;

public class FrpcProxyProperties {
    /**
     * 代理类型：tcp、udp、http、https、stcp、xtcp 等
     */
    private String type;

    /**
     * 代理名称
     */
    private String name = UUID.randomUUID().toString();

    @JsonIgnore
    private String localName;

    /**
     * 本地 IP
     */
    private String localIp;

    /**
     * 本地端口
     */
    private Integer localPort;

    /**
     * 远程端口
     */
    private Integer remotePort;

    /**
     * 自定义域名（http/https类型用）
     */
    private List<String> customDomains;

    /**
     * 路径前缀（http/https类型用）
     */
    private String locations;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocalIp() {
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }

    public Integer getLocalPort() {
        return localPort;
    }

    public void setLocalPort(Integer localPort) {
        this.localPort = localPort;
    }

    public Integer getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(Integer remotePort) {
        this.remotePort = remotePort;
    }

    public List<String> getCustomDomains() {
        return customDomains;
    }

    public void setCustomDomains(List<String> customDomains) {
        this.customDomains = customDomains;
    }

    public String getLocations() {
        return locations;
    }

    public void setLocations(String locations) {
        this.locations = locations;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }
}
