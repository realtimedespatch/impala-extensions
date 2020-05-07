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

import org.impalaframework.extension.mvc.annotation.RequestAttribute;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the
 * {@link RequestAttribute} to inject an {@link javax.servlet.http.HttpSession}
 * as a method argument.
 * 
 * @author Phil Zoio
 */
public class RequestAttributeArgumentResolver extends BaseAttributeArgumentResolver {

	@Override
	protected boolean isSupportedAnnotation(Annotation annotation) {
		return (RequestAttribute.class.isInstance(annotation));
	}	
	
    protected String getAttribute(Annotation paramAnn) {
        String attributeName = null;
        if (RequestAttribute.class.isInstance(paramAnn)) {
            RequestAttribute attribute = (RequestAttribute) paramAnn;
            attributeName = attribute.value();
        }
        return attributeName;
    }

    protected Object getValue(NativeWebRequest webRequest, String attributeName, Annotation annotation) {
        Object attribute = webRequest.getAttribute(attributeName, WebRequest.SCOPE_REQUEST);
        return attribute;
    }

}
