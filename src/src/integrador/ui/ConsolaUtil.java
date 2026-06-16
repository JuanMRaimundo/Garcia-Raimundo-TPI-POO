/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package src.integrador.ui;

import java.util.Scanner;

/**
 * Utilidad centralizada para lectura de datos por consola.
 * 
 * RESUELVE EL PROBLEMA: Evita múltiples Scanner compitiendo por System.in.
 * Patrón Singleton simplificado: instancia única compartida.
 * 
 * Uso: ConsolaUtil.getInstance().leerEntero("Mensaje");
 *
 * @author Garcia Bautista
 */
public class ConsolaUtil {
    
    // ============================================
    // INSTANCIA ÚNICA (Singleton)
    // ============================================
    
    /** Instancia estática única - solo existe UN Scanner en todo el programa */
    private static ConsolaUtil instancia;
    
    /** El único Scanner que lee de System.in */
    private Scanner scanner;
    
    // ============================================
    // CONSTRUCTOR PRIVADO (Patrón Singleton)
    // ============================================
    
    /**
     * Constructor privado. Se llama solo una vez via getInstance().
     * Inicializa el Scanner sobre System.in (entrada estándar).
     */
    private ConsolaUtil() {
        this.scanner = new Scanner(System.in);
        // Configuramos para que no haya problemas con saltos de línea
        this.scanner.useDelimiter("\\n"); // Opcional: personalizar delimitador
    }
    
    // ============================================
    // MÉTODO DE ACCESO GLOBAL
    // ============================================
    
    /**
     * Obtiene la única instancia de ConsolaUtil.
     * Si no existe, la crea (lazy initialization).
     * 
     * @return La instancia única de ConsolaUtil lista para usar
     */
    public static ConsolaUtil getInstance() {
        if (instancia == null) {
            instancia = new ConsolaUtil();
        }
        return instancia;
    }
    
    // ============================================
    // MÉTODOS DE LECTURA VALIDADA
    // (Estos reemplazan a los que tenían en cada menú)
    // ============================================
    
    /**
     * Lee un número entero desde consola.
     * Repite hasta que el usuario ingrese un número válido.
     * 
     * @param mensaje Texto a mostrar antes de leer (ej: "Ingrese ID: ")
     * @return El entero válido ingresado por el usuario
     */
    public int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String linea = scanner.nextLine().trim();
                return Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                System.out.println("   ⚠️  Error: Debe ingresar un número entero válido. Intente nuevamente.");
            } catch (Exception e) {
                System.out.println("   ⚠️  Error de lectura. Intente nuevamente.");
                scanner.nextLine(); // Limpia buffer si hay error extraño
            }
        }
    }
    
    /**
     * Lee un entero positivo (>= 1).
     * Útil para cantidades, stocks, etc.
     * 
     * @param mensaje Texto a mostrar
     * @return Entero positivo ingresado
     */
    public int leerEnteroPositivo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= 1) {
                return valor;
            }
            System.out.println("   ⚠️  Error: El valor debe ser mayor o igual a 1.");
        }
    }
    
    /**
     * Lee un entero sin signo (>= 0).
     * Útil para stock, precios enteros, etc.
     * 
     * @param mensaje Texto a mostrar
     * @return Entero no negativo
     */
    public int leerEnteroNoNegativo(String mensaje) {
        while (true) {
            int valor = leerEntero(mensaje);
            if (valor >= 0) {
                return valor;
            }
            System.out.println("   ⚠️  Error: El valor no puede ser negativo.");
        }
    }
    
    /**
     * Lee un número Long (para IDs largos).
     * 
     * @param mensaje Texto a mostrar
     * @return Long válido
     */
    public long leerLong(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String linea = scanner.nextLine().trim();
                return Long.parseLong(linea);
            } catch (NumberFormatException e) {
                System.out.println("   ⚠️  Error: Debe ingresar un número entero (ID).");
            }
        }
    }
    
    /**
     * Lee un número decimal (double).
     * Útil para precios ($850.50).
     * 
     * @param mensaje Texto a mostrar
     * @return Double válido
     */
    public double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                String linea = scanner.nextLine().trim();
                return Double.parseDouble(linea);
            } catch (NumberFormatException e) {
                System.out.println("   ⚠️  Error: Debe ingresar un número decimal válido (ej: 99.90).");
            }
        }
    }
    
    /**
     * Lee un double positivo (>= 0).
     * Valida que no sea negativo.
     * 
     * @param mensaje Texto a mostrar
     * @return Double positivo o cero
     */
    public double leerDoublePositivo(String mensaje) {
        while (true) {
            double valor = leerDouble(mensaje);
            if (valor >= 0) {
                return valor;
            }
            System.out.println("   ⚠️  Error: El valor no puede ser negativo.");
        }
    }
    
    /**
     * Lee una línea de texto (String).
     * Elimina espacios en blanco al inicio y final (trim).
     * 
     * @param mensaje Texto a mostrar
     * @return String ingresado (puede estar vacío)
     */
    public String leerString(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine().trim();
    }
    
    /**
     * Lee un string que NO puede estar vacío.
     * Repite hasta que el usuario ingrese algo.
     * 
     * @param mensaje Texto a mostrar
     * @return String no vacío
     */
    public String leerStringNoVacio(String mensaje) {
        while (true) {
            String valor = leerString(mensaje);
            if (!valor.isEmpty()) {
                return valor;
            }
            System.out.println("   ⚠️  Error: Este campo es obligatorio y no puede estar vacío.");
        }
    }
    
    /**
     * Lee un email con formato básico validado.
     * Verifica que contenga '@' y '.' .
     * 
     * @param mensaje Texto a mostrar
     * @return Email válido (normalizado a minúsculas)
     */
    public String leerEmail(String mensaje) {
        while (true) {
            String email = leerStringNoVacio(mensaje).toLowerCase(); // Normalizamos a minúsculas
            
            // Validación básica de formato
            if (email.contains("@") && email.contains(".") && email.length() > 5) {
                return email;
            }
            
            System.out.println("   ⚠️  Error: Formato de email inválido. Debe contener '@' y '.' (ej: usuario@dominio.com).");
        }
    }
    
    /**
     * Pide confirmación S/N al usuario.
     * Útil para operaciones destructivas (eliminar, etc.)
     * 
     * @param mensaje Texto a mostrar (ej: "¿Está seguro? (S/N): ")
     * @return true si confirma con 'S' o 'SI', false en cualquier otro caso
     */
    public boolean confirmarAccion(String mensaje) {
        System.out.print(mensaje);
        String respuesta = scanner.nextLine().trim().toUpperCase();
        return respuesta.equals("S") || respuesta.equals("SI") || respuesta.equals("SÍ");
    }
    
    /**
     * Lee un string opcional (permite vacío).
     * Retorna null si el usuario presiona Enter sin nada.
     * Útil para campos opcionales en edición.
     * 
     * @param mensaje Texto a mostrar
     * @return String ingresado o null si estuvo vacío
     */
    public String leerStringOpcional(String mensaje) {
        String valor = leerString(mensaje);
        return valor.isEmpty() ? null : valor;
    }
    
    /**
     * Lee un Integer opcional (permite vacío).
     * Retorna null si el usuario no ingresa nada.
     * 
     * @param mensaje Texto a mostrar
     * @return Integer o null
     */
    public Integer leerEnteroOpcional(String mensaje) {
        String valor = leerString(mensaje);
        if (valor.isEmpty()) return null;
        
        try {
            int v = Integer.parseInt(valor);
            if (v < 0) {
                System.out.println("   ⚠️  Valor negativo no válido, se ignorará.");
                return null;
            }
            return v;
        } catch (NumberFormatException e) {
            return null; // No era número, tratamos como "no cambió"
        }
    }
    
    /**
     * Lee un Double opcional (permite vacío).
     * 
     * @param mensaje Texto a mostrar
     * @return Double o null
     */
    public Double leerDoubleOpcional(String mensaje) {
        String valor = leerString(mensaje);
        if (valor.isEmpty()) return null;
        
        try {
            double v = Double.parseDouble(valor);
            if (v < 0) {
                System.out.println("   ⚠️  Valor negativo no válido, se ignorará.");
                return null;
            }
            return v;
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    /**
     * Imprime una línea separadora decorativa.
     * 
     * @param caracter Carácter a repetir (ej: "-" o "=")
     * @param cantidad Veces a repetir
     */
    public void imprimirLinea(char caracter, int cantidad) {
        for (int i = 0; i < cantidad; i++) {
            System.out.print(caracter);
        }
        System.out.println();
    }
    
    /**
     * Pausa la ejecución hasta que el usuario presione Enter.
     * Útil para "presione una tecla para continuar".
     * 
     * @param mensaje Mensaje a mostrar (default: "Presione Enter para continuar...")
     */
    public void pausar(String mensaje) {
        if (mensaje == null || mensaje.isEmpty()) {
            mensaje = "Presione Enter para continuar...";
        }
        System.out.print(mensaje);
        scanner.nextLine();
    }
    
    /**
     * Limpia/respeta el buffer del scanner si es necesario.
     * Método de seguridad por si hubo errores de lectura.
     */
    public void limpiarBuffer() {
        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }
    }
}