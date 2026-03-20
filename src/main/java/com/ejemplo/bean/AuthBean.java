package com.ejemplo.bean;

import java.io.Serializable;

import com.ejemplo.dao.UserDAO;
import com.ejemplo.model.User;
import com.ejemplo.util.PasswordUtil;

import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Named("authBean")
@SessionScoped
@Getter
@Setter
public class AuthBean implements Serializable {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email")
  private String email;
  @NotBlank(message = "Password is required")
  private String password;
  private User userLogged;

  @Inject
  private UserDAO userDAO;

  public String redirect() {
    if (userLogged != null) {
      return "/views/dashboard?faces-redirect=true";
    }
    return "login?faces-redirect=true";
  }

  public String login() {
    try {
      User u = userDAO.findByEmail(email);
      if (u != null && PasswordUtil.checkPassword(password, u.getPassword())) {
        this.userLogged = u;
        return redirect();
      } else {
        FacesContext.getCurrentInstance().addMessage(null,
            new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Incorrect credentials"));
        return null;
      }
    } catch (Exception e) {
      return null;
    }
  }

  public String logout() {
    System.out.println("DEBUG: Ejecutando logout...");
    userLogged = null;
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    return "/login?faces-redirect=true";
  }

  public boolean isLogged() {
    return userLogged != null;
  }
}
