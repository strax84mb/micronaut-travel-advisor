package com.mn.travel.controllers;

import com.mn.travel.dto.CommentResponse;
import com.mn.travel.dto.UpdateCommentRequest;
import com.mn.travel.dto.WriteCommentRequest;
import com.mn.travel.services.CommentService;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.authentication.AuthenticationUserDetailsAdapter;
import io.micronaut.security.rules.SecurityRule;

import java.security.Principal;

/**
 * Controller for /city/comment endpoint
 */
@Controller("/city/comment")
public class CommentController {

    private CommentService commentService;

    /**
     * Controller used to instantiate this class
     * @param commentService
     */
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * POST request handler for endpoint /city/comment
     * This is how new comment is saved
     * @param dto Comment data to be saved
     * @param principal Security principal provided by JWT
     * @throws com.mn.travel.exceptions.CityNotFoundException Thrown if provided city ID does not exist
     * @throws com.mn.travel.exceptions.UsernameNotFoundException Thrown if JWT provides non existent username
     * @return
     */
    @Post
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    CommentResponse addComment(@Body WriteCommentRequest dto, Principal principal) {
        return commentService.addComment(dto, getUsername(principal));
    }

    /**
     * PUT request handler for endpoint /city/comment/123
     * This is how comment is changed
     * @param id ID of comment
     * @param dto Comment data to be saved
     * @param principal Security principal provided by JWT
     * @throws com.mn.travel.exceptions.CommentDoesNotExistException Thrown if ID references non existent comment
     * @throws com.mn.travel.exceptions.UsernameNotFoundException Thrown username references non existent user
     * @throws com.mn.travel.exceptions.WrongUsernameOfCommentException Thrown if user with role USER tries to change someone else's comment
     * @return
     */
    @Put("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Secured(SecurityRule.IS_AUTHENTICATED)
    CommentResponse updateComment(@PathVariable("id") Long id, @Body UpdateCommentRequest dto, Principal principal) {
        return commentService.updateComment(id, dto, getUsername(principal));
    }

    /**
     * DELETE request handler for endpoint /city/comment/123
     * This is how comment is deleted
     * @param id ID of comment
     * @param principal Security principal provided by JWT
     * @throws com.mn.travel.exceptions.CommentDoesNotExistException Thrown if ID references non existent comment
     * @throws com.mn.travel.exceptions.UsernameNotFoundException Thrown username references non existent user
     * @throws com.mn.travel.exceptions.WrongUsernameOfCommentException Thrown if user with role USER tries to change someone else's comment
     * @return
     */
    @Delete("/{id}")
    @Secured(SecurityRule.IS_AUTHENTICATED)
    boolean deleteComment(Long id, Principal principal) {
        return commentService.deleteComment(id, getUsername(principal));
    }

    private String getUsername(Principal principal) {
        return ((AuthenticationUserDetailsAdapter) principal).getName();
    }
}
