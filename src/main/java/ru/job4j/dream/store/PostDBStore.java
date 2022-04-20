package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDBStore {

    private final BasicDataSource pool;

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post order by id")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(createPostFromResultSet(it));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }


    public Post add(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "INSERT INTO post(name, description, visible, created, city_id) "
                             + "VALUES (?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            setValuesFromPreparedStatement(post, ps);
            ps.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            ps.setInt(5, post.getCity().getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    post.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    public void update(Post post) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement(
                     "UPDATE post SET name = ?, description = ?, visible = ?"
                             + " WHERE id = ?")
        ) {
            setValuesFromPreparedStatement(post, ps);
            ps.setInt(4, post.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public Post findById(int id) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post WHERE id = ?")
        ) {
            ps.setInt(1, id);
            try (ResultSet it = ps.executeQuery()) {
                if (it.next()) {
                    return createPostFromResultSet(it);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Post createPostFromResultSet(ResultSet resultSet) {
        Post post = null;
        try {
            post = new Post(
                  resultSet.getInt("id"),
                  resultSet.getString("name")
            );
            String str1 = resultSet.getString("description");
            post.setDescription(str1 != null ? str1 : "описание отсутствует");
            post.setVisible(resultSet.getBoolean("visible"));
            Timestamp time = resultSet.getTimestamp("created");
            post.setCreated(time != null ? time.toLocalDateTime() : LocalDateTime.now());
            post.setCity(new City(resultSet.getInt("city_id"), null));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    private void setValuesFromPreparedStatement(Post post, PreparedStatement ps) throws SQLException {
        ps.setString(1, post.getName());
        ps.setString(2, post.getDescription());
        ps.setBoolean(3, post.getVisible());
    }
}
