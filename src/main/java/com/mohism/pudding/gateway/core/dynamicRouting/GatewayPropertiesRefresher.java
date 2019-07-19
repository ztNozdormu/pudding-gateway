package com.mohism.pudding.gateway.core.dynamicRouting;

import com.ctrip.framework.apollo.enums.PropertyChangeType;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GatewayPropertiesRefresher implements ApplicationContextAware,ApplicationEventPublisherAware {

    private static final Logger logger = LoggerFactory.getLogger(GatewayPropertiesRefresher.class);

    private static final String ID_PATTERN = "spring\\.cloud\\.gateway\\.routes\\[\\d+\\]\\.id";

    private static final String DEFAULT_FILTER_PATTERN = "spring\\.cloud\\.gateway\\.default-filters\\[\\d+\\]\\.name";

    private ApplicationContext applicationContext;

    private ApplicationEventPublisher publisher;

    @Autowired
    private GatewayProperties gatewayProperties;

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    @ApolloConfigChangeListener(interestedKeyPrefixes = "spring.cloud.gateway.")
    public void onChange(ConfigChangeEvent changeEvent) {
        refreshGatewayProperties(changeEvent);
    }

    /***
     * 刷新网关信息
     */
    private void refreshGatewayProperties(ConfigChangeEvent changeEvent) {
        logger.info("Refreshing GatewayProperties!");
        // 如果有删除的key，将删除key数据从GatewayProperties(网关配置信息)清除
        preDestroyGatewayProperties(changeEvent);
        // 更新变动配置的值以及相应的bean的属性值，主要是存在@ConfigurationProperties注解的bean(默认情况下@ConfigurationProperties注解标注的配置类是不会实时更新的)
        this.applicationContext.publishEvent(new EnvironmentChangeEvent(changeEvent.changedKeys()));
        // 发布路由动态刷新事件
        refreshGatewayRouteDefinition();
        logger.info("GatewayProperties refreshed!");
    }

    /***
     * 如果有删除的key，将删除的key对应的数据从GatewayProperties清除
     */
    private synchronized void preDestroyGatewayProperties(ConfigChangeEvent changeEvent) {
        logger.info("Pre Destroy GatewayProperties!");
        final boolean needClearRoutes = this.checkNeedClear(changeEvent, ID_PATTERN);
        if (needClearRoutes) {
            this.gatewayProperties.getRoutes().clear();
        }
        final boolean needClearDefaultFilters = this.checkNeedClear(changeEvent, DEFAULT_FILTER_PATTERN);
        if (needClearDefaultFilters) {
            this.gatewayProperties.getDefaultFilters().clear();
        }
        logger.info("Pre Destroy GatewayProperties finished!");
    }

    /***
     * 发布路由动态刷新事件
     */
    private void refreshGatewayRouteDefinition() {
        logger.info("Refreshing Gateway RouteDefinition!");
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        logger.info("Gateway RouteDefinition refreshed!");
    }

    /***
     * 根据changeEvent和定义的pattern匹配key，如果所有对应PropertyChangeType为DELETED则需要清空GatewayProperties里相关集合
     */
    private boolean checkNeedClear(ConfigChangeEvent changeEvent, String pattern) {

        return changeEvent.changedKeys().stream().filter(key -> key.matches(pattern))
                .filter(key -> {
                    ConfigChange change = changeEvent.getChange(key);
                    return PropertyChangeType.DELETED.equals(change.getChangeType());
                }).count() > 0;
    }

}
