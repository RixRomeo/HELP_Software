package progetto_help;




public class Utente {
	
    private String cf;
    private String username;
    private String password;
    private String tipo;
    
    
    public Utente() {
    	this("-","-","-","-");
    }

    public Utente(String cf, String username, String password, String tipo) {
        this.setCF(cf);
        this.setUsername(username);
        this.setPassword(password);
        this.setTipo(tipo);
    }

    // Getters e setters:

    public String getCF() {
        return cf;
    }

    public void setCF(String cf) {
        this.cf = cf;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
    public String toString() {
    	return("CF: "+getCF() + " Username: "+getUsername() + " Tipo: "+getTipo());
    }
    
}

