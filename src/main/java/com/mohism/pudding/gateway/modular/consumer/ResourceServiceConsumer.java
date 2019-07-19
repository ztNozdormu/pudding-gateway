package com.mohism.pudding.gateway.modular.consumer;

import com.mohism.pudding.kernel.model.api.ResourceService;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Service;
/**
 * 资源服务的消费者
 *
 * @author fengshuonan
 * @date 2018-08-07-下午3:12
 */
@FeignClient("pudding-system")
@Service
public interface ResourceServiceConsumer extends ResourceService {

}
