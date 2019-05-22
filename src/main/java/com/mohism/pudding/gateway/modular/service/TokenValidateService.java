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
package com.mohism.pudding.gateway.modular.service;

import com.mohism.pudding.core.util.ToolUtil;
import com.mohism.pudding.gateway.core.exception.AuthExceptionEnum;
import com.mohism.pudding.kernel.jwt.properties.JwtProperties;
import com.mohism.pudding.kernel.model.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.http.HttpServletRequest;

import static com.mohism.pudding.gateway.core.constants.AuthConstants.AUTH_HEADER;

/**
 * Token校验的服务
 *
 * @author fengshuonan
 * @date 2018-08-13 21:50
 */
public abstract class TokenValidateService {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * @author stylefeng
     * @Date 2018/8/13 22:11
     */
    public boolean doValidate(ServerWebExchange exchange) {
        //先获取token
        String token =  getTokenFromRequest(exchange);
        //校验token是否正确
        return this.validateToken(token, exchange);
    }

    /**
     * 获取请求中的token
     *
     * @author stylefeng
     * @Date 2018/8/13 22:05
     */
    private String getTokenFromRequest(ServerWebExchange exchange) {
        //获取token
        String authToken = exchange.getRequest().getQueryParams().getFirst("token");
        if (ToolUtil.isEmpty(authToken)) {
            throw new ServiceException(AuthExceptionEnum.TOKEN_EMPTY);
        }
        return authToken;
    }

    /**
     * 校验token
     *
     * @author stylefeng
     * @Date 2018/8/13 21:50
     */
    protected abstract boolean validateToken(String token, ServerWebExchange exchange);

}
