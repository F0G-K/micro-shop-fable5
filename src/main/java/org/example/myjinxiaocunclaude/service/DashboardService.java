package org.example.myjinxiaocunclaude.service;

import org.example.myjinxiaocunclaude.vo.DashboardVO;

public interface DashboardService {

    /**
     * 获取仪表盘统计数据 (商品总数、今日订单数、总销售额)
     */
    DashboardVO getStats();
}
