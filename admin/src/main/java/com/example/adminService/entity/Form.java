package com.example.adminService.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author 劳威锟
 * @since 2022-10-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="FoxForm对象", description="权限")
public class Form implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "编号")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "问卷创建者")
    private String userId;

    @ApiModelProperty(value = "问卷类型（0：不反馈 1：反馈）")
    private Integer type;

    @ApiModelProperty(value = "状态（0:未发布 1:发布）")
    private Integer status;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "部门id")
    private String departmentId;

    @ApiModelProperty(value = "反馈逻辑")
    private String evaluateLogic;

    @ApiModelProperty(value = "反馈pc界面")
    private Integer evaluateWeb;

    @ApiModelProperty(value = "反馈移动界面")
    private Integer evaluatePhone;

    @ApiModelProperty(value = "描述")
    private String description;
}
