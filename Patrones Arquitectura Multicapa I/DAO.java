import java.sql.*
public void connectToAndQueryDatabase(String username, String password) {
    try{
        Connection con = DriverManager.getConnection("jdbc:myDriver:myDatabase", username, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT a, b, c FROM Table1");
        while (rs.next()) { 
            int x = rs.getInt("a");
            String s = rs.getString("b");float f = rs.getFloat("c");
        }
    } finally {
        if (stmt != null) stmt.close();
        if (con != null) con.close(); 
    }
}


import java.sql.*;
public class UpdateCar {
    public static void UpdateCarNum(int carNo, int empNo)throwsSQLException {
        Connection con = null;
        PreparedStatement pstmt = null;
        try { con = DriverManager.getConnection("jdbc:default:connection");
            pstmt = con.prepareStatement("UPDATE EMPLOYEES " + "SET CAR_NUMBER = ? " + "WHERE EMPLOYEE_NUMBER = ?");
            pstmt.setInt(1, carNo);
            pstmt.setInt(2, empNo);
            pstmt.executeUpdate();
        }
        finally {
            if (pstmt != null) pstmt.close();
            if (con != null) con.close();
        }
    }
}


public interface DAOUsuario {
    public int create(TUsuario tUsuario);
    public TUsuario read(int id);
    public Collection<TUsuario> readAll();
    public TUsuario readByName(int dni);
    public int update(TUsuario tUsuario);
    public int delete (int id);
}
public class DAOUsuarioImp implements DAOUsuario {
    // ……
    public Boolean create(TUsuario tUsuario) {
        int id = -1;
        //conexión con la base de datos
        PreparedStatement ps;
        ps = conexion.prepareStatement(“INSERT INTO usuario (nombre, eMail, activo) VALUES (?,?,?)“, Statement.RETURN_GENERATED_KEYS );
        ps.setString(1, tUsuario.getNombre());
        ps.setString(2, tUsuario.getEMail());
        ps.setBoolean(3, tUsuario.getActivo());
        ps.execute();
        ResultSet rs = pstmt.getGeneratedKeys();
        if (rs.next()) {
            res = rs.getInt(1);
        }
        //cerrar conexión y tratar excepciones
        return id;
    }
    // ………
}