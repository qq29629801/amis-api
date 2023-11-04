package com.yuyaogc.lowcode.engine.plugin.activerecord;

import com.yuyaogc.lowcode.engine.exception.EngineException;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

public class Config implements AutoCloseable {
    private static ThreadLocal<Connection> threadLocal = new ThreadLocal<>();
    private static Logger logger = LoggerFactory.getLogger(Config.class);
    /**
     * decent limit on size of IN queries - guideline = Oracle limit
     */
    static int IN_MAX = 1000;
    Connection connection;
    public SqlDialect dialect;
    DataSource dataSource;
    String name;
    boolean showSql;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    protected Config(SqlDialect dialect) {
        this.dialect = dialect;
    }

    public Config(String name, DataSource ds, SqlDialect dialect) {
        this.name = name;
        dataSource = ds;
        try {
            connection = ds.getConnection();
        } catch (SQLException e) {
            throw new ActiveRecordException(e);
        }
        threadLocal.set(connection);
        this.dialect = dialect;
        setAutoCommit(false);
    }


    public SqlDialect getSqlDialect() {
        return dialect;
    }

    /**
     * 重连
     *
     * @throws SQLException
     */
    public void reConnection() throws SQLException {
        if (connection.isClosed()) {
            connection = getConnection();
            threadLocal.set(connection);
            setAutoCommit(false);
        }
    }

    public Connection getConnection() throws SQLException {
        Connection conn = threadLocal.get();
        if (conn != null && !conn.isClosed()){
            return conn;
        }
        if(connection != null && !connection.isClosed()){
            return connection;
        }

        return showSql ? new SqlReporter(dataSource.getConnection()).getConnection() : dataSource.getConnection();
    }


    public void setAutoCommit(boolean autoCommit) {
        try {
            reConnection();
            connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new ActiveRecordException("事务提交失败", e);
        }
    }

    public void rollback() {
        try {
            connection.rollback();
        } catch (SQLException e) {
            throw new ActiveRecordException("事务回滚失败", e);
        }
    }

    public SqlPara mogrify(String sql, Collection<?> params) {
        List<Object> args = new ArrayList<>();
        for (Object p : params) {
            if (p instanceof Collection<?>) {
                Collection<?> col = (Collection<?>) p;
                String v = col.stream().map(c -> "?").collect(Collectors.joining(","));
                sql = sql.replaceFirst("%s", "(" + v + ")");
                args.addAll(col);
            } else {
                sql = sql.replaceFirst("%s", "?");
                args.add(p);
            }
        }
        return new SqlPara(sql, args);
    }

    @Override
    public void close() {
        try {
            if (connection.isClosed()) {
                return;
            }
            if (null != connection && !connection.getAutoCommit()) {
                connection.commit();
            }
        } catch (SQLException e) {
            rollback();
        }
        close(connection);
    }





    static Boolean printSql;

    boolean isPrintSql() {
        if (printSql == null) {
            printSql = false;
        }
        return printSql;
    }

    /**
     * 设置是否打印sql
     *
     * @param isPrint
     */
    public static void setPrintSql(boolean isPrint) {
        printSql = isPrint;
    }









    public String quote(String identify) {
        return dialect.quote(identify);
    }


    public void setThreadLocalConnection(Connection connection) {
        threadLocal.set(connection);
    }


    public void removeThreadLocalConnection() {
        threadLocal.remove();
    }

    public Connection getThreadLocalConnection() {
        return threadLocal.get();
    }

    public boolean isInTransaction() {
        return threadLocal.get() != null;
    }

    public List<Object[]> splitForInConditions(Object[] ids) {
        List<Object[]> result = new ArrayList<>();
        if (ids.length < IN_MAX) {
            result.add(ids);
        } else {
            Integer from = 0;
            while (from < ids.length) {
                Integer to = from + IN_MAX;
                if (to > ids.length) {
                    to = ids.length;
                }
                result.add(Arrays.copyOfRange(ids, from, to));
                from += IN_MAX;
            }
        }
        return result;
    }

    public void close(ResultSet rs, Statement st, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (threadLocal.get() == null) {    // in transaction if conn in threadlocal
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new ActiveRecordException(e);
                }
            }
        }
    }

    public void close(Statement st, Connection conn) {
        if (st != null) {
            try {
                st.close();
            } catch (SQLException e) {
                logger.error(e.getMessage(), e);
            }
        }

        if (threadLocal.get() == null) {    // in transaction if conn in threadlocal
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new ActiveRecordException(e);
                }
            }
        }
    }

    public void close(Connection conn) {
        if (threadLocal.get() == null)        // in transaction if conn in threadlocal
            if (conn != null)
                try {
                    conn.close();
                } catch (SQLException e) {
                    throw new ActiveRecordException(e);
                }
    }
}
