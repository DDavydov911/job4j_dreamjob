package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dream.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
public class UserStoreDb {

    private final BasicDataSource pool;

    public UserStoreDb(BasicDataSource pool) {
        this.pool = pool;
    }

    public Optional<User> add(User user) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO users(email, password) VALUES (?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.execute();
            try (ResultSet id = preparedStatement.getGeneratedKeys()) {
                if (id.next()) {
                    user.setId(id.getInt(1));
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> findById(int id) {
        User user = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM users WHERE id = ?",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    user = new User(
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                    user.setId(resultSet.getInt("id"));
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.of(user);
    }

    public Optional<User> findUserByEmailAndPwd(String email, String password) {
        User user = null;
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM users WHERE email = ?",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
                preparedStatement.setString(1, email);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        user = new User(
                                resultSet.getString("email"),
                                resultSet.getString("password")
                        );
                        user.setId(resultSet.getInt("id"));
                    }
                }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return (user != null && password.equals(user.getPassword()))
                ? Optional.of(user) : Optional.empty();
    }
}
