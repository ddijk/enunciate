package com.webcohesion.enunciate.util;

import org.junit.Test;

import javax.annotation.Nonnull;
import javax.lang.model.element.Element;
import javax.lang.model.type.PrimitiveType;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BeanValidationUtilsTest {

    @Test
    public void verifyForElementWithoutNotNullAnnotation() {

        Element mockedElement = mock(Element.class);

        assertFalse(BeanValidationUtils.isNotNull(mockedElement));
    }

    @Test
    public void verifyForElementWithNotNullAnnotation() {

        Element mockedElement = mock(Element.class);
        Nonnull nonnull = mock(Nonnull.class);
        when(mockedElement.getAnnotation(Nonnull.class)).thenReturn(nonnull);

        assertTrue(BeanValidationUtils.isNotNull(mockedElement));
    }

    @Test
    public void verifyForPrimitiveType() {

        Element mockedElement = mock(Element.class);
        PrimitiveType mockedType = mock(PrimitiveType.class);
        when(mockedElement.asType()).thenReturn(mockedType);

        assertTrue(BeanValidationUtils.isNotNull(mockedElement));
    }

}