<%@ page import="de.irf.it.tuocci.httpng.Location"%>
<%@ page import="java.util.Set"%>
<%@ page import="java.util.HashSet"%>
<%--
  ~ This file is part of tuOCCI.
  ~
  ~     tuOCCI is free software: you can redistribute it and/or modify
  ~     it under the terms of the GNU Lesser General Public License as
  ~     published by the Free Software Foundation, either version 3 of
  ~     the License, or (at your option) any later version.
  ~
  ~     tuOCCI is distributed in the hope that it will be useful,
  ~     but WITHOUT ANY WARRANTY; without even the implied warranty of
  ~     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  ~     GNU Lesser General Public License for more details.
  ~
  ~     You should have received a copy of the GNU Lesser General Public
  ~     License along with tuOCCI.  If not, see <http://www.gnu.org/licenses/>.
  --%>
<%@ page contentType="text/plain;charset=UTF-8" language="java" %>
<%@ page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    Location location = (Location) request.getAttribute("location");
    Set<String> paths = new HashSet<String>();
    for (Location l : location.resources()) {
        if (l.isEntity()) {
            paths.add(l.path());
        } // if
    } // for
    request.setAttribute("paths", paths);
%><c:forEach var="p" items="${paths}">
X-OCCI-Location: ${p}
</c:forEach>

