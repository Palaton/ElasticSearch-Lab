package com.paranora.Spring;

import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

/**
 * Created by yangqun on 2016/10/31.
 */
public class SpringDataSource {

    public static DataSource getMsSqlServerDataSource(String dataBaseUrl,String dataBase,String user,String pwd) {

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        dataSource.setUrl(String.format("jdbc:sqlserver:%s;databaseName=%s",dataBaseUrl,dataBase));
        dataSource.setUsername(user);
        dataSource.setPassword(pwd);
        return dataSource;
    }
}
