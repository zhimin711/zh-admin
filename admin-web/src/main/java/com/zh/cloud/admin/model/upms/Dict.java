package com.zh.cloud.admin.model.upms;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据字典表
 * </p>
 *
 * @author zhimin.ma
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "mt_dict")
@ApiModel(value = "Dict对象", description = "数据字典表")
public class Dict extends Model {

    public static final DictFinder find = new DictFinder();

    public static class DictFinder extends Finder<Long, Dict> {

        /**
         * Construct using the default EbeanServer.
         */
        public DictFinder() {
            super(Dict.class);
        }

    }

    @ApiModelProperty(value = "字典主键")
    @Id
    private Long id;

    @ApiModelProperty(value = "上级id")
    private Long pid;

    @ApiModelProperty(value = "字典名称")
    private String name;

    @ApiModelProperty(value = "字典类型")
    private String code;

    @ApiModelProperty(value = "显示顺序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态（0停用 1正常）")
    private String status;

    @ApiModelProperty(value = "删除标志（0代表存在 1代表删除）")
    @JsonIgnore
    private Boolean deleted;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createAt;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateAt;

    @Transient
    private List<Dict> children;
}
