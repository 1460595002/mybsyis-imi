package cn.jinronga.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

/**
 * @ClassName MapperWrapper
 * @Author 郭金荣
 * @Date 2021/3/13 0:47
 * @Description MapperWrapper
 * @Version 1.0用于描述一个Mapper的方法的必要条件
 */
@ToString
@AllArgsConstructor
@Data
public class MapperWrapper {
    /**
     * 类型，insert|update|delete
     */
    private String type;
    /***
     *  返回值的类型
     */
    private String resultType;
    /***
     * 参数的类型
     * */
    private String paramType;
    /*** sql语句 */
    private String sql;


}
