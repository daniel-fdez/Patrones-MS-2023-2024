interface ApplicationController {
    ResponseContext handleRequest(RequestContext requestContext);
    void handleResponse(RequestContext requestContext, ResponseContext responseContext);
}

public class WebApplicationController implements ApplicationController {
    public ResponseContext handleRequest(RequestContext requestContext) {
        ResponseContext responseContext = null;
        try {
            String commandName = requestContext.getCommandName();
            CommandFactory commandFactory = CommandFactory.getInstance();
            Command command = commandFactory.getCommand(commandName);
            CommandProcessor commandProcessor = new CommandProcessor();
            responseContext = commandProcessor.invoke(command, requestContext);
        } catch (java.lang.InstantiationException e) {
        } catch (java.lang.IllegalAccessException e) {
        }
        return responseContext; 
    }
    //. . . 
}
