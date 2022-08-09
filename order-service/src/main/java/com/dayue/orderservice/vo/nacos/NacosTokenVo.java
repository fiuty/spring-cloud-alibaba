package com.dayue.orderservice.vo.nacos;

/**
 * @author zhengdayue
 * @time 2022/8/10 0:54
 */
public class NacosTokenVo {

    private String accessToken;

    private Long tokenTtl;

    private boolean globalAdmin;

    private String username;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getTokenTtl() {
        return tokenTtl;
    }

    public void setTokenTtl(Long tokenTtl) {
        this.tokenTtl = tokenTtl;
    }

    public boolean isGlobalAdmin() {
        return globalAdmin;
    }

    public void setGlobalAdmin(boolean globalAdmin) {
        this.globalAdmin = globalAdmin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "NacosTokenVo{" +
                "accessToken='" + accessToken + '\'' +
                ", tokenTtl=" + tokenTtl +
                ", globalAdmin=" + globalAdmin +
                ", username='" + username + '\'' +
                '}';
    }
}
