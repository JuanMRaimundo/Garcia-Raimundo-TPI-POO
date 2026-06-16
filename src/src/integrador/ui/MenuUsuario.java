/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.ui;

import src.integrador.entities.Usuario;
import src.integrador.enums.Rol;
import src.integrador.services.UsuarioService;
import src.integrador.exception.DatoInvalidoException;
import src.integrador.exception.EmailDuplicadoException;
import src.integrador.exception.EntidadNoEncontradaException;
import java.util.List;
import src.integrador.services.PedidoService;

/**
 * Submenú Usuarios - Sin Scanner.
 *
 * @author Garcia Bautista
 */
public class MenuUsuario {
    private PedidoService pedidoService;
    private UsuarioService usuarioService;
    private ConsolaUtil c;
    
    public MenuUsuario(UsuarioService usuarioService, PedidoService pedidoService) {
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
        this.c = ConsolaUtil.getInstance();
    }
    
    public void mostrar() {
        int opcion = -1;
        
        do {
            System.out.println("\n┌────────────────────────────────────────────┐");
            System.out.println("│      👤 GESTION DE USUARIOS                  │");
            System.out.println("├────────────────────────────────────────────┤");
            System.out.println("│  1. Listar usuarios                         │");
            System.out.println("│  2. Crear usuario                           │");
            System.out.println("│  3. Editar usuario                          │");
            System.out.println("│  4. Eliminar usuario                        │");
            System.out.println("│  0. Volver                                  │");
            System.out.println("└────────────────────────────────────────────┘");
            
            opcion = c.leerEntero("Seleccione: ");
            
            try {
                switch (opcion) {
                    case 1: listar(); break;
                    case 2: crear(); break;
                    case 3: editar(); break;
                    case 4: eliminar(); break;
                    case 0: System.out.println("<<< Volviendo..."); break;
                    default: System.out.println("❌ Invalido.");
                }
            } catch (Exception e) {
                System.out.println("⚠️  " + e.getMessage());
            }
        } while (opcion != 0);
    }
    
    private void listar() {
        System.out.println("\n--- LISTADO DE USUARIOS ---");
        List<Usuario> lista = usuarioService.listarUsuarios();
        
        if (lista.isEmpty()) {
            System.out.println("📭 No hay usuarios registrados.");
        } else {
            c.imprimirLinea('─', 85);
            System.out.printf("%-5s %-18s %-18s %-30s %-10s%n", 
                "ID", "NOMBRE", "APELLIDO", "EMAIL", "ROL");
            c.imprimirLinea('─', 85);
            for (Usuario u : lista) {
                System.out.printf("%-5d %-18s %-18s %-30s %-10s%n",
                    u.getId(), u.getNombre(), u.getApellido(), u.getMail(), u.getRol());
            }
            c.imprimirLinea('═', 85);
        }
    }
    
    private void crear() {
        System.out.println("\n--- CREAR NUEVO USUARIO ---");
        
        String nombre = c.leerStringNoVacio("Nombre: ");
        String apellido = c.leerStringNoVacio("Apellido: ");
        String mail = c.leerEmail("Correo electronico: "); // Ya valida formato @
        String celular = c.leerString("Celular (opcional): ");
        String pass = c.leerStringNoVacio("Contraseña: ");
        
        System.out.println("Roles disponibles:");
        System.out.println("   1. ADMIN");
        System.out.println("   2. USUARIO");
        // Validamos que la opción sea 1 o 2, sino lanzamos error
        int rolOp;
        do {
            rolOp = c.leerEntero("Seleccione rol (1 o 2): ");
            if (rolOp != 1 && rolOp != 2) {
                System.out.println("❌ Opcion invalida. Por favor, ingrese exactamente 1 o 2.");
            }
        } while (rolOp != 1 && rolOp != 2);
        // en caso de ser una opción correcta, guardamos
        Rol rol = (rolOp == 1) ? Rol.ADMIN : Rol.USUARIO;
        
        try {
            Usuario nuevo = usuarioService.crearUsuario(nombre, apellido, mail, celular, pass, rol);
            System.out.println("✅ Usuario creado. ID #" + nuevo.getId());
            System.out.println("   Bienvenido, " + nuevo.getNombre() + " (" + nuevo.getRol() + ")");
        } catch (DatoInvalidoException | EmailDuplicadoException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private void editar() {
        System.out.println("\n--- EDITAR USUARIO ---");
        listar();
        
        if (usuarioService.listarUsuarios().isEmpty()) return;

        
        long id = c.leerLong("ID del usuario a editar: ");
        
        // Validamos existencia ANTES de pedir los datos
        try {
            usuarioService.buscarUsuarioPorId(id);
        } catch (EntidadNoEncontradaException e) {
            System.out.println("❌ " + e.getMessage());
            return; // Cortamos la ejecución acá para no hacerle perder tiempo al usuario
        }
        
        System.out.println("(Deje vacio para mantener valor actual)");
        String nom = c.leerStringOpcional("Nuevo nombre: ");
        String ape = c.leerStringOpcional("Nuevo apellido: ");
        String mail = c.leerStringOpcional("Nuevo email: "); // Revalidará si no está vacío
        String cel = c.leerStringOpcional("Nuevo celular: ");
        String pass = c.leerString("Nueva contraseña (deje vacío para no cambiar): ");
        
        Rol nuevoRol = null;
        String rolStr;
        do {
            //Validamos que el rol ingresado no sea distinto a ADMIN o USUARIO - caso contrario seguimos pidiendo
            rolStr = c.leerStringOpcional("Nuevo rol (ADMIN/USUARIO, Enter=no cambiar): ");
            if (rolStr != null && !rolStr.equalsIgnoreCase("ADMIN") && !rolStr.equalsIgnoreCase("USUARIO")) {
                System.out.println("❌ Rol invalido. Escriba ADMIN, USUARIO o presione Enter para cancelar.");
            }
        } while (rolStr != null && !rolStr.equalsIgnoreCase("ADMIN") && !rolStr.equalsIgnoreCase("USUARIO"));
        //si cumple se guarda el rol
        if (rolStr != null) {
            nuevoRol = rolStr.equalsIgnoreCase("ADMIN") ? Rol.ADMIN : Rol.USUARIO;
        }
        
        try {
            Usuario editado = usuarioService.editarUsuario(id, nom, ape, mail, cel, 
                (pass.isEmpty()) ? null : pass, nuevoRol);
            System.out.println("✅ Usuario #" + id + " actualizado.");
        } catch (Exception e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
    
    private void eliminar() {
        System.out.println("\n--- ELIMINAR USUARIO (Baja Logica) ---");
        listar();
        
        if (usuarioService.listarUsuarios().isEmpty()) return;
        
        long id = c.leerLong("ID a eliminar: ");
        
      // Primero verificamos que el usuario exista 
        try {
            usuarioService.buscarUsuarioPorId(id);
        } catch (EntidadNoEncontradaException e) {
            System.out.println("❌ Error: " + e.getMessage());
            return;
        }

        // Si tiene pedidos, bloqueamos la eliminación
        if (pedidoService.tienePedidosElUsuario(id)) {
            System.out.println("❌ ERROR: No se puede eliminar el usuario porque tiene pedidos asociados en el sistema.");
            return; // Cortamos la ejecución acá
        }
        //doble confirmación al dar de baja al usuario
        if (c.confirmarAccion("¿Dar de baja a este usuario? (S/N): ")) {
            try {
                usuarioService.eliminarUsuario(id);
                System.out.println("✅ Usuario #" + id + " dado de baja exitosamente.");
            } catch (EntidadNoEncontradaException e) {
                System.out.println("❌ " + e.getMessage());
            }
        }
    }
}