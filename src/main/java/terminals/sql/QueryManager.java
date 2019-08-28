package terminals.sql;



import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class QueryManager {
    private static final Logger LOG = LoggerFactory.getLogger(QueryManager.class);
    private final Connection connection;
    private final Map<Class, TriConsumer<Integer, PreparedStatement, Object>> dispatcher = new HashMap<>();

    public QueryManager(Connection connection) {
        this.connection = connection;
        initDispatcher();
    }

    private void initDispatcher() {
        dispatcher.put(Integer.class, (index, ps, obj) -> {
            ps.setInt(index + 1, (int) obj);
        });
        dispatcher.put(String.class, (index, ps, obj) -> {
            ps.setString(index + 1, (String) obj);
        });
        dispatcher.put(Boolean.class, (index, ps, obj) -> {
            ps.setBoolean(index + 1, (Boolean) obj);
        });
    }


    public void runQuery(String query, List<Object> params, Consumer<PreparedStatement> consumer) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("PRAGMA foreign_keys = ON; ");
            PreparedStatement ps = connection.prepareStatement(query);
            for (int i = 0; i < params.size(); i++) {
                dispatcher.get(params.get(i).getClass()).accept(i, ps, params.get(i));
            }
            consumer.accept(ps);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
    }

    public <R> Optional<R> runQuery(String query, List<Object> params, Func<PreparedStatement, R> func) throws Exception {
        Optional<R> result = Optional.empty();
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("PRAGMA foreign_keys = ON; ");
            PreparedStatement ps = connection.prepareStatement(query);

            for (int i = 0; i < params.size(); i++) {
                dispatcher.get(params.get(i).getClass()).accept(i, ps, params.get(i));
            }
            result = Optional.of(func.apply(ps));

        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }
        return result;
    }

    public String queryComposerForExactSearch(String query, Map<String, String> parameters) {
        StringBuilder sb = new StringBuilder(query);
        for(Map.Entry<String, String> each : parameters.entrySet()) {
            sb.append(" ")
                    .append(each.getKey())
                    .append(" ")
                    .append("=")
                    .append(" '")
                    .append(each.getValue())
                    .append("' ")
                    .append("AND");
        }
        sb.setLength(sb.length() - 4);
        return sb.toString();
    }

}
