public TUsuario implements Serializable {
    protected int id;
    protected String nombre;
    protected String eMail;
    protected boolean activo;

    public TUsuario(String nombre, String eMail) { 
        this.id = 0; 
        this.nombre = nombre;
        this.eMail = eMail; 
        this.activo = true; 
    }
    public TUsuario(int id, String nombre, String eMail, boolean activo) { 
        this.id = id; 
        this.nombre = nombre;
        this.eMail = eMail; 
        this.activo = activo; 
    }
    public int getId() { return id; }
    public String getNombre () { return nombre; }
    public String getEMail() { return eMail; }
    public boolean getActivo() { return activo; }
    public setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEMail(String eMail) { this.eMail = eMail; }
    public void setActivo(boolean activo) { this.activo = activo; }
}

public DAOUsuarioImp implements DAOUsuario {
    public TUsuario read (int id) {
        //código acceso a la base de datos
        TUsuario usuario = new TUsuario(id, nombre, eMail, activo);
        return usuario;
    }
    // ……………
}