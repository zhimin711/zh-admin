package com.zh.cloud.admin.model.upms;

import com.zh.cloud.admin.model.Model;
import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * <p>
 * 职位信息表
 * </p>
 *
 * @author zhimin.ma
 * @since 2020-07-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "st_position")
@ApiModel(value="Position对象", description="职位信息表")
public class Position extends Model {
    public static final PositionFinder find = new PositionFinder();

    public static class PositionFinder extends Finder<Long, Position> {

        /**
         * Construct using the default EbeanServer.
         */
        public PositionFinder() {
            super(Position.class);
        }

    }

    @ApiModelProperty(value = "ID")
    @Id
    private Long id;

    @ApiModelProperty(value = "职位编码")
    private String code;

    @ApiModelProperty(value = "职位名称")
    private String name;

    @ApiModelProperty(value = "显示顺序")
    private Integer sort;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "状态（0正常 1停用）")
    private String status;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    @WhenCreated
    private Date createAt;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    @WhenModified
    private Date updateAt;


}
