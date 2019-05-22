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
package com.mohism.pudding.gateway.core.filter;

import com.mohism.pudding.gateway.core.constants.GwFiltersOrder;
import com.mohism.pudding.kernel.model.constants.RosesConstants;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.gateway.filter.GatewayFilter;

import javax.servlet.http.HttpServletResponse;

/**
 * requestNo生成过滤器
 *
 * @author fengshuonan
 * @date 2017-11-08-下午2:49
 */
public class RequestNoGenerateFilter extends GatewayFilter {

    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return GwFiltersOrder.REQUEST_NO_GENERATE_FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext currentContext = RequestContext.getCurrentContext();
        HttpServletResponse response = currentContext.getResponse();

        //生成唯一请求号uuid
        String requestNo = IdWorker.getIdStr();
        currentContext.set(RosesConstants.REQUEST_NO_HEADER_NAME, requestNo);
        currentContext.addZuulRequestHeader(RosesConstants.REQUEST_NO_HEADER_NAME, requestNo);
        response.addHeader(RosesConstants.REQUEST_NO_HEADER_NAME, requestNo);

        return null;
    }
}
