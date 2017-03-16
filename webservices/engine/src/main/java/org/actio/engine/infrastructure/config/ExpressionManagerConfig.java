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
package org.actio.engine.infrastructure.config;

import java.util.Map;

import org.actio.engine.infrastructure.expression.ExpressionResolver;
import org.activiti.engine.delegate.VariableScope;
import org.activiti.engine.impl.el.ReadOnlyMapELResolver;
import org.activiti.engine.impl.javax.el.ArrayELResolver;
import org.activiti.engine.impl.javax.el.BeanELResolver;
import org.activiti.engine.impl.javax.el.CompositeELResolver;
import org.activiti.engine.impl.javax.el.ELResolver;
import org.activiti.engine.impl.javax.el.JsonNodeELResolver;
import org.activiti.engine.impl.javax.el.ListELResolver;
import org.activiti.engine.impl.javax.el.MapELResolver;
import org.activiti.spring.ApplicationContextElResolver;
import org.activiti.spring.SpringExpressionManager;
import org.springframework.context.ApplicationContext;

/**
 * @author quirino.brizi
 *
 */
public class ExpressionManagerConfig extends SpringExpressionManager {

    public ExpressionManagerConfig(ApplicationContext applicationContext, Map<Object, Object> beans) {
        super(applicationContext, beans);
    }

    @Override
    protected ELResolver createElResolver(VariableScope variableScope) {
        CompositeELResolver compositeElResolver = new CompositeELResolver();
        compositeElResolver.add(new ExpressionResolver(variableScope));

        if (beans != null) {
            // Only expose limited set of beans in expressions
            compositeElResolver.add(new ReadOnlyMapELResolver(beans));
        } else {
            // Expose full application-context in expressions
            compositeElResolver.add(new ApplicationContextElResolver(applicationContext));
        }

        compositeElResolver.add(new ArrayELResolver());
        compositeElResolver.add(new ListELResolver());
        compositeElResolver.add(new MapELResolver());
        compositeElResolver.add(new JsonNodeELResolver());
        compositeElResolver.add(new BeanELResolver());
        return compositeElResolver;
    }
}
