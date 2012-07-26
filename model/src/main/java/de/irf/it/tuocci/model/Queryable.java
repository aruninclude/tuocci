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

package de.irf.it.tuocci.model;

import de.irf.it.tuocci.model.exceptions.ActionTriggerException;
import de.irf.it.tuocci.model.exceptions.AttributeAccessException;
import de.irf.it.tuocci.model.representation.Attribute;
import de.irf.it.tuocci.model.representation.Element;

import java.util.Map;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public interface Queryable {

    /**
     * Returns the attributes currently accessible on this
     * <code>Queryable</code>.
     * <p/>
     * The result contains all attribute representations that are currently
     * usable (but not necessarily set) on this
     * object. Because of the dynamic nature of mixins induced attributes, the
     * result of this method may vary over
     * time. If no attributes are usable, the result will be an empty array.
     *
     * @return The attribute representations currently accessible on this
     *         object
     *         (including an empty array if none are
     *         currently accessible).
     */
    Attribute[] retrieveAccessibleAttributes();

    /**
     * Retrieves the value of an attribute on this entity or an attached mixin.
     * <p/>
     * More specifically, this method searches both the entity instance and all
     * currently attached mixins for an attribute with the provided name and,
     * if found, returns its value.
     *
     * @param attribute
     *         The model representation of the attribute to be retrieved.
     * @return The current value of the requested attribute.
     *
     * @throws AttributeAccessException
     *         if the requested attribute cannot be found on this entity or any
     *         attached mixin, or underlying retrieval failed.
     */
    String getValue(Attribute attribute)
            throws AttributeAccessException;

    /**
     * Manipulates the value of an attribute on this entity or an attached
     * mixin.
     * <p/>
     * More specifically, this method searches both the entity instance and all
     * currently attached mixins for an attribute with the provided name and,
     * if found, modifies its value to the provided argument.
     *
     * @param attribute
     *         The model representation of the attribute to be set.
     * @param value
     *         The new value for the requested attribute.
     * @throws AttributeAccessException
     *         if the requested attribute cannot be found on this entity or any
     *         attached mixin, or underlying manipulation failed.
     */
    void setValue(Attribute attribute, String value)
            throws AttributeAccessException;

    /**
     * Triggers the action identified by the provided term/scheme combination,
     * using the provided attribute values as arguments to it.
     * <p/>
     * More specifically, this method searches both the entity instance and all
     * currently attached mixins for a method with an {@link
     * de.irf.it.tuocci.model.annotations.Action} annotation
     * of {@link de.irf.it.tuocci.model.annotations.Category} identified by the
     * term/scheme combination as
     * provided
     * and, if found, invokes this method with the attribute values as its
     * arguments.
     *
     * @param action
     *         The model representation identifying the <code>Action</code> to
     *         be triggered.
     * @param parameters
     *         A map of attribute model representations and values for the
     *         triggering of the requested action, as denoted in the action
     *         definition.
     * @throws ActionTriggerException
     *         if the underlying method for the requested action could not be
     *         found or invoked, a signature mismatch is detected, the
     *         underlying mechanisms of the invocation fail, an attribute
     *         cannot
     *         be found on this method, or the underlying creation of the
     *         corresponding attribute instance failed.
     */
    void triggerAction(Element action, Map<Attribute, String> parameters)
            throws ActionTriggerException;
}
