/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import src.integrador.entities.Usuario;
import src.integrador.enums.Rol;
import src.integrador.exception.DatoInvalidoException;
import src.integrador.exception.EmailDuplicadoException;
import src.integrador.exception.EntidadNoEncontradaException;

/**
 *
 * @author JuanMR
 */
public class UsuarioService {
 
    private List<Usuario> usuarios;    //lista de usuarios registrados
    private Long nextId; //Contador autoincremental para ID
    
    //CONSTRUCTOR 
      public UsuarioService() {
        this.usuarios = new ArrayList<>();
        this.nextId = 1L;
    }
    
    
               // METODOS CRUD   
      
    
    // CREACION de usuarios                                                        
    public Usuario crearUsuario(String nombre, String apellido, String mail,String celular, String contrasenia, Rol rol)
            //se manejan excepciones por datos inválidos y/o mails duplicados
            throws DatoInvalidoException, EmailDuplicadoException{
        // Validaciones en los datos ingresados
        if (nombre == null || nombre.trim().isEmpty()) throw new DatoInvalidoException("ERROR: El nombre es obligatorio."); //validacion por nombre obligatorio
        if (apellido == null || apellido.trim().isEmpty()) throw new DatoInvalidoException("ERROR: El apellido es obligatorio."); //validacion por apellido obligatorio
        if (contrasenia == null || contrasenia.trim().isEmpty()) throw new DatoInvalidoException("ERROR: La contraseña es obligatoria."); // validacion por contraseña obligatoria
        if (rol == null) throw new DatoInvalidoException("ERROR: El rol del usuario es obligatorio (ADMIN o USUARIO)."); //Validacion por el rol obligatorio
        
        // Validaciones del mail
        if (mail == null || mail.trim().isEmpty()) throw new DatoInvalidoException("ERROR: El correo electrónico es obligatorio."); // Validacion por obligatoreidad
        if (!mail.contains("@")) throw new DatoInvalidoException("ERROR: El formato del correo es inválido (falta '@')."); // validación por formato requerido 
        
        String mailLimpio = mail.trim().toLowerCase();
        // Validacion por si existe o no el mismo mail ingresado
        if (existeMailRegistrado(mailLimpio)) {
            throw new EmailDuplicadoException("ERROR: El correo '" + mailLimpio + "' ya se encuentra registrado.");
        }
        // Instanciamos el usuario luego de pasar las validaciones
        Usuario nuevoUsuario = new Usuario(
            nombre.trim(),
            apellido.trim(),
            mailLimpio,
            celular != null ? celular.trim() : "",
            contrasenia,
            rol
        );
        
        // Asignación de ID y guardado del contador
        nuevoUsuario.setId(this.nextId);
        this.nextId++;
        // Agregamos al usuario a la coleccion
        this.usuarios.add(nuevoUsuario);
        
        return nuevoUsuario;
    }  
    
    // READ usuarios no eliminados
    public List<Usuario> listarUsuarios() {
        return this.usuarios.stream() //procesamos y filtramos la lista
                .filter(u -> !u.isEliminado()) // vemos si no fue eliminado,si fue eliminado se filtra y queda afuera, en caso contrario, 
                .collect(Collectors.toList());// lo guardamos para retornarlos en la lista
    }
    
    // READ usuario por ID 
    public Usuario buscarUsuarioPorId(Long id) throws EntidadNoEncontradaException{
        for (Usuario u : this.usuarios) {
            if (u.getId().equals(id)){ //primero chequeamos que el id ingresaddo coincida con algun usuario registrado
                if (u.isEliminado()){// luego chequeamos que no haya sido dado de baja pasado a "eliminado"
                    throw new EntidadNoEncontradaException("ERROR: El usuario con ID: "+id+" fue dado de baja.");
                }
                return u; // lo retornamos si existe y no esta 'eliminado'
            } 
        }
        // Lanzamos excepción si no se encontró en la base de datos
        throw new EntidadNoEncontradaException("ERROR: No existe ningún usuario con ID" + id +".");
    }    
    
    // UPDATE de usuario                       
    public Usuario editarUsuario(Long id, String nuevoNombre, String nuevoApellido,String nuevoMail, String nuevoCelular, String nuevaContrasenia, Rol nuevoRol)
              //excepciones a manejar 
            throws EntidadNoEncontradaException, DatoInvalidoException, EmailDuplicadoException {
        
        // PRIMERA GRAN VALIDACIÓN: Reutilizamos el metodo buscar por ID
        Usuario usuario = buscarUsuarioPorId(id); //en caso de no ser encontrado, lanzamos excepciones manejadas en el metodo
        

        // Actualizaciones manejando las validaciones en cada campo
        if (nuevoNombre != null && !nuevoNombre.trim().isEmpty())usuario.setNombre(nuevoNombre.trim()); //si pasa validaciones, seteamos el nombre
        if (nuevoApellido != null && !nuevoApellido.trim().isEmpty()) usuario.setApellido(nuevoApellido.trim()); // si para validaciones, seteamos el apellido
        if (nuevoCelular != null) usuario.setCelular(nuevoCelular.trim()); // si pasa validaciones, seteamos el nuevo celular
        if (nuevaContrasenia != null && !nuevaContrasenia.isEmpty()) usuario.setContrasenia(nuevaContrasenia); 
        if (nuevoRol != null) usuario.setRol(nuevoRol);
        
        // Actualización Mail (requiere re-validación de formato y unicidad)
        if (nuevoMail != null && !nuevoMail.trim().isEmpty()) {
            String mailLimpio = nuevoMail.trim().toLowerCase();
            
            if (!mailLimpio.contains("@")) {
                throw new DatoInvalidoException("ERROR: Formato de email inválido (falta '@').");
            }
            if (existeMailRegistradoExcluyendo(mailLimpio, id)) {
                throw new EmailDuplicadoException("ERROR: El email '" + mailLimpio + "' ya está registrado por otro usuario.");
            }
            usuario.setMail(mailLimpio);
        }
        
        return usuario;
    }
    
    
    // DELETE usuario  
    // Si tiene pedidos asociados, no se puede eliminar al usuario
    // esa validación se hará antes de llegar a este metodo
    
    public void eliminarUsuario(Long id) throws EntidadNoEncontradaException {
        // Validamos que exista el usuario
        Usuario usuario = buscarUsuarioPorId(id);
        //Si existe pasamos a cambiar su estado a "eliminado"
        usuario.setEliminado(true);
    }
    
    
    
    //Metodos auxiliares PRIVADOS de validaciones de mails
    
     // Validación al CREAR un usuario
    private boolean existeMailRegistrado(String mail) {
        for (Usuario u : this.usuarios) {
            if (!u.isEliminado() && 
                u.getMail().equalsIgnoreCase(mail)) return true;
        }
        return false;
    }
     // Validación al EDITAR usuario, ignoramos al usuario a editar para evitar "falso positivo"
    private boolean existeMailRegistradoExcluyendo(String mail, Long idExcluir) {
        for (Usuario u : this.usuarios) {
            if (!u.isEliminado() && 
                !u.getId().equals(idExcluir) && // No es el usuario que estamos editando
                 u.getMail().equalsIgnoreCase(mail)) return true;
        }
        return false;
    }
    
    
    //METODO DEMOSTRACIÓN DEL SOFT DELETE 
    public void imprimirTodosDebug() {
        System.out.println("\n--- TODOS LOS USUARIOS EN MEMORIA (Activos y Bajas) ---");
        this.usuarios.forEach(u -> {
            String estado = u.isEliminado() ? "ELIMINADO" : "ACTIVO";
            System.out.println("ID: " + u.getId() + " | " + u.getNombre() + " " + u.getApellido() + " | Estado: " + estado);
        });
        System.out.println("-----------------------------------------------------------------");
    }
    
}
