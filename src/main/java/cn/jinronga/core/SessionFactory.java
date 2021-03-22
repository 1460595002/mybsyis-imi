package cn.jinronga.core;

import cn.jinronga.DataSourceFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * @ClassName SessionFactory
 * @Author 郭金荣
 * @Date 2021/3/13 14:21
 * @Description SessionFactory
 * @Version 1.0
 */
public class SessionFactory {
    private DataSource dataSource;
    private Map<String, Map<String, MapperWrapper>> env = new HashMap<>(8);

    public SessionFactory(String config) {
        loadXML(config);
    }

    //打开一个会话
    public Session openSession() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return new Session(connection, env);
    }

    /**
     * 加载资源环境
     *
     * @param config 配置文件名称
     */
    public void loadXML(String config) {

        try {
            InputStream resource = SessionFactory.class.getClassLoader().getResourceAsStream(config);

            SAXReader reader = new SAXReader();
            //读取配置文件解析数据源
            Document document = reader.read(resource);

            //得到配置文件的根节点
            Element configRoot = document.getRootElement();
            //拿到配置文件中的数据源
            String dataSourceType = configRoot.element("dataSource").getTextTrim();
            dataSource = DataSourceFactory.createDataSource(dataSourceType);
            //获取所有的mapper文件
            List elements = configRoot.elements("mapper");
            List<String> mapperPaths = new ArrayList<String>();
            for (Object element : elements) {
                Element mapper = (Element) element;

                System.out.println("mapper:"+((Element) element).getText());
                mapperPaths.add(mapper.getText());
            }

            //把每一个文件读出来
            for (String mapperPath : mapperPaths) {
                Map<String, MapperWrapper> wrapper = new HashMap<>(2);
                Document documentw = reader.read(Session.class.getClassLoader().getResourceAsStream(mapperPath));
                Element root = documentw.getRootElement();
                String namespace = root.attribute("namespace").getValue();

                Iterator iterator = root.elementIterator();
                //
                while (iterator.hasNext()) {

                    Element el = (Element) iterator.next();
                    //读到标签中的name
                    String type = el.getName();
                     //读到标签中的id
                    String id = el.attribute("id").getValue();
                    String resultType = el.attribute("resultType").getValue();
                    String paramType = el.attribute("paramType").getValue();
                    //sql语句
                    String sqlStr = el.getTextTrim();
                    wrapper.put(id, new MapperWrapper(type, resultType, paramType, sqlStr));
                }
                env.put(namespace, wrapper);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
