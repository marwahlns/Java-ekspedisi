package LOGIN;

import ADMIN.DASHBOARD_ADMIN;
import Connection.DBConnect;
import KASIR.DASHBOARD_KASIR;
import MANAGER.DASHBOARD_MANAGER;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LOGIN extends JFrame{
    private JPanel Panel_Kiri;
    private JPanel Panel_Kanan;
    private JPanel Panel_Atas;
    private JPanel Panel_Bawah;
    private JPanel Panel_Tengah;
    private JTextField txtUsername;
    private JButton btnLogin;
    private JPanel Panel_Button;
    private JLabel Label_Username;
    private JLabel Label_Password;
    private JLabel Label_Gambar;
    public JPanel PanelLogin;
    private JCheckBox Checkbox_Password;
    private JPanel Panel_Login;
    private JPanel Panel_Bantuan_Atas;
    private JPanel Panel_Bantuan_Bawah;
    private JPasswordField txtPassword;
    private JButton btnExit;
    String nama,gudang,username,password,jabatan,id,notelp;

    public void FrameConfig(){
        add(this.PanelLogin);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    public String[] validasi(String username, String password){
        if(txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty()){
            JOptionPane.showMessageDialog(PanelLogin,"Please enter username and password","Warning",JOptionPane.WARNING_MESSAGE);
        }else {
            try{
                // Instantiasi koneksi database
                DBConnect connection = new DBConnect();

                String functionCall = "SELECT * FROM dbo.VerifikasiLogin(?,?)";
                connection.pstat = connection.conn.prepareStatement(functionCall);
                connection.pstat.setString(1, username);
                connection.pstat.setString(2, password);

                connection.result = connection.pstat.executeQuery();

                // Mengecheck isi query , apabila isi query kosong maka akan dilakukan perintah dibawahnya
                if(connection.result.next()){
                    // Mengambil isi query pada index ke - n
                    id = connection.result.getString(1);
                    jabatan = connection.result.getString(2);
                    gudang = connection.result.getString(3);

                    // Mengembalikan nilai validasi
                    return new String[] {"true",id,jabatan,gudang,nama,username,password,notelp};
                }
                throw new Exception("User not found!");


            }catch (Exception ex){
                System.out.println(".."+ex.getMessage());
                JOptionPane.showMessageDialog(PanelLogin,ex.getMessage(),"Warning",JOptionPane.WARNING_MESSAGE);
            }
        }
        // Mengembalikan nilai validasi
        return new String[] {"false"};
    }

    public LOGIN() {
        FrameConfig();
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Mengisi array value dengan nilai yang dikembalikan dari metode validasi()
                String[] value = validasi(txtUsername.getText(), txtPassword.getText());
                // Mengisi boolean valid dengan nilai dari index ke 0 value
                Boolean valid = Boolean.parseBoolean(value[0]);
                // Melakukan pengecheckan
                if(valid){
                    // Menampilkan dialog pesan
                    JOptionPane.showMessageDialog(PanelLogin,"Welcome to UDASAMPE","Information",JOptionPane.INFORMATION_MESSAGE);
                    System.out.println(value[0]); //id Pegawai
                    System.out.println(value[2]); // jabatan

                    if(value[2].equals("Manager")){
                        dispose();
                        DASHBOARD_MANAGER form = new DASHBOARD_MANAGER(value);
                        form.setVisible(true);
                    }else if(value[2].equals("Admin")){
                        dispose();
                        DASHBOARD_ADMIN form = new DASHBOARD_ADMIN(value);
                        form.setVisible(true);
                    }else if(value[2].equals("Kasir")){
                        dispose();
                        DASHBOARD_KASIR form = new DASHBOARD_KASIR(value);
                        form.setVisible(true);
                    }
                }
            }
        });
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        Checkbox_Password.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(Checkbox_Password.isSelected() == true){
                    txtPassword.setEchoChar('\0');
                }else {
                    txtPassword.setEchoChar('*');
                }
            }
        });
    }
}