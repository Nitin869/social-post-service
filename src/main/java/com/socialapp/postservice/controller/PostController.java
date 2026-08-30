package com.socialapp.postservice.controller;

import com.socialapp.postservice.dto.CommentResponse;
import com.socialapp.postservice.dto.CreateCommentRequest;
import com.socialapp.postservice.dto.CreatePostRequest;
import com.socialapp.postservice.dto.PostResponse;
import com.socialapp.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    //-----Posts---------------------------------------------------------------------------------

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @RequestBody CreatePostRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.createPost(userId,request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @PathVariable Long postId){
        postService.deletePost(postId,userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long postId){
        return ResponseEntity.ok().body(postService.getPost(postId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PostResponse>> getPostByUser(
            @PathVariable Long userId){
        return ResponseEntity.ok(postService.getPostByUser(userId));
    }
    //-----Comment------------------------------------------------------------------------------

    @PostMapping("/{postId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @PathVariable Long postId,
            @RequestBody CreateCommentRequest request){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(postService.addComment(postId, userId, request));
    }

    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(
            @RequestHeader("X-Auth-User-Id") Long userId,
            @PathVariable Long postId,
            @PathVariable Long commentId){
        return ResponseEntity.ok(postService.deleteComment(postId, userId, commentId));
    }

    @GetMapping("/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long postId){
        return ResponseEntity.ok(postService.getComments(postId));
    }

    //-----Like / Unlike-------------------------------------------------------------------------
    @PostMapping("/{postId}/like")
    public ResponseEntity<PostResponse> likePost(
            @PathVariable Long postId,
            @RequestHeader("X-Auth-User-Id") Long userId){
        return ResponseEntity.ok(postService.likePost(postId,userId));
    }

    @DeleteMapping("/{postId}/unlike")
    public ResponseEntity<PostResponse> unlikePost(
            @PathVariable Long postId,
            @RequestHeader("X-Auth-User-Id") Long userId) {
        return ResponseEntity.ok(postService.unlikePost(postId,userId));
    }
}
