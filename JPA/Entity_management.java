package examples.model;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
public class EmployeeService {
    //cada servicio de aplicación tiene su entity manager. Esto puede generar problemas en entornos concurrentes
    protected EntityManager em;
    public EmployeeService(EntityManager em) { this.em = em; }
    public Employee createEmployee(int id, String name, long salary){ 
        Employee emp = new Employee(id);
        emp.setName(name);
        emp.setSalary(salary);
        em.persist(emp);
        return emp;
    }
    public void removeEmployee(int id) {
        Employee emp = findEmployee(id);
        if (emp != null) {
            em.remove(emp);
        }
    }
    public Employee raiseEmployeeSalary(int id, long raise) {
        Employee emp = em.find(Employee.class, id);
        if (emp != null) {
            emp.setSalary(emp.getSalary() + raise); 
        }
        return emp; 
    }
    public Employee findEmployee(int id) {
        return em.find(Employee.class, id); 
    }
    public List<Employee> findAllEmployees() {
        TypedQuery query = em.createQuery("SELECT e FROM Employee e", Employee.class);
        return query.getResultList();
    }
}


package examples.client;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import examples.model.Employee;
import examples.model.EmployeeService;
public class EmployeeTest {
    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("EmployeeExample");
        EntityManager em = emf.createEntityManager();
        //nuestros SAs acceden por si mismos al entity manager
        EmployeeService service = new EmployeeService(em);
        //nosotros gestionaremos las transacciones desde los SAs
        
        // create and persist an employee
        em.getTransaction().begin();
        Employee emp = service.createEmployee(158, "John Doe", 45000);
        em.getTransaction().commit();
        System.out.println("Persisted " + emp);
        
        // find a specific employee
        emp = service.findEmployee(158);
        System.out.println("Found " + emp);
        
        // find all employees
        Collection<Employee> emps = service.findAllEmployees();
        for (Employee e : emps)
            System.out.println("Found Employee: " + e);
        
        // update the employee
        em.getTransaction().begin();
        emp = service.raiseEmployeeSalary(158, 1000);
        em.getTransaction().commit();
        System.out.println("Updated " + emp);
        
        // remove an employee
        em.getTransaction().begin();
        service.removeEmployee(158);
        em.getTransaction().commit();
        System.out.println("Removed Employee 158");
        
        // close the EM and EMF when done
        em.close();
        emf.close();
    }
}
