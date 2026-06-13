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
@TableName("customer")
@Schema(name = "Customer", description = "客户")
public class Customer {

    @TableId(type = IdType.AUTO)
    @Schema(name = "id", title = "主键")
    private Long id;

    @Schema(name = "name", title = "客户名称")
    private String name;

    @Schema(name = "contact", title = "联系人")
    private String contact;

    @Schema(name = "phone", title = "联系电话")
    private String phone;

    @Schema(name = "email", title = "电子邮箱")
    private String email;

    @Schema(name = "address", title = "联系地址")
    private String address;

    @Schema(name = "createTime", title = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

    @TableLogic
    @JsonIgnore
    private Integer isDeleted;
}
