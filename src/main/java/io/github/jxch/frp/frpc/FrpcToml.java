package io.github.jxch.frp.frpc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moandjiezana.toml.TomlWriter;
import io.github.jxch.frp.frpc.config.FrpcProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class FrpcToml {
    private static final Logger log = LoggerFactory.getLogger(FrpcToml.class);
    private final FrpcProperties frpc;
    private final File toml;

    public FrpcToml(FrpcProperties frpc, File toml) {
        this.frpc = frpc;
        this.toml = toml;
        if (!toml.exists() || frpc.isTomlFileRefresh()) {
            toml.getParentFile().mkdirs();
            write(toml);
        }
    }

    public void write(File file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> frpcMap = objectMapper.convertValue(frpc, Map.class);

            TomlWriter tomlWriter = new TomlWriter();
            String toml = tomlWriter.write(frpcMap).replaceAll("(?m)^\"([^\"]+)\"(\\s*=)", "$1$2");
            Files.write(Paths.get(file.getAbsolutePath()), toml.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    public String getFilePath() {
        return toml.getAbsolutePath();
    }

}
