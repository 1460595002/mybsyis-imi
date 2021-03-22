package cn.jinronga;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * @ClassName DataSourceFactory
 * @Author 郭金荣
 * @Date 2021/3/13 0:51
 * @Description DataSourceFactory
 * @Version 1.0
 * 数据源工厂
 * 简单工厂应用
 */
public class DataSourceFactory {
    public static final String DATASOURCE_TYPE_HIKARI = "hikari";
    public static final String DATASOURCE_TYPE_DRUID = "druid";

    public static DataSource createDataSource(String type) {
        DataSource dataSource = null;
        Properties properties = new Properties();
        //判断用那个数据源连接数据库
        if (DATASOURCE_TYPE_HIKARI.equals(type)) {
            try {
                properties.load(DataSourceFactory.class.getClassLoader()
                        .getResourceAsStream("hikari.properties"));

                HikariConfig hikariConfig = new HikariConfig(properties);
                dataSource = new HikariDataSource(hikariConfig);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (DATASOURCE_TYPE_DRUID.equals(type)) {
            try {
                properties.load(DataSourceFactory.class.getClassLoader()
                        .getResourceAsStream("druid.properties"));
                DruidDataSource druidDataSource = new DruidDataSource();
                druidDataSource.configFromPropety(properties);
                dataSource = druidDataSource;
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }

        return dataSource;
    }
}
