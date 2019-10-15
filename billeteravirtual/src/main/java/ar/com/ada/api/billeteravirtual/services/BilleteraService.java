package ar.com.ada.api.billeteravirtual.services;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.ada.api.billeteravirtual.entities.Billetera;
import ar.com.ada.api.billeteravirtual.entities.Cuenta;
import ar.com.ada.api.billeteravirtual.entities.Movimiento;
import ar.com.ada.api.billeteravirtual.entities.Usuario;
import ar.com.ada.api.billeteravirtual.repo.BilleteraRepository;

/**
 * BilleteraService
 */
@Service
public class BilleteraService {

    @Autowired
    BilleteraRepository billeteraRepo;

    @Autowired
    UsuarioService usuarioService;

    public void save(Billetera b) {
        billeteraRepo.save(b);
    }

    // Busca una billetera por su id
    public Billetera buscarPorId(int id) {
        Optional<Billetera> b = billeteraRepo.findById(id);

        if (b.isPresent())
            return b.get();
        return null;
    }

    public Billetera traerBilletera(Usuario usuario) {

        Billetera b = usuario.getPersona().getBilletera();

        return b;

    }

    public void transferirDinero(Billetera billeteraOrigen, BigDecimal importe, String email, String conceptoDeOperacion, String tipoDeOperacion, String moneda) {

        Usuario usuarioDestino = usuarioService.buscarUsuarioPorEmail(email);

        Usuario usuarioOrigen = billeteraOrigen.getPersona().getUsuario();
        
        Movimiento enviarDinero = new Movimiento();
        enviarDinero.setImporte(importe.negate());
        enviarDinero.setDeUsuarioId(usuarioOrigen.getUsuarioId());
        enviarDinero.setCuenta(usuarioOrigen.getPersona().getBilletera().getCuenta(0));
        enviarDinero.setAUsuarioId(usuarioDestino.getUsuarioId());
        enviarDinero.setCuentaDestinoId(usuarioDestino.getPersona().getBilletera().buscarCuenta(moneda).getNroCuentaId());
        enviarDinero.setCuentaOrigenId(usuarioOrigen.getPersona().getBilletera().buscarCuenta(moneda).getNroCuentaId());
        enviarDinero.setConceptoDeOperacion(conceptoDeOperacion);
        enviarDinero.setFechaMovimiento(new Date());
        enviarDinero.setEstado(0);
        enviarDinero.setTipoDeOperacion("Transferencia");

        //bOrigen.agregarMovimiento(enviarDinero);
        
        usuarioOrigen.getPersona().getBilletera().agregarM(enviarDinero);
        billeteraRepo.save(usuarioOrigen.getPersona().getBilletera());
        //billeteraRepo.save.update(usuarioOrigen.getPersona().getBilletera());

        Movimiento recibirDinero = new Movimiento();
        recibirDinero.setImporte(importe);
        recibirDinero.setDeUsuarioId(usuarioOrigen.getPersona().getUsuario().getUsuarioId());
        recibirDinero.setAUsuarioId(usuarioDestino.getUsuarioId());
        recibirDinero.setCuentaDestinoId(usuarioDestino.getPersona().getBilletera().buscarCuenta(moneda).getNroCuentaId());
        recibirDinero.setCuentaOrigenId(usuarioOrigen.getPersona().getBilletera().buscarCuenta(moneda).getNroCuentaId());
        recibirDinero.setConceptoDeOperacion("Regalo");
        recibirDinero.setFechaMovimiento(new Date());
        recibirDinero.setEstado(0);
        recibirDinero.setTipoDeOperacion("Transferencia");

        usuarioDestino.getPersona().getBilletera().agregarM(recibirDinero);
        billeteraRepo.save(usuarioDestino.getPersona().getBilletera());

    }

    public BigDecimal getSaldo(int id) {
        Optional<Billetera> b = billeteraRepo.findById(id);

        if (b.isPresent()){
            Cuenta c = b.get().getCuenta(0);
          
            return c.getSaldo();
        }
        return null;

    }
    public BigDecimal getSaldoDisponible(int id) {
        Optional<Billetera> b = billeteraRepo.findById(id);

        if (b.isPresent()){
            Cuenta c = b.get().getCuenta(0);
          
            return c.getSaldoDisponible();
        }
      return null;
    }
}