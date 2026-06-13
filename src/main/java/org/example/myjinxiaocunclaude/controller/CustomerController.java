package org.example.myjinxiaocunclaude.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.myjinxiaocunclaude.common.Result;
import org.example.myjinxiaocunclaude.entity.Customer;
import org.example.myjinxiaocunclaude.service.CustomerService;
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
@RequestMapping("/api/customers")
@Tag(name = "客户管理")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @Operation(summary = "获取客户列表")
    public Result<List<Customer>> list() {
        return Result.success(customerService.list());
    }

    @PostMapping
    @Operation(summary = "添加客户")
    public Result<Object> add(@RequestBody Customer customer) {
        return customerService.save(customer) ? Result.success(null) : Result.fail("添加失败");
    }

    @PutMapping
    @Operation(summary = "更新客户")
    public Result<Object> update(@RequestBody Customer customer) {
        return customerService.updateById(customer) ? Result.success(null) : Result.fail("更新失败");
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除客户")
    public Result<Object> delete(@PathVariable Long id) {
        return customerService.removeById(id) ? Result.success(null) : Result.fail("删除失败");
    }
}
