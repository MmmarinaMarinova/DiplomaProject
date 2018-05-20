package com.example.model.Utilities;

import com.example.model.Post;

import java.util.Comparator;

/**
 * Created by Marina on 20.5.2018 г..
 */
public enum NewsfeedType {
    BY_TIME(new COMPARE_BY_TIME()),
    BY_MOST_POPULAR(new COMPARE_BY_MOST_POPULAR());

    private final Comparator<Post> comparator;

    NewsfeedType(Comparator<Post> comparator){
        this.comparator = comparator;
    }

    public Comparator<Post> getComparator(){
        return this.comparator;
    }
}
