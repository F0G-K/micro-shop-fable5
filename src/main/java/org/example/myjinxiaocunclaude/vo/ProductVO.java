package org.example.myjinxiaocunclaude.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(name = "ProductVO", description = "商品列表视图对象")
public class ProductVO {

    private Long id;

    private String name;

    private BigDecimal price;

    private Integer stock;

    @Schema(description = "分类ID")
    private Long categoryId;

    @Schema(description = "分类名称 (数据库中无此字段, 关联查询所得)")
    private String categoryName;

    @Schema(description = "状态: 1-上架, 0-下架")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
    private LocalDateTime createTime;
}
