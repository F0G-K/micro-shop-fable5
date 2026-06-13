package org.example.myjinxiaocunclaude.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.myjinxiaocunclaude.entity.Product;
import org.example.myjinxiaocunclaude.vo.ProductVO;

public interface ProductService extends IService<Product> {

    /**
     * 分页查询商品（包含分类名称）
     */
    IPage<ProductVO> getProductList(Page<ProductVO> page, String name, Long categoryId);
}
