package com.mohism.pudding.gateway.core.filter;

import com.mohism.pudding.gateway.core.constants.AuthConstants;
import com.mohism.pudding.gateway.core.constants.GwFiltersOrder;
import com.mohism.pudding.gateway.modular.service.TokenValidateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import javax.servlet.http.HttpServletRequest;
/**
 * 权限校验的过滤器---全局过滤器
 *
 * @author real earth
 * @date 2017-11-08-下午2:49
 */
@Component
public class JwtTokenFilter implements GlobalFilter, Ordered {

   private  static Logger logger = LoggerFactory.getLogger(JwtTokenFilter.class);
    @Autowired
    private TokenValidateService tokenValidateService;
    @Override
    public int getOrder() {
        return GwFiltersOrder.JWT_TOKEN_FILTER_ORDER;
    }
    /**
     * 全局过滤器
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // String token = exchange.getRequest().getHeaders().getFirst("token");
//        String token = exchange.getRequest().getQueryParams().getFirst("token");
//        String url = exchange.getRequest().getPath().pathWithinApplication().value();
        HttpServletRequest request = (HttpServletRequest)exchange.getRequest();

        logger.info("请求URL:"+request.getServletPath());
       // 登陆接口和验证token放过资源过滤
        if (!(request.getServletPath().equals(AuthConstants.AUTH_ACTION_URL)||request.getServletPath().equals(AuthConstants.VALIDATE_TOKEN_URL))) {
//            tokenValidateService.doValidate(request);
        }
//        if (token == null || token.isEmpty()) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
        return chain.filter(exchange);
    }
}
