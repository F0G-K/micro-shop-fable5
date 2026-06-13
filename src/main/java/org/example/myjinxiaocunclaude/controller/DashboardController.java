package org.example.myjinxiaocunclaude.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.myjinxiaocunclaude.common.Result;
import org.example.myjinxiaocunclaude.service.DashboardService;
import org.example.myjinxiaocunclaude.vo.DashboardVO;
import org.example.myjinxiaocunclaude.vo.ProductDetailVO;
import org.example.myjinxiaocunclaude.vo.SaleOrderVO;
import org.example.myjinxiaocunclaude.vo.SalesDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "仪表盘")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "获取统计数据")
    public Result<DashboardVO> getStats() {
        return Result.success(dashboardService.getStats());
    }

    @GetMapping("/product-detail")
    @Operation(summary = "商品统计详情 (库存预警榜 + 分类分布)")
    public Result<ProductDetailVO> getProductDetail() {
        return Result.success(dashboardService.getProductDetail());
    }

    @GetMapping("/today-orders")
    @Operation(summary = "今日订单列表")
    public Result<List<SaleOrderVO>> getTodayOrders() {
        return Result.success(dashboardService.getTodayOrders());
    }

    @GetMapping("/sales-detail")
    @Operation(summary = "销售统计详情 (状态汇总 + 近7日趋势)")
    public Result<SalesDetailVO> getSalesDetail() {
        return Result.success(dashboardService.getSalesDetail());
    }
}
