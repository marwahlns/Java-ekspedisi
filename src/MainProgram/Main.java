package MainProgram;

import LOGIN.LOGIN;

public class Main {
    public static void main(String[] args) {
        // instantiasi form, nantinya login form akan menjadi form pertama yang dibuka saat program di jalankan
        LOGIN form = new LOGIN();
        form.setVisible(true);
    }
}