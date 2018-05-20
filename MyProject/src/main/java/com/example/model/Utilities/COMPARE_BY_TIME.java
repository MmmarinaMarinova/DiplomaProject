package com.example.model.Utilities;

import com.example.model.Post;

import java.util.Comparator;

/**
 * Created by Marina on 20.5.2018 г..
 */
public class COMPARE_BY_TIME implements Comparator<Post> {
    @Override
    public int compare(Post o1, Post o2) {
        return o1.compareTo(o2);
    }
}
