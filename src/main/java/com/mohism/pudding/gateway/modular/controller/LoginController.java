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
package com.mohism.pudding.gateway.modular.controller;

import com.mohism.pudding.core.reqres.response.ResponseData;
import com.mohism.pudding.gateway.core.constants.AuthConstants;
import com.mohism.pudding.gateway.modular.consumer.AuthServiceConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 登录控制器
 *
 * @author fengshuonan
 * @date 2017-11-08-下午7:04
 */
@RestController
public class LoginController {

    @Autowired
    private AuthServiceConsumer authServiceConsumer;

    /**
     * 登录接口
     */
    @RequestMapping(AuthConstants.AUTH_ACTION_URL)
    public ResponseData auth(@RequestParam("userName") String userName, @RequestParam("password") String password) {
        String token = authServiceConsumer.login(userName, password);
        return ResponseData.success(token);
    }

    /**
     * 验证token是否正确
     */
    @RequestMapping(AuthConstants.VALIDATE_TOKEN_URL)
    public ResponseData validateToken(@RequestParam("token") String token) {
        boolean tokenFlag = authServiceConsumer.checkToken(token);
        return ResponseData.success(tokenFlag);
    }

    /**
     * 退出接口
     */
    @RequestMapping(AuthConstants.LOGOUT_URL)
    public ResponseData logout(@RequestParam("token") String token) {
        authServiceConsumer.logout(token);
        return ResponseData.success();
    }

}
