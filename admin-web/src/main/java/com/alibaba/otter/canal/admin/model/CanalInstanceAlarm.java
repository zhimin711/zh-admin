package com.alibaba.otter.canal.admin.model;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * Canal实例预警实体类
 *
 * @author zhimin 2020-07-13 下午05:12:16
 * @version 1.0.0
 */
@Entity
public class CanalInstanceAlarm extends Model {

    public static final CanalInstanceAlarmFinder find = new CanalInstanceAlarmFinder();

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(Date modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public static class CanalInstanceAlarmFinder extends Finder<Long, CanalInstanceAlarm> {

        /**
         * Construct using the default EbeanServer.
         */
        public CanalInstanceAlarmFinder(){
            super(CanalInstanceAlarm.class);
        }

    }

    @Id
    private Long         id;
    private String       name;
    private String       type;
    private String       message;
    private String       status;

    @WhenCreated
    private Date         createdTime;

    @WhenModified
    private Date         modifiedTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }
}
