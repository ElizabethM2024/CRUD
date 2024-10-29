package Formulario_registrate;

import javax.swing.*;
import java.awt.event.*;
import java.sql.*;

public class DatosPersonalesForm extends JFrame {
    private JTextField txtNombre, txtApellidos, txtDocumento, txtCorreo, txtDireccion;
    private JPasswordField txtContrasena;
    private JButton btnGuardar, btnConsultar, btnActualizar, btnEliminar, btnCerrar;

    public DatosPersonalesForm() {
        setTitle("Registrate");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(null);

        // Componentes del formulario
        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 100, 30);
        txtNombre = new JTextField();
        txtNombre.setBounds(130, 20, 200, 30);
        
        JLabel lblApellidos = new JLabel("Apellidos:");
        lblApellidos.setBounds(20, 60, 100, 30);
        txtApellidos = new JTextField();
        txtApellidos.setBounds(130, 60, 200, 30);
        
        JLabel lblDocumento = new JLabel("Documento:");
        lblDocumento.setBounds(20, 100, 100, 30);
        txtDocumento = new JTextField();
        txtDocumento.setBounds(130, 100, 200, 30);
        
        JLabel lblCorreo = new JLabel("Correo:");
        lblCorreo.setBounds(20, 140, 150, 30);
        txtCorreo = new JTextField();
        txtCorreo.setBounds(130, 140, 200, 30);
        
        JLabel lblDireccion = new JLabel("Dirección:");
        lblDireccion.setBounds(20, 180, 100, 30);
        txtDireccion = new JTextField();
        txtDireccion.setBounds(130, 180, 200, 30);
        
        JLabel lblContrasena = new JLabel("Contraseña:");
        lblContrasena.setBounds(20, 220, 100, 30);
        txtContrasena = new JPasswordField();
        txtContrasena.setBounds(130, 220, 200, 30);
        
        // Botones
        btnGuardar = new JButton("Guardar");
        btnGuardar.setBounds(20, 270, 90, 30);
        btnGuardar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                guardarDatos();
            }
        });

        btnConsultar = new JButton("Consultar");
        btnConsultar.setBounds(120, 270, 90, 30);
        btnConsultar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                consultarDatos();
            }
        });

        btnActualizar = new JButton("Actualizar");
        btnActualizar.setBounds(220, 270, 90, 30);
        btnActualizar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                actualizarDatos();
            }
        });

        btnEliminar = new JButton("Eliminar");
        btnEliminar.setBounds(320, 270, 90, 30);
        btnEliminar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                eliminarDatos();
            }
        });

        btnCerrar = new JButton("Cerrar");
        btnCerrar.setBounds(150, 320, 90, 30);
        btnCerrar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cerrarFormulario();
            }
        });

        // Añadir componentes al frame
        add(lblNombre);
        add(txtNombre);
        add(lblApellidos);
        add(txtApellidos);
        add(lblDocumento);
        add(txtDocumento);
        add(lblCorreo);
        add(txtCorreo);
        add(lblDireccion);
        add(txtDireccion);
        add(lblContrasena);
        add(txtContrasena);
        add(btnGuardar);
        add(btnConsultar);
        add(btnActualizar);
        add(btnEliminar);
        add(btnCerrar);
    }

    private void guardarDatos() {
        if (validarCampos()) {
            Connection con = null;
            PreparedStatement pstmt = null;
            try {
                con = DatabaseConnection.getConnection();
                String sql = "INSERT INTO formulario_registrate (nombre, apellidos, documento, correo_electronico, direccion, contrasena) VALUES (?, ?, ?, ?, ?, ?)";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, txtNombre.getText());
                pstmt.setString(2, txtApellidos.getText());
                pstmt.setString(3, txtDocumento.getText());
                pstmt.setString(4, txtCorreo.getText());
                pstmt.setString(5, txtDireccion.getText());
                pstmt.setString(6, new String(txtContrasena.getPassword()));
                pstmt.executeUpdate();
                JOptionPane.showMessageDialog(this, "Datos guardados correctamente.");
                
                limpiarCampos();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al guardar datos: " + ex.getMessage());
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (con != null) con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void consultarDatos() {
        String documento = txtDocumento.getText();
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese el documento para consultar.");
            return;
        }

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            con = DatabaseConnection.getConnection();
            String sql = "SELECT * FROM formulario_registrate WHERE documento = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, documento);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                // Llenar los campos del formulario con los datos encontrados
                txtNombre.setText(rs.getString("nombre"));
                txtApellidos.setText(rs.getString("apellidos"));
                txtCorreo.setText(rs.getString("correo_electronico"));
                txtDireccion.setText(rs.getString("direccion"));
                txtContrasena.setText(rs.getString("contrasena"));
                JOptionPane.showMessageDialog(this, "Datos consultados correctamente.");
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron datos para el documento: " + documento);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al consultar datos: " + ex.getMessage());
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void actualizarDatos() {
        if (validarCampos()) {
            Connection con = null;
            PreparedStatement pstmt = null;
            try {
                con = DatabaseConnection.getConnection();
                String sql = "UPDATE formulario_registrate SET nombre = ?, apellidos = ?, correo_electronico = ?, direccion = ?, contrasena = ? WHERE documento = ?";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, txtNombre.getText());
                pstmt.setString(2, txtApellidos.getText());
                pstmt.setString(3, txtCorreo.getText());
                pstmt.setString(4, txtDireccion.getText());
                pstmt.setString(5, new String(txtContrasena.getPassword()));
                pstmt.setString(6, txtDocumento.getText());
                int rowsUpdated = pstmt.executeUpdate();
                if (rowsUpdated > 0) {
                    JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "No se encontraron datos para actualizar.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error al actualizar datos: " + ex.getMessage());
            } finally {
                try {
                    if (pstmt != null) pstmt.close();
                    if (con != null) con.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    private void eliminarDatos() {
        String documento = txtDocumento.getText();
        if (documento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese el documento para eliminar.");
            return;
        }

        Connection con = null;
        PreparedStatement pstmt = null;
        try {
            con = DatabaseConnection.getConnection();
            String sql = "DELETE FROM formulario_registrate WHERE documento = ?";
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, documento);
            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(this, "Datos eliminados correctamente.");
                limpiarCampos();
            } else {
                JOptionPane.showMessageDialog(this, "No se encontraron datos para eliminar.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al eliminar datos: " + ex.getMessage());
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void cerrarFormulario() {
        dispose(); // Cierra el formulario
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtApellidos.setText("");
        txtDocumento.setText("");
        txtCorreo.setText("");
        txtDireccion.setText("");
        txtContrasena.setText("");
    }

    private boolean validarCampos() {
        if (txtNombre.getText().isEmpty() || txtApellidos.getText().isEmpty() ||
            txtDocumento.getText().isEmpty() || txtCorreo.getText().isEmpty() ||
            txtDireccion.getText().isEmpty() || txtContrasena.getPassword().length == 0) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new DatosPersonalesForm().setVisible(true);
            }
        });
    }
}
