# 硅谷进销存 Lite-IMS

轻量级进销存管理系统:Spring Boot 3 + MyBatis-Plus 后端,Vue 3 + Element Plus 前端,MySQL 数据库。

## 启动服务

需要启动 **2 个本地服务**,外加 1 个无需启动的远程依赖:

| 服务 | 启动命令 | 端口 | 说明 |
| :--- | :--- | :--- | :--- |
| 后端 Spring Boot | `./mvnw spring-boot:run`(项目根目录) | 8080 | 提供 RESTful API 和 Knife4j 文档(`/doc.html`) |
| 前端 Vite | `cd frontend && npm run dev` | 5173 | 页面入口,`/api` 请求由 Vite 代理转发到 8080 |
| MySQL 数据库 | 无需启动 | 3306 | 在云服务器 `8.136.201.57` 上常驻运行,后端启动时自动连接 |

- 建议先启动后端再启动前端(顺序不强制,前端先起来只是登录会暂时报错,等后端就绪即可)。
- 访问入口:`http://localhost:5173`,演示账号 `admin / 123456`。
- 接口文档:`http://localhost:8080/doc.html`。
- 停止服务:`lsof -ti:8080,5173 | xargs kill`。

## 部署提示

生产环境可执行 `cd frontend && npm run build` 将前端打包为静态文件,放入后端 `src/main/resources/static/` 或交由 Nginx 托管,这样线上只需运行一个后端服务。开发阶段保持两个服务是为了前端热更新。

## 项目文件树

```text
my-jinxiaocun-claude (单体工程, Maven)
│
├── src/main/java/org/example/myjinxiaocunclaude
│   │                       ★ 后端 Spring Boot 3 (45个类)
│   ├── MyJinxiaocunClaudeApplication   启动类 (@EnableScheduling)
│   ├── controller          ★ 控制器层 (7个, RESTful API)
│   │   ├── LoginController        登录/登出 (Session 会话)
│   │   ├── DashboardController    仪表盘统计 + 3个详情接口
│   │   ├── CategoryController     分类 CRUD
│   │   ├── ProductController      商品 CRUD + 分页搜索
│   │   ├── CustomerController     客户 CRUD
│   │   ├── SaleOrderController    订单创建/列表/状态流转
│   │   └── ExcelController        订单报表导出 (EasyExcel)
│   ├── service (+impl)     ★ 业务层 (5个接口+5个实现)
│   │   └── SaleOrderServiceImpl   核心: @Transactional 下单
│   │                              (库存校验→扣减→明细→总额)
│   ├── mapper              ★ 持久层 (6个, MyBatis-Plus BaseMapper)
│   │   ├── ProductMapper          + 关联分类分页 / 分类分布统计
│   │   └── SaleOrderMapper        + 订单关联查询 / 销售额 / 趋势统计
│   ├── entity              实体 (6个: SysUser/Category/Product/
│   │                       Customer/SaleOrder/SaleOrderItem,
│   │                       统一 @TableLogic 逻辑删除)
│   ├── dto                 OrderDTO (创建订单入参, 含明细列表)
│   ├── vo                  视图对象 (6个: Dashboard/Product/SaleOrder/
│   │                       OrderExport/ProductDetail/SalesDetail)
│   ├── common              Result 统一返回 + 全局异常处理器
│   ├── config              WebConfig (拦截器/CORS) + 分页插件 +
│   │                       JacksonConfig (LocalDateTime 格式化)
│   ├── interceptor         LoginInterceptor 登录拦截 (401)
│   ├── aspect              LogAspect 请求日志 (AOP 环绕通知)
│   └── task                StockTask 库存预警 (每分钟定时)
│
├── src/main/resources
│   ├── application.yml     端口/数据源/MyBatis-Plus 全局配置
│   └── mapper/*.xml        ProductMapper / SaleOrderMapper 自定义 SQL
│
├── frontend                ★ 前端 Vue 3 + Vite + Element Plus
│   ├── vite.config.js      dev 代理: /api → localhost:8080
│   └── src
│       ├── router          路由 + 登录守卫 (含隐藏的详情页路由)
│       ├── utils/request.js   axios 封装 (错误提示/401跳登录)
│       ├── layout/Layout.vue  深色侧边栏 + 顶栏布局
│       └── views           7个页面: Login / Dashboard /
│                           DashboardDetail (统计详情) / Product /
│                           Category / Customer / Order (开单抽屉)
│
├── mysql/lite_ims.sql      ★ 数据库脚本 (6张表+种子数据, 已导入远端)
│
├── docs                    ★ 文档
│   ├── 01~07_*.md          教程文档 (项目介绍/数据库/接口/搭建/实现)
│   ├── 08_项目架构图.md     架构图 (Mermaid: 整体/E-R/时序)
│   └── superpowers/specs   前后端设计决策记录 (2份)
│
├── .claude/settings.json   Claude Code 沙箱配置
└── pom.xml                 Spring Boot 3.0.5 + MyBatis-Plus/Knife4j/
                            Lombok/EasyExcel/AOP 依赖
```

## 相关文档

- 教程与功能说明:`docs/01~07_*.md`
- 项目架构图:[docs/08_项目架构图.md](docs/08_项目架构图.md)
- 设计决策记录:`docs/superpowers/specs/`
- 数据库初始化脚本:`mysql/lite_ims.sql`(远端库已导入)
