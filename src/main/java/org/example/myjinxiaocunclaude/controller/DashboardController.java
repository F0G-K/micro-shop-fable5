package org.example.myjinxiaocunclaude.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.myjinxiaocunclaude.common.Result;
import org.example.myjinxiaocunclaude.service.DashboardService;
import org.example.myjinxiaocunclaude.vo.DashboardVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "仪表盘")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/stats")
    @Operation(summary = "获取统计数据")
    public Result<DashboardVO> getStats() {
        return Result.success(dashboardService.getStats());
    }
}
