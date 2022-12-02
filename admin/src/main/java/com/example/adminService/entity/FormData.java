package com.example.adminService.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 劳威锟
 * @since 2022-12-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="FormData对象", description="")
public class FormData implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "数据id")
      @TableId(value = "id", type = IdType.ID_WORKER)
    private String id;

    @ApiModelProperty(value = "表单id键")
    private String formId;

    @ApiModelProperty(value = "表单数据")
    private String data;

    private String bg;

    @ApiModelProperty(value = "个性签名")
    private String sign;

    @ApiModelProperty(value = "来源")
    private String source;

    private String bu;

    @ApiModelProperty(value = "提交设备")
    private String submitOs;

    @ApiModelProperty(value = "工号")
    private String jobNo;

    @ApiModelProperty(value = "区域")
    private String area;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "反馈结果集")
    private String evaluateResult;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "提交地址")
    private String submitAddress;

    @ApiModelProperty(value = "提交ip")
    private String submitIp;

    @ApiModelProperty(value = "提交时间（秒）")
    private Long completeTime;

    @ApiModelProperty(value = "年龄范围")
    private String ageScope;

    @ApiModelProperty(value = "管理职")
    private String management;

    private String grade;

    private String gradeDepart;

    @ApiModelProperty(value = "真名")
    private String realname;

    @ApiModelProperty(value = "单位")
    private String unit;

    @ApiModelProperty(value = "学历")
    private String education;

    private String hrtype;

    @ApiModelProperty(value = "创建人")
    private String createBy;


}
