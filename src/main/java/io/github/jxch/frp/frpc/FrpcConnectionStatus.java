package io.github.jxch.frp.frpc;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.jxch.frp.frpc.config.FrpcProperties;
import io.github.jxch.frp.frpc.config.FrpcProxyProperties;
import io.github.jxch.frp.frpc.event.FrpcStartEvent;
import io.github.jxch.frp.frpc.model.ProxyApiRes;
import io.github.jxch.frp.frpc.model.ProxyInfoRes;
import org.springframework.context.event.EventListener;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class FrpcConnectionStatus {
    private final String API_PATH = "/api/proxy/http";
    private final String url;
    private final FrpcProperties frpcProperties;
    private final RestTemplate restTemplate;
    private final Supplier<HttpEntity<String>> httpEntitySupplier;
    private final String encodedAuth;
    private final Map<String, ProxyInfoRes> proxyInfoMap = new ConcurrentHashMap<>();

    public FrpcConnectionStatus(FrpcProperties frpcProperties, RestTemplate restTemplate) {
        this.frpcProperties = frpcProperties;
        this.restTemplate = restTemplate;
        this.url = UriComponentsBuilder.fromHttpUrl(frpcProperties.getDashboardUrl()).path(API_PATH).build().toUriString();
        this.encodedAuth = Base64.getEncoder().encodeToString((frpcProperties.getDashboardUsername() + ":" + frpcProperties.getDashboardPassword()).getBytes(StandardCharsets.UTF_8));
        this.httpEntitySupplier = () -> {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Basic " + encodedAuth);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            return new HttpEntity<>(headers);
        };
    }

    @Scheduled(fixedRate = 10000)
    @EventListener(FrpcStartEvent.class)
    public void health() {
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, httpEntitySupplier.get(), String.class);

        try {
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                String json = response.getBody();
                ObjectMapper objectMapper = new ObjectMapper();
                ProxyApiRes proxyApiRes = objectMapper.readValue(json, ProxyApiRes.class);
                proxyInfoMap.clear();
                proxyInfoMap.putAll(proxyApiRes.getProxies().stream().collect(Collectors.toMap(ProxyInfoRes::getName, Function.identity())));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isProxyOnline(String proxyName) {
        ProxyInfoRes proxyInfo = proxyInfoMap.get(proxyName);
        return proxyInfo != null && "online".equalsIgnoreCase(proxyInfo.getStatus());
    }

    public Integer getProxyRemotePort(String proxyName) {
        ProxyInfoRes proxyInfo = proxyInfoMap.get(proxyName);
        return isProxyOnline(proxyName) && Objects.nonNull(proxyInfo) ? proxyInfo.getConf().getRemotePort() : null;
    }

    public Map<String, ProxyInfoRes> getProxyInfoMap() {
        return proxyInfoMap;
    }

    public Map<String, ProxyInfoRes> getOnlineProxyInfoMap() {
        return getProxyInfoMap().entrySet().stream()
                .filter(entry -> entry.getValue().isOnline())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public Map<String, ProxyInfoRes> getOnlineCurrentProxyInfoMap() {
        Map<String, String> nameMap = frpcProperties.getProxies().stream().collect(Collectors.toMap(FrpcProxyProperties::getName, FrpcProxyProperties::getLocalName));
        return getProxyInfoMap().entrySet().stream()
                .filter(entry -> entry.getValue().isOnline())
                .filter(entry -> nameMap.containsKey(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public String getEncodedAuth() {
        return encodedAuth;
    }

    public Supplier<HttpEntity<String>> getHttpEntitySupplier() {
        return httpEntitySupplier;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }

}
