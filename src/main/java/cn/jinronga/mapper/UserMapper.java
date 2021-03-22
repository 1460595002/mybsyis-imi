package cn.jinronga.mapper;

import cn.jinronga.entity.User;

import java.util.List;

/**
 * @ClassName UserMapper
 * @Author 郭金荣
 * @Date 2021/3/13 0:40
 * @Description UserMapper
 * @Version 1.0
 */
public interface UserMapper {
    Integer saveUser(User user);

    List<User> findUser(Integer id);
}
