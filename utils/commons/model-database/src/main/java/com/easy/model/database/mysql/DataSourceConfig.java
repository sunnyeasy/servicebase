package com.easy.model.database.mysql;

public class DataSourceConfig {
    //@Value("${druid.driverClassName}")
    private String driverClassName;

    //@Value("${druid.url}")
    private String url;

    //@Value("${druid.username}")
    private String username;

    //@Value("${druid.password}")
    private String password;

    //@Value("${druid.filters}")
    private String filters;

    //@Value("${druid.initialSize}")
    private int initialSize;

    //@Value("${druid.maxActive}")
    private int maxActive;

    //@Value("${druid.minIdle}")
    private int minIdle;

    //@Value("${druid.maxWait}")
    private long maxWait;

    //@Value("${druid.timeBetweenEvictionRunsMillis}")
    private long timeBetweenEvictionRunsMillis;

    //@Value("${druid.minEvictableIdleTimeMillis}")
    private long minEvictableIdleTimeMillis;

    //@Value("${druid.validationQuery}")
    private String validationQuery;

    //@Value("${druid.testOnBorrow}")
    private boolean testOnBorrow;

    //@Value("${druid.testOnReturn}")
    private boolean testOnReturn;

    //@Value("${druid.removeAbandoned}")
    private boolean removeAbandoned;

    //@Value("${druid.removeAbandonedTimeout}")
    private int removeAbandonedTimeout;

    //@Value("${druid.logAbandoned}")
    private boolean logAbandoned;

    //@Value("${druid.testWhileIdle}")
    private boolean testWhileIdle;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFilters() {
        return filters;
    }

    public void setFilters(String filters) {
        this.filters = filters;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(int initialSize) {
        this.initialSize = initialSize;
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public long getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(long maxWait) {
        this.maxWait = maxWait;
    }

    public long getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public long getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestOnReturn() {
        return testOnReturn;
    }

    public void setTestOnReturn(boolean testOnReturn) {
        this.testOnReturn = testOnReturn;
    }

    public boolean isRemoveAbandoned() {
        return removeAbandoned;
    }

    public void setRemoveAbandoned(boolean removeAbandoned) {
        this.removeAbandoned = removeAbandoned;
    }

    public int getRemoveAbandonedTimeout() {
        return removeAbandonedTimeout;
    }

    public void setRemoveAbandonedTimeout(int removeAbandonedTimeout) {
        this.removeAbandonedTimeout = removeAbandonedTimeout;
    }

    public boolean isLogAbandoned() {
        return logAbandoned;
    }

    public void setLogAbandoned(boolean logAbandoned) {
        this.logAbandoned = logAbandoned;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }
}
