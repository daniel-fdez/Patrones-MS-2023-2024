public class Employee {
    private int id;
    private String name;
    private long salary;
    private Department department;
    // …………………
    Department getDepartment() { return department; }
    // …………………
}
public class Department {
    private int id;
    private String name;
    private Collection<Employee> employees;
    public int getId() { return id; }
    // ……………………………
    public Collection<Employee> getEmployees() { return employees; }
    public float payroll() { /*sum of employee’s salary …… */ }
    // ……………………………
}