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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.net.URI;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public class Category {

    private URI scheme;

    private String term;

    private String title;

    public Category() {
        super();
    }

    public Category(URI scheme, String term, String title) {
        this();
        this.scheme = scheme;
        this.term = term;
        this.title = title;
    }

    public boolean matchAnnotation(de.irf.it.tuocci.model.annotations.Category annotation) {
        return annotation.scheme().equals(this.scheme) && annotation.term().equals(this.term);
    }

    public URI getScheme() {
        return scheme;
    }

    public void setScheme(URI scheme) {
        this.scheme = scheme;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object other) {
        boolean result;

        if (this == other) {
            result = true;
        } // if
        else if (other == null || getClass() != other.getClass()) {
            result = false;
        } // else if
        else {
            Category candidate = (Category) other;
            result = new EqualsBuilder().append(this.scheme, candidate.scheme).append(this.term, candidate.term).isEquals();
        } // else

        return result;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(this.scheme).append(this.term).toHashCode();
    }
}
