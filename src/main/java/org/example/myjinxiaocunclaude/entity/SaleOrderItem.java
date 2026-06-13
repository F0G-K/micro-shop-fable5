package org.example.myjinxiaocunclaude.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("sale_order_item")
@Schema(name = "SaleOrderItem", description = "销售订单明细")
public class SaleOrderItem {

    @TableId(type = IdType.AUTO)
    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "orderId", title = "订单ID", description = "关联 sale_order.id")
    private Long orderId;

    @Schema(name = "productId", title = "商品ID", description = "关联 product.id")
    private Long productId;

    @Schema(name = "quantity", title = "购买数量")
    private Integer quantity;

    @Schema(name = "price", title = "下单时商品单价", description = "快照")
    private BigDecimal price;

    @Schema(name = "amount", title = "小计金额", description = "price * quantity")
    private BigDecimal amount;

    @Schema(name = "createTime", title = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

    @TableLogic
    @JsonIgnore
    private Integer isDeleted;
}
