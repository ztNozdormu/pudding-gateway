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
package com.mohism.pudding.gateway.config;

import com.mohism.pudding.gateway.core.filter.JwtTokenFilter;
import com.mohism.pudding.gateway.core.filter.PathMatchFilter;
import com.mohism.pudding.gateway.core.filter.RequestNoGenerateFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 过滤器的配置
 *
 * @author fengshuonan
 * @date 2017-11-08-下午3:23
 */
@Configuration
public class FilterConfig {

    /**
     * token过滤器，检查每次请求token是否合法
     */
    @Bean
    public JwtTokenFilter authFilter() {
        return new JwtTokenFilter();
    }

    /**
     * 资源过滤器，检查每次请求是否有权限访问某些资源
     */
    @Bean
    public PathMatchFilter pathMatchFilter() {
        return new PathMatchFilter();
    }

    /**
     * 请求唯一编号生成器，每次请求入网关时都会生成一个唯一编号，用来记录一次请求的所有日志和异常信息
     */
    @Bean
    public RequestNoGenerateFilter requestNoGenerateFilter() {
        return new RequestNoGenerateFilter();
    }

}
