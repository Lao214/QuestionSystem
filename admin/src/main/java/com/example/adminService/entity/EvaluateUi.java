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
 * 反馈的界面pc端
 * </p>
 *
 * @author 劳威锟
 * @since 2022-11-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="EvaluateUi对象", description="反馈的界面pc端")
public class EvaluateUi implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "模板主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "创建这个模版的人")
    private String user;

    @ApiModelProperty(value = "模版组件信息")
    private String components;

    private Date createTime;

    private Date updateTime;

    @ApiModelProperty(value = "是否发布（0：不发表，1:发表）")
    private Integer isPublish;

    @ApiModelProperty(value = "点赞数")
    private Long like;

    @ApiModelProperty(value = "收藏数")
    private Long collection;

    @ApiModelProperty(value = "类型")
    private String type;


}
