package org.example.myjinxiaocunclaude.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.myjinxiaocunclaude.entity.Product;
import org.example.myjinxiaocunclaude.vo.ProductDetailVO;
import org.example.myjinxiaocunclaude.vo.ProductVO;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapper<Product> {

    /**
     * 统计各分类下的商品数量 (含商品数为 0 的分类)
     */
    @Select("SELECT c.name AS name, COUNT(p.id) AS `count` " +
            "FROM category c LEFT JOIN product p ON p.category_id = c.id AND p.is_deleted = 0 " +
            "WHERE c.is_deleted = 0 GROUP BY c.id, c.name ORDER BY `count` DESC")
    List<ProductDetailVO.CategoryStat> selectCategoryStats();

    /**
     * 分页查询商品列表（包含分类名称）
     *
     * @param page       分页参数
     * @param name       商品名称 (可选, 模糊匹配)
     * @param categoryId 分类ID (可选)
     * @return 分页结果
     */
    IPage<ProductVO> selectProductList(Page<ProductVO> page,
                                       @Param("name") String name,
                                       @Param("categoryId") Long categoryId);
}
