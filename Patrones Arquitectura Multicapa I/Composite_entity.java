@Entity
public class Employee {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private long salary;
    @ManyToOne
    private Department department;
    // …………………
    Department getDepartment() { return department; }
    // …………………
}
@Entity
public class Department {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    //nótese que el departamento no tiene empleados
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    // ……………………………..
}
