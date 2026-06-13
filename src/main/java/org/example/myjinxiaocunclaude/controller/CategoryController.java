package org.example.myjinxiaocunclaude.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.myjinxiaocunclaude.common.Result;
import org.example.myjinxiaocunclaude.entity.Category;
import org.example.myjinxiaocunclaude.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "分类管理")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取所有分类")
    public Result<List<Category>> list() {
        // 自动追加 WHERE is_deleted = 0, 按排序值升序返回
        return Result.success(categoryService.list(new QueryWrapper<Category>().orderByAsc("sort")));
    }

    @PostMapping
    @Operation(summary = "添加分类")
    public Result<Object> add(@RequestBody Category category) {
        return categoryService.save(category) ? Result.success(null) : Result.fail("添加失败");
    }

    @PutMapping
    @Operation(summary = "更新分类")
    public Result<Object> update(@RequestBody Category category) {
        return categoryService.updateById(category) ? Result.success(null) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类")
    public Result<Object> delete(@PathVariable Long id) {
        // 逻辑删除: 自动执行 UPDATE category SET is_deleted=1 WHERE id=? AND is_deleted=0
        return categoryService.removeById(id) ? Result.success(null) : Result.fail("删除失败");
    }
}
