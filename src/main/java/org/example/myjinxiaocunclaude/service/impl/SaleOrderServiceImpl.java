package org.example.myjinxiaocunclaude.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myjinxiaocunclaude.dto.OrderDTO;
import org.example.myjinxiaocunclaude.entity.Product;
import org.example.myjinxiaocunclaude.entity.SaleOrder;
import org.example.myjinxiaocunclaude.entity.SaleOrderItem;
import org.example.myjinxiaocunclaude.mapper.ProductMapper;
import org.example.myjinxiaocunclaude.mapper.SaleOrderItemMapper;
import org.example.myjinxiaocunclaude.mapper.SaleOrderMapper;
import org.example.myjinxiaocunclaude.service.SaleOrderService;
import org.example.myjinxiaocunclaude.vo.SaleOrderVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrder> implements SaleOrderService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private SaleOrderItemMapper saleOrderItemMapper;

    @Override
    public IPage<SaleOrderVO> findPage(Integer page, Integer size, String orderNo) {
        Page<SaleOrderVO> pageParam = new Page<>(page, size);
        return baseMapper.findPage(pageParam, orderNo);
    }

    @Override
    @Transactional(rollbackFor = Exception.class) // 开启事务, 任何异常都回滚
    public void createOrder(OrderDTO orderDTO) {
        // 1. 创建订单主表对象
        SaleOrder order = new SaleOrder();
        order.setOrderNo(UUID.randomUUID().toString().replace("-", "").substring(0, 16));
        order.setCustomerId(orderDTO.getCustomerId());
        order.setUserId(orderDTO.getUserId());
        order.setTotalAmount(BigDecimal.ZERO); // 先初始化为 0, 防止数据库非空校验失败
        order.setStatus(0);
        order.setCreateTime(LocalDateTime.now());

        // 插入主表 (MyBatis-Plus 会自动将生成的 ID 回填到 order 对象中)
        baseMapper.insert(order);

        BigDecimal totalAmount = BigDecimal.ZERO;

        // 2. 遍历商品列表
        for (OrderDTO.OrderItemDTO itemDTO : orderDTO.getItems()) {
            // 2.1 查询商品并检查库存
            Product product = productMapper.selectById(itemDTO.getProductId());
            if (product == null) {
                throw new RuntimeException("商品不存在: " + itemDTO.getProductId());
            }
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("库存不足: " + product.getName()); // 抛出异常, 触发事务回滚
            }

            // 2.2 扣减库存
            product.setStock(product.getStock() - itemDTO.getQuantity());
            productMapper.updateById(product);

            // 2.3 保存订单明细
            SaleOrderItem orderItem = new SaleOrderItem();
            orderItem.setOrderId(order.getId()); // 使用主表回填的 ID
            orderItem.setProductId(product.getId());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setAmount(product.getPrice().multiply(new BigDecimal(itemDTO.getQuantity())));
            // 显式赋值, 不依赖数据库默认值 (远端 MySQL 全局时区为 UTC, CURRENT_TIMESTAMP 会差 8 小时)
            orderItem.setCreateTime(LocalDateTime.now());
            saleOrderItemMapper.insert(orderItem);

            // 累加总金额
            totalAmount = totalAmount.add(orderItem.getAmount());
        }

        // 3. 更新订单主表的总金额
        order.setTotalAmount(totalAmount);
        baseMapper.updateById(order);
    }

    @Override
    public void updateStatus(Long id, Integer status) {
        SaleOrder order = new SaleOrder();
        order.setId(id);
        order.setStatus(status);
        baseMapper.updateById(order);
    }
}
