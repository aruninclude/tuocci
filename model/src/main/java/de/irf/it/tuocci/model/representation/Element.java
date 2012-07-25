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

package de.irf.it.tuocci.model.representation;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public interface Element {

    /**
     * Indicates the type of this element instance, as described in
     * {@link Type}.
     *
     * @return This element instance's type.
     */
    Type getType();

//    public Class<? extends Queryable> getImplementingClass()

    /**
     * Returns the categorisation scheme of this element instance.
     *
     * @return This element instance's categorisation scheme.
     */
    Category getCategory();

    /**
     * Returns the set of related element instances. The result may be empty,
     * if
     * no related element instances exist.
     *
     * @return This element instance's related element instances.
     */
    Element[] getRelated();

    /**
     * Returns the set of attachable element instances. The result may be
     * empty,
     * if no related element instances exist.
     *
     * @return This element instance's attachable element instances.
     */
    Element[] getAttaches();

    /**
     * Returns the set of attributes of this element instance. The result may
     * be
     * empty, if no attributes exist.
     *
     * @return This element instance's attributes.
     */
    Attribute[] getAttributes();

    /**
     * Returns the set of actions of this element instance. The result may be
     * empty, if no actions exist.
     *
     * @return This element instance's actions.
     */
    Element[] getActions();

    /**
     * An indicator for the type of a given {@link Element} instance.
     *
     * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
     *         Papaspyrou</a>
     * @version $Revision$ (as of $Date$)
     * @since 0.5 (plymouth)
     */
    public enum Type {
        KIND,
        MIXIN,
        ACTION,
        TAG;
    }

}
