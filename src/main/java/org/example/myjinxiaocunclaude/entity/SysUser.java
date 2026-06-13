package org.example.myjinxiaocunclaude.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("sys_user")
@Schema(name = "SysUser", description = "系统用户信息")
public class SysUser {

    @TableId(type = IdType.AUTO)
    @Schema(name = "id", title = "主键", description = "自动生成用户唯一标识")
    private Long id;

    @Schema(name = "username", title = "用户名")
    private String username;

    // 仅接收请求参数,登录响应不回传密码
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Schema(name = "password", title = "密码")
    private String password;

    @Schema(name = "nickname", title = "昵称")
    private String nickname;

    @Schema(name = "avatar", title = "头像URL")
    private String avatar;

    @Schema(name = "email", title = "邮箱")
    private String email;

    @Schema(name = "createTime", title = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime createTime;

    @Schema(name = "updateTime", title = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime updateTime;

    @TableLogic
    @JsonIgnore
    private Integer isDeleted;
}
