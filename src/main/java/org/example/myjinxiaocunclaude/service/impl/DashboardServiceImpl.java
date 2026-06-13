package org.example.myjinxiaocunclaude.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.myjinxiaocunclaude.entity.Product;
import org.example.myjinxiaocunclaude.entity.SaleOrder;
import org.example.myjinxiaocunclaude.mapper.ProductMapper;
import org.example.myjinxiaocunclaude.mapper.SaleOrderMapper;
import org.example.myjinxiaocunclaude.service.DashboardService;
import org.example.myjinxiaocunclaude.vo.DashboardVO;
import org.example.myjinxiaocunclaude.vo.ProductDetailVO;
import org.example.myjinxiaocunclaude.vo.SaleOrderVO;
import org.example.myjinxiaocunclaude.vo.SalesDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private SaleOrderMapper saleOrderMapper;

    @Override
    public DashboardVO getStats() {
        DashboardVO vo = new DashboardVO();

        // 1. 统计商品总数 (MyBatis-Plus 会自动过滤 is_deleted=1 的数据)
        vo.setProductCount(productMapper.selectCount(null));

        // 2. 统计今日订单数
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        vo.setTodayOrderCount(saleOrderMapper.selectCount(new QueryWrapper<SaleOrder>()
                .between("create_time", start, end)));

        // 3. 统计总销售额 (自定义 SQL, 排除已取消订单)
        vo.setTotalSales(saleOrderMapper.selectTotalSales());

        return vo;
    }

    @Override
    public ProductDetailVO getProductDetail() {
        ProductDetailVO vo = new ProductDetailVO();
        // 库存预警: stock < 10, 按库存升序
        vo.setLowStock(productMapper.selectList(
                new QueryWrapper<Product>().lt("stock", 10).orderByAsc("stock")));
        vo.setCategoryStats(productMapper.selectCategoryStats());
        return vo;
    }

    @Override
    public List<SaleOrderVO> getTodayOrders() {
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        return saleOrderMapper.selectTodayOrders(start, end);
    }

    @Override
    public SalesDetailVO getSalesDetail() {
        SalesDetailVO vo = new SalesDetailVO();
        vo.setStatusStats(saleOrderMapper.selectStatusStats());

        // 近7日趋势: 数据库只返回有订单的日期, 此处补齐空缺日为 0
        LocalDate today = LocalDate.now();
        LocalDateTime start = LocalDateTime.of(today.minusDays(6), LocalTime.MIN);
        Map<String, BigDecimal> amountByDate = saleOrderMapper.selectSalesTrend(start).stream()
                .collect(Collectors.toMap(SalesDetailVO.TrendPoint::getDate,
                        SalesDetailVO.TrendPoint::getAmount, (a, b) -> a));

        List<SalesDetailVO.TrendPoint> trend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            String date = today.minusDays(i).toString();
            SalesDetailVO.TrendPoint point = new SalesDetailVO.TrendPoint();
            point.setDate(date);
            point.setAmount(amountByDate.getOrDefault(date, BigDecimal.ZERO));
            trend.add(point);
        }
        vo.setTrend(trend);
        return vo;
    }
}
