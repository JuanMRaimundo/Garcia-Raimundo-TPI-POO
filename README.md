# 🍕 Food Store - Sistema de Gestión de Pedidos de Comida

<div align="center">

![Java](https://img.shields.io/badge/Java-21-orange?style=flat-square&logo=java&logoColor=white)
![NetBeans](https://img.shields.io/badge/Apache%20NetBeans-23-blue?style=flat-square&logo=apache-netbeans-ide&logoColor=white)
![POO](https://img.shields.io/badge/Paradigm-OOP-blue?style=flat-square)
![Console](https://img.shields.io/badge/Interface-Console-green?style=flat-square)

**Sistema de consola en Java desarrollado en Apache NetBeans 23 para gestión completa de un negocio de comidas**

[Descripción](#-descripción) • 
[Características](#-características) • 
[Tecnologías](#-Tecnologías-y-Herramientas) • 
[Requisitos e Instalación](#-requisitos-e-instalación) • 
[Cómo Abrir en NetBeans](#-cómo-abrir-el-proyecto-en-apache-netbeans) • 
[Ejecución](#-ejecución-desde-netbeans) • 
[Arquitectura](#-arquitectura) • 
[Autores](#-autores)

</div>

---

## 📝 Descripción

**Food Store** es una aplicación de consola desarrollada en **Java 21** utilizando **Apache NetBeans 23** como Entorno de Desarrollo Integrado (IDE). El sistema implementa un modelo completo de gestión de pedidos para un negocio de comida, permitiendo administrar categorías de productos, productos con control de stock, usuarios con roles diferenciados (ADMIN/USUARIO), y un flujo completo de creación de pedidos con múltiples líneas de detalle, cálculo automático de totales y seguimiento de estados.

### 🎯 Objetivo Académico

Este proyecto fue desarrollado como **Trabajo Práctico Integrador** para la cátedra de **Programación 2** de la Tecnicatura Universitaria en Programación a Distancia de la **UTN - Universidad Tecnológica Nacional**.

### ✅ Requisitos del Enunciado Cumplidos

- ✅ Modelado UML correcto con herencia, interfaces y composición
- ✅ Programación Orientada a Objetos (encapsulamiento, polimorfismo, abstracción)
- ✅ Estructuras dinámicas (Colecciones Java - ArrayList)
- ✅ Interfaces para comportamientos calculables (`Calculable`)
- ✅ Excepciones personalizadas (`DatoInvalidoException`, `EmailDuplicadoException`, etc.)
- ✅ Manejo robusto de errores con validaciones en todas las capas
- ✅ Patrón Service Layer (separación Entities ↔ Services ↔ UI)
- ✅ Inyección de dependencias manual
- ✅ Baja lógica (Soft Delete) para integridad histórica
- ✅ Validaciones de negocio (precio ≥ 0, stock ≥ 0, email único, etc.)
- ✅ Todo el desarrollo realizado íntegramente en **Apache NetBeans**

---

## ✨ Características

### 📂 Gestión de Categorías
- CRUD completo (Crear, Leer, Actualizar, Eliminar)
- Validación de nombre único (no duplicados)
- Baja lógica: las categorías eliminadas se conservan ocultas
- No permite eliminar si tiene productos asociados

### 🛒 Gestión de Productos
- CRUD completo con 6 atributos (nombre, precio, descripción, stock, imagen, disponibilidad)
- Relación N:1 con Categorías (cada producto pertenece a una categoría)
- **Validaciones críticas**: Precio ≥ 0, Stock ≥ 0
- Listado filtrable por categoría
- No permite eliminar si está referenciado en pedidos

### 👥 Gestión de Usuarios
- CRUD completo con almacenamiento de credenciales
- Roles diferenciados: **ADMIN** y **USUARIO**
- **Validación de email única** (no puede haber dos usuarios con mismo mail)
- Formato de email validado (debe contener @)
- No permite eliminar si tiene pedidos asociados

### 📦 Gestión de Pedidos (Módulo Principal)
- **Flujo multipaso guiado desde consola**:
  1. Selección de cliente (usuario existente)
  2. Agregado dinámico de líneas de producto (detalles)
  3. Cálculo automático de subtotales por línea
  4. Cálculo automático de total general (via interface `Calculable`)
  5. Selección de forma de pago (Tarjeta/Transferencia/Efectivo)
- **Seguimiento de estados**: PENDIENTE → CONFIRMADO → TERMINADO → CANCELADO
- Validación de stock al agregar productos
- Baja lógica que preserva historial

### 🔒 Robustez y Seguridad
- Entrada de datos validada (nunca crashea por ingresar texto donde va número)
- Excepciones personalizadas con mensajes amigables en español
- Confirmación antes de operaciones destructivas (S/N)
- Menú estable (siempre vuelve al nivel anterior)

---

## 🛠 Tecnologías y Herramientas

| Tecnología/Herramienta | Versión | Uso en el Proyecto |
|------------------------|---------|---------------------|
| **Java (JDK)** | 21 (LTS) | Lenguaje de programación principal |
| **Apache NetBeans** | 23 (o superior) | IDE de desarrollo: edición, compilación, depuración, ejecución |
| **Paradigma** | POO (OOP) | Diseño orientado a objetos |
| **Estructuras** | `java.util.ArrayList` | Colecciones dinámicas (simula BD en memoria) |
| **Fechas** | `java.time.*` | LocalDate, LocalDateTime (API moderna Java 8+) |
| **Entrada/Salida** | `java.util.Scanner` | Lectura por consola centralizada |
| **Patrones** | Singleton, Service Layer, DI | Arquitectura limpia |

### ¿Por qué Apache NetBeans?

Este proyecto fue desarrollado utilizando **Apache NetBeans 23** debido a:

- ✅ **Integración nativa con Java**: Sin configuración extra, reconoce proyectos Java automáticamente
- ✅ **Gestor de paquetes visual**: Navegación cómoda por la estructura `src/integrador/entities/`, `/services/`, `/ui/`
- ✅ **Compilación con un clic**: Botón "Run" (F6) compila y ejecuta sin comandos manuales
- ✅ **Autocompletado inteligente**: Ayuda con métodos de clases propias y de la API Java
- ✅ **Depurador integrado**: Breakpoints para verificar flujo del programa paso a paso
- ✅ **Refactorización segura**: Renombrar clases/métodos sin romper referencias
- ✅ **Formato de código**: Ctrl+Shift+F para indentación automática consistente
- ✅ **Multiplataforma**: Funciona igual en Windows, macOS y Linux

---

## 📦 Requisitos e Instalación

### Prerrequisitos Antes de Abrir NetBeans

#### 1. Instalar JDK 21 (Java Development Kit)

**Opción A: Descarga oficial (Recomendada)**

1. Ir a: https://adoptium.net/
2. Seleccionar:
   - **Platform:** Windows / macOS / Linux (según su SO)
   - **Package:** JDK (no JRE)
   - **Version:** 21 (LTS) + HotSpot JVM
3. Descargar el instalador (`.msi` para Windows, `.dmg` para Mac, `.tar.gz` para Linux)
4. Ejecutar instalador con opciones por defecto

**Verificar instalación:**
```bash
# Abrir terminal/consola y ejecutar:
java -version
# Debe mostrar: openjdk version "21.x.x" 202x-xx-xx
#                 OpenJDK Runtime Environment (...)
```

#### 2. Instalar Apache NetBeans 23

1. Descargar desde: https://netbeans.apache.org/download/
2. Seleccionar versión **23** (o la más reciente estable)
3. Elegir instalador que incluya **JDK 21** o usar el JDK ya instalado
4. Ejecutar instalador:
   - Windows: Ejecutar `.exe`
   - Mac: Abrir `.dmg` → Arrastrar a Applications
   - Linux: Descomprimir `.sh` → ejecutar con permisos

**Nota importante para NetBeans:**
Al abrir NetBeans por primera vez, verificar que reconozca el JDK:
```
Tools → Java Platforms → Debe aparecer "JDK 1.21" (o similar)
Si no aparece: Add Platform → Buscar carpeta de instalación del JDK
```

#### 3. Tener Git instalado (para clonar este repositorio)

- **Windows:** Descargar desde https://git-scm.com/download/win
- **Mac:** Viene incluido o `brew install git`
- **Linux:** `sudo apt install git`

**Verificar:**
```bash
git --version
# git version 2.x.x
```

---

## 🔽 Cómo Abrir el Proyecto en Apache NetBeans

### Método 1: Desde GitHub (Recomendado - Clonar directo en NetBeans)

1. **Abrir Apache NetBeans**

2. **Menú:** `Team → Git → Clone...`

3. **Pegar la URL del repositorio:**
   ```
   Repository URL: https://github.com/TU_USUARIO/food-store-prog2.git
   ```
   
4. **Clic en "Next"** → Esperar a que descargue branches

5. **Seleccionar branch:** `main` (o `master`) → **Next**

6. **Elegir directorio local:** 
   ```
   C:\Users\TuUsuario\Documents\NetProjects\food-store-prog2
   ```
   (o la ruta que prefieras)

7. **Clic en "Finish"** → Esperar clonación

8. **NetBeans preguntará si desea abrir el proyecto:**
   - Si detecta automáticamente el proyecto Java → **"Open Project"**
   - Si no: Continuar con el Método 2

9. **¡Proyecto abierto!** Verás en el panel izquierdo (Projects):
   ```
   📁 food-store-prog2
       📁 Source Packages
           📁 integrador
               📁 entities (6 archivos)
               📁 enums (3 archivos)
               📁 interfaces (1 archivo)
               📁 services (4 archivos)
               📁 exception (3 archivos)
               📁 ui (6 archivos)
               📄 Main.java
   ```

### Método 2: Importar proyecto ya descargado

1. **Descargar/clonar el repositorio** (vía terminal o GitHub Desktop):
   ```bash
   cd C:\Users\TuUsuario\Documents\NetProjects
   git clone https://github.com/TU_USUARIO/food-store-prog2.git
   ```

2. **En NetBeans:** `File → Open Project...`

3. **Navegar hasta la carpeta `food-store-prog2`** (la raíz, no entrar a `src`)

4. **Seleccionar la carpeta** → Debe aparecer el icono de proyecto Java (☕)

5. **Clic en "Open Project"**

### Método 3: Crear proyecto nuevo y pegar código (si hay problemas)

1. **File → New Project → Java → Java Application**

2. **Nombre:** `FoodStoreProg2`
3. **Location:** Tu carpeta de proyectos
4. **Desmarcar** "Create Main Class" (ya tenemos la nuestra)
5. **Finish**

6. **En el panel Projects**, expandir **Source Packages → <default package>**

7. **Crear estructura de paquetes** (clic derecho → New → Java Package):
   - `integrador`
   - `integrador.entities`
   - `integrador.enums`
   - `integrador.interfaces`
   - `integrador.services`
   - `integrador.exception`
   - `integrador.ui`

8. **Copiar los archivos .java** de este repo en sus respectivos paquetes

9. **Listo!**

---

## ▶️ Ejecución desde NetBeans

### Opción A: Ejecutar Proyecto Completo (Más Usada)

1. **Asegurar que `Main.java` esté seleccionado** como clase principal:
   - Clic derecho sobre `Main.java` (en `integrador`)
   - `Set as Main File` (o "Establecer como archivo principal")

2. **Botón verde "Run"** (▶️) en la barra de herramientas O tecla **F6**

3. **Se abre la ventana "Output"** (abajo) mostrando:
   ```
   run:
   ╔══════════════════════════════════════════╗
   ║     🍕 FOOD STORE - SISTEMA DE GESTIÓN    ║
   ╚══════════════════════════════════════════╝
   Inicializando sistema...
   
   ╔═══════════════════════════════════════════════════════╗
   ║         🍕 SISTEMA DE PEDIDOS - FOOD STORE             ║
   ╚═══════════════════════════════════════════════════════╝
   ...
   Seleccione una opción: 
   ```

4. **Interactuar en la misma ventana Output** (es la consola dentro de NetBeans)

### Opción B: Ejecutar Solo un Archivo (Para pruebas rápidas)

- Clic derecho sobre cualquier archivo `.java` → `Run File` (Shift+F6)
- Útil para probar entidades individuales (si tienen main propio)

### Opción C: Depurar con Breakpoints (Para encontrar errores)

1. **Hacer clic en el margen izquierdo** de una línea de código (aparece un punto rojo = breakpoint)
   
2. **Menú:** `Debug → Debug Project` (Ctrl+F5)

3. **El programa se detiene** en esa línea → Podemos inspeccionar variables en la ventana "Variables"

4. **Botones de depuración:**
   - Step Over (F8): Ejecuta siguiente línea
   - Step Into (F7): Entra a métodos
   - Continue (F5): Siguiente breakpoint

### Si hay errores de compilación en NetBeans

**Error común: "Package does not exist"**
- **Causa:** Los archivos no están en la carpeta correcta según su declaración `package`
- **Solución:** Verificar que `Main.java` esté físicamente en `src/integrador/Main.java` (respetando minúsculas)

**Error común: "Cannot find symbol"**
- **Causa:** Falta importar una clase
- **Solución:** Clic derecho en el error → "Fix imports" (Ctrl+Shift+I)

**Error común: "Main method not found"**
- **Causa:** NetBeans no detectó qué es el main
- **Solución:** Clic derecho en `Main.java` → "Set as Main File"

---

## 🏗 Arquitectura del Sistema (Desarrollada en NetBeans)

### Estructura de Paquetes Vista en NetBeans

Al abrir el proyecto en el IDE, verás esta jerarquía en el panel **Projects**:

```
📁 Food Store Project
└── 📁 Source Packages
    └── 📁 integrador                    ← Paquete raíz
        ├── 📄 Main.java                ← Punto de entrada (main)
        │
        ├── 📁 entities/                ← Capa de Modelo (Entidades)
        │   ├── Base.java              ← Clase abstracta padre
        │   ├── Categoria.java         ← Extiende Base
        │   ├── Producto.java          ← Extiende Base
        │   ├── Usuario.java           ← Extiende Base
        │   ├── Pedido.java            ← Extiende Base + Calculable
        │   └── DetallePedido.java     ← Entidad débil
        │
        ├── 📁 enums/                  ← Enumeraciones
        │   ├── Rol.java               ← { ADMIN, USUARIO }
        │   ├── Estado.java            ← { PENDIENTE, CONFIRMADO, ... }
        │   └── FormaPago.java         ← { TARJETA, TRANSFERENCIA, EFECTIVO }
        │
        ├── 📁 interfaces/             ← Contratos de comportamiento
        │   └── Calculable.java        ← double calcularTotal()
        │
        ├── 📁 services/               ← Capa de Negocio (Lógica)
        │   ├── CategoriaService.java  ← CRUD categorías
        │   ├── ProductoService.java   ← CRUD productos
        │   ├── UsuarioService.java    ← CRUD usuarios
        │   └── PedidoService.java     ← CRUD pedidos + detalles
        │
        ├── 📁 exception/              ← Excepciones personalizadas
        │   ├── DatoInvalidoException.java
        │   ├── EmailDuplicadoException.java
        │   └── EntidadNoEncontradaException.java
        │
        └── 📁 ui/                     ← Capa de Presentación (Consola)
            ├── ConsolaUtil.java      ← Singleton Scanner compartido
            ├── MenuPrincipal.java    ← Menú raíz
            ├── MenuCategoria.java     ← Submenú categorías
            ├── MenuProducto.java      ← Submenú productos
            ├── MenuUsuario.java       ← Submenú usuarios
            └── MenuPedido.java        ← Submenú pedidos
```

### Diagrama de Clases (Resumen)

```
<<interface>> Calculable
    +calcularTotal(): double
            ▲
            │ implements
    ┌───────┴────────┐
    │               │
  Base (abstract)   DetallePedido
  ├─ id: Long       ├─ cantidad: int
  ├─ eliminado: bool├─ subtotal: Double  
  └─ createdAt: LDt └─ producto: Producto
       ▲ extends
       │
  ┌────┼────────┬──────────────┬──────┐
  │    │        │              │      │
Categoria Producto Usuario   Pedido   │  ◀──1:m──┐
  │      │        │            │      │     DetallePedido
  1:m   N:1      1:N          1:m     │          ▲
  │                                   │        m : 1
  └───────────────────────────────────┘       Producto
```

### Patrones de Diseño Implementados

| Patrón | Dónde | Beneficio |
|--------|-------|-----------|
| **Singleton** | `ConsolaUtil` | Un solo Scanner compartido (evita conflictos de buffer en NetBeans console) |
| **Service Layer** | Separación `entities/` ↔ `services/` ↔ `ui/` | Código mantenible y testeable |
| **Dependency Injection** | Constructores de Services reciben otros Services | Bajo acoplamiento entre componentes |
| **Template Method** | Interface `Calculable` → implementada por `Pedido` | Polimorfismo en cálculos |
| **Soft Delete** | Campo `eliminado` en `Base` | Integridad histórica (no se pierden datos) |

---

## 📸 Capturas de Pantalla (Desde NetBeans)

### Vista del Proyecto en NetBeans (Panel Projects)


<img width="314" height="701" alt="image" src="https://github.com/user-attachments/assets/418b6afe-b019-4e30-a683-f9c7bfd4a84f" />

### Ventana de Output (Ejecutando el Programa)

<img width="515" height="850" alt="image" src="https://github.com/user-attachments/assets/c2874f7f-3e09-46c6-97df-58309d0565b0" />

Creación exitosa de la categoría “Pizzas”. El sistema asigna automáticamente ID=1, registra timestamp, y confirma la operación.

---

<img width="447" height="546" alt="image" src="https://github.com/user-attachments/assets/992695bb-6977-49af-86d7-6c5a2f563ad9" />

Validación de precio negativo rechazada. El servicio ProductoService detecta la violación de la regla R1 y lanza DatoInvalidoException, que el menú captura y muestra amigablemente.

---

<img width="676" height="390" alt="image" src="https://github.com/user-attachments/assets/c38d6a4a-7486-442f-bcd9-524ba0edd29c" />

Listado de productos mostrando la relación N:1 con Categorías. Cada producto displays su categoría asociada ("Pizzas"), demostrando la inyección de dependencia funcional.

---

<img width="676" height="390" alt="image" src="https://github.com/user-attachments/assets/c32ab1c0-72ce-4fb5-9a54-e20300b0e62a" />

Listado de productos mostrando la relación N:1 con Categorías. Cada producto displays su categoría asociada ("Pizzas"), demostrando la inyección de dependencia funcional.

---

<img width="785" height="854" alt="image" src="https://github.com/user-attachments/assets/42a33320-7178-4d8a-8457-00fc46e3d61f" />

Creación de usuario con validación de email. El sistema verifica formato (@) y unicidad contra la base de datos en memoria.


---

<img width="517" height="833" alt="image" src="https://github.com/user-attachments/assets/13a4ce60-ccfa-4817-9fb0-64af73ee3acf" />

Flujo completo de creación de pedido (paso a paso): selección de usuario → agregado de primer detalle (2 Pizzas Napolitana, subtotal $1000) → agregado de segundo detalle (3 Gaseosas, subtotal $210) → resumen final mostrando total $2150 y forma de pago Efectivo. Se observa la interface Calculable calculando el total automáticamente.

---

<img width="645" height="527" alt="image" src="https://github.com/user-attachments/assets/b0ace272-0a21-4ce2-b4ea-2425e7e72ff9" />

Actualización de estado de pedido de PENDIENTE a CONFIRMADO, demostrando el ciclo de vida definido por el enum Estado.

---

## 🎬 Video

🎥 **Video completo (15 minutos):**

[🔗 Ver explicacion del proyecto en YouTube](https://www.youtube.com/watch?v=tQp_0tStC1U)

---

## 👥 Autores

| Integrante | Comisión | Email |
|------------|----------|-----------|
| **Juan Marco Raimundo** | 2 | juanmr.093@gmail.com  |
| **Lucas Bautista Garcia** | 5 | bautigarcia264@gmail.com |

### Datos Académicos
- **Institución:** UTN - Universidad Tecnológica Nacional
- **Carrera:** Tecnicatura Universitaria en Programación a Distancia
- **Materia:** Programación 2
- **Año Lectivo:** 2026
- **IDE Utilizado:** Apache NetBeans 30

---
### PDF Marco Teórico

📄 [Descargar / Ver Documentación Técnica (PDF)](./Raimundo_Garcia_TP-Integrador.pdf)

---

## 📚 Bibliografía y Recursos

### Documentación 

### 4.1 Material de Cátedra
- [1] UTN FRRO. (2026). Trabajo Práctico Integrador - Programación 2. Enunciado oficial con especificación de requisitos UML, historias de usuario y criterios de evaluación.
- [2] UTN FRRO. (2026). UNIDAD 1: Introducción a Java
- [3] UTN FRRO. (2026). UNIDAD 2: Programación Estructurada 
- [4] UTN FRRO. (2026). UNIDAD 3: Introducción a la Orientación a Objetos
- [5] UTN FRRO. (2026). UNIDAD 4: Programación Orientada a Objetos - Nivel Intermedio 
- [6] UTN FRRO. (2026). UNIDAD 5: UML Básico 
- [7] UTN FRRO. (2026). UNIDAD 6: Colecciones en Java
- [8] UTN FRRO. (2026). UNIDAD 7: Herencia y polimorfismo
- [9] UTN FRRO. (2026). UNIDAD 8: Interfaces y Excepciones
- [10] UTN FRRO. (2026). UNIDAD 9: Genéricos en Java
- [11] UTN FRRO. (2026). UNIDAD 10: Acceso a Base de Datos con JDBC

### 4.2 Herramientas
- [12] The Apache Software Foundation. Apache NetBeans IDE 23 - Official Website. Recuperado de: https://netbeans.apache.org/
- [13] GitHub, Inc. Git - Documentación Oficial. Recuperado de: https://git-scm.com/doc

### 4.3  Material extra (HTML para hacer el Readme)
- [14] LenguajeHTML.com. Tutorial de HTML: Aprende HTML desde cero. Recuperado de https://lenguajehtml.com/
- [15] Mozilla Developer Network.. HTML: Lenguaje de Marcas de Hipertexto | MDN. Recuperado de https://developer.mozilla.org/es/docs/Web/HTML
- [16] W3Schools.  HTML Tutorial. Recuperado de https://www.w3schools.com/html/
- [17] Khan Academy. HTML basics: Introduction to HTML. Recuperado de https://es.khanacademy.org/computing/computer-programming/html-css


### Recursos del Curso
- **Enunciado del TP:** Cátedra Programación 2 - UTN 
- **Material de cátedra:** Diapositivas sobre POO, Colecciones, Excepciones, etc.


---

## 🙏 Agradecimientos

- A la **cátedra de Programación 2 de la UTN** por el material didáctico y el enunciado del trabajo práctico.
- Al equipo de **Apache NetBeans** por proporcionar un IDE gratuito y potente para desarrollo Java.

---

## ❓ Preguntas Frecuentes (FAQ)

### Q: ¿Necesito configurar algo especial en NetBeans antes de abrir el proyecto?
**R:** Solo asegúrate de tener JDK 21 configurado en `Tools → Java Platforms`. El resto es automático.

### Q: ¿Puedo abrir este proyecto en IntelliJ IDEA o Eclipse?
**R:** Sí, es código Java estándar. Pero fue diseñado y probado en **NetBeans 23**. En otros IDEs quizás debas ajustar la configuración de ejecución.

### Q: ¿Por qué usaron NetBeans y no otro IDE?
**R:** NetBeans es ligero, gratuito, reconoce proyectos Java sin configuración XML compleja, y es muy usado en entornos académicos de habla hispana. Además, su gestión visual de paquetes facilita entender la arquitectura del proyecto.

### Q: ¿Cómo hago si me da error "Cannot find symbol" al compilar?
**R:** En NetBeans: Clic derecho en el proyecto → "Clean and Build" (Limpiar y Construir). Esto fuerza recompilación completa. Si persiste, verifica que todos los archivos estén en la carpeta correcta según su `package`.

### Q: ¿El proyecto usa Maven o Gradle?
**R:** No. Es un proyecto Java "puro" (Ant-based) creado directamente en NetBeans, sin sistemas de build externos. Esto facilita su apertura inmediata sin instalar nada extra.

---
