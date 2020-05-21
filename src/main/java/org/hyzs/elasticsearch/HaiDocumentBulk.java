package org.hyzs.elasticsearch;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.common.xcontent.XContentType;
import org.hyzs.config.ElasticSearchConnector;
import org.hyzs.utils.ElasticSearchUtils;
import org.hyzs.utils.MySQLUtil;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @program: esIndex
 * @description:
 * @author: hux
 * @create: 2020-04-02 13:42
 **/
public class HaiDocumentBulk {
    private static ElasticSearchUtils elasticSearchUtils;

    private static void createIndex () throws SQLException, UnknownHostException {
        ElasticSearchConnector elasticSearchConnector=new ElasticSearchConnector();

        Connection connection = MySQLUtil.getConnection();
        Statement statement = connection.createStatement();
        int offset = 0;
        int i=offset;
        while (i< 142001){
            ResultSet resultSet = statement.executeQuery("SELECT doc_id,content,judgement_date FROM judicial_document FORCE INDEX ( key_trial_time ) WHERE trial_procedures = '0201' AND create_time >=(SELECT create_time FROM judicial_document WHERE trial_procedures = '0201' ORDER BY create_time LIMIT " + offset + ",1 ) ORDER BY create_time LIMIT 0,1000");

            if (resultSet.next()) {
                String text = resultSet.getString("content").replace("\r", "");
                String docId = resultSet.getString("doc_id");
                String judgementDate = resultSet.getString("judgement_date");
                JSONObject json=new JSONObject();
                json.put("document",text);
                json.put("doc_id",docId);
                json.put("judgementDate",judgementDate);
                elasticSearchUtils.writeBulk(json);
                if (i % 1000== 0&&i!=0) {
                    elasticSearchUtils.excuteBulk();
                    System.out.println("提交1000个");
                }
                System.out.println(docId+" 执行完成 第"+i+"个");
                i++;
            }else {
                if (i % 1000 == 0) {
                    elasticSearchUtils.excuteBulk();
                    System.out.println("提交1000个");
                }
                System.out.println("超出啦");
                i++;
            }
        }
        offset = offset + 1000;
    }

    public static void main(String[] args) throws SQLException, UnknownHostException {
        createIndex();
    }
}

