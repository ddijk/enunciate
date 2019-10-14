package com.webcohesion.enunciate.beanval;

import org.junit.Test;

import javax.lang.model.element.AnnotationMirror;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.webcohesion.enunciate.beanval.ValidationGroupHelper.JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL;
import static org.junit.Assert.*;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValidationGroupHelperTest {

    @Test
    public void getSingleGroupOnField() {

        String input = "{groups()={com.a.A.class}}";
        assertEquals(Arrays.asList("com.a.A"), ValidationGroupHelper.getGroupsOnField(input));
    }

    @Test
    public void getMultipleGroupsOnField() {

        String input = "{groups()={com.a.A.class, com.b.B.class}}";
        assertEquals(Arrays.asList("com.a.A", "com.b.B"), ValidationGroupHelper.getGroupsOnField(input));
    }

    @Test
    public void getMultipleGroupsOnFieldWithOtherAttributes() {

        String input = "{names()={a,b},groups()={com.a.A.class, com.b.B.class},age()={12}}";
        assertEquals(Arrays.asList("com.a.A", "com.b.B"), ValidationGroupHelper.getGroupsOnField(input));
    }

    @Test
    public void getNoGroupsOnFieldWithOtherAttributes() {

        String input = "{names()={a,b},age()={12}}";
        assertEquals(Collections.emptyList(), ValidationGroupHelper.getGroupsOnField(input));
    }

    Map<String, AnnotationMirror> fieldAnnotations = new HashMap<>();

    @Test
    public void verifyNotRequiredWhenNotNullAnnotationIsMissing() {
        assertFalse("Field does not have @NotNull", ValidationGroupHelper.isRequired("com.a.A, com.b.B", Collections.EMPTY_MAP));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasDefaultGroupAndFieldHasGroups() {

        AnnotationMirror mockedMirror = mock(AnnotationMirror.class, RETURNS_DEEP_STUBS);
        when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},groups()={com.a.A.class, com.b.B.class},age()={12}}");
        fieldAnnotations.put(JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL, mockedMirror);

        assertFalse("Field does not have @NotNull with groups, but validation runs with default group", ValidationGroupHelper.isRequired("", fieldAnnotations));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasDefaultGroupAndFieldHasNoGroups() {

        AnnotationMirror mockedMirror = mock(AnnotationMirror.class, RETURNS_DEEP_STUBS);
        when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},age()={12}}");
        fieldAnnotations.put(JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL, mockedMirror);

        assertTrue("Field does have @NotNull without groups and validation runs with default group", ValidationGroupHelper.isRequired("", fieldAnnotations));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasGroupAndFieldHasNoGroups() {

        AnnotationMirror mockedMirror = mock(AnnotationMirror.class, RETURNS_DEEP_STUBS);
        when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},age()={12}}");
        fieldAnnotations.put(JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL, mockedMirror);

        assertFalse("Field does have @NotNull without groups, but validation runs with specific groups", ValidationGroupHelper.isRequired("com.c.C, com.b.B", fieldAnnotations));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasGroupAndFieldHasMatchingGroups() {

        AnnotationMirror mockedMirror = mock(AnnotationMirror.class, RETURNS_DEEP_STUBS);
        when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},groups()={com.a.A.class, com.b.B.class},age()={12}}");
        fieldAnnotations.put(JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL, mockedMirror);

        assertTrue("Field does have @NotNull with groups, and validation runs with specific group", ValidationGroupHelper.isRequired("com.c.C, com.b.B", fieldAnnotations));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasGroupAndFieldHasNoMatchingGroups() {

        AnnotationMirror mockedMirror = mock(AnnotationMirror.class, RETURNS_DEEP_STUBS);
        when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},groups()={com.a.A.class, com.d.D.class},age()={12}}");
        fieldAnnotations.put(JAVAX_VALIDATION_CONSTRAINTS_NOT_NULL, mockedMirror);

        assertFalse("Field does have @NotNull with groups, and validation runs with specific group, but none is matching", ValidationGroupHelper.isRequired("com.c.C, com.b.B", fieldAnnotations));
    }
}