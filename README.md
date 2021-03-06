pudding

布丁微服务网关服务，采用spring cloud gateway组件实现微服务请求的路由转发与过滤

介绍

本项目为pudding系列微服务框架的模块之一，pudding基于Spring Boot 2和Spring Cloud Green.witch，致力做更简洁的分布式和服务化解决方案，pudding拥有高效率的开发体验，提供可靠消息最终一致性分布式事务解决方案，提供基于调用链的服务治理，提供可靠的服务异常定位方案（Log + Trace）等等，一个分布式框架不仅需要构建高效稳定的底层开发框架，更需要解决分布式带来的种种挑战，请关注pudding微服务框架https://gitee.com/stylefeng/pudding

本项目模块介绍

模块名称	说明	端口	备注
pudding-gateway	网关	9000	网关
注意事项

开发环境为jdk 1.8
maven推荐使用阿里云镜像，拉取jar包保证成功
运行pudding-gateway之前，需要先运行pudding-config-server（如果启用分布式配置的话），pudding-cloud-register（服务注册发现）和pudding-system-web（调用鉴权相关接口）
本项目没有调用数据库，所以排除了数据源的自动配置DataSourceAutoConfiguration
启动步骤

直接运行PuddingGatewayApplication的main方法
使用介绍

当前版本过滤器介绍：

JwtTokenFilter，jwt鉴权过滤器，其中登陆接口AuthConstants.AUTH_ACTION_URL和校验token接口AuthConstants.VALIDATE_TOKEN_URL会放过过滤，不被校验，其他接口都需要携带token才可以访问，另外，可通过jwt.secret配置秘钥，jwt.expiration设置jwt失效时间，默认1天
PathMatchFilter，资源权限校验过滤器，pudding通过@ApiResource来搜集所有微服务中的资源，网关通过调用pudding-system提供的用户权限数据，来确定当前用户是否有权限访问当前访问接口
RequestNoGenerateFilter，请求唯一号生成器，每次经过网关的请求，不管后续经过多少个微服务都会生成这样一个唯一请求号，用来日志追踪和异常排查
控制器接口介绍：
> 1. /gatewayAction/auth接口，相当于登陆接口，传递账号密码返回用户token，然后访问别的接口就带着这个token访问即可
> 2. /gatewayAction/validateToken接口，相当于单点登陆SSO接口，多个内部系统之间，通过此接口来确定当前登陆用户（携带的token）是否正确
> 3. /gatewayAction/logout接口，退出接口，服务器清除掉登陆记录缓存
动态路由介绍
> 1.集成阿波罗配置管理中心
> 2.通过监听方式动态刷新路由
全局异常处理:
http://www.cnblogs.com/yinjihuan/p/10474774.html
---

/gatewayAction/auth接口，相当于登陆接口，传递账号密码返回用户token，然后访问别的接口就带着这个token访问即可
/gatewayAction/validateToken接口，相当于单点登陆SSO接口，多个内部系统之间，通过此接口来确定当前登陆用户（携带的token）是否正确
/gatewayAction/logout接口，退出接口，服务器清除掉登陆记录缓存 全局异常处理: http://www.cnblogs.com/yinjihuan/p/10474774.html
项目特点

pudding中，所有业务请求经过网关，网关做统一的鉴权，权限过滤，数据签名校验，这样做的好处就是业务系统中不用集成鉴权模块，只需要关心各自服务的业务编写。鉴权方面pudding依旧使用jwt，如果想对某个用户作某些资源访问的限制，需要开启PathMatchFilter(默认没开启)，该过滤器会对用户当前访问的资源进行权限校验，资源的收集方式也做了大大的简化，pudding中通过资源搜集器pudding-scanner收集资源，所有的资源只需要用@ApiResource注解标识，项目运行后会把所有资源发送到pudding-system服务中，对比以往的每新增一个资源都需要在后台管理系统中新增一条记录，省了不少工作量。

另外，pudding-gateway网关还为每个请求生成该次请求的唯一请求号。唯一请求号的作用如下：请求在业务流转过程中可能经过多个微服务，查看这次请求的info日志信息，或者error日志信息等，需要从多个微服务的日志记录里去查找，效率非常低，那么，有了唯一请求号标识之后，可以用唯一请求号把请求经过的所有业务流转串起来，并存储起来，当请求遇到问题后，可以通过唯一请求号快速把这次请求的所有日志搜集并展示起来，从而方便排查问题。
pudding中，请求号在中转过程中填充到请求的Request-Header中，与之对应，响应时也会在Response-Hedaer中把本次的请求号输出。过滤器中的写法如下：

RequestContext currentContext = RequestContext.getCurrentContext();
HttpServletResponse response = currentContext.getResponse();

String requestNo = IdWorker.getIdStr();

currentContext.addZuulRequestHeader(puddingConstants.REQUEST_NO_HEADER_NAME, requestNo);
response.addHeader(puddingConstants.REQUEST_NO_HEADER_NAME, requestNo);

为了让Feign调用中，自动填充网关生成的唯一号，pudding增加了puddingFeignHeaderProcessInterceptor拦截器，实现如下：

public class PuddingFeignHeaderProcessInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        HttpServletRequest request = HttpContext.getRequest();

        if (request == null) {
            if (log.isDebugEnabled()) {
                log.debug("被调环境中不存在request对象，则不往header里添加当前请求环境的header!");
            }
            return;
        } else {
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String values = request.getHeader(name);
                    requestTemplate.header(name, values);
                }
            }
        }
        this.addOtherHeaders(requestTemplate);
    }

    public void addOtherHeaders(RequestTemplate requestTemplate) {
    
    }
}

# 动态路由实现
### 一. Spring Cloud Gateway动态路由实现思路

首先实现ApplicationContextAware和ApplicationEventPublisherAware两个接口，然后通过ApolloConfigChangeListener注解监听spring.cloud.gateway命名空间的配置更新事件，若该事件发生，则调用onChange方法，刷新变动的bean实例和路由信息。



### 二. apollo信息配置

- server.port：网关应用端口

- spring.cloud.gateway.routes[0].id：路由id

- spring.cloud.gateway.routes[0].uri：将请求路由到哪里

- spring.cloud.gateway.routes[0].predicates[0]：进行判断当前路由是否满足给定条件

- spring.cloud.gateway.discovery.locator.enabled：是否与服务发现组件进行结合

  

### 三. 案例总结

- platform-bom解决jar冲突，apollo-client：引入apollo客户端依赖。
- ApolloConfigChangeListener：监听指定命名空间的配置更新事件
- applicationContext.publishEvent：更新变动配置的值以及相应的bean的属性值，主要是存在@ConfigurationProperties注解的bean(默认情况下@ConfigurationProperties注解标注的配置类是不会实时更新的)
- publisher.publishEvent(new RefreshRoutesEvent(this))：路由动态刷新