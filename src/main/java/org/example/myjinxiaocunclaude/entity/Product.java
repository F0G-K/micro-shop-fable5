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
@TableName("product")
@Schema(name = "Product", description = "商品")
public class Product {

    @TableId(type = IdType.AUTO)
    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "categoryId", title = "分类ID", description = "关联 category.id")
    private Long categoryId;

    @Schema(name = "name", title = "商品名称")
    private String name;

    @Schema(name = "price", title = "销售单价")
    private BigDecimal price;

    @Schema(name = "stock", title = "当前库存数量")
    private Integer stock;

    @Schema(name = "status", title = "状态", description = "1-上架,0-下架")
    private Integer status;

    @Schema(name = "createTime", title = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

    @TableLogic
    @JsonIgnore
    private Integer isDeleted;
}
