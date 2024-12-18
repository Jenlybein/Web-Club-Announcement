package com.announce.dao;

import com.announce.util.JDBCUtil;

import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 通用的数据库操作代码封装
 */
public class BaseDao {
    /**
     * 通用的增删改方法
     * @param sql 调用者要执行的sql语句
     * @param params sql语句中占位符要赋值的参数
     * @return 受影响的行数
     * @throws Exception 错误
     */
    public int executeUpdate(String sql, Object... params) throws Exception {
        // 获取数据库连接，预编译 sql 语句
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        // 为占位符复制，执行sql，接收返回结果
        if(params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]); //?的编号从1开始，不是从0开始，数组的下标是从0开始
            }
        }
        int row  = ps.executeUpdate();
        // 释放资源
        ps.close();

        //这里检查下是否开启事务，开启不关闭连接，业务方法关闭!
        //connection.getAutoCommit()为false，不要在这里回收connection，由开启事务的地方回收
        //connection.getAutoCommit()为true，正常回收连接
        //没有开启事务的话，直接回收关闭即可!
        if (conn.getAutoCommit()) {
            JDBCUtil.releaseConnection();
        }
        return row;
    }

    /**
     * 通用查询查询多个Javabean对象的方法
     * @param clazz 接收T类型的Class对象（例如Employee.class)
     */
    public <T> List<T> executeQuery(Class<T> clazz, String sql, Object... params) throws Exception {
        // 获取数据库连接，预编译 sql 语句
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        // 为占位符复制，执行sql，接收返回结果
        if(params != null && params.length > 0) {
            for (int i = 0; i < params.length; i++) {
                ps.setObject(i + 1, params[i]);
            }
        }
        // 接收结果并处理
        ResultSet rs = ps.executeQuery();
        List<T> list = new ArrayList<>();

        // 获取结果集中的元数据对象（包含：列的数量、每列的名称）
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();

        //遍历结果集ResultSet，把查询结果中的一条一条记录，变成一个一个T 对象，放到list中。
        while (rs.next()) {
            // 循环一次，代表有一行数据，通过反射创建一个对象
            T t = clazz.getDeclaredConstructor().newInstance(); //要求这个类型必须有公共的无参构造
            // 循环遍历当前行的列
            for (int i = 1; i <= columnCount; i++) {
                //通过下标获取列的值
                Object value = rs.getObject(i);

                // 处理sql中datatime 和java中Date类型不匹配问题
                if (value.getClass().toString().equals("class java.time.LocalDateTime")){
                    value = Timestamp.valueOf((LocalDateTime) value);
                }

                // 获取到列的value值，这个值就是t这个对象的某一属性
                // 获取当前列的名字 = 对象的属性名
                String fieldName = rsmd.getColumnLabel(i);
                // 通过类对象喝fieldName获取要封装的对象的属性
                Field field = clazz.getDeclaredField(fieldName);
                // 突破封装的private
                field.setAccessible(true);//这么做可以操作private的属性
                // 通过fieldName和对象的属性名来将值设入
                field.set(t,value);
            }
            list.add(t);
        }
        rs.close();
        ps.close();
        //这里检查下是否开启事务，开启不关闭连接，业务方法关闭!
        //没有开启事务的话，直接回收关闭即可!
        if (conn.getAutoCommit()) {
            JDBCUtil.releaseConnection();
        }
        return list;
    }

    /**
     * 查询单条，复用查询多条的方法
     */
    public <T> T executeQueryBean(Class<T> clazz, String sql, Object...params) throws Exception{
        List<T> list = this.executeQuery(clazz, sql, params);
        if(list!=null && !list.isEmpty()) {
            return list.get(0);
        }else{
            return null;
        }
    }

    /**
     * 公共的查询方法  返回的是单个对象
     */
    public <T> T executeQuerySingle(Class<T> clazz, String sql, Object ... args) {
        T t = null;
        Connection connection = JDBCUtil.getConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int rows = 0;
        try {
            // 准备语句对象
            preparedStatement = connection.prepareStatement(sql);
            // 设置语句上的参数
            for (int i = 0; i < args.length; i++) {
                preparedStatement.setObject(i + 1, args[i]);
            }

            // 执行 查询
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                t = (T) resultSet.getObject(1);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (null != resultSet) {
                try {
                    resultSet.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            if (null != preparedStatement) {
                try {
                    preparedStatement.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            }
            JDBCUtil.releaseConnection();
        }
        return t;
    }
}