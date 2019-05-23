package com.mohism.pudding.gateway.modular.controller;

//import com.mohism.pudding.core.reqres.response.ResponseData;
import com.mohism.pudding.gateway.core.constants.AuthConstants;
import com.mohism.pudding.gateway.modular.consumer.AuthServiceConsumer;
import org.bouncycastle.asn1.ocsp.ResponseData;
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

//    @Autowired
//    private AuthServiceConsumer authServiceConsumer;
//
//    /**
//     * 登录接口
//     */
//    @RequestMapping(AuthConstants.AUTH_ACTION_URL)
//    public ResponseData auth(@RequestParam("userName") String userName, @RequestParam("password") String password) {
//        String token = authServiceConsumer.login(userName, password);
//        return null;//ResponseData.success(token);
//    }
//
//    /**
//     * 验证token是否正确
//     */
//    @RequestMapping(AuthConstants.VALIDATE_TOKEN_URL)
//    public ResponseData validateToken(@RequestParam("token") String token) {
//        boolean tokenFlag = authServiceConsumer.checkToken(token);
//        return null;//ResponseData.success(tokenFlag);
//    }
//
//    /**
//     * 退出接口
//     */
//    @RequestMapping(AuthConstants.LOGOUT_URL)
//    public ResponseData logout(@RequestParam("token") String token) {
//        authServiceConsumer.logout(token);
//        return null;//ResponseData.success();
//    }

}
