package com.mn.travel.repository;

import com.mn.travel.entity.Comment;
import io.micronaut.data.annotation.Repository;
import io.micronaut.data.jpa.repository.JpaRepository;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.Optional;

@Repository // Annotation this interface as a DB repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByCityId(Long cityId, Pageable pageable);

    Optional<Comment> findByIdAndPosterUsername(Long id, String posterUsername);
}
