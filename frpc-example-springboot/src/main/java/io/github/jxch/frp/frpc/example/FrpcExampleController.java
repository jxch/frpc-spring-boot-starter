package io.github.jxch.frp.frpc.example;

import io.github.jxch.frp.frpc.FrpcClient;
import io.github.jxch.frp.frpc.FrpcConnectionStatus;
import io.github.jxch.frp.frpc.model.ProxyInfoRes;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class FrpcExampleController {
    private final FrpcConnectionStatus frpcConnectionStatus;
    private final FrpcClient frpcClient;

    @RequestMapping("/test")
    public Map<String, ProxyInfoRes> test() {
        return frpcConnectionStatus.getProxyInfoMap();
    }

    @RequestMapping("/getRemotePortByLocalName")
    public Map<String, Integer> getRemotePortByLocalName() {
        return frpcClient.getRemotePortByLocalName();
    }

}
