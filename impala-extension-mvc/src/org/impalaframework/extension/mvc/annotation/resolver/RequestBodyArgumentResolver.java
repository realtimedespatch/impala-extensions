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

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.annotation.Annotation;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;

import org.impalaframework.extension.mvc.annotation.RequestBody;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.support.WebArgumentResolver;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * Custom {@link WebArgumentResolver} which can use the
 * {@link RequestBody} to inject the body of a request
 * as a method argument.
 * 
 * @author Phil Zoio
 */
public class RequestBodyArgumentResolver extends BaseAttributeArgumentResolver {
	
	@Override
	protected boolean isSupportedAnnotation(Annotation annotation) {
		return (RequestBody.class.isInstance(annotation));
	}

    protected String getAttribute(Annotation paramAnn) {
        if (RequestBody.class.isInstance(paramAnn)) {
            RequestBody attribute = (RequestBody) paramAnn;
            return attribute.value();
        }
        return null;
    }

    protected Object getValue(NativeWebRequest webRequest, String encoding, Annotation annotation) {
        Object nativeRequest = webRequest.getNativeRequest();
        if (nativeRequest instanceof HttpServletRequest) {
            HttpServletRequest req = (HttpServletRequest) nativeRequest;
            try {
                ServletInputStream inputStream = req.getInputStream();
                String body = FileCopyUtils.copyToString(new InputStreamReader(inputStream, encoding));
                return body;
            } catch (IOException e) {
                //FIXME log
                e.printStackTrace();
                return null;
            }
        }
        return null;
    }

}
