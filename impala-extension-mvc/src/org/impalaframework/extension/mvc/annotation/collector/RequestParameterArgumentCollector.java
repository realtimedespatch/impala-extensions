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

package org.impalaframework.extension.mvc.annotation.collector;

import org.springframework.beans.TypeConverter;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * {@link ArgumentCollector} which uses {@link RequestParam} annotation as data source
 * @author Phil Zoio
 */
public class RequestParameterArgumentCollector implements ArgumentCollector {

    private RequestParam annotation;
    
    private Class<?> type;

    public RequestParameterArgumentCollector(RequestParam annotation, Class<?> type) {
        super();
        this.annotation = annotation;
        this.type = type;
    }

    public Object getArgument(NativeWebRequest request, ExtendedModelMap implicitModel, TypeConverter typeConverter) {
        String value = annotation.value();
        boolean required = annotation.required();
        
        String parameter = request.getParameter(value);
        
        /*
        if (!StringUtils.hasText(parameter)) {
            parameter = annotation.defaultValue();
        }*/

        if (!StringUtils.hasText(parameter)) {
            if (required) {
                throw new IllegalArgumentException("Parameter '" + value + "' is required.");
            }
        }
        
        return typeConverter.convertIfNecessary(parameter, type);
    }
}
