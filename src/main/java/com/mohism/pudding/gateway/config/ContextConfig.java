//package com.mohism.pudding.gateway.config;
//
//import cn.hutool.core.bean.BeanUtil;
//import com.mohism.pudding.core.reqres.response.ErrorResponseData;
//import com.mohism.pudding.kernel.logger.chain.aop.ChainOnConsumerAop;
//import com.mohism.pudding.kernel.logger.chain.aop.ChainOnControllerAop;
//import com.mohism.pudding.kernel.logger.chain.aop.ChainOnProviderAop;
//import com.mohism.pudding.kernel.model.exception.ServiceException;
//import com.mohism.pudding.kernel.model.exception.enums.CoreExceptionEnum;
//import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.context.request.WebRequest;
//
//import java.util.Map;
//
///**
// * 错误信息提示的配置
// *
// * @author fengshuonan
// * @date 2017-11-14-下午5:56
// */
//@Configuration
//public class ContextConfig {
//
//    /**
//     * zuul错误信息提示重写
//     */
//    @Bean
//    public RosesErrorAttributes rosesErrorAttributes() {
//        return new RosesErrorAttributes();
//    }
//
//    /**
//     * 调用链治理(调用远程服务之前的日志)
//     */
//    @Bean
//    public ChainOnConsumerAop chainOnConsumerAop() {
//        return new ChainOnConsumerAop();
//    }
//
//    /**
//     * 调用链治理(控制器日志，和一些参数填充)
//     */
//    @Bean
//    public ChainOnControllerAop chainOnControllerAop() {
//        return new ChainOnControllerAop();
//    }
//
//    /**
//     * 调用链治理(参数校验和错误日志的记录)
//     */
//    @Bean
//    public ChainOnProviderAop chainOnProviderAop() {
//        return new ChainOnProviderAop();
//    }
//
//    private class RosesErrorAttributes extends DefaultErrorAttributes {
//
//        @Override
//        public Map<String, Object> getErrorAttributes(WebRequest webRequest, boolean includeStackTrace) {
//            RequestContext currentContext = RequestContext.getCurrentContext();
//            Throwable throwable = currentContext.getThrowable();
//            if (throwable instanceof ZuulException) {
//                ZuulException zuulException = (ZuulException) throwable;
//                Throwable cause = zuulException.getCause();
//                if (cause instanceof ServiceException) {
//                    ServiceException serviceException = (ServiceException) cause;
//                    return BeanUtil.beanToMap(new ErrorResponseData(serviceException.getCode(), serviceException.getMessage(), null));
//                }
//            }
//
//            return BeanUtil.beanToMap(new ErrorResponseData(CoreExceptionEnum.SERVICE_ERROR.getCode(), CoreExceptionEnum.SERVICE_ERROR.getMessage(), null));
//        }
//    }
//
//}
//
