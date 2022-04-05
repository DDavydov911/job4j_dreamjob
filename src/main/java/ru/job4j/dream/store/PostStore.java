package ru.job4j.dream.store;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Repository;
import ru.job4j.dream.model.Post;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
@Repository
public class PostStore {
    @GuardedBy("this")
    public final AtomicInteger id;

    @GuardedBy("this")
    private final Map<Integer, Post> posts = new ConcurrentHashMap<>();

    private PostStore() {
        id = new AtomicInteger(0);
        posts.put(id.incrementAndGet(), new Post(id.get(), "Junior Java Job"));
        posts.put(id.incrementAndGet(), new Post(id.get(), "Middle Java Job"));
        posts.put(id.incrementAndGet(), new Post(id.get(), "Senior Java Job"));
    }

    public Collection<Post> findAll() {
        return posts.values();
    }

    public void add(Post post) {
        post.setId(id.incrementAndGet());
        posts.put(post.getId(), post);
    }

    public Post findById(int id) {
        return posts.get(id);
    }

    public void update(Post post) {
        posts.replace(post.getId(), post);
    }

    public void create(Post post) {
        post.setId(id.incrementAndGet());
        posts.put(post.getId(), post);
    }

    public static void main(String[] args) {
        PostStore ps = new PostStore();
        for (int i = 1; i <= ps.findAll().size(); i++) {
            System.out.print(i + " ");
            System.out.println(ps.findById(i).getId());
        }
    }
}