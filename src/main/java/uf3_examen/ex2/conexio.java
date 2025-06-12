package uf3_examen.ex2;

import javax.mail.MessagingException;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

public class conexio {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        System.out.println("Introdueix el codi de títol d'una pel·lícula de la base de dades IMDb: ");
        String codi = sc.nextLine();

        System.out.println("Introdueix una adreça de correu electrònic per enviar la pel·lícula:");
        String correu = sc.nextLine();

        URL url = new URL("https://www.imdb.com/title/" + codi);
        URLConnection hc = url.openConnection();
        hc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter("pagina.html"));
        BufferedReader in = new BufferedReader(new InputStreamReader(
                hc.getInputStream()));
        String inputLine;

        while ((inputLine = in.readLine()) != null){
            fileWriter.write(inputLine);
            fileWriter.newLine();
            System.out.println(inputLine);
        }
        fileWriter.close();
        in.close();

        try {
            GestorEnviaCorreu gestorEnviaCorreu = new GestorEnviaCorreu();
            gestorEnviaCorreu.enviaCorreu(correu, "SeAprueba?", "Esto funca!!!", new File[]{new File("pagina.html")} );
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            System.out.println("error al enviar correo");
        }
    }


}
