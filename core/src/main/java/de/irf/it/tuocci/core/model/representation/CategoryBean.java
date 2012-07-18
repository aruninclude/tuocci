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

package de.irf.it.tuocci.core.model.representation;

import de.irf.it.tuocci.core.model.Type;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URI;
import java.util.Set;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public class CategoryBean {

    private Type type;

    private String name;

    private String term;

    private URI scheme;

    private String title;

    private Set<AttributeBean> attributes;

    public Type getType() {
        return this.type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerm() {
        return this.term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public URI getScheme() {
        return this.scheme;
    }

    public void setScheme(URI scheme) {
        this.scheme = scheme;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Set<AttributeBean> getAttributes() {
        return this.attributes;
    }

    public void setAttributes(Set<AttributeBean> attributes) {
        this.attributes = attributes;
    }

    @Override
    public boolean equals(Object other) {
        boolean result;

        if (this == other) {
            result = true;
        } // if
        if (other == null || getClass() != other.getClass()) {
            result = false;
        } // if

        CategoryBean that = (CategoryBean) other;
        result = new EqualsBuilder().append(this.term, that.term).append(this.scheme, that.scheme).isEquals();

        return result;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.term).append(this.scheme).toHashCode();
    }
}
