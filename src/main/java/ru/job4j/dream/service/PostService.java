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

    private final CityService cityService;

    public PostService(PostDBStore store, CityService cityService) {
        this.store = store;
        this.cityService = cityService;
    }

    public List<Post> findAll() {
        List<Post> list = new ArrayList<>(store.findAll());
        list.forEach(
                post -> post.setCity(
                        cityService.findById(post.getCity().getId())
                )
        );
        return list;
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
        Post post = store.findById(id);
        post.setCity(cityService.findById(post.getCity().getId()));
        return post;
    }

    public void create(Post post) {
        add(post);
    }
}
