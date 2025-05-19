package io.github.jxch.frp.frpc;

import io.github.jxch.frp.frpc.config.FrpcProperties;
import io.github.jxch.frp.frpc.config.FrpcProxyProperties;

import java.io.Closeable;
import java.util.Map;
import java.util.stream.Collectors;

public class FrpcClient implements Closeable {
    private final FrpcProperties frpcProperties;
    private final FrpcRunner frpcRunner;
    private final FrpcConnectionStatus frpcConnectionStatus;

    public FrpcClient(FrpcProperties frpcProperties, FrpcRunner frpcRunner, FrpcConnectionStatus frpcConnectionStatus) {
        this.frpcProperties = frpcProperties;
        this.frpcRunner = frpcRunner;
        this.frpcConnectionStatus = frpcConnectionStatus;
    }

    public Map<String, Integer> getRemotePortByLocalName() {
        Map<String, String> nameMap = frpcProperties.getProxies().stream().collect(Collectors.toMap(FrpcProxyProperties::getName, FrpcProxyProperties::getLocalName));
        return frpcConnectionStatus.getOnlineCurrentProxyInfoMap().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> nameMap.get(entry.getKey()),
                        entry -> entry.getValue().getConf().getRemotePort()));
    }

    public Map<String, String> getNameByLocalName() {
        return frpcProperties.getProxies().stream().collect(Collectors.toMap(FrpcProxyProperties::getLocalName, FrpcProxyProperties::getName));
    }

    public Map<String, String> getUsernameByLocalName() {
        return frpcProperties.getProxies().stream().collect(Collectors.toMap(FrpcProxyProperties::getLocalName, FrpcProxyProperties::getUsername));
    }

    public Map<String, String> getPasswordByLocalName() {
        return frpcProperties.getProxies().stream().collect(Collectors.toMap(FrpcProxyProperties::getLocalName, FrpcProxyProperties::getPassword));
    }

    public Map<String, String> getStatusByLocalName() {
        Map<String, String> nameMap = frpcProperties.getProxies().stream().collect(Collectors.toMap(FrpcProxyProperties::getName, FrpcProxyProperties::getLocalName));
        return frpcConnectionStatus.getOnlineCurrentProxyInfoMap().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> nameMap.get(entry.getKey()),
                        entry -> entry.getValue().getStatus()));
    }

    public FrpcProxyProperties getByLocalName(String localName) {
        return frpcProperties.getProxies().stream()
                .filter(proxy -> proxy.getLocalName().equals(localName))
                .findFirst()
                .orElse(null);
    }

    public void clearOffline() {
        frpcRunner.clearOffline();
    }

    @Override
    public void close() {
        frpcRunner.close();
    }

}
