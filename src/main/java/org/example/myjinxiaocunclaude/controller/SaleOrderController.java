package org.example.myjinxiaocunclaude.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import org.example.myjinxiaocunclaude.common.Result;
import org.example.myjinxiaocunclaude.dto.OrderDTO;
import org.example.myjinxiaocunclaude.entity.SysUser;
import org.example.myjinxiaocunclaude.service.SaleOrderService;
import org.example.myjinxiaocunclaude.vo.SaleOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "订单管理")
public class SaleOrderController {

    @Autowired
    private SaleOrderService saleOrderService;

    @GetMapping
    @Operation(summary = "获取订单列表")
    public Result<IPage<SaleOrderVO>> findPage(@RequestParam(defaultValue = "1") Integer page,
                                               @RequestParam(defaultValue = "10") Integer size,
                                               String orderNo) {
        return Result.success(saleOrderService.findPage(page, size, orderNo));
    }

    @PostMapping
    @Operation(summary = "创建订单")
    public Result<Object> createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {
        SysUser sysUser = (SysUser) session.getAttribute("user");
        orderDTO.setUserId(sysUser.getId());
        saleOrderService.createOrder(orderDTO);
        return Result.success("订单创建成功", null);
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "更新订单状态")
    public Result<Object> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        saleOrderService.updateStatus(id, status);
        return Result.success(null);
    }
}
