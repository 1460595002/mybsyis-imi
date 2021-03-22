package cn.jinronga.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName User
 * @Author 郭金荣
 * @Date 2021/3/13 0:39
 * @Description User
 * @Version 1.0
 */
@Data
@AllArgsConstructor
public class User implements Serializable {
    private Integer id;
    private String userName;
    private String passWord;
}
