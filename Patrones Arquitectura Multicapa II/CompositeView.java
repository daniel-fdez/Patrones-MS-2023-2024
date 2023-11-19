// Ejemplo
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core" %>
<%@ taglib uri="/web-INF/corej2eetaglibrary.tld" prefix="cjp" %> 
<jsp:useBean id="contentFeeder"class="corepatterns.compositeview.javabean.ContentFeeder" scope="request"/>
<table valign="top" cellpadding="30%" width="100%">
    <cjp:personalizer interest='global'>
    <tr>
        <td><B><c:out value="${contentFeeder.worldNews}"/></B></td> 
    </tr>
    </cjp:personalizer>
    <cjp:personalizer interest='technology'>
    <tr>
        <td><U><c:out value="${contentFeeder.technologyNews}"/></U></td>
    </tr>
    </cjp:personalizer>

    <cjp:personalizer interest='weird'>
    <tr>
        <td><I><c:out value="${contentFeeder.weirdNews}"/></I></td> 
    </tr>
    </cjp:personalizer>

    <cjp:personalizer interest='astronomy'>
    <tr>
        <td><c:out value="${contentFeeder.astronomyNews}"/></td>
    </tr>
    </cjp:personalizer>
</table>
// Ejemplo
<html>
    <body>
        <jsp:include page="/jsp/CompositeView/javabean/banner.seg" flush="true"/>
            <table width="100%">
                <tr align="left" valign="middle">
                    <td width="20%">
                        <jsp:include page="/jsp/CompositeView/javabean/ProfilePane.jsp" flush="true"/> 
                    </td>
                    <td width="70%" align="center">
                        <jsp:include page="/jsp/CompositeView/javabean/mainpanel.jsp" flush="true"/>
                    </td>
                </tr>
            </table>
        <jsp:include page="/jsp/CompositeView/javabean/footer.seg" flush="true"/> 
    </body>
</html>