package com.mohism.pudding.gateway.core.filter;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.mohism.pudding.gateway.core.constants.GwFiltersOrder;
import com.mohism.pudding.kernel.model.constants.PuddingConstants;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletResponse;

/**
 * 权限校验的过滤器---全局过滤器
 *
 * @author real earth
 * @date 2017-11-08-下午2:49
 */
public class RequestNoGenerateFilter implements  GatewayFilter,Ordered {

    @Override
    public int getOrder() {
        return GwFiltersOrder.REQUEST_NO_GENERATE_FILTER_ORDER;
    }
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpServletResponse response = (HttpServletResponse)exchange.getResponse();
        //生成唯一请求号uuid
        String requestNo = IdWorker.getIdStr();
        response.addHeader(PuddingConstants.REQUEST_NO_HEADER_NAME, requestNo);
        return chain.filter(exchange);
    }
}
