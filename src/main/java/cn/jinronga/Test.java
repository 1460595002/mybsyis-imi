package cn.jinronga;

import cn.jinronga.core.Session;
import cn.jinronga.core.SessionFactory;
import cn.jinronga.entity.User;
import cn.jinronga.mapper.UserMapper;

/**
 * @ClassName Test
 * @Author 郭金荣
 * @Date 2021/3/13 15:17
 * @Description Test
 * @Version 1.0
 */
public class Test {
    public static void main(String[] args) {
        SessionFactory sessionFactory = new SessionFactory("mybatis-config.xml");
        Session session = sessionFactory.openSession();
        UserMapper mapper = session.getMapper(UserMapper.class);

        mapper.saveUser(new User(1,"jinrong","123"));

    }
}
