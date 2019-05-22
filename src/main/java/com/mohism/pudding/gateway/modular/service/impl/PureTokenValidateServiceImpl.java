package com.mohism.pudding.gateway.modular.service.impl;

import com.mohism.pudding.gateway.core.exception.AuthExceptionEnum;
import com.mohism.pudding.gateway.modular.service.TokenValidateService;
import com.mohism.pudding.kernel.jwt.utils.JwtTokenUtil;
import com.mohism.pudding.kernel.model.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 纯token验证鉴权服
 *
 * @author fengshuonan
 * @date 2018-08-13 21:52
 */
@Service
public class PureTokenValidateServiceImpl extends TokenValidateService {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Override
    public boolean validateToken(String token, HttpServletRequest request) {

        try {
            boolean flag = jwtTokenUtil.isTokenExpired(token);
            if (flag) {
                throw new ServiceException(AuthExceptionEnum.TOKEN_ERROR);
            } else {
                return true;
            }
        } catch (Exception e) {
            throw new ServiceException(AuthExceptionEnum.TOKEN_ERROR);
        }
    }
}
