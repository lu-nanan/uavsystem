package com.hnu.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author hrui
 * @date 2023/9/8 14:55
 */
public class SqlSessionUtil {

    //工具类的构造方法一般都是私有化
    //方法都是静态的
    //为了防止new对象,构造方法私有化
    /*private SqlSessionUtil(){
 
    }
    private static SqlSessionFactory sqlSessionFactory;
 
    //类加载时候执行
    //SqlSessionUtil工具类在被加载的时候,解析mybatis-config1.xml.创建sqlSessionFactory对象
    static{
        try {
            //SqlSessionFactoryBuilder sqlSessionFactoryBuilder=new SqlSessionFactoryBuilder();
            //下面这么写的原因是SqlSessionFactoryBuilder就是为了创建sqlSessionFactory而来的,使用完后,就不需要,都不需要创建个局部变量
            //一个sqlSessionFactory对应一个数据库
            sqlSessionFactory= new SqlSessionFactoryBuilder().build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    //全局的 服务器级别的,一个服务器当中定义一个即可
    private static ThreadLocal<SqlSession> local=new ThreadLocal<>();
 
    //获取会话对象 返回会话对象
    public static SqlSession openSession(){
        SqlSession sqlSession=local.get();
        if(sqlSession==null){
            sqlSession = sqlSessionFactory.openSession();
            local.set(sqlSession);
        }
 
        return sqlSession;
    }
 
    //提供一个关闭的方法
    public static void close(SqlSession sqlSession){
        if(sqlSession!=null){
            //因为核心配置文件中配置POOLED  这里关闭是交还给连接池
            sqlSession.close();
            //注意移除SqlSession对象和当前线程的绑定关系
            //因为Tomcat服务器支持线程池 比如说t1线程用完了,close交还给连接池了,这个sqlSession属于不可用的状态,你没有remove出去 如果t2线程拿到了,那么这个sqlSession不可用
            local.remove();
        }
    }

     */
    /**
     * 获取SqlSession对象
     */
    /*public static SqlSession createSqlSesstion(){
        SqlSessionFactory sqlSessionFactory=null;
        InputStream inputStream=null;
        SqlSession session=null;
        try {
            String resource="mybatis-config.xml";
            inputStream=Resources.getResourceAsStream(resource);
            sqlSessionFactory=new SqlSessionFactoryBuilder().build(inputStream);
            session = sqlSessionFactory.openSession();
            return session;
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        }
    }
    public static void main(String[] arg){
        System.out.println(createSqlSesstion());
    }

     */
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * 类加载时初始化sqlSessionFactory对象
     */
    static {
        try {
            SqlSessionFactoryBuilder sqlSessionFactoryBuilder = new SqlSessionFactoryBuilder();
            sqlSessionFactory = sqlSessionFactoryBuilder.build(Resources.getResourceAsStream("mybatis-config.xml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 每调用一次openSession()可获取一个新的会话，该会话支持自动提交。
     *
     * @return 新的会话对象
     */
    public static SqlSession openSession() {
        return sqlSessionFactory.openSession(true);
    }
}