package com.webcohesion.enunciate.beanval;

import javax.lang.model.element.AnnotationMirror;
import java.util.Map;

public class BeanValidationContext {

   private   String targetGroups;

   private Map<String, AnnotationMirror> fieldAnnotations;

    public BeanValidationContext(String targetGroups, Map<String, AnnotationMirror> fieldAnnotations) {
        this.targetGroups = targetGroups;
        this.fieldAnnotations = fieldAnnotations;
    }

    public String getTargetGroups() {
        return targetGroups;
    }

    public Map<String, AnnotationMirror> getFieldAnnotations() {
        return fieldAnnotations;
    }

    public void setTargetGroups(String targetGroups) {
        this.targetGroups = targetGroups;
    }

    public void setFieldAnnotations(Map<String, AnnotationMirror> fieldAnnotations) {
        this.fieldAnnotations = fieldAnnotations;
    }
}
