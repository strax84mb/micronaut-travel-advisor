package com.mn.travel.services;

import com.mn.travel.dto.CommentResponse;
import com.mn.travel.dto.UpdateCommentRequest;
import com.mn.travel.dto.WriteCommentRequest;

public interface CommentService {

    CommentResponse addComment(WriteCommentRequest dto, String username);

    CommentResponse updateComment(Long id, UpdateCommentRequest dto, String username);

    boolean deleteComment(Long id, String username);
}
