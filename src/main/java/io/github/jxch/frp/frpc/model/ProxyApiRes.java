package io.github.jxch.frp.frpc.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProxyApiRes {
    private List<ProxyInfoRes> proxies;

    public List<ProxyInfoRes> getProxies() {
        return proxies;
    }

    public void setProxies(List<ProxyInfoRes> proxies) {
        this.proxies = proxies;
    }
}
