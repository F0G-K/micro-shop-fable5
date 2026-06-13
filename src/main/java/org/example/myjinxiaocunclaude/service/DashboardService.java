package org.example.myjinxiaocunclaude.service;

import org.example.myjinxiaocunclaude.vo.DashboardVO;
import org.example.myjinxiaocunclaude.vo.ProductDetailVO;
import org.example.myjinxiaocunclaude.vo.SaleOrderVO;
import org.example.myjinxiaocunclaude.vo.SalesDetailVO;

import java.util.List;

public interface DashboardService {

    /**
     * 获取仪表盘统计数据 (商品总数、今日订单数、总销售额)
     */
    DashboardVO getStats();

    /**
     * 商品统计详情: 库存预警榜 + 分类分布
     */
    ProductDetailVO getProductDetail();

    /**
     * 今日订单列表
     */
    List<SaleOrderVO> getTodayOrders();

    /**
     * 销售统计详情: 状态汇总 + 近7日趋势
     */
    SalesDetailVO getSalesDetail();
}
