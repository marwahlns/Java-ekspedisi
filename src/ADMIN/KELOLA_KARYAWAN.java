package ADMIN;

import Connection.DBConnect;
import component.ComboboxOpion;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;

public class KELOLA_KARYAWAN extends JFrame{
    private JTextField txtIdKar;
    private JComboBox cbRole;
    private JComboBox cbGudang;
    private JTextField txtNamaKar;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTextField txtNoTelp;
    private JTable tableKaryawan;
    private JButton btnSave;
    private JScrollPane Panel_Data;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnClear;
    private JPanel Panel_Button;
    public JPanel PanelKaryawan;
    private JPanel Panel_Form;
    private JTextField txtCari;
    private JButton btnSearch;
    private JPanel Panel_Atas;
    private JPanel Panel_Kanan;
    private JPanel Panel_Bawah;
    private JPanel Panel_Kiri;
    private JPanel Panel_Cari;
    private JComboBox cbFilter;
    private DefaultTableModel tableModel;
    private DBConnect connection;
    public KELOLA_KARYAWAN() {
        connection = new DBConnect();

        tableModel = new DefaultTableModel();
        tableKaryawan.setModel(tableModel);
        addColumn();
        loadRole();
        loadGudang();
        cbRole.setSelectedItem(null);
        cbGudang.setSelectedItem(null);
        cbFilter.setSelectedItem(null);
        clear();

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbRole.getSelectedItem()==null || cbGudang.getSelectedItem()==null || txtNamaKar.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtNoTelp.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else if(txtNoTelp.getText().length() > 13){
                    JOptionPane.showMessageDialog(null, "Phone number cannot be more than 13 digits!", "Warning",JOptionPane.WARNING_MESSAGE);
                }else if (!txtNoTelp.getText().matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Phone numbers should only contain digits!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    try {
                        String ID = txtIdKar.getText();
                        String Nama = txtNamaKar.getText();
                        String Username = txtUsername.getText();
                        String Password = txtPassword.getText();
                        String Telepon = txtNoTelp.getText();
                        int Status = 1; //default status 1 = Aktif

                        ComboboxOpion selectedOptionRole = (ComboboxOpion) cbRole.getSelectedItem();
                        String Role = selectedOptionRole.getValue();

                        ComboboxOpion selectedOptionGudang = (ComboboxOpion) cbGudang.getSelectedItem();
                        String Gudang = selectedOptionGudang.getValue();

                        // Prepare the query to check if the username already exists
                        String checkQuery = "SELECT COUNT(*) AS count FROM tblKaryawan WHERE Username = '" + txtUsername.getText() + "'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();

                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            // Username already exists
                            JOptionPane.showMessageDialog(null, "Username already exists, choose another one!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Prepare the stored procedure call
                        String storeProcedure = "{CALL sp_InsertKaryawan(?,?,?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Role);
                        connection.pstat.setString(3, Gudang);
                        connection.pstat.setString(4, Nama);
                        connection.pstat.setString(5, Username);
                        connection.pstat.setString(6, Password);
                        connection.pstat.setString(7, Telepon);
                        connection.pstat.setInt(8, Status);

                        // Execute the stored procedure
                        connection.pstat.execute();
                        // Close the statement and connection
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data saved successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        clear();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while saving the data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (cbRole.getSelectedItem()==null || cbGudang.getSelectedItem()==null || txtNamaKar.getText().isEmpty() || txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty() || txtNoTelp.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else if(txtNoTelp.getText().length() > 13){
                    JOptionPane.showMessageDialog(null, "Phone number cannot be more than 13 digits!", "Warning",JOptionPane.WARNING_MESSAGE);
                }else if (!txtNoTelp.getText().matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "Phone numbers should only contain digits!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else{
                    try {
                        String ID = txtIdKar.getText();
                        String Nama = txtNamaKar.getText();
                        String Username = txtUsername.getText();
                        String Password = txtPassword.getText();
                        String Telepon = txtNoTelp.getText();
                        int Status = 1; //default status 1 = Aktif

                        ComboboxOpion selectedOptionRole = (ComboboxOpion) cbRole.getSelectedItem();
                        String Role = selectedOptionRole.getValue();

                        ComboboxOpion selectedOptionGudang = (ComboboxOpion) cbGudang.getSelectedItem();
                        String Gudang = selectedOptionGudang.getValue();

                        // Prepare the query to check if the username already exists
                        String checkQuery = "SELECT COUNT(*) AS count FROM tblKaryawan WHERE Username = '" + txtUsername.getText() + "'AND ID_Karyawan != '"+ txtIdKar.getText()+"'";
                        connection.stat = connection.conn.createStatement();
                        connection.result = connection.stat.executeQuery(checkQuery);
                        connection.result.next();

                        int count = connection.result.getInt("count");

                        if (count > 0) {
                            // Username already exists
                            JOptionPane.showMessageDialog(null, "Username already exists, choose another one!", "Warning", JOptionPane.WARNING_MESSAGE);
                            return;
                        }

                        // Prepare the stored procedure call
                        String storeProcedure = "{CALL sp_UpdateKaryawan(?,?,?,?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Role);
                        connection.pstat.setString(3, Gudang);
                        connection.pstat.setString(4, Nama);
                        connection.pstat.setString(5, Username);
                        connection.pstat.setString(6, Password);
                        connection.pstat.setString(7, Telepon);
                        connection.pstat.setInt(8, Status);

                        // Execute the stored procedure
                        connection.pstat.execute();
                        // Close the statement and connection
                        connection.pstat.close();

                        JOptionPane.showMessageDialog(null, "Data updated successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                        loadData();
                        clear();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(null, "An error occurred while updating the data!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String ID;
                    ID = txtIdKar.getText();

                    String procedureCall = "{CALL sp_DeleteKaryawan(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, ID);

                    connection.pstat.execute();
                    connection.pstat.close();

                    JOptionPane.showMessageDialog(null, "Data deleted successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    loadData();
                    clear();
                }catch (SQLException ex){
                    if (ex.getErrorCode() == 547) {
                        // Kode kesalahan 547 menunjukkan adanya REFERENCE constraint
                        JOptionPane.showMessageDialog(null, "Data cannot be deleted because it is referenced in another table.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        System.out.println("Error: " + ex.toString());
                        JOptionPane.showMessageDialog(null, "An error occurred!");
                    }
                } catch (Exception exc) {
                    System.out.println("Error: "+exc.toString());
                    JOptionPane.showMessageDialog(null, "An error occurred while deleting the data!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clear();
            }
        });
        btnSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedFilter = cbFilter.getSelectedItem() != null ? cbFilter.getSelectedItem().toString() : "";
                String searchText = txtCari.getText().trim();
                if (selectedFilter.isEmpty()) {
                    // Show a message box to inform the user to select a filter
                    JOptionPane.showMessageDialog(null, "Please select a filter before searching.", "Filter not selected", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                if (searchText.isEmpty()) {
                    // Show a message box to inform the user to enter a search term
                    JOptionPane.showMessageDialog(null, "Please enter a search term.", "Search term not entered", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(cbFilter.getSelectedItem().toString() == "Name") {
                    searchNama(txtCari.getText());
                } else if (cbFilter.getSelectedItem().toString() == "Position") {
                    searchJabatan();
                }else{
                    searchGudang();
                }
            }
        });
        tableKaryawan.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);int i = tableKaryawan.getSelectedRow();
                if (i == -1) {
                    return;
                }
                txtIdKar.setText((String) tableModel.getValueAt(i, 0));

                String roleID = (String) tableModel.getValueAt(i, 1);
                String gudangID = (String) tableModel.getValueAt(i, 2);

                for (int x = 0; x < cbRole.getItemCount(); x++) {
                    Object item = cbRole.getItemAt(x);
                    String roleIDCb = ((ComboboxOpion) item).getDisplay();
                    if (roleIDCb.equals(roleID)) {
                        cbRole.setSelectedItem(item);
                        break;
                    }
                }

                for (int x = 0; x < cbGudang.getItemCount(); x++) {
                    Object item = cbGudang.getItemAt(x);
                    String gudangIDCb = ((ComboboxOpion) item).getDisplay();
                    if (gudangIDCb.equals(gudangID)) {
                        cbGudang.setSelectedItem(item);
                        break;
                    }
                }

                txtNamaKar.setText((String) tableModel.getValueAt(i, 3));
                txtUsername.setText((String) tableModel.getValueAt(i,4));
                txtPassword.setText((String) tableModel.getValueAt(i,5));
                txtNoTelp.setText((String) tableModel.getValueAt(i,6));

                btnSave.setEnabled(false);
                btnUpdate.setEnabled(true);
                btnDelete.setEnabled(true);
                btnClear.setEnabled(true);
            }
        });
        txtNamaKar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (Character.isDigit(c)) {
                    e.consume(); // Ignore the key press if it's a digit
                }
            }
        });
        txtNoTelp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (Character.isLetter(c)) {
                    e.consume(); // Ignore the key press if it's a letter
                }
            }
        });
    }

    public void addColumn(){
        tableModel.addColumn("Employee ID");
        tableModel.addColumn("Position");
        tableModel.addColumn("Warehouse");
        tableModel.addColumn("Name");
        tableModel.addColumn("Username");
        tableModel.addColumn("Password");
        tableModel.addColumn("Phone Number");
    }

    public void loadData(){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "SELECT kar.ID_Karyawan, r.Jabatan, g.Nama_Gudang, kar.Nama, kar.Username, kar.Password, kar.No_telp "+
                    "FROM tblKaryawan kar " +
                    "JOIN tblRole r ON kar.ID_Role = r.ID_Role "+
                    "JOIN tblGudang g ON kar.ID_Gudang = g.ID_Gudang";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[7]; // Menyesuaikan jumlah kolom dengan tabel tblPenyewa
                obj[0] = connection.result.getString("ID_Karyawan");
                obj[1] = connection.result.getString("Jabatan");
                obj[2] = connection.result.getString("Nama_Gudang");
                obj[3] = connection.result.getString("Nama");
                obj[4] = connection.result.getString("Username");
                obj[5] = connection.result.getString("Password");
                obj[6] = connection.result.getString("No_telp");

                tableModel.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        }catch (SQLException e){
            System.out.println("Error: " + e.toString());
        }
    }

    public void loadRole() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT ID_Role, Jabatan FROM tblRole";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String ID = connection.result.getString("ID_Role");
                String Jabatan = connection.result.getString("Jabatan");
                cbRole.addItem(new ComboboxOpion( ID, Jabatan));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }

    public void loadGudang() {
        try {
            connection.stat = connection.conn.createStatement();
            String sql = "SELECT ID_Gudang, Nama_Gudang FROM tblGudang";
            connection.result = connection.stat.executeQuery(sql);

            while (connection.result.next()) {
                String ID = connection.result.getString("ID_Gudang");
                String Gudang = connection.result.getString("Nama_Gudang");
                cbGudang.addItem(new ComboboxOpion( ID, Gudang));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException exc) {
            System.out.println("Error: " + exc.toString());
        }
    }

    public void clear(){
        generateId();
        loadData();
        cbRole.setSelectedItem(null);
        cbGudang.setSelectedItem(null);
        txtNamaKar.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        txtNoTelp.setText("");
        txtCari.setText("");
        cbFilter.setSelectedItem(null);
        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnClear.setEnabled(true);
    }

    public void generateId() {
        try {
            String query = "SELECT dbo.GenerateIdKaryawan() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtIdKar.setText(connection.result.getString("newId"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void searchNama(String karyawan){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getKaryawan(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, karyawan);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[7]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("ID_Karyawan");
                obj[1] = connection.result.getString("Jabatan");
                obj[2] = connection.result.getString("Nama_Gudang");
                obj[3] = connection.result.getString("Nama");
                obj[4] = connection.result.getString("Username");
                obj[5] = connection.result.getString("Password");
                obj[6] = connection.result.getString("No_telp");

                tableModel.addRow(obj);
            }

            if(tableModel.getRowCount() == 0){
                JOptionPane.showMessageDialog(null,"Data not found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void searchJabatan(){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT kar.*, r.Jabatan, gud.Nama_Gudang FROM tblKaryawan kar " +
                    "JOIN tblRole r ON kar.ID_Role = r.ID_Role " +
                    "JOIN tblGudang gud ON kar.ID_Gudang = gud.ID_Gudang " +
                    "WHERE r.Jabatan LIKE '%" +txtCari.getText() +"%'";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[7]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("ID_Karyawan");
                obj[1] = connection.result.getString("Jabatan");
                obj[2] = connection.result.getString("Nama_Gudang");
                obj[3] = connection.result.getString("Nama");
                obj[4] = connection.result.getString("Username");
                obj[5] = connection.result.getString("Password");
                obj[6] = connection.result.getString("No_telp");

                tableModel.addRow(obj);
            }

            if(tableModel.getRowCount() == 0){
                JOptionPane.showMessageDialog(null,"Data not found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void searchGudang(){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            DBConnect connection = new DBConnect();
            connection.stat = connection.conn.createStatement();
            String query = "SELECT kar.*, r.Jabatan, gud.Nama_Gudang FROM tblKaryawan kar " +
                    "JOIN tblRole r ON kar.ID_Role = r.ID_Role " +
                    "JOIN tblGudang gud ON kar.ID_Gudang = gud.ID_Gudang " +
                    "WHERE gud.Nama_Gudang LIKE '%" +txtCari.getText() +"%'";
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[7]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("ID_Karyawan");
                obj[1] = connection.result.getString("Jabatan");
                obj[2] = connection.result.getString("Nama_Gudang");
                obj[3] = connection.result.getString("Nama");
                obj[4] = connection.result.getString("Username");
                obj[5] = connection.result.getString("Password");
                obj[6] = connection.result.getString("No_telp");

                tableModel.addRow(obj);
            }

            if(tableModel.getRowCount() == 0){
                JOptionPane.showMessageDialog(null,"Data not found!", "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }
}

