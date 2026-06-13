package org.example.myjinxiaocunclaude.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.myjinxiaocunclaude.entity.SaleOrder;
import org.example.myjinxiaocunclaude.entity.SaleOrderItem;
import org.example.myjinxiaocunclaude.vo.SaleOrderVO;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface SaleOrderMapper extends BaseMapper<SaleOrder> {

    /**
     * 统计所有有效订单的总金额
     * 注意：使用 @Select 手写 SQL 时, MyBatis-Plus 的自动逻辑删除过滤不会生效, 必须手动添加 WHERE is_deleted = 0;
     * 状态枚举以数据库为准: 4-已取消, 统计时排除
     *
     * @return 总销售额
     */
    @Select("SELECT IFNULL(SUM(total_amount), 0) FROM sale_order WHERE is_deleted = 0 AND status != 4")
    BigDecimal selectTotalSales();

    /**
     * 分页查询订单列表 (包含客户名称、操作员)
     */
    IPage<SaleOrderVO> findPage(IPage<SaleOrderVO> pageParam, @Param("orderNo") String orderNo);

    /**
     * 根据订单id查询订单项列表
     *
     * @param orderId 订单id
     * @return 订单项列表
     */
    List<SaleOrderItem> selectItemByOrderId(Long orderId);
}
