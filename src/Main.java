import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

class Doctor implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contadorDoctores = 1;
    private int id;
    private String nombreCompleto;
    private String especialidad;

    public Doctor(String nombreCompleto, String especialidad) {
        this.id = contadorDoctores++;
        this.nombreCompleto = nombreCompleto;
        this.especialidad = especialidad;
    }

    public int getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getEspecialidad() {
        return especialidad;
    }
}

class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contadorPacientes = 1;
    private int id;
    private String nombreCompleto;

    public Paciente(String nombreCompleto) {
        this.id = contadorPacientes++;
        this.nombreCompleto = nombreCompleto;
    }

    public int getId() {
        return id;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }
}

class Cita implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int contadorCitas = 1;
    private int id;
    private Date fechaHora;
    private String motivo;
    private Doctor doctor;
    private Paciente paciente;

    public Cita(Date fechaHora, String motivo, Doctor doctor, Paciente paciente) {
        this.id = contadorCitas++;
        this.fechaHora = fechaHora;
        this.motivo = motivo;
        this.doctor = doctor;
        this.paciente = paciente;
    }

    public int getId() {
        return id;
    }

    public Date getFechaHora() {
        return fechaHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Paciente getPaciente() {
        return paciente;
    }
}

class ControlAcceso implements Serializable {
    private static final long serialVersionUID = 1L;
    private String identificador;
    private String contrasena;

    public ControlAcceso(String identificador, String contrasena) {
        this.identificador = identificador;
        this.contrasena = contrasena;
    }

    public boolean autenticarUsuario() {
        // Implementa la lógica de autenticación aquí
        return true; // Temporalmente siempre devuelve true, debes implementar la lógica real
    }
}

public class Main {
    private static final String DOCTORES_FILE = "doctores.txt";
    private static final String PACIENTES_FILE = "pacientes.txt";
    private static final String CITAS_FILE = "citas.txt";
    private static final String ADMIN_FILE = "admin.txt";

    private List<Doctor> doctores = new ArrayList<>();
    private List<Paciente> pacientes = new ArrayList<>();
    private List<Cita> citas = new ArrayList<>();

    public static void main(String[] args) {
        Main main = new Main();
        main.iniciarAplicacion();
    }

    private void iniciarAplicacion() {
        cargarDatosDesdeArchivos();

        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            mostrarMenu();
            System.out.print("Seleccione una opción: ");
            opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    darDeAltaDoctor(scanner);
                    break;
                case 2:
                    darDeAltaPaciente(scanner);
                    break;
                case 3:
                    crearCita(scanner);
                    break;
                case 4:
                    // Otras opciones...
                    break;
                case 0:
                    System.out.println("Saliendo del programa. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
                    break;
            }
        } while (opcion != 0);

        guardarDatosEnArchivos();
        scanner.close();
    }

    private void mostrarMenu() {
        System.out.println("\n--- Menú ---");
        System.out.println("1. Dar de alta doctor");
        System.out.println("2. Dar de alta paciente");
        System.out.println("3. Crear cita");
        System.out.println("4. Otras opciones...");
        System.out.println("0. Salir");
    }

    private void darDeAltaDoctor(Scanner scanner) {
        System.out.print("Ingrese el nombre completo del doctor: ");
        String nombreCompleto = scanner.nextLine();
        System.out.print("Ingrese la especialidad del doctor: ");
        String especialidad = scanner.nextLine();

        Doctor doctor = new Doctor(nombreCompleto, especialidad);
        doctores.add(doctor);

        System.out.println("Doctor dado de alta correctamente.");
    }

    private void darDeAltaPaciente(Scanner scanner) {
        System.out.print("Ingrese el nombre completo del paciente: ");
        String nombreCompleto = scanner.nextLine();

        Paciente paciente = new Paciente(nombreCompleto);
        pacientes.add(paciente);

        System.out.println("Paciente dado de alta correctamente.");
    }

    private void crearCita(Scanner scanner) {
        if (doctores.isEmpty() || pacientes.isEmpty()) {
            System.out.println("No hay doctores o pacientes registrados para crear una cita.");
            return;
        }

        System.out.print("Ingrese la fecha y hora de la cita (formato dd/MM/yyyy HH:mm): ");
        String fechaHoraString = scanner.nextLine();
        System.out.print("Ingrese el motivo de la cita: ");
        String motivo = scanner.nextLine();

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date fechaHora = dateFormat.parse(fechaHoraString);

            System.out.println("Seleccione un doctor:");
            mostrarDoctores();
            int idDoctor = scanner.nextInt();
            Doctor doctorSeleccionado = obtenerDoctorPorId(idDoctor);

            System.out.println("Seleccione un paciente:");
            mostrarPacientes();
            int idPaciente = scanner.nextInt();
            Paciente pacienteSeleccionado = obtenerPacientePorId(idPaciente);

            Cita cita = new Cita(fechaHora, motivo, doctorSeleccionado, pacienteSeleccionado);
            citas.add(cita);

            System.out.println("Cita creada correctamente.");
        } catch (ParseException e) {
            System.out.println("Error al parsear la fecha y hora. Intente nuevamente.");
        }
    }

    private void mostrarDoctores() {
        for (Doctor doctor : doctores) {
            System.out.println(new StringBuilder()
                    .append(doctor.getId())
                    .append(". ")
                    .append(doctor.getNombreCompleto())
                    .append(" - ")
                    .append(doctor.getEspecialidad())
                    .toString());
        }
    }

    private void mostrarPacientes() {
        for (Paciente paciente : pacientes) {
            System.out.println(new StringBuilder()
                    .append(paciente.getId())
                    .append(". ")
                    .append(paciente.getNombreCompleto())
                    .toString());
        }
    }

    private Doctor obtenerDoctorPorId(int id) {
        for (Doctor doctor : doctores) {
            if (doctor.getId() == id) {
                return doctor;
            }
        }
        return null;
    }

    private Paciente obtenerPacientePorId(int id) {
        for (Paciente paciente : pacientes) {
            if (paciente.getId() == id) {
                return paciente;
            }
        }
        return null;
    }

    private void cargarDatosDesdeArchivos() {
        try (ObjectInputStream doctoresStream = new ObjectInputStream(new FileInputStream(DOCTORES_FILE));
             ObjectInputStream pacientesStream = new ObjectInputStream(new FileInputStream(PACIENTES_FILE));
             ObjectInputStream citasStream = new ObjectInputStream(new FileInputStream(CITAS_FILE))) {

            doctores = (List<Doctor>) doctoresStream.readObject();
            pacientes = (List<Paciente>) pacientesStream.readObject();
            citas = (List<Cita>) citasStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error al cargar datos desde archivos. Se crearán archivos nuevos.");
        }
    }

    private void guardarDatosEnArchivos() {
        try (ObjectOutputStream doctoresStream = new ObjectOutputStream(new FileOutputStream(DOCTORES_FILE));
             ObjectOutputStream pacientesStream = new ObjectOutputStream(new FileOutputStream(PACIENTES_FILE));
             ObjectOutputStream citasStream = new ObjectOutputStream(new FileOutputStream(CITAS_FILE))) {

            doctoresStream.writeObject(doctores);
            pacientesStream.writeObject(pacientes);
            citasStream.writeObject(citas);

        } catch (IOException e) {
            System.out.println("Error al guardar datos en archivos.");
        }
    }
}1