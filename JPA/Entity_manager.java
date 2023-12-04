// Persistence contexts
public class Test {
    public static void main(String[] args) {
        EntityManagerFactory emf =
        Persistence.createEntityManagerFactory("EmployeeExample");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        Employee e = em.find(Employee.class, 25);
        e.setSalary(2000);
        em.getTransaction().commit();
        em.close();
        emf.close();
    }
}

// OPERATIONS
// persist()
Department dept = em.find(Department.class, 30);
Employee emp = new Employee();
emp.setId(53);
emp.setName(“Fausto”);
//si se comenta la siguiente sentencia, el empleado no se vincula al departamento, a pesar de incluir al empleado en la lista del departamentoemp.setDepartment(dept);
dept.getEmployees().add(emp);
em.persist(emp);

// find() 
em.getTransaction().begin();
Project pp = em.find(Project.class, 1);
Collection<Employee> employees = pp.getEmployees();
Iterator<Employee> i t= employees.iterator();
while (it.hasNext()) {
    System.out.println(it.next());
}
em.getTransaction().commit();

// remove()
em.getTransaction().begin();
Employee emp = em.find(Employee.class, 25);
ParkingSpace ps = emp.getParkingSpace();
emp.setParkingSpace(null);
em.remove(ps);
em.getTransaction().commit();



