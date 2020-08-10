package com.mn.travel.services.impl;

import com.mn.travel.converters.Converter;
import com.mn.travel.converters.qualifiers.CommentToCommentResponseQualifier;
import com.mn.travel.dto.CommentResponse;
import com.mn.travel.dto.UpdateCommentRequest;
import com.mn.travel.dto.WriteCommentRequest;
import com.mn.travel.entity.Comment;
import com.mn.travel.entity.UserRole;
import com.mn.travel.exceptions.CityNotFoundException;
import com.mn.travel.exceptions.CommentDoesNotExistException;
import com.mn.travel.exceptions.UsernameNotFoundException;
import com.mn.travel.exceptions.WrongUsernameOfCommentException;
import com.mn.travel.repository.CityRepository;
import com.mn.travel.repository.CommentRepository;
import com.mn.travel.repository.UserRepository;
import com.mn.travel.services.CommentService;

import javax.inject.Singleton;
import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Singleton
@Transactional
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private CityRepository cityRepository;
    private UserRepository userRepository;
    private Converter<Comment, CommentResponse> converter;

    public CommentServiceImpl(CommentRepository commentRepository,
                              CityRepository cityRepository,
                              UserRepository userRepository,
                              @CommentToCommentResponseQualifier Converter<Comment, CommentResponse> converter) {
        this.commentRepository = commentRepository;
        this.cityRepository = cityRepository;
        this.userRepository = userRepository;
        this.converter = converter;
    }

    @Override
    public CommentResponse addComment(WriteCommentRequest dto, String username) {
        var city = cityRepository.findById(dto.getCityId());
        var poster = userRepository.findByUsername(username);
        var comment = Comment.builder()
                .text(dto.getText())
                .poster(poster.orElseThrow(() -> new UsernameNotFoundException(username)))
                .city(city.orElseThrow(() -> new CityNotFoundException(dto.getCityId())))
                .created(LocalDateTime.now())
                .modified(LocalDateTime.now())
                .build();
        comment = commentRepository.save(comment);
        return converter.convert(comment);
    }

    @Override
    public CommentResponse updateComment(Long id, UpdateCommentRequest dto, String username) {
        var commentOptional = commentRepository.findById(id);
        var userOptional = userRepository.findByUsername(username);
        var comment = commentOptional.orElseThrow(() -> new CommentDoesNotExistException(id));
        var user = userOptional.orElseThrow(() -> new UsernameNotFoundException(username));
        if (!comment.getPoster().getId().equals(user.getId()) && !UserRole.ADMIN.equals(user.getRole())) {
            throw new WrongUsernameOfCommentException(username);
        }
        comment.setText(dto.getText());
        comment.setModified(LocalDateTime.now());
        comment = commentRepository.save(comment);
        return converter.convert(comment);
    }

    @Override
    public boolean deleteComment(Long id, String username) {
        var comment = commentRepository.findById(id)
                .orElseThrow(() -> new CommentDoesNotExistException(id));
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        if (!comment.getPoster().getId().equals(user.getId()) && !UserRole.ADMIN.equals(user.getRole())) {
            throw new WrongUsernameOfCommentException(username);
        }
        commentRepository.delete(comment);
        return true;
    }
}
