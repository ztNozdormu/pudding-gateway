package com.mohism.pudding.gateway.modular.consumer;

import com.mohism.pudding.kernel.model.api.AuthService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;

/**
 * 鉴权服务的消费者
 *
 * @author fengshuonan
 */
@FeignClient("pudding-system")
@Service
public interface AuthServiceConsumer extends AuthService {

}
