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
import com.mohism.pudding.gateway.modular.consumer.AuthServiceConsumer;
import com.mohism.pudding.gateway.modular.consumer.ResourceServiceConsumer;
import com.mohism.pudding.kernel.jwt.properties.JwtProperties;
import com.mohism.pudding.kernel.model.exception.ServiceException;
import com.mohism.pudding.kernel.model.resource.ResourceDefinition;
import com.netflix.zuul.GwFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

import static com.mohism.pudding.gateway.core.constants.AuthConstants.AUTH_HEADER;

/**
 * 请求路径权限过滤器
 *
 * @author fengshuonan
 * @date 2017-11-14-上午10:43
 */
public class PathMatchFilter extends GatewayFilter {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private ResourceServiceConsumer resourceServiceConsumer;

    @Autowired
    private AuthServiceConsumer authServiceConsumer;

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return GwFiltersOrder.PATH_MATCH_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

        //登陆接口和验证token放过资源过滤
        if (request.getServletPath().equals(AuthConstants.AUTH_ACTION_URL) ||
                request.getServletPath().equals(AuthConstants.VALIDATE_TOKEN_URL)) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletRequest request = currentContext.getRequest();

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
            if (currentResource.getRequiredPermission()) {
                final String sysToken = request.getHeader(AUTH_HEADER);
                Set<Object> permissionUrls = authServiceConsumer.getLoginUserByToken(sysToken).getResourceUrls();
                boolean hasPermission = permissionUrls.contains(servletPath);
                if (hasPermission) {
                    return null;
                } else {
                    throw new ServiceException(AuthExceptionEnum.NO_PERMISSION);
                }
            } else {
                return null;
            }
        }
    }
}
