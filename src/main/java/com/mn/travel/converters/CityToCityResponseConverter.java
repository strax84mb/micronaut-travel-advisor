package com.mn.travel.converters;

import com.mn.travel.converters.qualifiers.CityToCityResponseQualifier;
import com.mn.travel.converters.qualifiers.CommentToCommentResponseQualifier;
import com.mn.travel.dto.CityResponse;
import com.mn.travel.dto.CommentResponse;
import com.mn.travel.entity.City;
import com.mn.travel.entity.Comment;
import com.mn.travel.repository.CommentRepository;
import io.micronaut.data.model.Pageable;
import io.micronaut.data.model.Sort;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@CityToCityResponseQualifier
public class CityToCityResponseConverter implements CityConverter<CityResponse> {

    private Converter<Comment, CommentResponse> converter;
    private CommentRepository commentRepository;

    public CityToCityResponseConverter(@CommentToCommentResponseQualifier
                                               Converter<Comment, CommentResponse> converter,
                                       CommentRepository commentRepository) {
        this.converter = converter;
        this.commentRepository = commentRepository;
    }

    @Override
    public CityResponse convert(City city, int maxComments) {
        List<CommentResponse> comments = new ArrayList<>();
        if (maxComments > 0) {
            var pageable = Pageable.from(0, maxComments, Sort.of(Sort.Order.desc("created")));
            commentRepository.findByCityId(city.getId(), pageable)
                    .getContent().stream()
                    .map(comment -> converter.convert(comment))
                    .forEach(comments::add);
        }
        return makeResponse(city, comments);
    }

    @Override
    public CityResponse convert(City city) {
        var commentDtos = city.getComments().stream()
                .map(comment -> converter.convert(comment))
                .collect(Collectors.toList());
        return makeResponse(city, commentDtos);
    }

    private CityResponse makeResponse(City city, List<CommentResponse> commentDtos) {
        return CityResponse.builder()
                .id(city.getId())
                .name(city.getName())
                .country(city.getCountry())
                .comments(commentDtos)
                .build();
    }
}
