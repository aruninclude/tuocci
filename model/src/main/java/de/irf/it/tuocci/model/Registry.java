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

package de.irf.it.tuocci.model;

import de.irf.it.tuocci.model.exceptions.ActionTriggerException;
import de.irf.it.tuocci.model.exceptions.AttributeAccessException;
import de.irf.it.tuocci.model.representation.Attribute;
import de.irf.it.tuocci.model.representation.Category;
import de.irf.it.tuocci.model.representation.ModelElement;

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

    private Set<ModelElement> modelElementSet;

    public Registry() {
        this.modelElementSet = new HashSet<ModelElement>();
        for (ModelElement modelElementInfo : ServiceLoader.load(ModelElement.class)) {
            this.modelElementSet.add(modelElementInfo);
        } // for
    }

    public ModelElement[] getRegisteredModelElements() {
        return this.modelElementSet.toArray(new ModelElement[this.modelElementSet.size()]);
    }

    public final void triggerAction(ModelAware target, Category actionCategory, Map<Attribute, String> actionParameters)
            throws ActionTriggerException, AttributeAccessException {

    }
}
