package org.example.myjinxiaocunclaude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@Schema(name = "SalesDetailVO", description = "销售统计详情")
public class SalesDetailVO {

    @Schema(description = "按订单状态的笔数与金额汇总")
    private List<StatusStat> statusStats;

    @Schema(description = "近7日销售趋势 (按天合计, 排除已取消)")
    private List<TrendPoint> trend;

    @Data
    @Schema(name = "StatusStat", description = "订单状态汇总")
    public static class StatusStat {

        @Schema(description = "状态: 0-待支付,1-已支付,2-已发货,3-已完成,4-已取消")
        private Integer status;

        @Schema(description = "订单笔数")
        private Long count;

        @Schema(description = "金额合计")
        private BigDecimal amount;
    }

    @Data
    @Schema(name = "TrendPoint", description = "单日销售额")
    public static class TrendPoint {

        @Schema(description = "日期 yyyy-MM-dd")
        private String date;

        @Schema(description = "当日销售额")
        private BigDecimal amount;
    }
}
