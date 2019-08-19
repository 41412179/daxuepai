package com.daxuepai.gaoxiao.service;

import com.daxuepai.gaoxiao.model.Post;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FilterService {



    public List<Post> filter(List<Post> list) {
        List<Post> posts = new ArrayList<>();
        for (Post p:
             list) {
            String title = p.getTitle();
            title = filterString(title);
            p.setTitle(title);
            p.setContent(filterString(p.getContent()));
            posts.add(p);
        }
        return posts;
    }

    private String filterString(String string) {
        string =string.replace("贴吧", "大学派");
        string = string.replace("tieba", "大学派");
        return string;
    }
}
