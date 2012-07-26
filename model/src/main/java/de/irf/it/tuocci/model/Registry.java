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

package de.irf.it.tuocci.model;

import de.irf.it.tuocci.model.representation.Element;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public class Registry {

    private static Registry instance;

    public static Registry getInstance() {
        if (instance == null) {
            instance = new Registry();
        } // if
        return instance;
    }

    private Set<Element> elementSet;

    private Map<Class<?>, Element> classToElementMap;

    public Registry() {
        this.elementSet = new HashSet<Element>();
        this.classToElementMap = new HashMap<Class<?>, Element>();

        for (Element modelElementInfo : ServiceLoader.load(Element.class)) {
            this.elementSet.add(modelElementInfo);
        } // for
    }

    public Element[] getRegisteredModelElements() {
        return this.elementSet.toArray(new Element[this.elementSet.size()]);
    }

    public Element findElementForClass(Class<?> c1ass) {
        Element result;

        if (!this.classToElementMap.containsKey(c1ass)) {
            de.irf.it.tuocci.model.annotations.Category a = c1ass.getAnnotation(de.irf.it.tuocci.model.annotations.Category.class);
            for (Element e : this.elementSet) {
                if (e.getCategory().matchAnnotation(a)) {
                    this.classToElementMap.put(c1ass, e);
                    break;
                } // if
            } // if
        } // if
        result = this.classToElementMap.get(c1ass);

        return result;
    }
}
