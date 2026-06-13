# Lite-IMS 后端实现设计(2026-06-13,已经用户确认)

## 范围
按 docs 05(核心功能)+ docs 06(进阶功能)一次性实现完整后端;前端不在本仓库,不涉及。

## 决策
- 包名:保留 `org.example.myjinxiaocunclaude`(用户选定),AOP 切点等路径相应调整。
- 数据库:远端 `8.136.201.57:3306/lite_ims`(root,密码在 application.yml),库与种子数据已存在,不再导入。
- 实体字段以 `mysql/lite_ims.sql` 实际结构为准(订单表含 `remark`,订单状态枚举 0-待支付/1-已支付/2-已发货/3-已完成/4-已取消)。

## 包结构
common(Result)/ config(MybatisPlusConfig, WebConfig)/ entity×6 / mapper×6(+2 XML)/ dto(OrderDTO)/ vo×4 / service×5(+impl)/ controller×7 / interceptor / aspect / task。

## 与教程文档的有意偏离
1. 总销售额 SQL 排除取消订单用 `status != 4`(文档写 `!= 2`,与实际状态枚举不符)。
2. Excel 导出状态中文映射按实际五档枚举。
3. WebConfig 增加全局 CORS(文档放行 OPTIONS 但漏配响应头)。
4. `SysUser.password` 使用 `@JsonProperty(WRITE_ONLY)`,登录响应不回传密码。
5. `SaleOrderVO` 补充 XML resultMap 映射所需的 `items` 字段;修正文档笔误(priavte、orderDTo)。
6. 分类列表按 `sort` 升序返回(文档定义了排序语义但代码未排序)。
7. 新增 `JacksonConfig`:`spring.jackson.date-format` 对 `LocalDateTime` 无效,需注册 JSR-310 序列化器实现全局 `yyyy-MM-dd HH:mm:ss`。
8. 新增 `GlobalExceptionHandler`:业务异常(库存不足等)统一转 `Result` 格式,替代 Spring 原始 500。
9. 订单明细 `createTime` 由 Java 显式赋值,与主表时间保持一致。
10. 远端 MySQL 全局时区原为 SYSTEM(UTC),导致 `CURRENT_TIMESTAMP` 默认值差 8 小时;已于 2026-06-13 通过 `SET PERSIST time_zone = '+8:00'` 修正(重启后保持),并用插入验证默认时间戳正确。

## 验证策略(代替单元测试,经用户确认)
编译 → 启动连远端库 → curl 实测:登录拿 Cookie → 未登录 401 → 分类/商品/客户 CRUD → 创建订单(核对库存扣减、库存不足事务回滚)→ 订单分页/状态流转 → 仪表盘统计 → Excel 导出 → doc.html 可达。
