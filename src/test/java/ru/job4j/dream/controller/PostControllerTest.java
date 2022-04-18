package ru.job4j.dream.controller;

import org.apache.commons.dbcp2.BasicDataSource;
import org.junit.Ignore;
import org.junit.Test;

import javax.servlet.http.HttpSession;

import org.springframework.ui.Model;
import ru.job4j.dream.model.City;
import ru.job4j.dream.model.Post;
import ru.job4j.dream.service.CityService;
import ru.job4j.dream.service.PostService;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class PostControllerTest {

    @Test
    public void whenPosts() {
        List<Post> posts = Arrays.asList(
                new Post(1, "New post"),
                new Post(2, "New post")
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        when(postService.findAll()).thenReturn(posts);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        HttpSession session = mock(HttpSession.class);
        String page = postController.posts(model, session);
        verify(model).addAttribute("posts", posts);
        assertThat(page, is("posts"));
    }

    @Test
    public void whenFormAddPost() {
        List<City> cities = Arrays.asList(
                new City(1, "Msk"),
                new City(1, "Msk")
        );
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        when(cityService.getAllCities()).thenReturn(cities);
        PostController postController = new PostController(postService, cityService);
        HttpSession session = mock(HttpSession.class);
        String page = postController.formAddPost(model, session);
        verify(model).addAttribute("cities", cities);
        assertThat(page, is("addPost"));
    }

    @Test
    public void whenSavePost() {
        Post post = new Post(0, "New post");
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.savePost(post);
        verify(postService).add(post);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenFormUpdatePost() {
        Post post2 = new Post(2, "post2");
        Model model = mock(Model.class);
        PostService postService = mock(PostService.class);
        when(postService.findById(2)).thenReturn(post2);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.formUpdatePost(model, 2);
        verify(model).addAttribute("post", post2);
        assertThat(page, is("updatePost"));
    }

    @Test
    public void whenUpdatePost() {
        Post post3 = new Post(3, "post3");
        PostService postService = mock(PostService.class);
        when(postService.findById(2)).thenReturn(post3);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.updatePost(post3);
        verify(postService).update(post3);
        assertThat(page, is("redirect:/posts"));
    }

    @Test
    public void whenCreatePost() {
        Post post4 = new Post(4, "post4");
        PostService postService = mock(PostService.class);
        CityService cityService = mock(CityService.class);
        PostController postController = new PostController(
                postService,
                cityService
        );
        String page = postController.createPost(post4);
        verify(postService).create(post4);
        assertThat(page, is("redirect:/posts"));
    }
}