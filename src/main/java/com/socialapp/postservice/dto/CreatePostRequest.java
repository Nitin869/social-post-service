package com.socialapp.postservice.dto;

import lombok.Data;

@Data
public class CreatePostRequest {

    private String image;
    private String caption;

}
