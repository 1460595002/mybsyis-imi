package cn.jinronga.core;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Session
 * @Author 郭金荣
 * @Date 2021/3/13 12:50
 * @Description Session
 * @Version 1.0
 * 会话对象
 */

public class Session {
    /**
     * 每个会话持有一个连接
     */
    private Connection connection;

    private Map<String, Map<String, MapperWrapper>> env = new HashMap<>(8);

    public Session(Connection connection, Map<String, Map<String, MapperWrapper>> env) {
        this.connection = connection;
        this.env = env;
    }

    /**
     * 拿到一个包装类
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getMapper(Class<T> clazz) {
        //根据xml的内容和接口的名字实际生成方法体
        T t = (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),
                new Class[]{clazz},
                new SQLHandler(connection, clazz, env.get(clazz.getName())));

        return t;
    }

    //开启会话
    public void begin() {
        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //提交
    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //回滚
    public void rollback() {

        try {
            connection.rollback();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
