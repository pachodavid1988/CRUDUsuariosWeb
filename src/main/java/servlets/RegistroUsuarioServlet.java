package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.DriverManager;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/RegistroUsuarioServlet")
public class RegistroUsuarioServlet extends HttpServlet {
    private static final String URL = "jdbc:mysql://localhost:3306/crud_usuarios";
    private static final String USER = "root";
    private static final String PASS = ""; // <-- root sin contraseña

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String nombre = request.getParameter("nombre");
        String correo = request.getParameter("correo");
        String password = request.getParameter("password");
        String rol = request.getParameter("rol");

        try {
            // Conexión a la base de datos
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection(URL, USER, PASS);

            // Insertar usuario
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO usuarios(nombre, correo, password, rol) VALUES (?, ?, ?, ?)"
            );
            ps.setString(1, nombre);
            ps.setString(2, correo);
            ps.setString(3, password);
            ps.setString(4, rol);

            int filas = ps.executeUpdate();
            if (filas > 0) {
                request.setAttribute("mensaje", "Usuario registrado correctamente");
            } else {
                request.setAttribute("mensaje", "Error al registrar usuario");
            }

            ps.close();
            con.close();

        } catch (Exception e) {
            request.setAttribute("mensaje", "Error: " + e.getMessage());
        }

        // Redirigir al JSP de resultado (ajustado al nombre correcto)
        request.getRequestDispatcher("Resultado.jsp").forward(request, response);
    }
}