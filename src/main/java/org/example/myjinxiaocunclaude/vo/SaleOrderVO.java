package org.example.myjinxiaocunclaude.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.myjinxiaocunclaude.entity.SaleOrderItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Schema(name = "SaleOrderVO", description = "订单列表视图对象")
public class SaleOrderVO {

    private Long id;

    private String orderNo;

    private BigDecimal totalAmount;

    @Schema(description = "状态: 0-待支付, 1-已支付, 2-已发货, 3-已完成, 4-已取消")
    private Integer status;

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "客户名称")
    private String customerName;

    @Schema(description = "操作员ID")
    private Long userId;

    @Schema(description = "操作员用户名")
    private String userName;

    @Schema(description = "订单明细列表")
    private List<SaleOrderItem> items;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;
}
