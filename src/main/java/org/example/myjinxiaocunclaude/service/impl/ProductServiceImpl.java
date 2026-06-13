package org.example.myjinxiaocunclaude.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myjinxiaocunclaude.entity.Product;
import org.example.myjinxiaocunclaude.mapper.ProductMapper;
import org.example.myjinxiaocunclaude.service.ProductService;
import org.example.myjinxiaocunclaude.vo.ProductVO;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public IPage<ProductVO> getProductList(Page<ProductVO> page, String name, Long categoryId) {
        return baseMapper.selectProductList(page, name, categoryId);
    }
}
