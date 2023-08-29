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
    PreparedStatement statement;
    ResultSet resultSet;
    public SqlDialect dialect;
    CursorState state;
    DataSource dataSource;
    String name;
    boolean showSql;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 游标状态
     */
    public enum CursorState {
        /**
         * 未执行execute，此时调用fetch*()将抛异常
         */
        Unexecuted,
        /**
         * 已执行execute但没有返回数据
         */
        ExecuteNonQuery,
        /**
         * 数据可读，此时可执行fetch*()
         */
        Fetchable,
        /**
         * 数据已读完
         */
        EndOfFetch,
    }

    int rowcount;

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
        if (conn != null && !conn.isClosed())
            return conn;
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
        reset();
        try {
            connection.close();
        } catch (SQLException e) {
            throw new ActiveRecordException(e);
        }
    }

    public void execute(String sql) {
        execute(sql, Collections.emptyList(), false);
    }

    public void execute(String sql, Collection<?> params) {
        execute(sql, params, false);
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

    public boolean execute(String sql, Collection<?> params, boolean logExceptions) {
        reset();
        SqlPara format = mogrify(sql, params);
        try {
            reConnection();
            statement = connection.prepareStatement(format.getSql());
            int parameterIndex = 1;
            for (Object p : format.getParmas()) {
                statement.setObject(parameterIndex++, p);
            }
            if (isPrintSql()) {
                System.out.println(format.toString());
            }
            boolean res = statement.execute();
            if (res) {
                resultSet = statement.getResultSet();
                rowcount = resultSet.getRow();
                scroll();
            } else {
                int count = statement.getUpdateCount();
                reset();
                rowcount = count;
                state = CursorState.ExecuteNonQuery;
            }
            return res;
        } catch (SQLException e) {
            throw getSqlDialect().getError(e, format);
        }
    }

    public int getRowCount() {
        return rowcount;
    }

    public Object[] fetchOne() {
        ensureExecuted();
        if (state == CursorState.Fetchable) {
            Object[] row = readRow();
            scroll();
            return row;
        }
        return ArrayUtils.EMPTY_OBJECT_ARRAY;
    }

    public List<Object[]> fetchMany(int size) {
        ensureExecuted();
        List<Object[]> list = new ArrayList<>();
        int i = 0;
        while (state == CursorState.Fetchable && i++ < size) {
            list.add(readRow());
            scroll();
        }
        return list;
    }

    public List<Object[]> fetchAll() {
        ensureExecuted();
        List<Object[]> list = new ArrayList<>();
        while (state == CursorState.Fetchable) {
            list.add(readRow());
            scroll();
        }
        return list;
    }

    public Map<String, Object> fetchMapOne() {
        ensureExecuted();
        if (state == CursorState.Fetchable) {
            Map<String, Object> map = readMap();
            scroll();
            return map;
        }
        return KvMap.empty();
    }

    public List<Map<String, Object>> fetchMapMany(int size) {
        ensureExecuted();
        List<Map<String, Object>> list = new ArrayList<>();
        int i = 0;
        while (state == CursorState.Fetchable && i++ < size) {
            list.add(readMap());
            scroll();
        }
        return list;
    }

    public List<Map<String, Object>> fetchMapAll() {
        ensureExecuted();
        List<Map<String, Object>> list = new ArrayList<>();
        while (state == CursorState.Fetchable) {
            list.add(readMap());
            scroll();
        }
        return list;
    }

    Map<String, Object> readMap() {
        try {
            ResultSetMetaData meta = resultSet.getMetaData();
            Object[] values = new Object[meta.getColumnCount()];
            KvMap map = new KvMap(values.length);
            for (int i = 1; i <= values.length; i++) {
                map.put(meta.getColumnLabel(i), resultSet.getObject(i));
            }
            return map;
        } catch (SQLException e) {
            throw new EngineException("读取行失败", e);
        }
    }

    Object[] readRow() {
        try {
            ResultSetMetaData meta = resultSet.getMetaData();
            Object[] values = new Object[meta.getColumnCount()];
            for (int i = 0; i < values.length; i++) {
                values[i] = resultSet.getObject(i + 1);
            }
            return values;
        } catch (SQLException e) {
            throw new EngineException("读取行失败", e);
        }
    }

    void ensureExecuted() {
        if (state == CursorState.Unexecuted) {
            throw new EngineException("没有执行SQL");
        }
    }

    private void scroll() {
        try {
            if (resultSet.next()) {
                rowcount++;
                state = CursorState.Fetchable;
            } else {
                reset();
                state = CursorState.EndOfFetch;
            }
        } catch (Exception e) {
            throw new EngineException("记录集滚动失败", e);
        }
    }

    private void reset() {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            logger.warn("记录集关闭失败", e);
        }
        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException e) {
            logger.warn("SQL关闭失败", e);
        }
        resultSet = null;
        statement = null;
        rowcount = 0;
        state = CursorState.Unexecuted;
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
