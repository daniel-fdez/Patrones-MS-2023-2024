// Field access
@Entity
public class Employee {
    @Id
    private int id;
    private String name;
    private long salary;
    public Employee() {}
    public Employee(int id) { this.id = id; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // ………………………………
}

// Property access
@Entity
public class Employee {
    private int id;
    private String name;
    private long salary;
    @Id
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    // ………………………………
}

// Table mapping
@Entity
@Table(name=“EMP”)
public class Employee {/*...*/}

// Simple types mapping
@Entity
public class Employee {
    @Id
    @Column(name="EMP_ID")
    private int id;
    private String name;
    @Basic(fetch=FetchType.LAZY) // or EAGER
    private String comments;
    // ……………
}

// PK mapping
@Entity
public class Employee {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    // ……………… 
}

// Relationships mapping
// N to 1
@Entity
public class Employee {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private long salary;
    @ManyToOne
    @JoinColumn(name="id_departamento")
    private Department department;
    // …………………
}
@Entity
public class Department {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    // ……………………………..
}

// 1 to N
@Entity
public class Employee {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private long salary;
    @ManyToOne
    private Department department;
    // …………………
}
@Entity
public class Department {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    @OneToMany(mappedBy=“department”)
    private Collection<Employee> employees;
    // ……………………………..
}

// 1 to 1
@Entity
public class Employee {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private long salary;
    @OneToOne
    private ParkingSpace parkingSpace;
    // …………………
}
@Entity
public class ParkingSpace {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private int lot;
    private String location;
    public int getId() {
        return id;
    }
    // Bidirectional if \/ exists
    @OneToOne(mappedBy="parkingSpace")
    public void setId(int id) {
        this.id = id;
    }
    // …………………………………
}

// M to N
@Entity
public class Employee {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    private long salary;
    @ManyToMany
    private Collection<Project> projects;
    // …………………
}
@Entity
public class Project {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private int id;
    private String name;
    @ManyToMany(mappedBy="projects")
    private Collection<Employee> employees;
    // …………………
}

// Composed PKs
public class EmployeeId {
    private String contry;
    private int id;
    public EmployeeId() {}
    public EmployeeId(String country, int id) {
        this.country= country;
        this.id= id;
    }
    public getCountry() { return country; }
    public getId() { return id; }
    public boolean equals(Object o) { 
        return (o instanceof EmployeeId) && country.equals((EmployeeId)o).getCountry()) &&
            id == ((EmployeeId)o).getId());
    }
    public int hashCode() {
        return country.hashCode() + id;
    }
}
@Entity
@IdClass(EmployeeId.class)
public class Employee {
    @Id private String country;
    @Id private int id;
    private String name;
    private long salary;
    public Employee() {}
    public Employee(String country, int id) {
        this.country= country;
        this.id= id;
    }
    // …………………
}
@Embeddable
public class EmployeeId {
    private String contry;
    private int id;
    // ………………………………………………………………
}
@Entity
public class Employee {
    @EmbeddedId private EmployeeId id;
    private String name;
    private long salary;
    public Employee() {}
    public Employee(String country, int id) {
        this.id = new EmployeeId(country, id);
    }
    public String getCountry(){ return id.getCountry(); }
    public int getId() { return id.getId(); }
    // …………………
}
// ……………………….
EmployeeId id = new EmployeeId("España", 73);
Employee emp = em.find(Employee.class, id);

// Association class mapping
@Entity
public class Employee {
    @Id private int id;
    ……
    @OneToMany(mappedBy=“employee”)
    private Collection<ProjectAssingment> assingments;
…… }
@Entity
public class Project {
    @Id private int id;
    ……
    @OneToMany(mappedBy=“project”)
    private Collection<ProjectAssingment> projects;
…… }
public class ProjectAssingmentId implements Serializable {
    private int employee;
    private int project;
    ……
}
@Entity
@IdClass(ProjectAssingmentId.class)
public class ProjectAssignment {
    @Id
    @ManyToOne
    private Employee employee;
    @Id
    @ManyToOne
    private Project project;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    ……
}

@Embeddable
public class ProjectAssingmentId implements Serializable {
    private int employee;
    private int project;
    ……
}
@Entity
public class ProjectAssingment{
    @EmbeddedId private ProjectAssingmentId id;
    @ManyToOne
    @MapsId private Employee employee;
    @ManyToOne
    @MapsId private Project project;
    @Temporal(TemporalType.DATE)
    private Date startDate;
    ……
}