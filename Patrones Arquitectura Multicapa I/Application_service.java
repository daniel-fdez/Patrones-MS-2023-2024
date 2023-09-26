public interface SAUsuario {
    public int create(TUsuario tUsuario);
    public TUsuario read(int id);
    public Collection<TUsuario> readAll();
    public int update(TUsuario tUsuario);
    public int delete (int id);
}
public class SAUsuarioImp implements SAUsuario {
    public int create(TUsuario tUsuario) { 
        int id= -1;
        DAOUsuario daoUsuario;
        if (tUsuario != null) { //acceso al DAO
            TUsuario leido = daoUsuario.readByName(tUsuario.getNombre());
            if (leido == null) id = daoUsuario.create(tu);
        }
        return id;
    }
    // ………………………………….
}