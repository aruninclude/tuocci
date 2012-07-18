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

package de.irf.it.tuocci.httpng;

import de.irf.it.tuocci.core.api.Entity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public class Location {

    private String path;

    private Set<Location> resources;

    private Entity entity;

    protected Location() {
        this.resources = new HashSet<Location>();
    }

    public Location(String path) {
        this();
        this.path = path;
    }

    public Location(String path, Entity entity) {
        this(path);
        this.entity = entity;
    }

    public String getPath() {
        return this.path;
    }

    public void addResource(Location location) {
        this.resources.add(location);
    }

    public void removeResource(Location location) {
        this.resources.remove(location);
    }

    public Set<Location> getResources() {
        return Collections.unmodifiableSet(this.resources);
    }

    public Entity getEntity() {
        return this.entity;
    }

    public boolean isCollection() {
        return this.path.endsWith("/");
    }

    public boolean isEntity() {
        return !this.path.endsWith("/");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        Location other = (Location) o;
        return new EqualsBuilder().append(this.path, other.path).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(resources).append(entity).toHashCode();
    }
}
