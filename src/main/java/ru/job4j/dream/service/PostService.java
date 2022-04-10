package ru.job4j.dream.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.store.PostDBStore;

import java.util.ArrayList;
import java.util.List;


@ThreadSafe
@Service
public class PostService {

    private final PostDBStore store;

    public PostService(PostDBStore store) {
        this.store = store;
    }

    public List<Post> findAll() {
        return new ArrayList<>(store.findAll());
    }

    public void add(Post post) {
        store.add(post);
    }

    public void update(Post post) {
        store.update(post);
    }

    /*
    public void create(Post post) {
        post.setId(store.id.incrementAndGet());
        store.create(post);
    }
    */

    public Post findById(int id) {
        return store.findById(id);
    }

    public void create(Post post) {
        add(post);
    }
}
