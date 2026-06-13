package org.example.myjinxiaocunclaude.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.myjinxiaocunclaude.dto.OrderDTO;
import org.example.myjinxiaocunclaude.entity.SaleOrder;
import org.example.myjinxiaocunclaude.vo.SaleOrderVO;

public interface SaleOrderService extends IService<SaleOrder> {

    /**
     * 分页查询订单列表 (含客户名称、操作员、订单明细)
     */
    IPage<SaleOrderVO> findPage(Integer page, Integer size, String orderNo);

    /**
     * 创建订单: 插入主表/明细表并扣减库存, 整体在一个事务中
     */
    void createOrder(OrderDTO orderDTO);

    /**
     * 更新订单状态
     */
    void updateStatus(Long id, Integer status);
}
