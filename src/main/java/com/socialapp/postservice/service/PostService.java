package com.socialapp.postservice.service;

import com.socialapp.postservice.dto.CommentResponse;
import com.socialapp.postservice.dto.CreateCommentRequest;
import com.socialapp.postservice.dto.CreatePostRequest;
import com.socialapp.postservice.dto.PostResponse;
import com.socialapp.postservice.exception.*;
import com.socialapp.postservice.model.Comment;
import com.socialapp.postservice.model.Like;
import com.socialapp.postservice.model.Post;
import com.socialapp.postservice.repository.CommentRepository;
import com.socialapp.postservice.repository.LikeRepository;
import com.socialapp.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final CommentRepository commentRepository;

    //Post CRUD
    public PostResponse createPost( Long userId, CreatePostRequest request){
        Post post = Post.builder()
                .userId(userId)
                .image(request.getImage())
                .caption(request.getCaption())
                .build();
        postRepository.save(post);
        return toPostResponse(post);
    }

    public PostResponse getPost(Long postId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException(postId));
        return toPostResponse(post);
    }

    public List<PostResponse> getPostByUser(Long userId){
        return postRepository.findByUserIdOrderByInsertTimeStampDesc(userId)
                .stream()
                .map(this::toPostResponse)
                .toList();
    }

    public void deletePost(Long postId, Long userId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException(postId));
        if(post.getUserId().equals(userId))
            throw new UnauthorizedActionException("You can only delete your own posts");
        postRepository.deleteById(postId);
    }

    //Comment CRUD
    @Transactional
    public CommentResponse addComment(Long postId, Long userId, CreateCommentRequest request){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException(postId));
        Comment comment = Comment.builder()
                .postId(postId)
                .userId(userId)
                .content(request.getContent())
                .build();
        Comment saved = commentRepository.save(comment);

        post.setCommentCount(post.getCommentCount()+1);
        postRepository.save(post);
        return toCommentResponse(saved);

    }

    @Transactional
    public CommentResponse deleteComment(Long postId, Long userId, Long commentId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException(postId));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(()->new CommentNotFoundException(commentId));

        if(!comment.getUserId().equals(userId))
            throw new UnauthorizedActionException("You can only delete your comment");

        commentRepository.deleteById(commentId);

        post.setCommentCount(post.getCommentCount()-1);
        postRepository.save(post);

        return toCommentResponse(comment);
    }

    //Like & Unlike
    @Transactional
    public PostResponse likePost(Long postId, Long userId){
        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException(postId));

        if(likeRepository.existsByPostIdAndUserId(postId,userId))
            throw new AlreadyLikedException(postId,userId);

        Like like= Like.builder()
                .postId(postId)
                .userId(userId)
                .build();
        likeRepository.save(like);

        post.setLikeCount(post.getLikeCount()+1);
        postRepository.save(post);

        return toPostResponse(post);
    }

    @Transactional
    public PostResponse unlikePost(Long postId, Long userId){

        Post post = postRepository.findById(postId)
                .orElseThrow(()->new PostNotFoundException(postId));

        if(!likeRepository.existsByPostIdAndUserId(postId,userId))
            throw new NotLikedException(postId,userId);

        likeRepository.deleteByPostIdAndUserId(postId,userId);

        post.setLikeCount(post.getLikeCount()-1);
        postRepository.save(post);

        return toPostResponse(post);
    }

    public List<CommentResponse> getComments(Long postId){
        if(!postRepository.existsById(postId))
                throw new PostNotFoundException(postId);
        return commentRepository.findByPostIdOrderByTimestampDesc(postId)
                .stream()
                .map(this::toCommentResponse)
                .toList();
    }

    private PostResponse toPostResponse(Post post) {
        return PostResponse.builder()
                .id(post.getId())
                .userId(post.getUserId())
                .image(post.getImage())
                .caption(post.getCaption())
                .likeCount(post.getLikeCount())
                .commentCount(post.getCommentCount())
                .insertTimeStamp(post.getInsertTimeStamp())
                .build();
    }

    private CommentResponse toCommentResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .postId(comment.getPostId())
                .userId(comment.getUserId())
                .content(comment.getContent())
                .timestamp(comment.getTimestamp())
                .build();
    }
}
