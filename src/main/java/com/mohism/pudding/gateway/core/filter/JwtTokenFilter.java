/**
 * Copyright 2018-2020 stylefeng & fengshuonan (sn93@qq.com)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mohism.pudding.gateway.core.filter;

import com.mohism.pudding.gateway.core.constants.AuthConstants;
import com.mohism.pudding.gateway.core.constants.GwFiltersOrder;
import com.mohism.pudding.gateway.core.exception.AuthExceptionEnum;
import com.mohism.pudding.gateway.modular.service.TokenValidateService;
import com.mohism.pudding.kernel.model.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;

/**
 * 权限校验的过滤器
 *
 * @author real earth
 * @date 2019-05-21-晚上23:11
 */
public class JwtTokenFilter implements GlobalFilter, Ordered {

    @Autowired
    private TokenValidateService tokenValidateService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getQueryParams().getFirst("token");
        HttpServletRequest request = (HttpServletRequest)exchange.getRequest();
        ServerHttpRequest srequest = exchange.getRequest();

        //登陆接口和验证token放过资源过滤
        if (request.getServletPath().equals(AuthConstants.AUTH_ACTION_URL) ||
                request.getServletPath().equals(AuthConstants.VALIDATE_TOKEN_URL)) {
        }
//        else {
//                tokenValidateService.doValidate(exchange);
//        }
        if (token == null || token.isEmpty()) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            throw new ServiceException(AuthExceptionEnum.TOKEN_EMPTY);
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }
    // 过滤器执行优先顺序，值越小优先级越大
    @Override
    public int getOrder() {
        return GwFiltersOrder.JWT_TOKEN_FILTER_ORDER;
    }
//    @Override
//    public String filterType() {
//        return "pre";
//    }
//    // 过滤器执行优先顺序，值越小优先级越大
//    @Override
//    public int filterOrder() {
//        return GateFiltersOrder.JWT_TOKEN_FILTER_ORDER;
//    }
//
//    @Override
//    public boolean shouldFilter() {
//        RequestContext currentContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = currentContext.getRequest();
//
//        //登陆接口和验证token放过资源过滤
//        if (request.getServletPath().equals(AuthConstants.AUTH_ACTION_URL) ||
//                request.getServletPath().equals(AuthConstants.VALIDATE_TOKEN_URL)) {
//            return false;
//        } else {
//            return true;
//        }
//    }
//
//    @Override
//    public Object run() {
//        RequestContext currentContext = RequestContext.getCurrentContext();
//        HttpServletRequest request = currentContext.getRequest();
//
//        tokenValidateService.doValidate(request);
//
//        return null;
//    }
}
