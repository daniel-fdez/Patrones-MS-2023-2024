import javax.transaction.*;
public class EmployeeApplicationService {
    public String createEmployee(String lastName, String firstName, String ss, float salary, String jobClassification, String geography) {
        String id = null;
        String divisionId = null;
        // Create new id
        divisionId = getDivisionId(jobClassification, geography);
        // Create Employe
        Employee e = new Employee(id, lastName, firstName, ss, salary, divisionId);
        PersistenceManagerFactory factory = PersistenceManagerFactory.getInstance();
        PersistenceManager manager = factory.getPersistenceManager();
        try {
            manager.begin();
            e = (Employee) manager.persistNew(e);
            manager.commit();
        } catch (SystemException e1) {
        } catch (NotSupportedException e1) {
        } catch (HeuristicRollbackException e1) {
        } catch (RollbackException e1) {
        } catch (HeuristicMixedException e1) {
        }
        return id;
    }
    public void setEmployeeSalary(String id, float salary) {
        PersistenceManagerFactory factory = persistenceManagerFactory.getInstance();
        PersistenceManager manager = factory.getPersistenceManager();
        Employee e = manager.getEmployee(id);
        if (e != null) {
            e.setSalary(salary);
        }
        try {
            manager.begin();
            e = (Employee) manager.persist(e);
            manager.commit();
        } catch (SystemException e1) {
        } catch (NotSupportedException e1) {
        } catch (HeuristicRollbackException e1) {
        } catch (RollbackException e1) {
        } catch (HeuristicMixedException e1) {
        }
    }
}

public interface Persistable { }

public class Employee implements Persistable {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String ss;
    protected float salary;
    protected String divisionId;
    public Employee(String id) {
        this.id = id;
    }
    public Employee(String id, String lastName, String firstName, String ss, float salary, String divisionId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.firstName = firstName;
        this.ss = ss;
        this.salary = salary;
        this.divisionId = divisionId;
    }
    public void setId(String id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setSalary(float salary) { this.salary = salary; }
    public void setDivisionId(String divisionId) { this.divisionId = divisionId; }
    public void setSS(String ss) { this.ss = ss; }
    // . . . 
}

public class EmployeeStateManager implements StateManager {
    private final int ROW_LEVEL_CACHING = 1;
    private final int FIELD_LEVEL_CACHING = 2;
    int cachingType = ROW_LEVEL_CACHING;
    boolean isNew;
    private Employee employee;
    private PersistenceManager pm;

    public EmployeeStateManager(PersistenceManager pm, Employee employee, boolean isNew ) {
        this.pm = pm;
        this.employee = employee;
        this.isNew = isNew;
    }

    public void flush() {
        if (pm.isDirty(employee)) {
            EmployeeTO to = new EmployeeTO(employee.id, employee.lastName, employee.firstName, employee.ss, employee.salary, employee.divisionId);
            EmployeeStoreManager storeManager = new EmployeeStoreManager();
            if (isNew) { 
                storeManager.storeNew(to);
                isNew = false;
            }
            else { 
            storeManager.update(to); 
            }
            pm.resetDirty(employee);
        }
    }
    public void load() {
        EmployeeStoreManager storeManager = new EmployeeStoreManager();
        EmployeeTO to = storeManager.load(employee.id);
        updateEmployee(to);
    }
    //si algún atributo fuese referencia a un objeto debería
    //cargarlo a través del PersistenceManager
    public void load(int field) {
        if (fieldNeedsReloading(field)) {
            EmployeeStoreManager storeManager = new EmployeeStoreManager();
            if (cachingType == FIELD_LEVEL_CACHING) {
                Object o = storeManager.loadField(employee.id, field);updateEmployee( field, o ); 
            }
            else { 
                EmployeeTO to = storeManager.load(employee.id);
                updateEmployee(to);
            }
        } 
    }
    private boolean fieldNeedsReloading(int field) {
    // Caching and valid data rule apply here
    // data can be cached at the field or the row level
    switch (field) {
        case EmployeeStateDelegate.LAST_NAME:
            if (employee.lastName == null) return true;
            break;
        case EmployeeStateDelegate.FIRST_NAME:
            if (employee.firstName == null) return true;
            break;
        case EmployeeStateDelegate.DIVISION_ID:
            String did = employee.divisionId;
            if (did == null || did.indexOf("99-") == -1) return true;break;
        case EmployeeStateDelegate.SS:
            if (employee.ss == null) return true;
            break;
        case EmployeeStateDelegate.SALARY:
            if (employee.salary == 0.0) return true;
            break;
        }
        return false;
    }
    private void updateEmployee(EmployeeTO to) {
        employee.id = to.id;
        employee.lastName = to.lastName;
        employee.firstName = to.firstName;
        employee.ss = to.ss;
        employee.salary = to.salary;
        employee.divisionId = to.divisionId;
        isNew = false; 
    }
    public boolean needsLoading() {
        return (pm.needLoading(employee));
    }
}

//proxy que hace la carga dinámica de los atributos vía StateManager
public class EmployeeStateDelegate extends Employee {
    static final int LAST_NAME = 1;
    static final int FIRST_NAME = 2;
    static final int SS = 3;
    static final int SALARY = 4;
    static final int DIVISION_ID = 5;
    private EmployeeStateManager stateManager;

    public EmployeeStateDelegate(String id, String lastName, String firstName, String ss, float salary, String divisionId) {
        super(id, lastName, firstName, ss, salary, divisionId);
    }
    public EmployeeStateDelegate(Employee e) {
        super(e.id, e.lastName, e.firstName, e.ss, e.salary, e.divisionId);
    }
    public EmployeeStateDelegate(String employeeId) {
        super(employeeId);
    }
    public EmployeeStateDelegate(String employeeId, EmployeeStateManager stateManager) {
        super(employeeId);
        this.stateManager = stateManager;
    }
    public void setStateManager(EmployeeStateManager stateManager){
        this.stateManager = stateManager;
    }
    //El get de una referencia a un objeto hace que el state manager//lo cargue mediante el persistence manager
    public String getFirstName() {
        stateManager.load(FIRST_NAME);
        return firstName;
    }
    public String getDivisionId() {
        stateManager.load(DIVISION_ID);
        return divisionId;
    }
    public String getLastName() {
        stateManager.load(LAST_NAME);
        return lastName; 
    }
    public String getSS() {
        stateManager.load(SS);
        return ss;
    }
    public float getSalary() {
        stateManager.load(SALARY);
        return salary;
    }
    public EmployeeStateManager getStateManager() {
        return stateManager;
    } 
}

public class EmployeeTO {
    public String id;
    public String lastName;
    public String firstName;
    public String ss;
    public float salary;
    public String divisionId;
    public EmployeeTO(String id, String lastName, String firstName, String ss, float salary, String divisionId ) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.ss = ss;
        this.salary = salary;
        this.divisionId = divisionId;
    }
}

//Es un DAO
public class EmployeeStoreManager {
    public void storeNew(EmployeeTO to) {
        String sql = "Insert into Employee( id, last_name," 
        + " first_name, ss, salary, division_id ) " 
        +" values( '?', '?', '?', '?', '?', '?' )";
        // . . .
    }
    public void update(EmployeeTO to) {
        String sql = "Update Employee set last_name = '?'," +
        " first_name = '?', salary = '?'," +
        " division_id = '?' where id = '?'";
        // . . . 
    }

    public void delete(String empId) {
        String sql = "Delete from Employee where id = '?'";
        // . . .
    }
    public EmployeeTO load(String empId) {
        // . . .
    }
}
public class PersistenceManagerFactory {
    static private PersistenceManagerFactory me = null;
    public synchronized static PersistenceManagerFactory getInstance(){
        if (me == null) { me = new PersistenceManagerFactory(); }
        return me; 
    }
    private PersistenceManagerFactory() { }
    public PersistenceManager getPersistenceManager() {
        return new PersistenceManager();
    } 
}
import javax.transaction.*;
import java.util.HashSet;
import java.util.Iterator;
public class PersistenceManager {
    //En vez de guardar los objetos del negocio, guarda sus state managersHashSet stateManagers = new HashSet();
    TransactionManager tm;
    Transaction txn;
    public PersistenceManager() {
        tm = TransactionManager.getInstance();
        tm.register(this);
    }
    public Persistable persistNew(Persistable o) {
        if (o instanceof Employee) {
        return setupEmployee(new EmployeeStateDelegate((Employee)o), true);}
        return o;
    }
    public Persistable persist(Persistable o) {
        // Must already be an EmployeeStateDelegate
        if (o instanceof Employee) {
        EmployeeStateDelegate esd =(EmployeeStateDelegate) o; return esd; }
        return o; 
    }
    public void commit() throws SystemException, NotSupportedException, HeuristicRollbackException, RollbackException, HeuristicMixedException {
        if (txn == null) {
            throw new SystemException("Must call Transaction.begin() before" +" Transaction.commit()"); 
        }
        Iterator i = stateManagers.iterator();
        while (i.hasNext()) {
            Object o = i.next();
            StateManager stateManager = (StateManager) o;
            stateManager.flush();
        }
        txn.commit();
        txn = null; 
    }

    public void begin()throws SystemException, NotSupportedException {
        txn = tm.getTransaction();
        txn.begin();
    }
    public Employee getEmployee(String employeeId) {
        EmployeeStateDelegate esd = new EmployeeStateDelegate(employeeId);
        setupEmployee(esd, false);
        return esd;
    }
    private EmployeeStateDelegate setupEmployee(EmployeeStateDelegate esd, boolean isNew) {
        EmployeeStateManager stateManager = new EmployeeStateManager(this, esd, isNew);
        stateManagers.add(stateManager);
        esd.setStateManager(stateManager);
        return esd;
    }
    public void setDirty(Persistable o) {
        // set dirty marker to true
    }
    public void resetDirty(Persistable o) {
        // reset dirty marker to false
    }
    public boolean isDirty(Persistable o) {
        // check if object is dirty
        return true;
    }
    public boolean needLoading(Persistable o) {
        // check if needs to be loaded
        return true;
    } 
}
import javax.transaction.*;
import java.util.Iterator;
import java.util.LinkedList;
public class TransactionManager {
    static TransactionManager me = null;
    //hubiera sido más práctico un ConcurrentHashMap de Pmanager//indexados por Thread
    private LinkedList persistenceManagers = new LinkedList();class PManager {
        Thread thread;
        PersistenceManager manager;
        PManager(Thread thread, PersistenceManager manager) {
            this.thread = thread;
            this.manager = manager;
        }
        boolean equals(Thread thread, PersistenceManager manager) {
            return (this.thread == thread && this.manager == manager);
        }
    }
    public synchronized static TransactionManager getInstance() {
        if (me == null) {
            me = new TransactionManager();
        }
        return me;
    }
    private TransactionManager() { }
    public Transaction getTransaction() {
        return new Transaction();
    }
    public void register(PersistenceManager manager) {
    // . . . 
    }
    public void notifyCommit(Thread t) throws SystemException,
    HeuristicRollbackException,NotSupportedException, RollbackException,
    HeuristicMixedException {
        Iterator i = persistenceManagers.iterator();
        while (i.hasNext()) {
            // . . .
            pm.manager.commit();
        } 
    } 
}
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.transaction.*;
public class Transaction {
    UserTransaction txn;
    public void setSessionContext( SessionContext ctx ) {
        ctx.getUserTransaction();
    }
    public Transaction() {
        InitialContext ic = null;
        try {
            ic = new InitialContext();
            txn = (UserTransaction)ic.lookup("java:comp/UserTransaction");
        } catch (NamingException e) {} 
    }
    public void begin() throws SystemException, NotSupportedException { txn.begin(); }
    public void commit() throws SystemException, HeuristicRollbackException, RollbackException, HeuristicMixedException {
        txn.commit();
    }
    public void rollback() throws SystemException {
        txn.rollback();
    }
}
