package ar.com.ada.api.pootflix.services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import ar.com.ada.api.pootflix.entities.Usuario;
import ar.com.ada.api.pootflix.repo.UsuarioRepository;
import ar.com.ada.api.pootflix.security.Crypto;
import ar.com.ada.api.pootflix.sistema.comms.EmailService;

/**
 * UsuarioService
 */
@Service
public class UsuarioService {
    @Autowired
    UsuarioRepository repo;

    @Autowired
    EmailService emailService;

    public List<Usuario> getUsuarios() {

        return repo.findAll();
    }

    public Usuario buscarPorId(int id) {

        Optional<Usuario> u = repo.findById(id);

        if (u.isPresent())
            return u.get();
        return null;
    }

    public Usuario buscarPorEmail(String email) {

        return repo.findByUserEmail(email);

    }

    public Usuario buscarPorUsername(String username) {

        return repo.findByUsername(username);

    }

    public ObjectId crearUsuario(String nombre, String email, String password) {

        Usuario u = new Usuario();
        u.setNombre(nombre);
        u.setUsername(email);
        u.setEmail(email);

        String passwordEnTextoClaro;
        String passwordEncriptada;

        passwordEnTextoClaro = password;
        passwordEncriptada = Crypto.encrypt(passwordEnTextoClaro, u.getUsername());

        u.setPassword(passwordEncriptada);
        repo.save(u);

        emailService.SendEmail(u.getUserEmail(), "Bienvenido a Pootflix!!!",
                "Hola " + u.getNombre() + "\nGracias por registrar en Pootflix!\n");

        return u.get_id();

    }

    public void login(String username, String password) {

        Usuario u = repo.findByUsername(username);

        if (u == null || !u.getPassword().equals(Crypto.encrypt(password, u.getUsername()))) {

            throw new BadCredentialsException("Usuario o contraseña invalida");
        }

    }

}