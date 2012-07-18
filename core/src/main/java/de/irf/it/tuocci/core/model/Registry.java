/*
 * This file is part of tuOCCI.
 *
 *     tuOCCI is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Lesser General Public License as
 *     published by the Free Software Foundation, either version 3 of
 *     the License, or (at your option) any later version.
 *
 *     tuOCCI is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU Lesser General Public License for more details.
 *
 *     You should have received a copy of the GNU Lesser General Public
 *     License along with tuOCCI.  If not, see <http://www.gnu.org/licenses/>.
 */

// $Id$ //

package de.irf.it.tuocci.core.model;

import de.irf.it.tuocci.core.api.Category;
import de.irf.it.tuocci.core.api.Entity;
import de.irf.it.tuocci.core.api.Kind;
import de.irf.it.tuocci.core.model.representation.CategoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public class Registry{

    private static Registry instance;

    public static Registry getInstance() {
        if(instance == null) {
            instance = new Registry();
        } // if
        return instance;
    }

    private Set<CategoryBean> categoryBeanSet;

    public Registry() {
        this.categoryBeanSet = new HashSet<CategoryBean>();
    }



    /**
     * TODO: not yet commented.
     *
     * @param registeredCategories
     */
    public Registry(Set<Class<?>> registeredCategories) {
        for(Class<?> rc : registeredCategories) {
            if(rc.isAnnotationPresent(Category.class)) {
                Category c = rc.getAnnotation(Category.class);
            } // if
            else {
                String message = new StringBuilder("type not acceptable: '@Category' annotations missing on \"")
                        .append(rc.getName())
                        .append("\".")
                        .toString();
                throw new IllegalArgumentException(message);
            } // else
        } // for
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
            throws BeansException {
        Map<String, Object> entityTypes = beanFactory.getBeansWithAnnotation(Category.class);
        for(Object o : entityTypes.values()) {
            if(o instanceof Entity) {
                Entity e = (Entity) o;
                
            } // if


        } // for

        //To change body of implemented methods use File | Settings | File Templates.
    }
}
