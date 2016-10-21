package com.paranora;


import com.paranora.SpringDataElasticSearch.SpringDataElasticSearchApp;
import com.paranora.Utilis.httpUtility;


import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Hello world!
 */
public class App {


    public static void main(String[] args) throws Exception {


        bulkImportSqlDataToElasticSearch("10.4.254.30\\webchat",
                "WeChatOrg",
                "itdept",
                "it.good",
                "select * from users",
                "[AttentionTime]",
                "desc",
                1,
                300,
                "http://10.4.254.30:9200",
                "wechatuser",
                "user");

//        bulkImportSqlDataToElasticSearch("10.4.19.22",
//                "scsales_dev",
//                "dev",
//                "dev.good",
//                " SELECT [UNIT]\n" +
//                        "      ,[PKID]\n" +
//                        "      ,[STANDARD_ID]\n" +
//                        "      ,[CITY_CODE]\n" +
//                        "      ,[DISTRICT_CODE]\n" +
//                        "      ,[RESBLOCK_ID]\n" +
//                        "      ,[BUILDING_ID]\n" +
//                        "      ,[BUILDING_NAME]\n" +
//                        "      ,[ESTATE_ID]\n" +
//                        "      ,[ESTATE_NAME]\n" +
//                        "      ,[TOTAL_FLOOR]\n" +
//                        "      ,[FLOOR_ALIAS]\n" +
//                        "      ,[FLOOR]\n" +
//                        "      ,[HOU_ADDR]\n" +
//                        "      ,[ROOM_NO]\n" +
//                        "      ,[MAILBOX_NO]\n" +
//                        "      ,[IS_EXCHANGE]\n" +
//                        "      ,[BEDROOM_AMOUNT]\n" +
//                        "      ,[PARLOR_AMOUNT]\n" +
//                        "      ,[COOKROOM_AMOUNT]\n" +
//                        "      ,[TOILET_AMOUNT]\n" +
//                        "      ,[BALCONY_AMOUNT]\n" +
//                        "      ,[WAREHOUSE_AMOUNT]\n" +
//                        "      ,[GARDEN_AMOUNT]\n" +
//                        "      ,[TERRACE_AMOUNT]\n" +
//                        "      ,[PROPERTY_FEE]\n" +
//                        "      ,[BUILD_SIZE]\n" +
//                        "      ,[USE_SIZE]\n" +
//                        "      ,[NET_REG_SIZE]\n" +
//                        "      ,[BUILD_END_YEAR]\n" +
//                        "      ,[BUILDING_TYPE]\n" +
//                        "      ,[PROPERTY_TYPE]\n" +
//                        "      ,[ORIENTATION]\n" +
//                        "      ,[USAGE_TYPE]\n" +
//                        "      ,[SCENE]\n" +
//                        "      ,[IS_PIVOT_FRAME]\n" +
//                        "      ,[FRAME_CODE]\n" +
//                        "      ,[FRAME_CODE_TYPE]\n" +
//                        "      ,[PARLOR_SIZE]\n" +
//                        "      ,[BEDROOM_SIZE]\n" +
//                        "      ,[SANCTUM_SIZE]\n" +
//                        "      ,[COOKROOM_SIZE]\n" +
//                        "      ,[TOILET_SIZE]\n" +
//                        "      ,[BALCONY_SIZE]\n" +
//                        "      ,[WAREHOUSE_SIZE]\n" +
//                        "      ,[GARDEN_SIZE]\n" +
//                        "      ,[TERRACE_SIZE]\n" +
//                        "      ,[IS_ANEAR_SUBWAY]\n" +
//                        "      ,[IS_SCHOOL_DISTRICT]\n" +
//                        "      ,[SCHOOL_QUOTA]\n" +
//                        "      ,[IS_ECESIS]\n" +
//                        "      ,[FITMENT_TYPE]\n" +
//                        "      ,[FITMENT_AGE]\n" +
//                        "      ,[IS_REBUILD]\n" +
//                        "      ,[CREATED_BY]\n" +
//                        "      ,[CREATED_TIME]\n" +
//                        "      ,[UPDATED_BY]\n" +
//                        "      ,[UPDATED_TIME]\n" +
//                        "      ,[AGENCY_ID]\n" +
//                        "      ,[OWENER_TIME]\n" +
//                        "      ,[IS_HIDDEN_HOUSEROOM]\n" +
//                        "      ,[IS_HIDDEN]\n" +
//                        "      ,[IS_HIDDEN_USER]\n" +
//                        "      ,[IS_HIDDEN_TIME]\n" +
//                        "      ,[IS_HIDDEN_ORGID]\n" +
//                        "  FROM [sales].[T_HM_HOUSE]",
//                "pkid",
//                "asc",
//                1,
//                2000,
//                "http://10.4.254.30:9200",
//                "agency",
//                "house");

//        bulkImportSqlDataToElasticSearch("10.4.19.22",
//                "scsales_dev",
//                "dev",
//                "dev.good",
//                "SELECT * FROM sales.T_HM_HOUSEDEL",
//                "pkid",
//                "asc",
//                1,
//                2000,
//                "http://10.4.254.30:9200",
//                "agency",
//                "housedel");

//        JestApp.test();

/*        SpringDataElasticSearchApp.test();*/

//        bulkImportSqlDataToElasticSearch("10.4.254.30\\webchat",
//                "WeChatOrg",
//                "itdept",
//                "it.good",
//                "select * from Departments",
//                "[UpdateTime]",
//                "desc",
//                1,
//                300,
//                "http://10.4.254.30:9200",
//                "wechatdepartment",
//                "department");

        System.out.println("Hello ElasticSearch!");
    }

    public static void bulkImportSqlDataToElasticSearch(String serverInstanceName,
                                                        String dataBaseName,
                                                        String loginUserName,
                                                        String loginPwd,
                                                        String sql,
                                                        String orderByFieldName,
                                                        String sort,
                                                        int pageIndex,
                                                        int pageSize,
                                                        String esServerUrl,
                                                        String esIndexName,
                                                        String esTypeName) {
//        String driverName = "com.microsoft.jdbc.sqlserver.SQLServerDriver";
        String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
        String dataBaseServiceLinkUrl = String.format("jdbc:sqlserver://%s; databaseName=%s", serverInstanceName, dataBaseName);
        String esResult="";
        String errorMsg="";
        try {
            long totalBeginTime = System.currentTimeMillis();
            Class.forName(driverName);
            Connection dataBaseConnection = DriverManager.getConnection(dataBaseServiceLinkUrl, loginUserName, loginPwd);

            int pageCount = 0;
            int totalCount = 0;

            PreparedStatement ps = dataBaseConnection.prepareStatement(String.format("select count(1) from (%s) T1",sql));
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd;
            rs.next();
            totalCount = rs.getInt(1);
            pageCount = (int) Math.ceil((double) (totalCount / pageSize));

            rs = dataBaseConnection.prepareStatement(String.format("select top 1 * from (%s) T", sql)).executeQuery();
            rsmd = rs.getMetaData();

            StringBuilder esTypeMappingRequestData = new StringBuilder();
            esTypeMappingRequestData.append("{");
            esTypeMappingRequestData.append("\"settings\":{\"number_of_shards\":5,\"number_of_replicas\":1},");
            esTypeMappingRequestData.append("\"mappings\":{");
            esTypeMappingRequestData.append(String.format("\"%s\":{", esTypeName));
            esTypeMappingRequestData.append("\"dynamic\":\"false\",");
            esTypeMappingRequestData.append("\"properties\":{");
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                esTypeMappingRequestData.append(String.format("\"%s\":%s,", rsmd.getColumnName(i), transSqlDataTypeToElasticSearchDataType(rsmd.getColumnType(i))));
//                esTypeMappingRequestData.append(String.format("\"%s\":{\"type\":\"%s\"},", rsmd.getColumnName(i), "string"));
            }
            esTypeMappingRequestData.append("\"rowIndex\":{\"type\":\"integer\"}");
            esTypeMappingRequestData.append("}}}}");

            Map<String,String> requestProperties = new HashMap<String, String>() {
                {
                    put("Content-Type", "application/json");
                    put("Accept", "application/json");
                    put("Charset", "utf-8");
                }
            };

            httpUtility.delete(String.format("%s/%s",esServerUrl,esIndexName),"");
            httpUtility.put(String.format("%s/%s", esServerUrl, esIndexName), esTypeMappingRequestData.toString(),requestProperties);

            String sqlFormat = String.format("SELECT TOP %d * FROM ( select row_number() OVER (ORDER BY %s %s) rowIndex ,* from (%s) T1) T2 WHERE T2.rowIndex>?", pageSize, orderByFieldName, sort, sql);
//            String esIndexCreateStruct = String.format("{index:{\"_index\":\"%s\",\"_type\":\"%s\",\"_id\":%%s}}", "", "");
            String esIndexCreateStruct = String.format("{index:{\"_id\":%%s}}", "", "");

            while (pageIndex < pageCount) {
                long beginTime = System.currentTimeMillis();
                ps = dataBaseConnection.prepareStatement(sqlFormat);
                ps.setInt(1, (pageIndex - 1) * pageSize);
                rs = ps.executeQuery();

                StringBuilder esBulkReqeustString = new StringBuilder();
                while (rs.next()) {
                    esBulkReqeustString.append(String.format(esIndexCreateStruct, rs.getInt(1)));
                    esBulkReqeustString.append("\n");
                    esBulkReqeustString.append(readSingleRowToJsonFromResultSet(rs, rs.getMetaData()));
                    esBulkReqeustString.append("\n");
                }
                long endTime = System.currentTimeMillis();
                System.out.print(String.format("page %d prepare data costTime : %d,", pageIndex, endTime - beginTime));

                beginTime = System.currentTimeMillis();
                errorMsg=esBulkReqeustString.toString();
                httpUtility.post(String.format("%s/%s/%s/_bulk", esServerUrl,esIndexName,esTypeName), esBulkReqeustString.toString(),null);
                endTime = System.currentTimeMillis();
                System.out.println(String.format("push data costTime : %d", pageIndex, endTime - beginTime));

                pageIndex++;
            }
            if (null != rs) {
                rs.close();
            }
            long totalEndTime = System.currentTimeMillis();
            System.out.println(String.format("all is ok cost time : %d", totalEndTime - totalBeginTime));

            System.out.println("operation successful!");  //如果连接成功 控制台输出Connection Successful!
        } catch (Exception e) {
            System.out.println(errorMsg);
            e.printStackTrace();
        }
    }

    public static String readSingleRowToJsonFromResultSet(ResultSet rs, ResultSetMetaData rsmd) {
        StringBuilder sb = new StringBuilder();
        Object valueObj=null;
        String stringValue="";
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sb.append("{");
        try {
            for (int i = 1; i <= rsmd.getColumnCount(); i++) {
                valueObj=rs.getObject(i);
                if(null==valueObj)
                {
                    continue;
                }
                switch (rsmd.getColumnType(i)){
                    case Types.DATE:
                    case Types.TIME:
                    case Types.TIMESTAMP:
                        stringValue= dateFormat.format(rs.getTimestamp(i)).toString();
                        break;
                    default:
                        stringValue=valueObj.toString().trim().replaceAll("[\\t\\n\\r]", "");
                        break;
                }
                sb.append(String.format("\"%s\":\"%s\",", rsmd.getColumnName(i), stringValue));
            }
            sb.replace(sb.length() - 1, sb.length(), "");
            sb.append("}");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return sb.toString();
    }

    public static String transSqlDataTypeToElasticSearchDataType(int type) {
        StringBuilder esType =new StringBuilder();
        esType.append("{");
        esType.append("\"type\":");
        switch (type) {
            case Types.DATE:
            case Types.TIME:
            case Types.TIMESTAMP:
                esType.append("\"date\"");
//                esType.append(",\"format\":\"strict_date_optional_time||epoch_millis\" ");
                esType.append(",\"format\":\"strict_date_optional_time||epoch_millis||yyyy-MM-dd HH:mm:ss\" ");
                esType.append(",\"null_value\": \"1900-01-01\"");
                break;
            case Types.VARCHAR:
            case Types.CHAR:
            case Types.NVARCHAR:
            case Types.LONGVARCHAR:
            case Types.LONGNVARCHAR:
                esType.append("\"string\"");
                esType.append(",\"index\":\"not_analyzed\"");
                esType.append(",\"null_value\": \"na\"");
                break;
            case Types.INTEGER:
            case Types.BIGINT:
                esType.append("\"integer\"");
                esType.append(",\"null_value\": 0");
                break;
            case Types.DECIMAL:
            case Types.DOUBLE:
                esType.append("\"double\"");
                esType.append(",\"null_value\": 0");
                break;
            case Types.FLOAT:
                esType.append("\"float\"");
                esType.append(",\"null_value\": 0");
                break;
            default:
//                esType.append("\"string\"");
//                esType.append(",\"null_value\": \"na\"");
//                esType.append(",\"index\":\"not_analyzed\"");
                break;
        }
        esType.append("}");
        return esType.toString();
    }
}