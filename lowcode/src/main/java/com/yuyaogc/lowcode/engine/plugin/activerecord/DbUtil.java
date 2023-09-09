package com.yuyaogc.lowcode.engine.plugin.activerecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class DbUtil {
    public static int update(Config config, Connection conn, String sql, List<Object> paras) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            config.dialect.fillStatement(pst, paras);
            int result = pst.executeUpdate();
            return result;
        }
    }

    public static int update(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            config.dialect.fillStatement(pst, paras);
            int result = pst.executeUpdate();
            return result;
        }
    }
}
