package Vista;

import Modelo.Autobus;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class SeleccionAutobusVentana extends JPanel {
    private JButton irAtras;
    private JComboBox<String> lugarPartida;
    private JComboBox<String> destino;
    private JButton btnBuscar;
    private JTable tabla;
    private DefaultTableModel tablaSeleccionAutobus;

    private List<Autobus> autobuses = new ArrayList<>(); // Lista para almacenar los autobuses disponibles
    private List<Autobus> autobusesFiltrados = new ArrayList<>(); // Lista para almacenar los autobuses filtrados

    public SeleccionAutobusVentana() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // Panel superior con el botón "Atrás"
        JPanel panelSuperior = crearPanelSuperior();
        add(panelSuperior, BorderLayout.NORTH);

        // Panel central con los paneles "Busqueda" y "detallesAutobus"
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        panelCentral.setBackground(Color.WHITE);

        JPanel panelBusqueda = crearPanelBusqueda();
        JPanel panelDetalles = crearPanelDetalles();

        panelCentral.add(panelBusqueda);
        panelCentral.add(panelDetalles);

        add(panelCentral, BorderLayout.CENTER);
    }

    private JPanel crearPanelSuperior() {
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelSuperior.setBackground(Color.WHITE);

        irAtras = new JButton("Atrás") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(150, 150, 255));
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(180, 180, 255));
                } else {
                    g2.setColor(new Color(135, 66, 255, 50));
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

                g2.setColor(new Color(0x3B0193));
                g2.setFont(new Font("Arial", Font.BOLD, 16));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth("ATRÁS")) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent() + 2;
                g2.drawString("ATRÁS", x, y);
            }
        };

        irAtras.setContentAreaFilled(false);
        irAtras.setFocusPainted(false);
        irAtras.setBorderPainted(false);
        irAtras.setPreferredSize(new Dimension(100, 30));
        panelSuperior.add(irAtras);

        return panelSuperior;
    }

    private JPanel crearPanelBusqueda() {
        JPanel panelBusqueda = new JPanel();
        panelBusqueda.setLayout(null);
        panelBusqueda.setBackground(Color.WHITE);
        panelBusqueda.setPreferredSize(new Dimension(1040, 150));

        int x = 20, y = 20, width = 200, height = 30, gap = 20;

        JLabel lblPartida = new JLabel("Origen");
        lblPartida.setBounds(x, y, width, height);
        panelBusqueda.add(lblPartida);

        lugarPartida = new JComboBox<>();
        lugarPartida.setBounds(x, y + height + 5, width, height);
        panelBusqueda.add(lugarPartida);

        JLabel lblDestino = new JLabel("Destino");
        lblDestino.setBounds(x + width + gap, y, width, height);
        panelBusqueda.add(lblDestino);

        destino = new JComboBox<>();
        destino.setBounds(x + width + gap, y + height + 5, width, height);
        panelBusqueda.add(destino);

        // Botón "Buscar"
        btnBuscar = new JButton("BUSCAR") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(new Color(150, 150, 255)); // Color cuando se presiona
                } else if (getModel().isRollover()) {
                    g2.setColor(new Color(180, 180, 255)); // Color cuando pasa el ratón sobre el botón
                } else {
                    g2.setColor(new Color(135, 66, 255, 50)); // Color por defecto
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30); // Bordes redondeados

                g2.setColor(new Color(0x3B0193)); // Color del texto
                g2.setFont(new Font("Arial", Font.BOLD, 16)); // Fuente del texto
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth("BUSCAR")) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - fm.getDescent() + 2;
                g2.drawString("BUSCAR", x, y); // Centrar el texto
            }
        };

        btnBuscar.setContentAreaFilled(false);
        btnBuscar.setFocusPainted(false);
        btnBuscar.setBorderPainted(false);
        btnBuscar.setPreferredSize(new Dimension(100, 30));
        btnBuscar.setBounds(x + 2 * (width + gap), y + height + 5, width, height);
        panelBusqueda.add(btnBuscar);

        // Listener para el botón "Buscar"
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                realizarBusqueda();
            }
        });

        return panelBusqueda;
    }

    private JPanel crearPanelDetalles() {
        JPanel detallesAutobus = new JPanel(new BorderLayout());
        detallesAutobus.setPreferredSize(new Dimension(1040, 500));
        detallesAutobus.setBackground(Color.WHITE);

        // Establecer las columnas de la tabla
        String[] columnas = {"ID", "Origen", "Destino", "Hora", "Tipo"};

        tablaSeleccionAutobus = new DefaultTableModel(new Object[0][0], columnas) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // hace que las celdas no sean editables
            }
        };

        tabla = new JTable(tablaSeleccionAutobus);

        // Personalizar la tabla
        tabla.setFont(new Font("Arial", Font.PLAIN, 14));
        tabla.setRowHeight(30); // Altura filas
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setSelectionBackground(new Color(105, 101, 218, 100));
        tabla.setSelectionForeground(new Color(0x3B0192));
        tabla.getTableHeader().setFont(new Font("Arial", Font.BOLD, 16));
        tabla.getTableHeader().setBackground(new Color(59, 1, 145));
        tabla.getTableHeader().setForeground(Color.WHITE);
        tabla.setShowGrid(true); // Visibilidad de las líneas de la tabla

        // Ancho de las columnas
        tabla.getColumnModel().getColumn(0).setPreferredWidth(50); // ID
        tabla.getColumnModel().getColumn(1).setPreferredWidth(160); // Origen
        tabla.getColumnModel().getColumn(2).setPreferredWidth(160); // Destino
        tabla.getColumnModel().getColumn(3).setPreferredWidth(100); // Hora
        tabla.getColumnModel().getColumn(4).setPreferredWidth(100); // Tipo

        tabla.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {  // Doble clic
                    int row = tabla.getSelectedRow();
                    String autobusId = (String) tabla.getValueAt(row, 0);

                    Autobus autobusSeleccionado = obtenerAutobusPorId(autobusId);

                    if (autobusSeleccionado != null) {
                        // Crear la ventana de selección de asientos y pasar el autobús seleccionado
                        SeleccionAsientosVentana ventana = new SeleccionAsientosVentana(autobusSeleccionado); // Asumiendo que el constructor acepta un Autobus
                        ventana.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                        ventana.setVisible(true);  // Abrir la ventana

                        // Obtener las coordenadas de la ventana de selección de asientos
                        int x = getLocationOnScreen().x;
                        int y = getLocationOnScreen().y;

                        // Ajustar la ubicación para que la nueva ventana sea posicionada respecto a la primera ventana
                        ventana.setLocation(x + 50, y + 15);
                    }
                }
            }
        });


        detallesAutobus.add(new JScrollPane(tabla), BorderLayout.CENTER);

        return detallesAutobus;
    }


    // Método para obtener el autobus por ID
    private Autobus obtenerAutobusPorId(String id) {
        for (Autobus autobus : autobusesFiltrados) {
            if (autobus.getId().equals(id)) { // Usar equals para comparar Strings
                return autobus;
            }
        }
        return null;
    }

    // Método para realizar la búsqueda y filtrar los autobuses
    private void realizarBusqueda() {
        String partida = (String) lugarPartida.getSelectedItem();
        String destinoSeleccionado = (String) destino.getSelectedItem();

        // Filtrar autobuses
        autobusesFiltrados.clear();
        for (Autobus autobus : autobuses) {
            if (autobus.getLugarDeInicio().equals(partida) && autobus.getLugarDeDestino().equals(destinoSeleccionado)) {
                autobusesFiltrados.add(autobus);
            }
        }

        // Actualizar tabla
        actualizarTabla();
    }

    // Método para actualizar la tabla con los autobuses filtrados
    private void actualizarTabla() {
        tablaSeleccionAutobus.setRowCount(0); // Limpiar la tabla
        for (Autobus autobus : autobusesFiltrados) {
            tablaSeleccionAutobus.addRow(new Object[]{autobus.getId(), autobus.getLugarDeInicio(),
                    autobus.getLugarDeDestino(), autobus.getHorario(), autobus.getTipo()});
        }
    }

    public void setAutobuses(List<Autobus> listaAutobuses) {
        this.autobuses = listaAutobuses;
    }

    // Método para establecer los lugares de inicio
    public void setLugaresInicio(List<String> lugaresInicio) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(lugaresInicio.toArray(new String[0]));
        lugarPartida.setModel(model);
    }

    // Método para establecer los destinos
    public void setDestinos(List<String> destinos) {
        DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(destinos.toArray(new String[0]));
        destino.setModel(model);
    }

    public JButton getIrAtras() {
        return irAtras;
    }
}

