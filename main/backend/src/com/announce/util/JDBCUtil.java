package com.announce.util;

import com.alibaba.druid.pool.DruidDataSourceFactory;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

public class JDBCUtil {

    // 使用 ThreadLocal 确保每个线程拥有独立的数据库连接
    private static final ThreadLocal<Connection> threadLocal = new ThreadLocal<>();

    // 数据源对象，用于获取连接池实例
    private static final DataSource dataSource;

    // 静态代码块，在类加载时初始化数据源
    static {
        // 创建 Properties 对象用于读取配置文件
        Properties properties = new Properties();
        // 获取 db.properties 配置文件的输入流
        InputStream resourceAsStream = JDBCUtil.class.getClassLoader().getResourceAsStream("db.properties");

        try {
            properties.load(resourceAsStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            // 使用 Druid 连接池工厂根据配置文件创建数据源
            dataSource = DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    /**
     * 向外提供数据库连接的方法
     * 如果当前线程没有连接，则从连接池获取一个连接
     */
    public static Connection getConnection() {
        // 获取当前线程的数据库连接
        Connection connection = threadLocal.get();

        // 如果当前线程没有连接，则从连接池获取
        if (null == connection) {
            try {
                connection = dataSource.getConnection();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            threadLocal.set(connection);
        }

        return connection;
    }

    /**
     * 归还数据库连接的方法
     * 将当前线程的连接从 ThreadLocal 移除，并返回连接池
     */
    public static void releaseConnection() {
        Connection connection = threadLocal.get();

        if (null != connection) {
            threadLocal.remove();
            try {
                // 设置连接为自动提交模式
                connection.setAutoCommit(true);
                connection.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
