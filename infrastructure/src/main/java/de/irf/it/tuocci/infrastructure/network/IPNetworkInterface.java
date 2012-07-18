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

package de.irf.it.tuocci.infrastructure.network;

import de.irf.it.tuocci.core.api.Attribute;
import de.irf.it.tuocci.core.api.Category;
import de.irf.it.tuocci.core.api.Mixin;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
@Category(term = "ipnetworkinterface", scheme = "http://schemas.ogf.org/occi/infrastructure/networkinterface#", title = "IP NetworkInterface Mixin")
@Mixin
public class IPNetworkInterface {

    @Attribute(name = "occi.networkinterface.address", mutable = true)
    private String address;

    @Attribute(name = "occi.networkinterface.gateway", required = false, mutable = true)
    private String gateway;

    @Attribute(name = "occi.networkinterface.allocation", mutable = true)
    private Allocation allocation;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public Allocation getAllocation() {
        return allocation;
    }

    public void setAllocation(Allocation allocation) {
        this.allocation = allocation;
    }

    public enum Allocation {

        DYNAMIC(),

        STATIC()

    }

}
