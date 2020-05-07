/*
 * Copyright 2009-2010 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package org.impalaframework.extension.mvc.annotation.resolver;

import java.lang.annotation.Annotation;

import org.springframework.core.MethodParameter;
import org.springframework.util.Assert;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * Implements template method for attribute argument resolver implementations
 * @author Phil Zoio
 */
public abstract class BaseAttributeArgumentResolver implements WebArgumentResolver, HandlerMethodArgumentResolver {
	
	/**
	 * Returns true if annotation can be found.
	 */
	@Override
	public final boolean supportsParameter(MethodParameter parameter) {
		return findAnnotation(parameter) != null;
	}

	/**
	 * Simply calls {@link WebArgumentResolver#resolveArgument(MethodParameter, NativeWebRequest)}
	 */
	@Override
	public Object resolveArgument(
			MethodParameter methodParameter, 
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, 
			WebDataBinderFactory binderFactory) throws Exception {
		return resolveArgument(methodParameter, webRequest);
	}
    
    public Object resolveArgument(
    		MethodParameter methodParameter,
            NativeWebRequest webRequest) throws Exception {

		Annotation annotation = findAnnotation(methodParameter);
		Assert.state(annotation != null, "Expecting annotation to not be null");
        String attributeName = getAttribute(annotation);        
        Object value = getValue(webRequest, attributeName, annotation);
        
        return value;
    }

	/**
	 * Private method that loops through method parameter annotations, attempting to
	 * find one that is supported using this argument resolver.
	 */
	private Annotation findAnnotation(MethodParameter methodParameter) {
        
        Annotation[] paramAnns = methodParameter.getParameterAnnotations();
        for (Annotation annotation : paramAnns) {
            if (isSupportedAnnotation(annotation)) {
            	return annotation;
            }
        }
        
		return null;
	}
	
	/**
	 * Returns true if specified annotation is supported.
	 */
	protected abstract boolean isSupportedAnnotation(Annotation annotation);

	/**
	 * Gets attribute from the supported annotation.
	 */
    protected abstract String getAttribute(Annotation annotation);

    /**
     * Extract value from request for specified attribute. Annotation can be used for extra checks.
     */
    protected abstract Object getValue(NativeWebRequest webRequest, String attributeName, Annotation annotation);

}
