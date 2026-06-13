package org.example.myjinxiaocunclaude.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("category")
@Schema(name = "Category", description = "商品分类")
public class Category {

    @TableId(type = IdType.AUTO)
    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "name", title = "分类名称")
    private String name;

    @Schema(name = "sort", title = "排序值", description = "越小越靠前")
    private Integer sort;

    @Schema(name = "description", title = "分类描述")
    private String description;

    @Schema(name = "status", title = "状态", description = "1-启用,0-禁用")
    private Integer status;

    @Schema(name = "createTime", title = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间", description = "分类信息最后修改时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

    @TableLogic
    @JsonIgnore
    private Integer isDeleted;
}
