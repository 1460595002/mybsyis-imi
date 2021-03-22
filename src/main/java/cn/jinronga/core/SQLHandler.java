package cn.jinronga.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Map;

/**
 * @ClassName SQLHandler
 * @Author 郭金荣
 * @Date 2021/3/13 13:07
 * @Description SQLHandler
 * @Version 1.0
 */
public class SQLHandler implements InvocationHandler {
    /**
     * 需要传入一个连接
     */
    private Connection connection;
    /**
     * 需要传入一个dao得类型
     */
    private Class clazz;
    /**
     * 需传入一个独立的环境
     */
    private Map<String, MapperWrapper> env;

    private static final String SQL_TYPE_INSERT = "insert";
    private static final String SQL_TYPE_DELETE = "delete";
    private static final String SQL_TYPE_SELECT = "select";

    public SQLHandler(Connection connection, Class clazz, Map<String, MapperWrapper> env) {
        this.connection = connection;
        this.clazz = clazz;
        this.env = env;
    }

    /**
     * 生成代理类对象
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     * @throws Throwable
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //拿到包装
        MapperWrapper mapperWrapper = env.get(method.getName());

        PreparedStatement statement = connection.prepareStatement(mapperWrapper.getSql());

        //对每种sql语句进行独立得操作
        if (SQL_TYPE_INSERT.equals(mapperWrapper.getType())) {
            //暂定传入一个对象   传入insert的对象
            Class<?> clazz = args[0].getClass();
            Field[] fields = clazz.getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                fields[i].setAccessible(true);//fields插入的数据
                statement.setObject(i + 1, fields[i].get(args[0]));
            }
            return statement.executeUpdate();
        } else if (SQL_TYPE_DELETE.equals(mapperWrapper.getType())) {
            for (int i = 0; i < args.length; i++) {
                statement.setObject(i + 1, args[i]);
            }
            return statement.executeUpdate();
        } else if (SQL_TYPE_SELECT.equals(mapperWrapper.getType())) {
            for (int i = 0; i < args.length; i++) {
                //args[i]为select 的参数条件
                statement.setObject(i + 1, args[i]);
            }
            ResultSet result = statement.executeQuery();
            //将数据库 检索的数据遍历
            ArrayList<Object> list = new ArrayList<>();
            while (result.next()) {
                Class<?> clazz = Class.forName(mapperWrapper.getResultType());
                Object o = clazz.newInstance();
                Field[] fields = clazz.getDeclaredFields();
                for (int i = 0; i < fields.length; i++) {
                    fields[i].setAccessible(true);
                    fields[i].set(o, result.getObject(fields[i].getName()));
                }
                list.add(o);
            }
            return list;
        }
        return null;
    }
}
