package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dream.model.Candidate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CandidateDbStore {

    private final BasicDataSource pool;

    public CandidateDbStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Candidate> findAll() {
        List<Candidate> candidates = new ArrayList<>();
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM candidates ORDER BY id"
             )) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Candidate candidate = new Candidate(resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description"));
                    byte[] photo = resultSet.getBytes("photo");
                    if (photo != null) {
                        candidate.setPhoto(photo);
                    }
                    candidates.add(candidate);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return candidates;
    }

    public Candidate findById(int id) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM candidates WHERE id = ?"
             )
        ) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                Candidate candidate;
                if (resultSet.next()) {
                    candidate = new Candidate(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getString("description")
                    );
                    byte[] photo = resultSet.getBytes("photo");
                    if (photo != null) {
                        candidate.setPhoto(photo);
                    }
                    return candidate;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(Candidate candidate) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE candidates SET name = ?, description = ? WHERE id = ?"
             )) {
            preparedStatement.setString(1, candidate.getName());
            preparedStatement.setString(2, candidate.getDescription());
            preparedStatement.setInt(3, candidate.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void create(Candidate candidate) {
        try (Connection connection = pool.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO candidates(name, description, photo) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS
             )) {
            preparedStatement.setString(1, candidate.getName());
            preparedStatement.setString(2, candidate.getDescription());
            preparedStatement.setBytes(3, candidate.getPhoto());
            preparedStatement.execute();
            try (ResultSet id = preparedStatement.getGeneratedKeys()) {
                if (id.next()) {
                    candidate.setId(id.getInt(1));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}