package com.easy.gateway.transport.netty4;

import com.easy.common.code.ResponseCode;
import com.easy.common.exception.BusinessException;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.SslHandler;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLEngine;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;

public class SslHandlerFactory {
    private static final Logger logger = LoggerFactory.getLogger(SslHandlerFactory.class);

    public static SslHandler buildSslHandler(SslConfig sslConfig) {
        SslContext sslContext = buildSslContext(sslConfig);
        SSLEngine sslEngine = sslContext.newEngine(ByteBufAllocator.DEFAULT);
        sslEngine.setUseClientMode(false);
        if (sslConfig.isNeedClientAuth()) {
            sslEngine.setNeedClientAuth(true);
        }
        return new SslHandler(sslEngine);
    }

    public static SslContext buildSslContext(SslConfig sslConfig) {
        SslContext sslContext = null;

        try {
            String jksPath = sslConfig.getJksPath();
            if (StringUtils.isEmpty(jksPath)) {
                logger.error("Jks证书路径配置错误, jksPath={}", jksPath);
                throw new BusinessException(ResponseCode.CONFIG_ERROR);
            }

            String jksPassword = sslConfig.getJksPath();
            if (StringUtils.isEmpty(jksPassword)) {
                logger.error("Jks证书密钥配置错误");
                throw new BusinessException(ResponseCode.CONFIG_ERROR);
            }

            logger.info("加载Jks证书, jksPath={}", jksPath);
            InputStream jksInputStream = jksDatastore(jksPath);
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(jksInputStream, jksPassword.toCharArray());

            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, jksPassword.toCharArray());

            SslContextBuilder builder= SslContextBuilder.forServer(kmf);
            sslContext = builder.build();
            return sslContext;
        } catch (Exception e) {
            logger.error("创建Jks证书配置异常, jksPath={}", sslConfig.getJksPath(), e);
            throw new BusinessException(ResponseCode.CONFIG_ERROR);
        }
    }

    private static InputStream jksDatastore(String jksPath) {
        try {
            URL jksUrl = SslHandlerFactory.class.getClassLoader().getResource(jksPath);
            if (jksUrl != null) {
                return SslHandlerFactory.class.getClassLoader().getResourceAsStream(jksPath);
            }

            File jksFile = new File(jksPath);
            if (jksFile.exists()) {
                return new FileInputStream(jksFile);
            }

            logger.error("Jks证书文件不存在, jksPath={}", jksPath);
        } catch (Exception e) {
            logger.error("加载Jks证书失败, jksPath={}", jksPath, e);
        }

        throw new BusinessException(ResponseCode.CONFIG_ERROR);
    }

}
