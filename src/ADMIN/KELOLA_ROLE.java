package ADMIN;

import Connection.DBConnect;
import com.microsoft.sqlserver.jdbc.SQLServerException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;

public class KELOLA_ROLE extends JFrame{
    public JPanel PanelRole;
    private JTextField txtIdRole;
    private JTable tableRole;
    private JButton btnSave;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JPanel Panel_Form;
    private JPanel Panel_Button;
    private JScrollPane Panel_Data;
    private JLabel Label_IdRole;
    private JLabel Label_NamaRole;
    private JTextField txtJabatan;
    private JTextField txtCari;
    private JButton btnSearch;
    private JPanel Panel_Atas;
    private JPanel Panel_Bawah;
    private JPanel Panel_Kanan;
    private JPanel Panel_Kiri;
    private JPanel Panel_Cari;
    private DefaultTableModel tableModel;
    private DBConnect connection;

    public KELOLA_ROLE() throws HeadlessException {
        setContentPane(PanelRole);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(500, 500);
        connection = new DBConnect();

        tableModel = new DefaultTableModel();
        tableRole.setModel(tableModel);
        addColumn();
        clear();
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (txtJabatan.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdRole.getText();
                        String Jabatan = txtJabatan.getText();

                        // Prepare the query to check if the username already exists
                        String checkQuery = "SELECT COUNT(*) AS count FROM tblRole WHERE Jabatan = '" + txtJabatan.getText() + "'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();

                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            // jika jabatan sama
                            JOptionPane.showMessageDialog(null, "This position is already in place!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Prepare the stored procedure call
                        String storeProcedure = "{CALL sp_InsertRole(?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Jabatan);

                        // Execute the stored procedure
                        connection.pstat.execute();
                        // Close the statement and connection
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data saved successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        clear();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while saving data!");
                    }
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtJabatan.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdRole.getText();
                        String Jabatan = txtJabatan.getText();

                        String checkQuery = "SELECT COUNT(*) AS count FROM tblRole WHERE Jabatan = '" + txtJabatan.getText() + "'AND ID_Role != '"+ txtIdRole.getText()+"'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();
                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            JOptionPane.showMessageDialog(null, "This position is already in place!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        String storeProcedure = "{CALL sp_UpdateRole(?, ?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Jabatan);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data updated successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        clear();
                    } catch (Exception exc) {
                        System.out.println("Error: " + exc.toString());
                        JOptionPane.showMessageDialog(null, "An error occurred!");
                    }
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ID;
                    ID = txtIdRole.getText();

                    String procedureCall = "{CALL sp_DeleteRole(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, ID);

                    connection.pstat.execute();
                    connection.pstat.close();

                    JOptionPane.showMessageDialog(null, "Data deleted successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    clear();
                } catch (SQLServerException exc) {
                    if (exc.getErrorCode() == 547) {
                        // Kode kesalahan 547 menunjukkan adanya REFERENCE constraint
                        JOptionPane.showMessageDialog(null, "Data cannot be deleted because it is referenced in another table.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        System.out.println("Error: " + exc.toString());
                        JOptionPane.showMessageDialog(null, "An error occurred!");
                    }
                } catch (Exception exc) {
                    System.out.println("Error: " + exc.toString());
                    JOptionPane.showMessageDialog(null, "An error occurred!");
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        tableRole.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tableRole.getSelectedRow();
                if(i == -1) {
                    return;
                }

                txtIdRole.setText((String) tableModel.getValueAt(i, 0));
                txtJabatan.setText((String) tableModel.getValueAt(i, 1));

                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                btnClear.setEnabled(true);
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = txtCari.getText().trim();

                if (searchText.isEmpty()) {
                    // Show a message box to inform the user to enter a search term
                    JOptionPane.showMessageDialog(null, "Please enter a search term.", "Search term not entered", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                search(txtCari.getText());
            }
        });
    }

    public void addColumn(){
        tableModel.addColumn("ID Role");
        tableModel.addColumn("Jabatan");
    }

    public void loadData() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "SELECT * FROM tblRole";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[2]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("ID_Role");
                obj[1] = connection.result.getString("Jabatan");

                tableModel.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (SQLException e){
            System.out.println("Error: " + e.toString());
        }
    }

    public void clear(){
        generateId();
        loadData();
        txtJabatan.setText("");
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnClear.setEnabled(true);
    }

    public void generateId() {
        try {
            String query = "SELECT dbo.GenerateIdRole() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtIdRole.setText(connection.result.getString("newId"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void search(String jabatan) {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getRole(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, jabatan);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[2]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("ID_Role");
                obj[1] = connection.result.getString("Jabatan");

                tableModel.addRow(obj);
            }

            if(tableModel.getRowCount() == 0){
                JOptionPane.showMessageDialog(null,"Data not found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
