package pl.coderslab.utils;

import org.apache.log4j.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DbUtil {

    private static final Logger logger = Logger.getLogger(DbUtil.class);
    private static DataSource dataSource;

    public static Connection getConnection() throws SQLException {
        return getInstance().getConnection();
    }

    private static DataSource getInstance() {
        if (dataSource == null) {
            try {
                Context context = new InitialContext();
                dataSource = (DataSource) context.lookup("java:comp/env/jdbc/school"); //???
            } catch (NamingException ne) {
                logger.error("Błąd uzyskania źródła danych", ne);
                throw new RuntimeException("Błąd uzyskania źródła danych", ne);
            }
        }
        return dataSource;
    }
}
