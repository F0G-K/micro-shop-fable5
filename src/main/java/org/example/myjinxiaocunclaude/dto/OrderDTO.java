package org.example.myjinxiaocunclaude.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "创建订单参数")
public class OrderDTO {

    @Schema(description = "客户ID")
    private Long customerId;

    @Schema(description = "系统用户ID (由后端从 Session 填充)")
    private Long userId;

    @Schema(description = "商品明细列表")
    private List<OrderItemDTO> items;

    @Data
    public static class OrderItemDTO {

        @Schema(description = "商品ID")
        private Long productId;

        @Schema(description = "购买数量")
        private Integer quantity;
    }
}
