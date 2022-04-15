package ru.job4j.dream.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.stereotype.Repository;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PostDBStore {

    private final BasicDataSource pool;

    private final Gson gson = new GsonBuilder().create();

    public PostDBStore(BasicDataSource pool) {
        this.pool = pool;
    }

    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("SELECT * FROM post")
        ) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    posts.add(new Post(it.getInt("id"), it.getString("name")));
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
                     "INSERT INTO post(name, description, created, visible, city) "
                             + "VALUES (?, ?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)
        ) {
            setValuesFromPreparedStatement(post, ps);
//            ps.setString(1, post.getName());
//            ps.setString(2, post.getDescription());
//            ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
//            ps.setBoolean(4, post.getVisible());
//            ps.setObject(5, gson.toJson(post.getCity()));
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
                     "UPDATE post SET name = ?, description = ?, created = ?, visible = ?,"
                             + " city = ? WHERE id = ?")
        ) {
            setValuesFromPreparedStatement(post, ps);
//            ps.setString(1, String.valueOf(post.getName()));
            ps.setInt(6, post.getId());
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
            post.setDescription(resultSet.getString("description"));
            post.setCreated(resultSet.getTimestamp("created").toLocalDateTime());
            post.setVisible(resultSet.getBoolean("visible"));
            post.setCity(gson.fromJson(String.valueOf(resultSet.getObject("city")), City.class));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    private void setValuesFromPreparedStatement(Post post, PreparedStatement ps) throws SQLException {
        ps.setString(1, post.getName());
        ps.setString(2, post.getDescription());
        ps.setTimestamp(3, Timestamp.valueOf(post.getCreated()));
        ps.setBoolean(4, post.getVisible());
        ps.setObject(5, gson.toJson(post.getCity()));
    }
}
