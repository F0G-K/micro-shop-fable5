package org.example.myjinxiaocunclaude.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myjinxiaocunclaude.entity.Category;
import org.example.myjinxiaocunclaude.mapper.CategoryMapper;
import org.example.myjinxiaocunclaude.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
