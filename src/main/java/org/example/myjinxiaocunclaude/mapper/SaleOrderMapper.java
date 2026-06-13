package org.example.myjinxiaocunclaude.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.myjinxiaocunclaude.entity.SaleOrder;
import org.example.myjinxiaocunclaude.entity.SaleOrderItem;
import org.example.myjinxiaocunclaude.vo.SaleOrderVO;
import org.example.myjinxiaocunclaude.vo.SalesDetailVO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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

    /**
     * 查询今日订单列表 (含客户名称、操作员、明细)
     */
    List<SaleOrderVO> selectTodayOrders(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

    /**
     * 按订单状态汇总笔数与金额
     */
    @Select("SELECT status, COUNT(*) AS `count`, IFNULL(SUM(total_amount), 0) AS amount " +
            "FROM sale_order WHERE is_deleted = 0 GROUP BY status ORDER BY status")
    List<SalesDetailVO.StatusStat> selectStatusStats();

    /**
     * 近 N 日销售趋势 (按天合计, 排除已取消订单)
     */
    @Select("SELECT DATE_FORMAT(create_time, '%Y-%m-%d') AS `date`, IFNULL(SUM(total_amount), 0) AS amount " +
            "FROM sale_order WHERE is_deleted = 0 AND status != 4 AND create_time >= #{start} " +
            "GROUP BY DATE_FORMAT(create_time, '%Y-%m-%d') ORDER BY `date`")
    List<SalesDetailVO.TrendPoint> selectSalesTrend(@Param("start") LocalDateTime start);
}
