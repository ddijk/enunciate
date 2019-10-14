package com.webcohesion.enunciate.beanval;

import javax.lang.model.element.*;

public class NotNullVisitor<R, P extends BeanValidationContext> implements ElementVisitor<R, P> {

    private boolean required;


    public boolean isRequired() {

        return required;
    }


    @Override
    public R visit(Element e, P p) {
        required = ValidationGroupHelper.isRequired(p.getTargetGroups(), p.getFieldAnnotations());
        return null;
    }

    @Override
    public R visit(Element e) {
        return null;
    }

    @Override
    public R visitPackage(PackageElement e, P p) {
        return null;
    }

    @Override
    public R visitType(TypeElement e, P p) {
        return null;
    }

    @Override
    public R visitVariable(VariableElement e, P p) {
        return null;
    }

    @Override
    public R visitExecutable(ExecutableElement e, P p) {
        return null;
    }

    @Override
    public R visitTypeParameter(TypeParameterElement e, P p) {
        return null;
    }

    @Override
    public R visitUnknown(Element e, P p) {
        return null;
    }
}
