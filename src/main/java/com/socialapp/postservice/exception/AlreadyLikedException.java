package com.socialapp.postservice.exception;

public class AlreadyLikedException extends RuntimeException{

    public AlreadyLikedException(Long postId, Long userId) {
        super("User with id: " + userId + " has already liked the post: " + postId);
    }
}
