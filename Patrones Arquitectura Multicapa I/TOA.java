public class TBestOfShop {
    protected TCustomer bestCustomer;
    protected TProduct bestProduct;
    protected TBrand bestBrand;
    // ……..
}
public class ShopTOA {
    TBestOfShop bestOfShop() { 
        // ……………..
        TCustomer customer = CustomerDAO.best();
        TProduct product = ProductDAO.best();
        TBrand brand = BrandDAO.best();
        return new TBestOfShop(customer, product, brand); 
    }
    // …… 
}
