package KASIR;

import LOGIN.LOGIN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DASHBOARD_KASIR extends JFrame{
    private JPanel MainPanel;
    private JPanel Panel_Kiri;
    private JButton btnPengiriman;
    private JButton btnLogout;
    private JPanel Panel_Akun;
    private JLabel Label_Logo;
    private JLabel Label_Kasir;
    private JPanel Panel_Bantuan_Akun;
    private JPanel Panel_Bantuan;
    private JPanel Panel_Menu;
    private JPanel Panel_Tengah;
    private JLabel Label_Role;
    private JLabel Label_Username;
    private JPanel Panel_Form;
    private JLabel Label_Gudang;
    private JButton btnKonfirmasi;
    public static String namaGudang;
    Color newColor = new Color(164,172,118);
    Color defaultColor = new Color(32, 52, 27);

    public void FrameConfig(){
        add(this.MainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public DASHBOARD_KASIR(String[] value) {
        FrameConfig();
        Panel_Kiri.setSize(300,-1);
        Label_Username.setText(value[1]);
        Label_Role.setText(value[2]);
        Label_Gudang.setText(value[3]);
        namaGudang = Label_Gudang.getText();
        btnPengiriman.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Kiri.setSize(300,-1);
                btnPengiriman.setBackground(newColor);
                btnKonfirmasi.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                TRANSAKSI_PENGIRIMAN pengiriman = new TRANSAKSI_PENGIRIMAN();
                pengiriman.PanelPengiriman.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(pengiriman.PanelPengiriman);
            }
        });
        btnKonfirmasi.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Kiri.setSize(300,-1);
                btnKonfirmasi.setBackground(newColor);
                btnPengiriman.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                KONFIRMASI_PAKET konfirmasi = new KONFIRMASI_PAKET();
                konfirmasi.PanelKonfirmasi.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(konfirmasi.PanelKonfirmasi);
            }
        });
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogout.setBackground(newColor);
                btnKonfirmasi.setBackground(defaultColor);
                btnPengiriman.setBackground(defaultColor);

                dispose();
                LOGIN login = new LOGIN();
                login.setVisible(true);
            }
        });
    }
}
