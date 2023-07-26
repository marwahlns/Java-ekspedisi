package ADMIN;

import LOGIN.LOGIN;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DASHBOARD_ADMIN extends JFrame{
    private JButton btnKelolaRole;
    private JButton btnKelolaKaryawan;
    private JButton btnLogout;
    private JPanel MainPanel;
    private JPanel Panel_Kiri;
    private JButton btnKelolaGudang;
    private JButton btnKelolaJenisPengiriman;
    private JButton btnKelolaKab;
    private JButton btnKelolaKec;
    private JPanel Panel_Akun;
    private JPanel Panel_Bantuan_Akun;
    private JLabel Label_Admin;
    private JLabel Label_Role;
    private JLabel Label_Username;
    private JPanel Panel_Bantuan;
    private JLabel Label_Logo;
    private JPanel Panel_Menu;
    private JPanel Panel_Tengah;
    private JPanel Panel_Form;
    public JLabel Label_Gudang;
    private JButton btnPromo;
    Color newColor = new Color(164,172,118);
    Color defaultColor = new Color(32, 52, 27);

    public void FrameConfig(){
        add(this.MainPanel);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }
    public DASHBOARD_ADMIN(String[] value) {
        FrameConfig();

        Panel_Kiri.setSize(300,-1);
        Label_Username.setText(value[1]);
        Label_Role.setText(value[2]);
        Label_Gudang.setText(value[3]);
        btnKelolaRole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Kiri.setSize(300,-1);
                btnKelolaRole.setBackground(newColor);
                btnKelolaKaryawan.setBackground(defaultColor);
                btnKelolaGudang.setBackground(defaultColor);
                btnKelolaJenisPengiriman.setBackground(defaultColor);
                btnKelolaKab.setBackground(defaultColor);
                btnKelolaKec.setBackground(defaultColor);
                btnPromo.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                KELOLA_ROLE role = new KELOLA_ROLE();
                role.PanelRole.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(role.PanelRole);
            }
        });
        btnKelolaKaryawan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnKelolaKaryawan.setBackground(newColor);
                btnKelolaRole.setBackground(defaultColor);
                btnKelolaGudang.setBackground(defaultColor);
                btnKelolaJenisPengiriman.setBackground(defaultColor);
                btnKelolaKab.setBackground(defaultColor);
                btnKelolaKec.setBackground(defaultColor);
                btnPromo.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                KELOLA_KARYAWAN karyawan = new KELOLA_KARYAWAN();
                karyawan.PanelKaryawan.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(karyawan.PanelKaryawan);
            }
        });
        btnKelolaGudang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnKelolaGudang.setBackground(newColor);
                btnKelolaRole.setBackground(defaultColor);
                btnKelolaKaryawan.setBackground(defaultColor);
                btnKelolaJenisPengiriman.setBackground(defaultColor);
                btnKelolaKab.setBackground(defaultColor);
                btnKelolaKec.setBackground(defaultColor);
                btnPromo.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                KELOLA_GUDANG gudang = new KELOLA_GUDANG();
                gudang.PanelGudang.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(gudang.PanelGudang);
            }
        });
        btnKelolaJenisPengiriman.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnKelolaJenisPengiriman.setBackground(newColor);
                btnKelolaRole.setBackground(defaultColor);
                btnKelolaKaryawan.setBackground(defaultColor);
                btnKelolaGudang.setBackground(defaultColor);
                btnKelolaKab.setBackground(defaultColor);
                btnKelolaKec.setBackground(defaultColor);
                btnPromo.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                KELOLA_JENIS_PENGIRIMAN jenisPengiriman = new KELOLA_JENIS_PENGIRIMAN();
                jenisPengiriman.PanelJenisPengiriman.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(jenisPengiriman.PanelJenisPengiriman);
            }
        });
        btnKelolaKab.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnKelolaKab.setBackground(newColor);
                btnKelolaRole.setBackground(defaultColor);
                btnKelolaKaryawan.setBackground(defaultColor);
                btnKelolaGudang.setBackground(defaultColor);
                btnKelolaJenisPengiriman.setBackground(defaultColor);
                btnKelolaKec.setBackground(defaultColor);
                btnPromo.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                KELOLA_KAB kabupaten = new KELOLA_KAB();
                kabupaten.PanelKabupaten.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(kabupaten.PanelKabupaten);
            }
        });
        btnKelolaKec.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Kiri.setSize(300,-1);
                btnKelolaKec.setBackground(newColor);
                btnKelolaRole.setBackground(defaultColor);
                btnKelolaKaryawan.setBackground(defaultColor);
                btnKelolaGudang.setBackground(defaultColor);
                btnKelolaJenisPengiriman.setBackground(defaultColor);
                btnKelolaKab.setBackground(defaultColor);
                btnPromo.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                KELOLA_KEC kecamatan = new KELOLA_KEC();
                kecamatan.PanelKecamatan.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(kecamatan.PanelKecamatan);
            }
        });
        btnPromo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Panel_Kiri.setSize(300,-1);
                btnPromo.setBackground(newColor);
                btnKelolaRole.setBackground(defaultColor);
                btnKelolaKaryawan.setBackground(defaultColor);
                btnKelolaGudang.setBackground(defaultColor);
                btnKelolaJenisPengiriman.setBackground(defaultColor);
                btnKelolaKab.setBackground(defaultColor);
                btnKelolaKec.setBackground(defaultColor);
                btnLogout.setBackground(defaultColor);

                Panel_Form.removeAll();
                Panel_Form.revalidate();
                Panel_Form.repaint();
                KELOLA_PROMO promo = new KELOLA_PROMO();
                promo.PanelPromo.setVisible(true);
                Panel_Form.revalidate();
                Panel_Form.setLayout(new java.awt.BorderLayout());
                Panel_Form.add(promo.PanelPromo);
            }
        });
        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                btnLogout.setBackground(newColor);
                btnKelolaRole.setBackground(defaultColor);
                btnKelolaKaryawan.setBackground(defaultColor);
                btnKelolaGudang.setBackground(defaultColor);
                btnKelolaJenisPengiriman.setBackground(defaultColor);
                btnKelolaKab.setBackground(defaultColor);
                btnKelolaKec.setBackground(defaultColor);
                btnPromo.setBackground(defaultColor);

                dispose();
                LOGIN login = new LOGIN();
                login.setVisible(true);
            }
        });
    }
}