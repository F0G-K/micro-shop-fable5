package org.example.myjinxiaocunclaude.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.example.myjinxiaocunclaude.entity.SaleOrder;
import org.example.myjinxiaocunclaude.mapper.ProductMapper;
import org.example.myjinxiaocunclaude.mapper.SaleOrderMapper;
import org.example.myjinxiaocunclaude.service.DashboardService;
import org.example.myjinxiaocunclaude.vo.DashboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
}
