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

package de.irf.it.tuocci.model.representation;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public interface ModelElement {

    /**
     * Indicates the type of this model instance, as described in
     * {@link Type}.
     *
     * @return This model instance's type.
     */
    Type getModelType();

    /**
     * Returns the categorisation scheme of this model instance.
     *
     * @return This model instance's categorisation scheme.
     */
    Category getCategory();

    /**
     * Returns the set of related model instances. The result may be empty, if
     * no related model instances exist.
     *
     * @return This model instance's related model instances.
     */
    ModelElement[] getRelated();

    /**
     * Returns the set of attributes of this model instance. The result may be
     * empty, if no attributes exist.
     *
     * @return This model instance's attributes.
     */
    Attribute[] getAttributes();

    /**
     * Returns the set of actions of this model instance. The result may be
     * empty, if no actions exist.
     *
     * @return This model instance's actions.
     */
    ModelElement[] getActions();

    // Class<? extends Entity> getEntityType();

    /**
     * An indicator for the type of a given {@link ModelElement} instance.
     *
     * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
     *         Papaspyrou</a>
     * @version $Revision$ (as of $Date$)
     *
     * @since 0.5 (plymouth)
     */
    public enum Type {
        KIND,
        MIXIN,
        ACTION,
        TAG;
    }

}
