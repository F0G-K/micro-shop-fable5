package org.example.myjinxiaocunclaude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(name = "DashboardVO", description = "仪表盘统计数据")
public class DashboardVO {

    @Schema(description = "商品总数")
    private Long productCount;

    @Schema(description = "今日订单数")
    private Long todayOrderCount;

    @Schema(description = "总销售额")
    private BigDecimal totalSales;
}
