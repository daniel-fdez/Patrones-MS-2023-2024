public class StockListDelegate {
    private StockList stockList;
    //accede al objeto fachada de la aplicación
    private StockListDelegate() throws StockListException {
        try { 
            InitialContext ctx = new InitialContext();
            stockList = (StockList) ctx.lookup(StockList.class.getName());
        } catch(Exception e) { throw new StockListException(e.getMessage());}
    }
    // ……………………
    //stock es un objeto transferencia para las acciones
    public void addStock(StockTO stock) throws StockListException {//delega en la fachada
        try { stockList.add(stock); }
        catch (Exception re) { throw new StockListException (re.getMessage()); }
    }
    // ……………………
    // ……………………
    //el delegado en un singleton
    public static StockListDelegate getInstance() throws StockListException {
        if (stockListDelegate == null)
            stockListDelegate = new StockListDelegate();
        return stockListDelegate;
    }
} 
//interfaz remoto de la fachada
@Remote
public interface StockList {
    public List getStockRatings();
    public List getAllAnalysts();
    public List getUnratedStocks();
    public void addStockRating(StockTO stockTO);
    public void addAnalystAnalystTO analystTO);
    public void addStock(StockTo stockTO);
}
//implementación de la fachada como EJB de sesión
//sin estado
@Stateless
public class StockListBean implements StockList {
    // …………………………………………
}
