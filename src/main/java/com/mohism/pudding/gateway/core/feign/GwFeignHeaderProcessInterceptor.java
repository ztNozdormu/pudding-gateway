///**
// * Copyright 2018-2020 stylefeng & fengshuonan (sn93@qq.com)
// * <p>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package com.mohism.pudding.gateway.core.feign;
//
//import com.mohism.pudding.core.feign.RosesFeignHeaderProcessInterceptor;
//import com.mohism.pudding.kernel.model.constants.RosesConstants;
//import com.netflix.ribbon.RequestTemplate;
//
///**
// * zuul对feign拦截器的拓展
// *
// * @author fengshuonan
// * @Date 2018/8/23 下午12:55
// */
//public class GwFeignHeaderProcessInterceptor extends RosesFeignHeaderProcessInterceptor {
//
//    @Override
//    public void addOtherHeaders(RequestTemplate requestTemplate) {
//
//        RequestContext currentContext = RequestContext.getCurrentContext();
//        Object contextObject = currentContext.get(RosesConstants.REQUEST_NO_HEADER_NAME);
//
//        requestTemplate.header(RosesConstants.REQUEST_NO_HEADER_NAME, contextObject == null ? "" : contextObject.toString());
//    }
//}