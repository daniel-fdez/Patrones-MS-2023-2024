public class FrontController extends HttpServlet {
    private ApplicationController applicationController;
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        // Initialize Request Processing(Stateless) Components
        applicationController = new ApplicationControllerImpl();
        applicationController.initialize(); 
    } 
    // called from doGet and doPost
    protected void process(HttpServletRequest request, HttpServletResponse response) throws java.io.IOException {
        // Create RequestContext based on request type
        RequestContextFactory requestContextFactory = RequestContextFactory.getInstance(); RequestContext requestContext = requestContextFactory.createRequestContext(request); // Request Processing
        ResponseContext responseContext = applicationController.handleRequest(requestContext);
        // View Management - Navigate and Dispatch to appropriate view
        Dispatcher dispatcher = new Dispatcher(request, response);
        responseContext.setDispatcher(dispatcher);
        applicationController.handleResponse(requestContext, responseContext); 
    } 
    //. . . 
} 
public class ApplicationControllerImpl implements ApplicationController {
    public void initialize() {
        commandMapper = CommandMapper.getInstance();
    }
    public ResponseContext handleRequest(RequestContext requestContext) { 
        ResponseContext responseContext = null;
        try { // validate request parameters
            requestContext.validate();
            // Translate command name into Command abstraction
            String commandName = requestContext.getCommandName(); 
            Command command = commandMapper.getCommand(commandName);
            // Invoke Command
            responseContext = command.execute(requestContext);
            // Identify View Name
            CommandMap mapEntry = commandMapper.getCommandMap(commandName); 
            String viewName = mapEntry.getViewName();
            responseContext.setLogicalViewName(viewName);
        } catch (ValidatorException e1) { // Handle Exception 
        }
        return responseContext; 
        }
        // . . . 
        //el libro no da código, pero debe de ser muy parecido a éste
    public ResponseContext handleResponse(RequestContext requestContext, ResponseContext responseContext) {
        responseContext().getDispatcher().dispatch(responseContext.getViewName(), responseContext.getData());
    }
}
// POJO ContextObject Factory
public class RequestContextFactory {
    public RequestContext createRequestContext(ServletRequest request) {
        RequestContext requestContext = null;
        try { // Identify command string from request object
            String commandId = getCommandId(request);
            // Identify POJO RequestContext Class for the
            //given Command, using CommandMap
            CommandMapper commandMapper = CommandMapper.getInstance(); 
            CommandMap mapEntry = commandMapper.getCommandMap(commandId);
            Class requestContextClass = mapEntry.getContextObjectClass(); // Instantiate POJO
            requestContext = (RequestContext)requestContextClass.newInstance(); // Set Protocol-specific Request object
            requestContext.initialize(request);
        } catch(java.lang.InstantiationException e) { // Handle Exception 
        } catch(java.lang.IllegalAccessException e) { }
        return requestContext; 
    }
    private String getCommandId(ServletRequest request) {
        String commandId = null;
        if (request instanceof HttpServletRequest) {
            String pathInfo = ((HttpServletRequest)request).getPathInfo(); 
            commandId = pathInfo.substring(1);
            // skip the leading '/' 
        }
        return commandId;
    }
}
// Specialized POJO Request Context – instances of //these get created by the RequestContextFactory class
public class ProjectRegistrationRequestContext extends HttpRequestContext {
    String projectName;
    String projectDescription;
    String projectManager;
    public ProjectRegistrationRequestContext(HttpServletRequest request) {
        super(request);
        initialize(request);
    }
    public ProjectRegistrationRequestContext() { }
    public void initialize(ServletRequest request) {
        setRequest(request);
        setProjectName(request.getParameter("projectName"));
        setProjectDescription(request.getParameter("projectdescription"));
        setProjectManager(request.getParameter("projectmanager"));
    }
    public String getProjectDescription() {
        return projectDescription;
    }
    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription; 
    }
    public String getProjectManager() {
        return projectManager; 
    }
    public void setProjectManager(String projectManager) {
        this.projectManager = projectManager; 
    }
    public String getProjectName() {
        return projectName; 
    }
    public void setProjectName(String projectName) {
        this.projectName = projectName; 
    }
    public boolean validate() {
        // Validation Rules
        return true; 
    }
    // . . .
}