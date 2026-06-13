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
@TableName("sale_order")
@Schema(name = "SaleOrder", description = "销售订单主表")
public class SaleOrder {

    @TableId(type = IdType.AUTO)
    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "orderNo", title = "订单编号", description = "唯一")
    private String orderNo;

    @Schema(name = "customerId", title = "客户ID", description = "关联 customer.id")
    private Long customerId;

    @Schema(name = "userId", title = "操作员ID", description = "关联 sys_user.id")
    private Long userId;

    @Schema(name = "totalAmount", title = "订单总金额")
    private BigDecimal totalAmount;

    @Schema(name = "status", title = "状态", description = "0-待支付,1-已支付,2-已发货,3-已完成,4-已取消")
    private Integer status;

    @Schema(name = "remark", title = "备注")
    private String remark;

    @Schema(name = "createTime", title = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

    @TableLogic
    @JsonIgnore
    private Integer isDeleted;
}
