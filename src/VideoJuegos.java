import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;

public class VideoJuegos {
    private Connection connection;
    private JFrame frame;
    private JTextField idField;
    private JTextField tituloField;
    private JTextField generoField;
    private JTextField clasificacionField;
    private JTextField plataformaField;

    // Constructor
    public VideoJuegos() {
        initialize();
        connectToDatabase();

        // Crear la interfaz gráfica
        createGUI();
    }

    // Conexión a la base de datos
    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

    // Configura la conexión a tu base de datos
            String url = "jdbc:mysql://localhost:3306/bibliojuegos";
            String user = "root";
            String password = "";
            connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al conectar a la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    // Inicialización de la aplicación
    private void initialize() {
        frame = new JFrame("Biblioteca de Videojuegos");
        frame.setBounds(200, 200, 1000, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // Crear la interfaz gráfica
    private void createGUI() {
        JPanel panel = new JPanel(new GridLayout(7, 7));
        JPanel panel2 = new JPanel(new BorderLayout());
        frame.getContentPane().add(panel, BorderLayout.WEST);
        frame.getContentPane().add(panel2, BorderLayout.CENTER);

        // Campos de entrada
        JLabel idLabel = new JLabel("ID:");
        idField = new JTextField(4);
        JLabel tituloLabel = new JLabel("Titulo:");
        tituloField = new JTextField();
        JLabel generoLabel = new JLabel("Género:");
        generoField = new JTextField();
        JLabel clasificacionLabel = new JLabel("Clasificación:");
        clasificacionField = new JTextField();
        JLabel plataformaLabel = new JLabel("Plataforma:");
        plataformaField = new JTextField();

        // Botones
        JButton consultarButton = new JButton("Consultar");
        JButton insertarButton = new JButton("Insertar");
        JButton actualizarButton = new JButton("Actualizar");
        JButton borrarButton = new JButton("Borrar");

        // Área de texto para mostrar los resultados
        JTextArea resultTextArea = new JTextArea(7, 20);
        resultTextArea.setEditable(false);

        // Acción del botón Consultar
        consultarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultarJuego(resultTextArea);
            }
        });

        // Acción del botón Insertar
        insertarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                insertarJuego();
            }
        });

        // Acción del botón Actualizar
        actualizarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarJuego();
            }
        });

        // Acción del botón Borrar
        borrarButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                borrarJuego();
            }
        });

        // Agregar componentes al panel
        panel.add(idLabel);
        panel.add(idField);
        panel.add(tituloLabel);
        panel.add(tituloField);
        panel.add(generoLabel);
        panel.add(generoField);
        panel.add(clasificacionLabel);
        panel.add(clasificacionField);
        panel.add(plataformaLabel);
        panel.add(plataformaField);
        panel.add(consultarButton);
        panel.add(insertarButton);
        panel.add(actualizarButton);
        panel.add(borrarButton);

        // Añadir un MouseListener al JTextArea para detectar clics en los registros
        resultTextArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int offset = resultTextArea.viewToModel(e.getPoint());
                try {
                    int rowStart = Utilities.getRowStart(resultTextArea, offset);
                    int rowEnd = Utilities.getRowEnd(resultTextArea, offset);
                    String selectedText = resultTextArea.getText(rowStart, rowEnd - rowStart);
                    cargarDatosSeleccionados(selectedText);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
            }
        });

        panel2.add(new JScrollPane(resultTextArea));
        frame.setVisible(true);
    }

    // Insertar
    private void insertarJuego() {
        try {
            String titulo = tituloField.getText();
            String genero = generoField.getText();
            String clasificacion = clasificacionField.getText();
            String plataforma = plataformaField.getText();
            String sql = "INSERT INTO videojuegos (Titulo, Genero, Clasificacion, Plataforma) VALUES " +
                    "('" + titulo + "', '" + genero + "', '" + clasificacion + "', '" + plataforma + "')";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            JOptionPane.showMessageDialog(frame, "Video Juego ingresado.", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar los campos después de la inserción
            idField.setText("");
            tituloField.setText("");
            generoField.setText("");
            clasificacionField.setText("");
            plataformaField.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al ingresar el videojuego", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para consultar juegos y mostrar los resultados en el JTextArea
    private void consultarJuego(JTextArea resultTextArea) {
        try {
            String sql = "SELECT * FROM videojuegos";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

    // Limpiar el JTextArea antes de mostrar nuevos resultados
            resultTextArea.setText("");
            while (resultSet.next()) {
                int videojuegosID = resultSet.getInt("ID");
                String titulo = resultSet.getString("Titulo");
                String genero = resultSet.getString("Genero");
                String clasificacion = resultSet.getString("Clasificacion");
                String plataforma = resultSet.getString("Plataforma");

    // Agregar los resultados al JTextArea
                resultTextArea.append("ID: " + videojuegosID + ", Titulo: " + titulo + ", Genero: " + genero + ", Plataforma: " + plataforma + ", Clasificacion: " + clasificacion + "\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al consultar Videojuegos", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para cargar los datos del registro seleccionado en los campos correspondientes
    private void cargarDatosSeleccionados(String selectedText) {
        String[] parts = selectedText.split(", ");
        for (String part : parts) {
            String[] keyValue = part.split(": ");
            if (keyValue.length == 2) {
                String key = keyValue[0].trim();
                String value = keyValue[1].trim();
                switch (key) {
                    case "ID":
                        idField.setText(value);
                        break;
                    case "Titulo":
                        tituloField.setText(value);
                        break;
                    case "Genero":
                        generoField.setText(value);
                        break;
                    case "Clasificacion":
                        clasificacionField.setText(value);
                        break;
                    case "Plataforma":
                        plataformaField.setText(value);
                        break;
                }
            }
        }
    }

    // Actualizar
    private void actualizarJuego() {
        try {
            int videojuegosID = Integer.parseInt(idField.getText());
            String titulo = tituloField.getText();
            String genero = generoField.getText();
            String clasificacion = clasificacionField.getText();
            String plataforma = plataformaField.getText();
            String sql = "UPDATE videojuegos SET titulo = '" + titulo + "', genero = '" + genero +
                    "', clasificacion = " + clasificacion + ", plataforma = " + plataforma +" WHERE id = " + videojuegosID;
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            JOptionPane.showMessageDialog(frame, "Videojuego actualizado con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);

            // Limpiar los campos después de la actualización
            idField.setText("");
            tituloField.setText("");
            generoField.setText("");
            clasificacionField.setText("");
            plataformaField.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al actualizar videojuego", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Borrar
    private void borrarJuego() {
        try {
            int videojuegosID = Integer.parseInt(idField.getText());
            String sql = "DELETE FROM videojuegos WHERE id = " + videojuegosID;
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            JOptionPane.showMessageDialog(frame, "Videojuego borrado", "Éxito", JOptionPane.INFORMATION_MESSAGE);

        // Limpiar los campos después de la eliminación
            idField.setText("");
            tituloField.setText("");
            generoField.setText("");
            clasificacionField.setText("");
            plataformaField.setText("");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error al borrar el videojuego", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new VideoJuegos());
    }
}