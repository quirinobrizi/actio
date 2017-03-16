/*******************************************************************************
 * Copyright [2016] [Quirino Brizi (quirino.brizi@gmail.com)]
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.actio.engine.infrastructure.expression;

import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.regex.Pattern;

import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.el.VariableScopeElResolver;
import org.activiti.engine.impl.javax.el.ELContext;
import org.apache.commons.lang3.ClassUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.util.ReflectionUtils;

/**
 * @author quirino.brizi
 *
 */
public class ExpressionResolver extends VariableScopeElResolver {

    private static Pattern TEMPLATE_PATTERN = Pattern.compile(".*\\$\\{.+\\}.*");
    private VelocityEngine velocityEngine;

    public ExpressionResolver(VariableScope variableScope) {
        super(variableScope);
        this.velocityEngine = new VelocityEngine();
        this.velocityEngine.init();
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) {
        Object value = super.getValue(context, base, property);
        if (null != value && String.class.isAssignableFrom(value.getClass())) {
            value = tryResolveExpressionOrTemplate(value);
        }
        return value;
    }

    private Object tryResolveExpressionOrTemplate(Object property) {
        if (isExpression(property)) {
            return resolveExpression(property);
        } else if (isTemplate(property)) {
            return resolveTemplate((String) property, this.variableScope.getVariables());
        }
        return property;
    }

    private Object resolveTemplate(String property, Map<String, Object> variables) {
        StringWriter writer = new StringWriter();
        velocityEngine.evaluate(new VelocityContext(variables), writer, "actio", property);
        return writer.toString();
    }

    private Object resolveExpression(Object property) {
        String tmp = (String) property;
        String key = tmp.substring(2, tmp.length() - 1);
        String[] parts = key.split("\\.");
        Object obj = this.variableScope.getVariable(parts[0], true);
        for (int i = 1; i < parts.length; i++) {
            if (null == obj) {
                return obj;
            }
            if (ClassUtils.isPrimitiveOrWrapper(obj.getClass())) {
                return obj;
            } else if (Map.class.isAssignableFrom(obj.getClass())) {
                obj = ((Map<?, ?>) obj).get(parts[i]);
            } else if (!Collection.class.isAssignableFrom(obj.getClass())) {
                Field field = ReflectionUtils.findField(obj.getClass(), parts[1]);
                if (null != field) {
                    field.setAccessible(true);
                    obj = ReflectionUtils.getField(field, obj);
                }
            }
        }
        return obj;
    }

    private boolean isExpression(Object property) {
        return ((String) property).startsWith("${") && ((String) property).endsWith("}");
    }

    private boolean isTemplate(Object property) {
        return TEMPLATE_PATTERN.matcher((String) property).find();
    }
}
