package org.soademo.customerservice.business;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.soademo.customerservice.persistence.Customer;
@Stateless(name = "CustomerService")
public class CustomerServiceBean implements CustomerService, CustomerServiceLocal {
    @PersistenceContext(unitName = "customerServiceUnit" )
    private EntityManager em;
    public CustomerServiceBean() { }
    public Object mergeEntity(Object entity) {
        return em.merge(entity);
    }
    public Object persistEntity(Object entity) {
        em.persist(entity);
        return entity;
    }
    public Object refreshEntity(Object entity) {
        em.refresh(entity);
        return entity; 
    }
    public void removeEntity(Object entity) {
        em.remove(em.merge(entity));
    }
    /** <code>select object(cust) from Customer cust where cust.custid = :custid</code> */
    public Customer queryCustomerFindCustomerById(String custid) {
        return (Customer)em.createNamedQuery("Customer.findCustomerById").setParameter("custid", custid).getSingleResult();
    }
    public String getCustomerStatus(String CustomerID) {
        return findCustomerById(CustomerID).getStatus();
    }
    public String addNewCustomer(Customer customer) {
        em.persist(customer);
        return "New customer added sucessfully to customer database";
    }
    public Customer findCustomerByEmail(String email, String password) {
        return (Customer)em.createNamedQuery("Customer.findCustomerByEmail").setParameter("email", email).setParameter("password", password).getSingleResult();
    }
}
