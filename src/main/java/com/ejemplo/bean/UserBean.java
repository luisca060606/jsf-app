package com.ejemplo.bean;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

import org.primefaces.PrimeFaces;

import com.ejemplo.dao.UserDAO;
import com.ejemplo.model.User;
// import com.ejemplo.util.PasswordUtil;

@Named
@ViewScoped
@Getter
@Setter
public class UserBean implements Serializable {

  private User user = new User();
  private List<User> lista;

  @Inject
  private UserDAO dao;

  @PostConstruct
  public void init() {
    try {
      // dao = new UserDAO(); // Inicialízalo dentro del try
      lista = dao.listar();

      if (lista == null) {
        this.lista = new java.util.ArrayList<>();
      }
    } catch (Throwable e) { // Usamos Throwable para capturar errores de clase no encontrada
      System.err.println("¡ERROR REAL ENCONTRADO!: " + e.getMessage());
      e.printStackTrace();
      this.lista = new java.util.ArrayList<>();
    }
  }

  public void prepareNewUser() {
    this.user = new User();
    FacesContext context = FacesContext.getCurrentInstance();
    context.getViewRoot().getViewMap().remove("formUsers");

    PrimeFaces.current().resetInputs("formUsers:pnlDetalle");
  }

  public void save() {
    try {
      dao.crear(user);
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "User " + user.getUsername() + " created."));
      user = new User();
      lista = dao.listar();
      PrimeFaces.current().executeScript("PF('dlgUser').hide()");
    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Error to save: " + e.getMessage()));
    }
  }

  public void delete(int id) {
    try {
      dao.eliminar(id);
      lista = dao.listar();

      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_INFO, "Deleted", "User deleted correctly"));

    } catch (Exception e) {
      FacesContext.getCurrentInstance().addMessage(null,
          new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "It could not be deleted: " + e.getMessage()));
    }
  }

  // getters y setters
}