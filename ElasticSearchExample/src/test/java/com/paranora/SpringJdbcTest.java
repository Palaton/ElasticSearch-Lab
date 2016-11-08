package com.paranora;

import com.paranora.Spring.SpringDataSource;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.logging.impl.WeakHashtable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yangqun on 2016/10/31.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:/spring-context-test.xml")
public class SpringJdbcTest {

    @Test
    public void test(){
        DataSource dataSource = SpringDataSource.getMsSqlServerDataSource("//10.4.254.30\\webchat","wechatorg","itdept","it.good");
        NamedParameterJdbcTemplate namedParameterJdbcTemplate=new NamedParameterJdbcTemplate(dataSource);

        String sql = "SELECT * FROM users WHERE userID = :userID";

        Map<String,Object> parameter=new HashMap<String,Object>(){{
            put("userID","yangqun");
        }};
        SqlParameterSource namedParameters = new MapSqlParameterSource(parameter);

        Object obj =namedParameterJdbcTemplate.queryForMap(sql,namedParameters);

        StringBuilder sb=new StringBuilder();


        System.out.println("");
    }
}
