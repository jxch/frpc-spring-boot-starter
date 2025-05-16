package io.github.jxch.frp.frpc;

import io.github.jxch.frp.frpc.config.FrpcProperties;
import io.github.jxch.frp.frpc.config.FrpcProxyProperties;
import io.github.jxch.frp.frpc.event.FrpcStartEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class FrpcRunner implements ApplicationRunner, ApplicationEventPublisherAware, Closeable {
    private static final Logger log = LoggerFactory.getLogger(FrpcRunner.class);
    private ApplicationEventPublisher eventPublisher;
    private final FrpcProperties frpcProperties;
    private final FrpcToml frpcToml;
    private Process process;
    private final FrpcConnectionStatus frpcConnectionStatus;
    private final String clearOfflineUrl;

    public FrpcRunner(FrpcProperties frpcProperties, FrpcConnectionStatus frpcConnectionStatus) {
        check(frpcProperties);
        this.frpcProperties = frpcProperties;
        this.frpcConnectionStatus = frpcConnectionStatus;
        this.frpcToml = new FrpcToml(frpcProperties, Paths.get(frpcProperties.getToml()).toFile());
        this.clearOfflineUrl = UriComponentsBuilder.fromHttpUrl(frpcProperties.getDashboardUrl()).path("/api/proxies").queryParam("status", "offline").toUriString();
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
    }

    public void check(FrpcProperties frpcProperties) {
        List<String> names = frpcProperties.getProxies().stream().map(FrpcProxyProperties::getName).collect(Collectors.toList());
        Set<String> nameSet = new HashSet<>(names);

        if (names.size() != nameSet.size()) {
            throw new IllegalArgumentException("Proxy names must be unique");
        }

        List<String> localNames = frpcProperties.getProxies().stream().map(FrpcProxyProperties::getLocalName).collect(Collectors.toList());
        Set<String> localNameSet = new HashSet<>(localNames);

        if (localNames.size() != localNameSet.size()) {
            throw new IllegalArgumentException("Local proxy names must be unique");
        }
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!frpcProperties.isEnabled()) {
            log.info("frp client is disabled");
            return;
        }

        if (!isFrpcAvailable()) {
            log.warn("未检测到 frpc 命令，跳过 frpc 启动。");
            return;
        }

        List<String> command = Arrays.asList(frpcProperties.getFrpcCmd(), "-c", frpcToml.getFilePath());
        log.info("启动 frpc 命令：{}", String.join(" ", command));
        try {
            startFrpcAndMonitor(command, frpcProperties.getProxies());
            log.info("frpc 已启动进程。");
        } catch (Exception e) {
            log.error("启动 frpc 失败", e);
        }
    }

    private boolean isFrpcAvailable() {
        try {
            Process p = new ProcessBuilder(frpcProperties.getFrpcCmd(), "--version").start();
            int exitCode = p.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void startFrpcAndMonitor(List<String> command, List<FrpcProxyProperties> proxies) throws IOException {
        ProcessBuilder pb = new ProcessBuilder(command);
        this.process = pb.start();

        // 读取标准输出
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("[frpc] {}", line);
                    if (line.contains("start proxy success")) {
                        eventPublisher.publishEvent(new FrpcStartEvent(this));
                    }
                }
            } catch (Exception e) {
                log.warn("读取frpc日志输出失败", e);
            }
        }).start();

        // 读取错误输出流（同理，也可以监控）
        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.error("[frpc] {}", line);
                }
            } catch (Exception e) {
                log.warn("读取frpc错误日志输出失败", e);
            }
        }).start();
    }

    public synchronized void shutdown() {
        if (Objects.nonNull(process) && process.isAlive()) {
            try {
                process.destroy();
                process.waitFor(5, TimeUnit.SECONDS);
                if (process.isAlive()) {
                    process.destroyForcibly();
                }
                log.info("frpc shutdown.");
            } catch (Exception e) {
                log.warn("关闭frpc进程失败", e);
            }
        }

        frpcConnectionStatus.getRestTemplate().exchange(clearOfflineUrl, HttpMethod.DELETE, frpcConnectionStatus.getHttpEntitySupplier().get(), String.class);
        log.info("clear offline proxies");
    }

    @Override
    public void close() {
        shutdown();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }

}
