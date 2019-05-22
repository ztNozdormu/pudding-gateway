package com.mohism.pudding.gateway.core.filter;

import com.mohism.pudding.gateway.core.constants.AuthConstants;
import com.mohism.pudding.gateway.core.constants.GwFiltersOrder;
import com.mohism.pudding.gateway.core.exception.AuthExceptionEnum;
import com.mohism.pudding.gateway.modular.consumer.AuthServiceConsumer;
import com.mohism.pudding.gateway.modular.consumer.ResourceServiceConsumer;
import com.mohism.pudding.kernel.jwt.properties.JwtProperties;
import com.mohism.pudding.kernel.model.exception.ServiceException;
import com.mohism.pudding.kernel.model.resource.ResourceDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

/**
 * 权限校验的过滤器---全局过滤器
 *
 * @author real earth
 * @date 2017-11-08-下午2:49
 */
public class PathMatchFilter implements GatewayFilter,Ordered {


    private  static Logger logger = LoggerFactory.getLogger(PathMatchFilter.class);

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private ResourceServiceConsumer resourceServiceConsumer;

    @Autowired
    private AuthServiceConsumer authServiceConsumer;

    @Override
    public int getOrder() {
        return GwFiltersOrder.PATH_MATCH_FILTER_ORDER;
    }
    /**
     * 请求路径权限过滤
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
//            RequestContext currentContext = RequestContext.getCurrentContext();

        String requestUri = request.getRequestURI();
        String servletPath = request.getServletPath();

        ResourceDefinition currentResource = resourceServiceConsumer.getResourceByUrl(requestUri);
        if (currentResource == null) {
            return null;
        } else {

            //判断如果本接口不需要登录直接略过,不登录获取不到用户token
            if (!currentResource.getRequiredLogin()) {
                return null;
            }

            //判断本接口是否需要url资源过滤
//            if (currentResource.getRequiredPermission()) {
//                final String sysToken = request.getHeader(AuthConstants.AUTH_HEADER);
//                Set<Object> permissionUrls = authServiceConsumer.getLoginUserByToken(sysToken).getResourceUrls();
//                boolean hasPermission = permissionUrls.contains(servletPath);
//                if (hasPermission) {
//                    return null;
//                } else {
//                    throw new ServiceException(AuthExceptionEnum.NO_PERMISSION);
//                }
//            } else {
//                return null;
//            }
            //判断本接口是否需要url资源过滤
            if (currentResource.getRequiredPermission()) {
                final String sysToken = request.getHeader(AuthConstants.AUTH_HEADER);
                Set<Object> permissionUrls = authServiceConsumer.getLoginUserByToken(sysToken).getResourceUrls();
                boolean hasPermission = permissionUrls.contains(servletPath);
                if (!hasPermission) {
                    throw new ServiceException(AuthExceptionEnum.NO_PERMISSION);
                }
            }
        }
        }
        return chain.filter(exchange);
    }
}
