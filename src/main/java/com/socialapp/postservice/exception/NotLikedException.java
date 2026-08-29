package com.socialapp.postservice.exception;

public class NotLikedException extends RuntimeException{
    public NotLikedException(Long postId, Long userId){
        super("User with id " + userId + " has not liked the post with id "
                + postId + ", so it cannot be unliked");
    }
}
