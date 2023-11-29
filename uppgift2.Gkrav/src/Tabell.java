import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import com.eclipsesource.json.*;

public class Tabell extends JFrame {
    private  String url = "src/sample (kopia).csv";
    private Scanner sc;
    private JPanel panel1;
    private JButton button1;
    private JTable table1;

    public static void main(String[] args) {
        new Tabell();
    }

    private Tabell() {
        setTitle("Test");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);


        panel1 = new JPanel();
        button1 = new JButton("Välj fil");

        panel1.add(button1);
        add(panel1);
        setVisible(true);
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser j = new JFileChooser("src");
                int result = j.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    System.out.println("Vald fil: " + j.getSelectedFile().getPath());
                } else {
                    System.out.println("Ingen fil vald");
                }
            }
        });
        try {
            File f = new File(url);
            sc = new Scanner(f);
            String page = "";
            while (sc.hasNext()) {
                String line = sc.nextLine();
                page += line;
            }
            sc.close();

            JsonValue jv = Json.parse(page);
            JsonArray ja = jv.asArray();

            JsonObject jo = ja.get(0).asObject();
            System.out.println(jo.names().size());
            for (int i = 0; i < ja.size() - 1; i++) {
                JsonObject j = ja.get(i).asObject();
            }
        } catch (Exception e) {
            System.out.println("ERROR" + e.toString());
        }
    }
}
