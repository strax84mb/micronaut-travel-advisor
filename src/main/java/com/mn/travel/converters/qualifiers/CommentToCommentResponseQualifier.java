package com.mn.travel.converters.qualifiers;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier // Annotation designating this annotation as a bean qualifier
@Retention(RetentionPolicy.RUNTIME)
public @interface CommentToCommentResponseQualifier {
}
