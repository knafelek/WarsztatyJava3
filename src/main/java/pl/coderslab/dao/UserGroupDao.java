package pl.coderslab.dao;

import org.apache.log4j.Logger;
import pl.coderslab.model.User;
import pl.coderslab.model.UserGroup;
import pl.coderslab.utils.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserGroupDao {

    public static final Logger logger = Logger.getLogger(UserGroupDao.class);

    private static final String CREATE = "INSERT INTO users_groups (name) VALUES (?)";
    private static final String UPDATE = "UPDATE users_groups SET name = ? WHERE id = ?";
    private static final String FIND_ALL = "SELECT * FROM users_groups";
    private static final String FIND_BY_ID = "SELECT * FROM users_groups WHERE id = ?";

    public static void save(UserGroup modelObj) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(CREATE, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, modelObj.getName());
            int result = statement.executeUpdate();

            if (result != 1) {
                logger.error("Błąd dodania wpisu: " + modelObj);
                throw new RuntimeException("Błąd dodania wpisu: " + modelObj);
            }

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.first()) {
                    modelObj.setId(resultSet.getInt(1));
                }
                else {
                    logger.error("Błąd uzyskania klucza głównego dla: " + modelObj);
                    throw new RuntimeException("Błąd uzyskania klucza głównego dla: " + modelObj);
                }
            }
        } catch (SQLException sqlExc) {
            logger.error("Błąd zapisu", sqlExc);
            throw new RuntimeException("Błąd zapisu", sqlExc);
        }
    }

    public static void update(UserGroup modelObj) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(UPDATE)) {
            statement.setString(1, modelObj.getName());
            statement.setInt(2, modelObj.getId());

            int result = statement.executeUpdate();

            if (result != 1) {
                logger.error("Błąd zaktualizowania wpisu: " + modelObj);
                throw new RuntimeException("Błąd zaktualizowania wpisu: " + modelObj);
            }
        } catch (SQLException sqlExc) {
            logger.error("Błąd zapisu", sqlExc);
            throw new RuntimeException("Błąd zapisu", sqlExc);
        }
    }

    public static List<UserGroup> findAll() {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_ALL)) {
            ResultSet resultSet = statement.executeQuery();

            List<UserGroup> results = new LinkedList<>();
            while (resultSet.next()) {
                UserGroup object = convertFrom(resultSet);
                results.add(object);
            }

            return results;
        } catch (SQLException sqlExc) {
            logger.error("Błąd zapisu", sqlExc);
            throw new RuntimeException("Błąd zapisu", sqlExc);
        }
    }

    public static UserGroup findById(Integer id) {
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement statement = conn.prepareStatement(FIND_BY_ID)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                return convertFrom(resultSet);
            }

            return null;
        } catch (SQLException sqlExc) {
            logger.error("Błąd zapisu", sqlExc);
            throw new RuntimeException("Błąd zapisu", sqlExc);
        }
    }

    private static UserGroup convertFrom(ResultSet resultSet) throws SQLException {
        UserGroup object = new UserGroup();
        object.setId(resultSet.getInt("id"));
        object.setName(resultSet.getString("name"));
        return object;
    }
}
