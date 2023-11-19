public class FrontController extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        processRequest(request, response);
    }
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
        String page;
        ApplicationResources resource = ApplicationResources.getInstance();
        try {
            RequestContext requestContext = new RequestContext(request, response);
            ApplicationController applicationController = new ApplicationControllerImpl();
            ResponseContext responseContext = applicationController.handleRequest(requestContext);
            applicationController.handleResponse(requestContext, responseContext);
        } catch (Exception e) {
            LogManager.logMessage("FrontController:exception : " + e.getMessage());request.setAttribute(resource.getMessageAttr(), "Exception occurred : " + e.getMessage());page = resource.getErrorPage(e);
            dispatch(request, response, page);
        }
}
//sólo se utiliza esta función si hay error
protected void dispatch(HttpServletRequest request, HttpServletResponse response, String page) throws javax.servlet.ServletException, java.io.IOException {
        RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher(page);
        dispatcher.forward(request, response); 
    }
}
