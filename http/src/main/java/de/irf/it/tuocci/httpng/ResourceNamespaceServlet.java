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

//import de.irf.it.tuocci.httpng.getResources.Collection;

import de.irf.it.tuocci.httpng.mime.MediaType;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.TreeMap;

/**
 * TODO: not yet commented.
 *
 * @author <a href="mailto:alexander.papaspyrou@tu-dortmund.de>Alexander
 *         Papaspyrou</a>
 * @version $Revision$ (as of $Date$)
 */
public class ResourceNamespaceServlet
        extends HttpServlet {

    private Map<String, Location> resources = new TreeMap<String, Location>();

    @Override
    public void init()
            throws ServletException {
        Location l = new Location("/wurst/");
        Location a = new Location("/wurst/schinken");
        Location b = new Location("/wurst/salami");
        l.addResource(a);
        l.addResource(b);
        this.resources.put(l.getPath(), l);
        this.resources.put(a.getPath(), a);
        this.resources.put(b.getPath(), b);
    }

    //private Map<String, Entity> pathToEntityMapping;

    private String getBaseUrl(HttpServletRequest req) {
        StringBuilder sb = new StringBuilder();
        sb.append(req.getScheme());
        sb.append("://");
        sb.append(req.getServerName());
        sb.append(":");
        sb.append(req.getServerPort());
        sb.append(req.getContextPath());
        sb.append(req.getServletPath());
        return sb.toString();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        super.doPost(request, response);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        /*
         * Determine client-consumable media type.
         */
        MediaType mt = null;
        if (request.getHeader("Accept") == null) {
            mt = MediaType.TEXT_PLAIN;
        } // if
        else {
            mt = MediaType.parseFrom(request.getHeader("Accept"));
        } // else

        /*
         * Determine whether requested resource exists.
         */
        Location location = null;
        if (!this.resources.containsKey(path)) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        location = resources.get(path);

        /*
         * Handle ETags.
         */
        String inm = request.getHeader("If-None-Match");
        String etag = Integer.toHexString(location.hashCode());

        if (etag.equals(inm)) {
            response.sendError(HttpServletResponse.SC_NOT_MODIFIED);
            return;
        } // if

        response.addHeader("ETag", etag);

        /*
         * Determine the resource type.
         */
        if (location.isCollection()) {
            /*
             * Handle a collection resource
             */

            // TODO: implement filtering

            PrintWriter pw = response.getWriter();
            switch (mt) {
                case TEXT_URILIST:
                    for (Location l : location.getResources()) {
                        pw.print(this.getBaseUrl(request));
                        pw.println(l.getPath());
                    } // for
                    break; // TEXT_URI_LIST
                case TEXT_OCCI:
                    for (Location l : location.getResources()) {
                        if (l.isEntity()) {
                            StringBuffer sb = new StringBuffer();
                            sb.append(this.getBaseUrl(request));
                            sb.append(l.getPath());
                            response.addHeader("X-OCCI-Location", sb.toString());
                        } // if
                    } // for
                    break; // TEXT_OCCI
                case STAR_STAR:
                case TEXT_PLAIN:
                    request.setAttribute("baseUrl", path);
                    request.setAttribute("location", location);
                    ServletContext sc = this.getServletContext();
                    RequestDispatcher rd = sc.getRequestDispatcher("/text_plain.jsp");
                    rd.forward(request, response);
/*
                    for (Location l : location.getResources()) {
                        if (l.isEntity()) {
                            pw.print("X-OCCI-Location: ");
                            pw.print(this.getBaseUrl(request));
                            pw.println(l.getPath());
                        } // if
                    } // for
 */
                    break; // TEXT_PLAIN | STAR_STAR
                default:
                    response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } // switch
        }
        else {
            /*
             * Handle an entity resource.
             */
            String rendering = mt.renderEntities(location.getEntity());
            String[] lines = rendering.split("[\r\n]+");

            switch (mt) {
                case TEXT_OCCI:
                    for (String line : lines) {
                        int c = line.indexOf(":");
                        response.addHeader(line.substring(c), line.substring(c + 2, line.length()));
                    } // for
                    break; // TEXT_OCCI
                case TEXT_PLAIN:
                case STAR_STAR:
                    PrintWriter pw = response.getWriter();
                    pw.write(rendering);
                    pw.flush();
                    pw.close();
                    break; // TEXT_PLAIN | STAR_STAR
                default:
                    response.sendError(HttpServletResponse.SC_NOT_ACCEPTABLE);
            } // switch
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doPut(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doDelete(req, resp);    //To change body of overridden methods use File | Settings | File Templates.
    }

}
