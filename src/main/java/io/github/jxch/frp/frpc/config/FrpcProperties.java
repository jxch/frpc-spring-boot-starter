package io.github.jxch.frp.frpc.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "spring.frpc")
public class FrpcProperties {
    private String user;
    private String serverAddr = "127.0.0.1";
    private Integer serverPort = 7000;
    private Boolean loginFailExit;
    @JsonProperty("log.to")
    private String logFile = "console";
    @JsonProperty("log.level")
    private String logLevel = "info";
    @JsonProperty("log.maxDays")
    private Integer logMaxDays = 5;
    @JsonProperty("log.disablePrintColor")
    private Boolean logDisablePrintColor = true;
    @JsonProperty("auth.token")
    private String token;
    @JsonProperty("transport.protocol")
    private String protocol = "tcp";

    private Boolean enableTls;

    private Boolean useEncryption;
    private Boolean useCompression;

    private List<FrpcProxyProperties> proxies;

    @JsonIgnore
    private boolean enabled = true;
    @JsonIgnore
    private String toml = "/tmp/frpc.toml";
    @JsonIgnore
    private String frpcCmd = "frpc";
    @JsonIgnore
    private String dashboardUsername;
    @JsonIgnore
    private String dashboardPassword;
    @JsonIgnore
    private String dashboardUrl;
    @JsonIgnore
    private boolean tomlFileRefresh = false;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public Boolean getLoginFailExit() {
        return loginFailExit;
    }

    public void setLoginFailExit(Boolean loginFailExit) {
        this.loginFailExit = loginFailExit;
    }

    public String getLogFile() {
        return logFile;
    }

    public void setLogFile(String logFile) {
        this.logFile = logFile;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public Integer getLogMaxDays() {
        return logMaxDays;
    }

    public void setLogMaxDays(Integer logMaxDays) {
        this.logMaxDays = logMaxDays;
    }

    public Boolean getLogDisablePrintColor() {
        return logDisablePrintColor;
    }

    public void setLogDisablePrintColor(Boolean logDisablePrintColor) {
        this.logDisablePrintColor = logDisablePrintColor;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Boolean getEnableTls() {
        return enableTls;
    }

    public void setEnableTls(Boolean enableTls) {
        this.enableTls = enableTls;
    }

    public Boolean getUseEncryption() {
        return useEncryption;
    }

    public void setUseEncryption(Boolean useEncryption) {
        this.useEncryption = useEncryption;
    }

    public Boolean getUseCompression() {
        return useCompression;
    }

    public void setUseCompression(Boolean useCompression) {
        this.useCompression = useCompression;
    }

    public List<FrpcProxyProperties> getProxies() {
        return proxies;
    }

    public void setProxies(List<FrpcProxyProperties> proxies) {
        this.proxies = proxies;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getToml() {
        return toml;
    }

    public void setToml(String toml) {
        this.toml = toml;
    }

    public String getFrpcCmd() {
        return frpcCmd;
    }

    public void setFrpcCmd(String frpcCmd) {
        this.frpcCmd = frpcCmd;
    }

    public String getDashboardUsername() {
        return dashboardUsername;
    }

    public void setDashboardUsername(String dashboardUsername) {
        this.dashboardUsername = dashboardUsername;
    }

    public String getDashboardPassword() {
        return dashboardPassword;
    }

    public void setDashboardPassword(String dashboardPassword) {
        this.dashboardPassword = dashboardPassword;
    }

    public String getDashboardUrl() {
        return dashboardUrl;
    }

    public void setDashboardUrl(String dashboardUrl) {
        this.dashboardUrl = dashboardUrl;
    }

    public boolean isTomlFileRefresh() {
        return tomlFileRefresh;
    }

    public void setTomlFileRefresh(boolean tomlFileRefresh) {
        this.tomlFileRefresh = tomlFileRefresh;
    }
}
