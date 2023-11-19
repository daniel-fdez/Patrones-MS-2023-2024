public final class HitCounterFilter implements Filter {
    private FilterConfig filterConfig = null;
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }
    public void destroy() {
        this.filterConfig = null;
    }
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (filterConfig == null) return;
        StringWriter sw = new StringWriter();
        PrintWriter writer = new PrintWriter(sw);
        Counter counter = (Counter) filterConfig.getServletContext().getAttribute("hitCounter");
        writer.println();
        writer.println("===============");
        writer.println("The number of hits is: " + counter.incCounter());writer.println("===============");
        // Log the resulting string
        writer.flush();
        filterConfig.getServletContext().log(sw.getBuffer().toString());
        // ...
        chain.doFilter(request, wrapper);
        // ...
    }
}
