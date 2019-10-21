package com.webcohesion.enunciate.beanval;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;

import javax.lang.model.element.AnnotationMirror;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ValidationGroupHelperTest {

    @Test
    public void getSingleGroupOnField() {

        String input = "{groups()={ com.webcohesion.enunciate.beanval.validationgroups.GroupA.class}}";
        assertEquals(Arrays.asList("com.webcohesion.enunciate.beanval.validationgroups.GroupA"), ValidationGroupHelper.getGroupsOnField(input));
    }

    @Test
    public void getMultipleGroupsOnField() {

        String input = "{groups()={ com.webcohesion.enunciate.beanval.validationgroups.GroupA.class,  com.webcohesion.enunciate.beanval.validationgroups.GroupB.class}}";
        assertEquals(Arrays.asList("com.webcohesion.enunciate.beanval.validationgroups.GroupA", "com.webcohesion.enunciate.beanval.validationgroups.GroupB"), ValidationGroupHelper.getGroupsOnField(input));
    }

    @Test
    public void getMultipleGroupsOnFieldWithOtherAttributes() {

        String input = "{names()={a,b},groups()={ com.webcohesion.enunciate.beanval.validationgroups.GroupA.class,  com.webcohesion.enunciate.beanval.validationgroups.GroupB.class},age()={12}}";
        assertEquals(Arrays.asList("com.webcohesion.enunciate.beanval.validationgroups.GroupA", "com.webcohesion.enunciate.beanval.validationgroups.GroupB"), ValidationGroupHelper.getGroupsOnField(input));
    }

    @Test
    public void getNoGroupsOnFieldWithOtherAttributes() {

        String input = "{names()={a,b},age()={12}}";
        assertEquals(Collections.emptyList(), ValidationGroupHelper.getGroupsOnField(input));
    }

    Map<String, AnnotationMirror> fieldAnnotations = new HashMap<>();

    @Test
    public void verifyNotRequiredWhenNotNullAnnotationIsMissing() {
        assertFalse("Field does not have @NotNull", ValidationGroupHelper.hasMatchingValidationGroup(" com.webcohesion.enunciate.beanval.validationgroups.GroupA,  com.webcohesion.enunciate.beanval.validationgroups.GroupB", null));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasDefaultGroupAndFieldHasGroups() {

        AnnotationMirror mockedMirror = Mockito.mock(AnnotationMirror.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},groups()={ com.webcohesion.enunciate.beanval.validationgroups.GroupA.class,  com.webcohesion.enunciate.beanval.validationgroups.GroupB.class},age()={12}}");

        assertFalse("Field does not have @NotNull with groups, but validation runs with default group", ValidationGroupHelper.hasMatchingValidationGroup("", mockedMirror));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasDefaultGroupAndFieldHasNoGroups() {

        AnnotationMirror mockedMirror = Mockito.mock(AnnotationMirror.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},age()={12}}");

        assertTrue("Field does have @NotNull without groups and validation runs with default group", ValidationGroupHelper.hasMatchingValidationGroup("", mockedMirror));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasGroupAndFieldHasNoGroups() {

        AnnotationMirror mockedMirror = Mockito.mock(AnnotationMirror.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},age()={12}}");

        assertFalse("Field does have @NotNull without groups, but validation runs with specific groups", ValidationGroupHelper.hasMatchingValidationGroup(" com.webcohesion.enunciate.beanval.validationgroups.GroupC,  com.webcohesion.enunciate.beanval.validationgroups.GroupB", mockedMirror));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasGroupAndFieldHasMatchingGroups() {

        AnnotationMirror mockedMirror = Mockito.mock(AnnotationMirror.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},groups()={ com.webcohesion.enunciate.beanval.validationgroups.GroupA.class,  com.webcohesion.enunciate.beanval.validationgroups.GroupB.class},age()={12}}");

        assertTrue("Field does have @NotNull with groups, and validation runs with specific group", ValidationGroupHelper.hasMatchingValidationGroup(" com.webcohesion.enunciate.beanval.validationgroups.GroupC,  com.webcohesion.enunciate.beanval.validationgroups.GroupB", mockedMirror));
    }

    @Test
    public void verifyNotRequiredWhenConfigHasGroupAndFieldHasNoMatchingGroups() {

        AnnotationMirror mockedMirror = Mockito.mock(AnnotationMirror.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},groups()={ com.webcohesion.enunciate.beanval.validationgroups.GroupA.class,  com.webcohesion.enunciate.beanval.validationgroups.GroupD.class},age()={12}}");

        assertFalse("Field does have @NotNull with groups, and validation runs with specific group, but none is matching", ValidationGroupHelper.hasMatchingValidationGroup(" com.webcohesion.enunciate.beanval.validationgroups.GroupC,  com.webcohesion.enunciate.beanval.validationgroups.GroupB", mockedMirror));
    }

    @Test
    public void verifySubValidationGroupOnFieldMatchesSuperClass() {

        AnnotationMirror mockedMirror = Mockito.mock(AnnotationMirror.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},groups()={ com.webcohesion.enunciate.beanval.validationgroups.SubGroupA.class,  com.webcohesion.enunciate.beanval.validationgroups.GroupD.class},age()={12}}");

        assertTrue("Subclass of active validation group on field should match validation group in Enunciate config", ValidationGroupHelper.hasMatchingValidationGroup(" com.webcohesion.enunciate.beanval.validationgroups.GroupC,  com.webcohesion.enunciate.beanval.validationgroups.GroupA", mockedMirror));
    }

    @Test
    public void verifySubValidationGroupOnFieldMatchesSuperClassWithOrderIrrelevant() {

        AnnotationMirror mockedMirror = Mockito.mock(AnnotationMirror.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},groups()={ com.webcohesion.enunciate.beanval.validationgroups.GroupC.class,  com.webcohesion.enunciate.beanval.validationgroups.SubGroupA.class},age()={12}}");

        assertTrue("Subclass of active validation group on field should match validation group in Enunciate config", ValidationGroupHelper.hasMatchingValidationGroup(" com.webcohesion.enunciate.beanval.validationgroups.GroupA,  com.webcohesion.enunciate.beanval.validationgroups.GroupB", mockedMirror));
    }

    @Test
    public void verifySuperValidationGroupOnFieldNotMatchesSuperClassWith() {

        AnnotationMirror mockedMirror = Mockito.mock(AnnotationMirror.class, Answers.RETURNS_DEEP_STUBS);
        Mockito.when(mockedMirror.getElementValues().toString()).thenReturn("{names()={a,b},groups()={ com.webcohesion.enunciate.beanval.validationgroups.GroupC.class,  com.webcohesion.enunciate.beanval.validationgroups.GroupA.class},age()={12}}");

        assertFalse("Superclass of active validation group on field should not match subclass of validation group in Enunciate config", ValidationGroupHelper.hasMatchingValidationGroup(" com.webcohesion.enunciate.beanval.validationgroups.SubGroupA,  com.webcohesion.enunciate.beanval.validationgroups.GroupB", mockedMirror));
    }
}