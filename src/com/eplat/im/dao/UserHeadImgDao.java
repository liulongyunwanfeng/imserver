package com.eplat.im.dao;

import com.eplat.db.dao.AbstractBaseDao;
import com.eplat.db.dao.BaseDao;
import com.eplat.im.CommonConstants;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

/**
 * 核心组件获取头像工具类
 */
public class UserHeadImgDao extends AbstractBaseDao implements BaseDao {

   private static final String HRADIMGURLFIX =  CommonConstants.HRADIMGURLFIX;


    /**
     * 根据用户Id查询快信用户头像
     * @return
     * @throws Exception
     */
    public Map<String,String> getHeadImgByUserId(Long staffid) throws Exception{

        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Map<String,String> userHeadImgAndImstatusInof = new HashMap<>();
        String imStatus = "正常班";
        String fileNameWithPath = CommonConstants.HEADIMG_DEFAULT;

        try {
            String sql = " SELECT staffId,imHeadImg,imstatus FROM tb_imuser WHERE staffId=? ";
            pstmt = this.getDBConnection().createPreparedStatement(sql);
            pstmt.setLong(1,staffid);
            rs = pstmt.executeQuery();
            while (rs.next()){
                if(null!=rs.getString("imHeadImg")){
                    fileNameWithPath =  rs.getString("imHeadImg");
                }
                if(null!=rs.getString("imstatus")){
                    imStatus = rs.getString("imstatus");
                }

            }
        } catch (Exception e) {
            throw e;
        }
        userHeadImgAndImstatusInof.put("imheadimg",HRADIMGURLFIX+fileNameWithPath);
        userHeadImgAndImstatusInof.put("imstatus",imStatus);

        return  userHeadImgAndImstatusInof;
    }




}
