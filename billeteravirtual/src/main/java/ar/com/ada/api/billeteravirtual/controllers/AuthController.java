package ar.com.ada.api.billeteravirtual.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import ar.com.ada.api.billeteravirtual.entities.Usuario;
import ar.com.ada.api.billeteravirtual.models.request.RegistrationRequest;
import ar.com.ada.api.billeteravirtual.models.response.RegistrationResponse;
import ar.com.ada.api.billeteravirtual.services.PersonaService;
import ar.com.ada.api.billeteravirtual.services.UsuarioService;


/**
 * AuthController
 */
@RestController
public class AuthController {

    @Autowired
    PersonaService personaService;

    @Autowired
    UsuarioService usuarioService;


    //Registro de una persona
    @PostMapping("auth/register")
    public RegistrationResponse postRegisterUser(@RequestBody RegistrationRequest req){


        RegistrationResponse r = new RegistrationResponse();
      
        Usuario u = usuarioService.crearUsuario(req.nombre,req.dni, req.edad,req.email, req.password);
        
        r.isOk = true;
        r.message = "Te registraste con exito";
       
        return r;
        
    }
    
}