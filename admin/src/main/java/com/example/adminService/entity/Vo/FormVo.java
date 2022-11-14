package com.example.adminService.entity.Vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author peterlin
 * @version 1.0
 * @description: TODO
 * @date 2022/10/28 8:18 PM
 */
@Data
public class FormVo {

    private String id;

    private String title;

    private String description;

    private String values;

    private String data;

    private String scoreJSON;

    /**
     * 逻辑公式
     */
    private String formula;

}
