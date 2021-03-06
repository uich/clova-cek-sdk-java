/*
 * Copyright 2018 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.linecorp.clova.extension.boot.handler.resolver;

import org.springframework.core.MethodParameter;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.linecorp.clova.extension.boot.exception.MissingContextException;
import com.linecorp.clova.extension.boot.exception.UnsupportedHandlerArgumentException;
import com.linecorp.clova.extension.boot.handler.annnotation.ContextValue;
import com.linecorp.clova.extension.boot.message.context.ContextProperty;
import com.linecorp.clova.extension.boot.message.request.CEKRequestMessage;

/**
 * {@link CEKRequestHandlerArgumentResolver} for extracting {@link ContextProperty}.
 */
public class CEKContextPropertyArgumentResolver extends CEKRequestHandlerArgumentResolverSupport {

    private static final String PARAMS_NAME = "context";

    public CEKContextPropertyArgumentResolver(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @Override
    public boolean supports(MethodParameter methodParam) {
        if (canConvert(ContextProperty.class, methodParam)) {
            return true;
        }
        if (methodParam.hasParameterAnnotation(ContextValue.class)) {
            throw new UnsupportedHandlerArgumentException(
                    methodParam,
                    "The parameter annotated with ContextValue should be implemented ContextProperty.");
        }
        return false;
    }

    @Override
    public Object resolve(MethodParameter methodParam, CEKRequestMessage requestMessage) {
        return extractAndConvertMethodParam(PARAMS_NAME, requestMessage.getContext(), methodParam,
                                            MissingContextException::new);
    }

}
