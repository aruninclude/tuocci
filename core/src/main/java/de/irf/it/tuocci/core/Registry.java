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

package de.irf.it.tuocci.core;

import de.irf.it.tuocci.annotations.Category;

import de.irf.it.tuocci.core.model.CategoryBean;

import java.util.HashSet;
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
//                this.classesByCategory.put(c, rc);
//                this.categoriesByClass.put(rc, c);
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
}
