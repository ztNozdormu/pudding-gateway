//package com.mohism.pudding.gateway.core.feign;
//
//import com.mohism.pudding.kernel.model.constants.PuddingConstants;
//import feign.RequestTemplate;
//
///**
// * gateway对feign拦截器的拓展
// *
// * @author fengshuonan
// * @Date 2018/8/23 下午12:55
// */
//public class ZuulFeignHeaderProcessInterceptor extends PuddingFeignHeaderProcessInterceptor {
//
//    @Override
//    public void addOtherHeaders(RequestTemplate requestTemplate) {
//
////        RequestContext currentContext = RequestContext.getCurrentContext();
////        Object contextObject = currentContext.get(RosesConstants.REQUEST_NO_HEADER_NAME);
//
//        requestTemplate.header(PuddingConstants.REQUEST_NO_HEADER_NAME, contextObject == null ? "" : contextObject.toString());
//    }
//}