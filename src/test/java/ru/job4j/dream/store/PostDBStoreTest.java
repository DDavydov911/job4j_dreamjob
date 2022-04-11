package ru.job4j.dream.store;

import org.junit.Test;
import ru.job4j.dream.Main;
import ru.job4j.dream.model.Post;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class PostDBStoreTest {
    @Test
    public void whenCreatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(8, "Java Job");
        store.add(post);
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is(post.getName()));
    }

    @Test
    public void whenUpdatePost() {
        PostDBStore store = new PostDBStore(new Main().loadPool());
        Post post = new Post(0, "Java Junior");
        store.add(post);
        store.update(new Post(post.getId(), "Java Middle"));
        Post postInDb = store.findById(post.getId());
        assertThat(postInDb.getName(), is("Java Middle"));
    }
}