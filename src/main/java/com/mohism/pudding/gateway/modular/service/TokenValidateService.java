package com.mohism.pudding.gateway.modular.service;

import com.mohism.pudding.gateway.core.exception.AuthExceptionEnum;
import com.mohism.pudding.gateway.core.constants.AuthConstants;
import com.mohism.pudding.kernel.jwt.properties.JwtProperties;
import com.mohism.pudding.kernel.model.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.server.ServerWebExchange;

import javax.servlet.http.HttpServletRequest;

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
    public boolean doValidate(HttpServletRequest request) {

        //先获取token
        String tokenFromRequest = this.getTokenFromRequest(request);

        //校验token是否正确
        return this.validateToken(tokenFromRequest, request);
    }

    /**
     * 获取请求中的token
     *
     * @author stylefeng
     * @Date 2018/8/13 22:05
     */
    private String getTokenFromRequest(HttpServletRequest request) {
        //获取token
        String authToken = request.getHeader(AuthConstants.AUTH_HEADER);
//        if (ToolUtil.isEmpty(authToken)) {
//
//            //如果header中没有token，则检查请求参数中是否带token
//            authToken = request.getParameter("token");
//            if (ToolUtil.isEmpty(authToken)) {
//                throw new ServiceException(AuthExceptionEnum.TOKEN_EMPTY);
//            }
//        } else {
//            authToken = authToken.substring("Bearer ".length());
//        }

        return authToken;
    }

    /**
     * 校验token
     *
     * @author stylefeng
     * @Date 2018/8/13 21:50
     */
    protected abstract boolean validateToken(String token, HttpServletRequest request);

}
