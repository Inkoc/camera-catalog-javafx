package hr.algebra.camera.repository.postgres;

import java.sql.CallableStatement;
import java.sql.SQLException;

public final class CallableStatementFactory {
    private CallableStatementFactory(){}

    public static void bindParams(CallableStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}
