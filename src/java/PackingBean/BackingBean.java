/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PackingBean;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import model.User;

/**
 *
 * @author Seka
 */
@ManagedBean(name = "BB")
@SessionScoped
public class BackingBean implements Serializable {
    private User user;
    public boolean isLogged = false;
    public boolean update = false;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public BackingBean() {
        user = new User();
        
    }
    public String createUser() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (validate(user)) {
            facesContext.addMessage("user-frm", new FacesMessage("Validation Error"));
            }
        ClientBuilder.newClient().target("http://localhost:8080/UserManagement/api/users")
                .request().post(Entity.json(user));
        
        return "Login.xhtml";
    }

    public boolean validate(User user) {
        boolean status = false;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (user.getFirstname().trim().length() < 5) {
            facesContext.addMessage("user-frm", new FacesMessage("names must be more than five character large"));
            status = true;
        } else if (!isValid(user.getEmail())) {
            facesContext.addMessage("email-frm", new FacesMessage("your email is not valid"));
            status = true;
        } 
        return status;
    }
    public boolean isValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    
     public String aunthenticate() {
        User result = ClientBuilder.newClient()
                .target("http://localhost:8080/UserManagement/api/auth")
                .request().post(Entity.json(user), User.class);
        if (result != null) {
            isLogged = true;
            return "Home.xhtml";
        } else {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            facesContext.addMessage("auth-frm", new FacesMessage("Auth failed"));
            return null;
        }
    }
}
