package com.eplat.im.dao;

import com.eplat.db.dao.AbstractBaseDao;
import com.eplat.db.dao.BaseDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liulongyun on 2018/4/23.
 */
public class GroupMemberDao extends AbstractBaseDao implements BaseDao {

    /**
     * 查询组成员方法
     */
    public Map<String,String> getList(String groupid) throws Exception {
        // TODO Auto-generated method stub
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            Map<String,String> memberMap = new HashMap<>();
            StringBuffer queryGroupMembersql = new StringBuffer(" SELECT gp.groupId,gp.staffId,member.staffName FROM tb_imuser_group gp ");
            queryGroupMembersql.append(" LEFT JOIN dyn_member_staff member ON gp.staffId=member.staffId")
            .append(" WHERE groupId=? AND gp.isDel = ?");
            pstmt = this.getDBConnection().createPreparedStatement(queryGroupMembersql.toString());
            pstmt.setLong(1,new Long(groupid));
            pstmt.setLong(2,0);

            rs = pstmt.executeQuery();
            while (rs.next()){
                memberMap.put(rs.getString("staffId"),rs.getString("staffName"));

            }
            return memberMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            this.getDBConnection().closePreparedStatement(pstmt);
            this.getDBConnection().closeResultSet(rs);

        }
    }



}
