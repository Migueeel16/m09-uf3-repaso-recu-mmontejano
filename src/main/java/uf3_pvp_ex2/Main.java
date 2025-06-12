package uf3_pvp_ex2;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Introdueix un títol d'IMDb (ex. tt0245429)");
        String title = scanner.nextLine();

//        System.out.println("Ara introdueix un correu electronic");
//        String email = scanner.nextLine();

        try {
            Downloader downloader = new Downloader(title);

            File file = downloader.download();

//            EmailManager emailManager = new EmailManager();
//            emailManager.sendMail(email, title, "", file);

            System.out.println("Enviat correctament! :)");
        } catch (MalformedURLException e) {
            System.out.println("There's been an error formatting the URL: ");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("There's been an error downloading the file: ");
            e.printStackTrace();
        }
//        catch (MessagingException e) {
//            System.out.println("There's been an error sending the email: ");
//            e.printStackTrace();
//        }
    }
}
