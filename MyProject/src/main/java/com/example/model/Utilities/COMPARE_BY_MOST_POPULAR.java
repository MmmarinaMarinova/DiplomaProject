package com.example.model.Utilities;

import com.example.model.Post;

import java.util.Comparator;

/**
 * Created by Marina on 20.5.2018 г..
 */
public class COMPARE_BY_MOST_POPULAR implements Comparator<Post> {

    @Override
    public int compare(Post o1, Post o2) {
        return o2.getPeopleLiked().size() - o1.getPeopleLiked().size() != 0
                ? (o2.getPeopleLiked().size() - o1.getPeopleLiked().size())
                : (o2.getDateTime().compareTo(o1.getDateTime()));
    }
    //todo REFACTOR HERE!!!!!!!!!!!!!! AND ADD GENERICS
}
