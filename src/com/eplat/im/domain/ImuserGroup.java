package com.eplat.im.domain;

import java.io.Serializable;
import java.util.Date;

public class ImuserGroup extends ImuserGroupKey implements Serializable {
    private Date jointime;

    private Integer membersort;

    private Long inviterid;

    private Integer isgroupowner;//是否是群主

    private Long ownercreater;//如果这个用户是群主，是谁设置他为群主

    private String remark;




    public Date getJointime() {
        return jointime;
    }

    public void setJointime(Date jointime) {
        this.jointime = jointime;
    }

    public Integer getMembersort() {
        return membersort;
    }

    public void setMembersort(Integer membersort) {
        this.membersort = membersort;
    }


    public Long getInviterid() {
        return inviterid;
    }

    public void setInviterid(Long inviterid) {
        this.inviterid = inviterid;
    }

    public Integer getIsgroupowner() {
        return isgroupowner;
    }

    public void setIsgroupowner(Integer isgroupowner) {
        this.isgroupowner = isgroupowner;
    }

    public Long getOwnercreater() {
        return ownercreater;
    }

    public void setOwnercreater(Long ownercreater) {
        this.ownercreater = ownercreater;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}