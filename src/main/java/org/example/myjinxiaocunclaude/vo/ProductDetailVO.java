package org.example.myjinxiaocunclaude.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.myjinxiaocunclaude.entity.Product;

import java.util.List;

@Data
@Schema(name = "ProductDetailVO", description = "商品统计详情")
public class ProductDetailVO {

    @Schema(description = "库存预警商品列表 (stock < 10, 按库存升序)")
    private List<Product> lowStock;

    @Schema(description = "各分类商品数量分布")
    private List<CategoryStat> categoryStats;

    @Data
    @Schema(name = "CategoryStat", description = "分类商品数统计")
    public static class CategoryStat {

        @Schema(description = "分类名称")
        private String name;

        @Schema(description = "商品数量")
        private Long count;
    }
}
