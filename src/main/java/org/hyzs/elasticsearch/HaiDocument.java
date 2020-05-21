package org.hyzs.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import org.hyzs.utils.ElasticSearchUtils;
import org.hyzs.utils.MySQLUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

/**
 * @program: esIndex
 * @description:
 * @author: hux
 * @create: 2020-04-01 12:28
 **/
public class HaiDocument {
    private static final Logger logger = LoggerFactory.getLogger(HaiDocument.class);
    private static ElasticSearchUtils elasticSearchUtils;



    private static void createIndex() throws SQLException, UnknownHostException, InterruptedException {
        Connection connection = MySQLUtil.getConnection();
        Statement statement = connection.createStatement();
        String text = "";
        String docId = "";
        String judgementDate = "";
        int offset = 0;
        int i = offset;
        while (i <=141111) {
            ResultSet resultSet = statement.executeQuery("SELECT judicial_id,content,judgement_date FROM judicial_document FORCE INDEX ( key_trial_time ) WHERE trial_procedures = '0201' AND create_time >=(SELECT create_time FROM judicial_document WHERE trial_procedures = '0201' ORDER BY create_time LIMIT " + offset + ",1 ) ORDER BY create_time LIMIT 0,1000");
            while (resultSet.next()) {
                if (resultSet.getString("content") != null) {
                    text = resultSet.getString("content").replace("\r", "");
                }
                docId = resultSet.getString("judicial_id");
                judgementDate = resultSet.getString("judgement_date");

                JSONObject json = new JSONObject();
                json.put("document", text);
                json.put("doc_id", docId);
                json.put("judgementDate", judgementDate);
                elasticSearchUtils.write(json, docId);
                System.out.println(docId + " 执行完成 第" + i + "个");
                i++;
                TimeUnit.MILLISECONDS.sleep(50);
            }
            offset = offset + 1000;
        }

    }

    public static void main(String[] args) throws SQLException, UnknownHostException, InterruptedException {
        createIndex();

        TimeUnit.HOURS.sleep(1);
    }
}
