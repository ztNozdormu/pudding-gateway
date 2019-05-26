package com.mohism.pudding.gateway.config;

import com.alibaba.fastjson.JSON;
import com.mohism.pudding.base.route.dto.GatewayFilterDefinition;
import com.mohism.pudding.base.route.dto.GatewayPredicateDefinition;
import com.mohism.pudding.base.route.dto.GatewayRouteDefinition;
import com.mohism.pudding.base.route.entity.GatewayRoute;
import com.mohism.pudding.gateway.core.handler.dynamicRoute.DynamicRouteHandler;
import com.mohism.pudding.gateway.modular.consumer.DynamicVersionConsumer;
import com.mohism.pudding.gateway.modular.consumer.GatewayRouteConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.FilterDefinition;
import org.springframework.cloud.gateway.handler.predicate.PredicateDefinition;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 定时任务，拉取路由信息
 * 路由信息由base-route项目单独维护
 */
@Configuration
public class DynamicRouteScheduleConfig {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //发布路由信息的版本号
    private static Long  versionId = 0L;
    @Autowired
    private DynamicVersionConsumer dynamicVersionConsumer;

    @Autowired
    private GatewayRouteConsumer gatewayRouteConsumer;

    @Autowired
    private DynamicRouteHandler dynamicRouteHandler;

   //每60秒中执行一次
    //如果版本号不相等则获取最新路由信息并更新网关路由
    @Scheduled(cron = "*/60 * * * * ?")

    public void getDynamicRouteInfo(){
        try{
            System.out.println("拉取时间:" + dateFormat.format(new Date()));
            //先拉取版本信息，如果版本号不想等则更新路由
            Long resultVersionId = dynamicVersionConsumer.getLastVersion();
            System.out.println("路由版本信息：本地版本号：" + versionId + "，远程版本号：" + resultVersionId);
            if(resultVersionId != null && versionId != resultVersionId){
                System.out.println("开始拉取路由信息......");
               List<GatewayRoute> routes = gatewayRouteConsumer.getAllList();
               // 更新网关服务动态路由信息
                for(GatewayRoute gatewayRoute : routes){
                  GatewayRouteDefinition gatewayRouteDefinition = new GatewayRouteDefinition();
                    gatewayRouteDefinition.setId(gatewayRoute.getRouteId());
                    gatewayRouteDefinition.setUri(gatewayRoute.getRouteUri());
                    gatewayRouteDefinition.setFilters(gatewayRoute.getFilterDefinition());
                    gatewayRouteDefinition.setPredicates(gatewayRoute.getPredicateDefinition());
                    //更新路由
                    RouteDefinition routeDefinition = assembleRouteDefinition(gatewayRouteDefinition);
                    dynamicRouteHandler.update(routeDefinition);
                }
            }
            versionId = resultVersionId;
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    //把前端传递的参数转换成路由对象
    private RouteDefinition assembleRouteDefinition(GatewayRouteDefinition gwdefinition) {
        RouteDefinition definition = new RouteDefinition();
        definition.setId(gwdefinition.getId());
        definition.setOrder(gwdefinition.getOrder());

        //设置断言
        List<PredicateDefinition> pdList=new ArrayList<>();
        List<GatewayPredicateDefinition> gatewayPredicateDefinitionList=gwdefinition.getPredicates();
        for (GatewayPredicateDefinition gpDefinition: gatewayPredicateDefinitionList) {
            PredicateDefinition predicate = new PredicateDefinition();
            predicate.setArgs(gpDefinition.getArgs());
            predicate.setName(gpDefinition.getName());
            pdList.add(predicate);
        }
        definition.setPredicates(pdList);

        //设置过滤器
        List<FilterDefinition> filters = new ArrayList();
        List<GatewayFilterDefinition> gatewayFilters = gwdefinition.getFilters();
        for(GatewayFilterDefinition filterDefinition : gatewayFilters){
            FilterDefinition filter = new FilterDefinition();
            filter.setName(filterDefinition.getName());
            filter.setArgs(filterDefinition.getArgs());
            filters.add(filter);
        }
        definition.setFilters(filters);

        URI uri = null;
        if(gwdefinition.getUri().startsWith("http")){
            uri = UriComponentsBuilder.fromHttpUrl(gwdefinition.getUri()).build().toUri();
        }else{
            uri = URI.create(gwdefinition.getUri());
        }
        definition.setUri(uri);
        return definition;
    }
}
