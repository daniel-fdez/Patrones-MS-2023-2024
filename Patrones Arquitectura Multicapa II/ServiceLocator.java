package com.corej2eepatterns.servicelocator;
// imports
public class ServiceLocator {
    // . . .
    public DataSource getDataSource(String dataSourceName) throws ServiceLocatorException {
        DataSource dataSource = null; 
        try {
            if (cache.containsKey(dataSourceName)) {
            dataSource = (DataSource) cache.get(dataSourceName); } else {dataSource = (DataSource) initialContext.lookup(dataSourceName); cache.put(dataSourceName, dataSource ); }
        } 
        catch (NamingException nex) { throw new ServiceLocatorException(nex); } 
        catch (Exception ex) { throw new ServiceLocatorException(ex); } 
        return dataSource;
    }
    // . . .
}
