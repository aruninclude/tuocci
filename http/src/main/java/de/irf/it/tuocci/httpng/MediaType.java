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

package de.irf.it.tuocci.httpng;

import de.irf.it.tuocci.core.Entity;
import de.irf.it.tuocci.core.annotations.Category;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public enum MediaType {

    TEXT_URILIST("text/uri-list") {
        @Override
        public String renderEntity(Entity entity) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    },

    TEXT_PLAIN("text/plain") {
        @Override
        public String renderEntity(Entity entity) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    },

    TEXT_OCCI("text/occi") {
        @Override
        public String renderEntity(Entity entity) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    },

    APPLICATION_JSON("application/json") {
        @Override
        public String renderEntity(Entity entity) {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }
    };

    private String name;

    private MediaType(String name) {
        this.name = name;
    }

    public static MediaType parseFrom(String name) {
        MediaType result = null;
        for (MediaType value : MediaType.values()) {
            if (name.equals(value.name)) {
                result = value;
                break;
            } // if
        } // for
        return result;
    }

    abstract public String renderEntity(Entity entity);

}
