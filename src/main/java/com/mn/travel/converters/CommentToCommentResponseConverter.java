package com.mn.travel.converters;

import com.mn.travel.converters.qualifiers.CommentToCommentResponseQualifier;
import com.mn.travel.dto.CommentResponse;
import com.mn.travel.entity.Comment;

import javax.inject.Singleton;

@Singleton
@CommentToCommentResponseQualifier
public class CommentToCommentResponseConverter implements Converter<Comment, CommentResponse> {

    @Override
    public CommentResponse convert(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .poster(comment.getPoster().getUsername())
                .text(comment.getText())
                .created(comment.getCreated())
                .modified(comment.getModified())
                .build();
    }
}
