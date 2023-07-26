package ADMIN;

import Connection.DBConnect;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.sql.SQLException;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class KELOLA_PROMO extends JFrame{
    public JPanel PanelPromo;
    private JPanel Panel_Atas;
    private JPanel Panel_anan;
    private JPanel Panel_Kiri;
    private JPanel Panel_Bawah;
    private JPanel Panel_Form;
    private JPanel Panel_Member;
    private JTextField txtIdPromo;
    private JTextField txtNamaPromo;
    private JButton btnSave;
    private JTable tablePromo;
    private JButton btnClear;
    private JTextField txtPotongan;
    private JPanel jpTanggalAwal;
    private JPanel jpTanggalAkhir;
    private JPanel Panel_Kanan;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTextField txtCari;
    private JButton btnSearch;
    JDateChooser datechos1 = new JDateChooser();
    JDateChooser datechos2 = new JDateChooser();
    private DefaultTableModel tableModel;
    private DBConnect connection;

    public KELOLA_PROMO() {
        connection = new DBConnect();
        tableModel = new DefaultTableModel();
        tablePromo.setModel(tableModel);
        jpTanggalAwal.add(datechos1);
        jpTanggalAkhir.add(datechos2);

        addColumn();
        clear();
        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(txtIdPromo.getText().isEmpty() || txtNamaPromo.getText().isEmpty() || datechos1.getDate() == null || datechos2.getDate() == null || txtPotongan.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else if (datechos1.getDate().after(datechos2.getDate())) {
                    JOptionPane.showMessageDialog(null, "Start date cannot be after end date!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdPromo.getText();
                        String Nama = txtNamaPromo.getText();

                        Format formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String TanggalAwal = formatter1.format(datechos1.getDate());

                        Format formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String TanggalAkhir = formatter2.format(datechos2.getDate());

                        float Potongan = Integer.parseInt(txtPotongan.getText());

                        // Prepare the stored procedure call
                        String storeProcedure = "{CALL sp_InsertTrsPromo(?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Nama);
                        connection.pstat.setString(3, TanggalAwal);
                        connection.pstat.setString(4, TanggalAkhir);
                        connection.pstat.setFloat(5, Potongan);

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
                if(txtNamaPromo.getText().isEmpty() || datechos1.getDate() == null || datechos2.getDate() == null || txtPotongan.getText().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Please enter all required information!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else if (datechos1.getDate().after(datechos2.getDate())) {
                    JOptionPane.showMessageDialog(null, "Start date cannot be after end date!", "Warning", JOptionPane.WARNING_MESSAGE);
                }else {
                    try {
                        String ID = txtIdPromo.getText();
                        String Nama = txtNamaPromo.getText();
                        Format formatter1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String TanggalAwal = formatter1.format(datechos1.getDate());

                        Format formatter2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        String TanggalAkhir = formatter2.format(datechos2.getDate());
                        float Potongan = Float.parseFloat(txtPotongan.getText());

                        String storeProcedure = "{CALL sp_UpdateTrsPromo(?,?,?,?,?)}";
                        connection.pstat = connection.conn.prepareCall(storeProcedure);
                        connection.pstat.setString(1, ID);
                        connection.pstat.setString(2, Nama);
                        connection.pstat.setString(3, TanggalAwal);
                        connection.pstat.setString(4, TanggalAkhir);
                        connection.pstat.setFloat(5, Potongan);

                        connection.pstat.executeUpdate();
                        connection.pstat.close();
                        loadData();
                        clear();
                        JOptionPane.showMessageDialog(null, "Data updated successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception exc) {
                        System.out.println("Error: " + exc.toString());
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
                    ID = txtIdPromo.getText();

                    String procedureCall = "{CALL sp_DeleteTrsPromo(?)}";
                    connection.pstat = connection.conn.prepareCall(procedureCall);
                    connection.pstat.setString(1, ID);

                    connection.pstat.execute();
                    connection.pstat.close();

                    loadData();
                    clear();
                    JOptionPane.showMessageDialog(null, "Data deleted successfully!", "Information", JOptionPane.INFORMATION_MESSAGE);
                } catch (SQLException ex){
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
        tablePromo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int i = tablePromo.getSelectedRow();
                if(i == -1) {
                    return;
                }

                txtIdPromo.setText((String) tableModel.getValueAt(i, 0));
                txtNamaPromo.setText((String) tableModel.getValueAt(i, 1));

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date startdate = formatter.parse((String) tableModel.getValueAt(i, 2));
                    datechos1.setDate(startdate);
                    java.util.Date enddate = formatter.parse((String) tableModel.getValueAt(i,3));
                    datechos2.setDate(enddate);
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                Float potonganFloat = (Float) tableModel.getValueAt(i, 4);

                String potonganStr = potonganFloat.toString();
                txtPotongan.setText(String.valueOf(potonganStr));

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
        tableModel.addColumn("Promo ID");
        tableModel.addColumn("Promo Name");
        tableModel.addColumn("Start Date");
        tableModel.addColumn("End Date");
        tableModel.addColumn("Discount");
    }

    public void loadData() {
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();

        try {
            String query = "SELECT * FROM tblPromo";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            while (connection.result.next()) {
                Object[] obj = new Object[5];
                obj[0] = connection.result.getString("ID_Promo");
                obj[1] = connection.result.getString("Nama_Promo");
                obj[2] = connection.result.getString("Tanggal_BerlakuAwal");
                obj[3] = connection.result.getString("Tanggal_BerlakuAkhir");
                obj[4] = connection.result.getFloat("Potongan");

                tableModel.addRow(obj);
            }
            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            System.out.println("Error: " + e.toString());
        }
    }

    public void clear(){
        generateId();
        loadData();
        txtNamaPromo.setText("");
        datechos1.setDate(null);
        datechos2.setDate(null);
        txtPotongan.setText("");

        btnSave.setEnabled(true);
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        btnClear.setEnabled(true);
    }

    public void generateId() {
        try {
            String query = "SELECT dbo.GenerateIdPromo() AS newId";
            connection.stat = connection.conn.createStatement();
            connection.result = connection.stat.executeQuery(query);

            // perbarui data
            while (connection.result.next()) {
                txtIdPromo.setText(connection.result.getString("newId"));
            }

            connection.stat.close();
            connection.result.close();
        } catch (SQLException e) {
            // Handle any errors that occur during the execution
            e.printStackTrace();
        }
    }

    public void search(String Promo){
        tableModel.getDataVector().removeAllElements();
        tableModel.fireTableDataChanged();
        try {
            String functionCall = "SELECT * FROM dbo.getPromo(?)";
            connection.pstat = connection.conn.prepareStatement(functionCall);
            connection.pstat.setString(1, Promo);

            connection.result = connection.pstat.executeQuery();

            while (connection.result.next()) {
                Object[] obj = new Object[5]; // Menyesuaikan jumlah kolom dengan tabel
                obj[0] = connection.result.getString("ID_Promo");
                obj[1] = connection.result.getString("Nama_Promo");
                obj[2] = connection.result.getString("Tanggal_BerlakuAwal");
                obj[3] = connection.result.getString("Tanggal_BerlakuAkhir");
                obj[4] = connection.result.getFloat("Potongan");

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