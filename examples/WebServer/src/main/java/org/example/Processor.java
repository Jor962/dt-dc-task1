package org.example;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Processor of HTTP request.
 */
public class Processor {
    private final Socket socket;
    private final HttpRequest request;

    public Processor(Socket socket, HttpRequest request) {
        this.socket = socket;
        this.request = request;
    }

    public void process() throws IOException, InterruptedException {
        System.out.println("Got request:");
        System.out.println(request.toString());
        System.out.flush();

        PrintWriter output = new PrintWriter(socket.getOutputStream());

        // We are returning a simple web page now.
        output.println("HTTP/1.1 200 OK");
        output.println("Content-Type: text/html; charset=utf-8");
        output.println();
        output.println("<html>");
        output.println("<head><title>Hello</title></head>");
        output.println("<body><p>Hello, world!</p></body>");
        output.println("</html>");
        output.flush();
        create();
        execute();
        delete();
    }

    public void create() throws IOException {
        PrintWriter file = new PrintWriter("dastan.txt");
        file.append("File created!");
        file.close();
        System.out.println("File created");

        PrintWriter create_output = new PrintWriter(socket.getOutputStream());
        create_output.println("HTTP/1.1 200 OK");
        create_output.println("Content-Type: text/html; charset=utf-8");
        create_output.println();
        create_output.println("<html>");
        create_output.println("<body><p>File created!</p></body>");
        create_output.println("</html>");
        create_output.flush();
        socket.close();
    }

    public void execute() throws IOException {
        File file = new File("C:\\Users\\Windows\\IdeaProjects\\demo\\dc-course\\dastan.txt");
        FileInputStream fis = new FileInputStream(file);
        InputStreamReader is = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(is);

        String line;
        int word_count = 0;
        int character_count = 0;
        int paragraph = 0;
        int spaces = 0;
        int sentences = 0;

        while ((line = br.readLine()) != null) {
            if (line.equals("")) {
                paragraph += 1;
            }
            else {
                character_count += line.length();
                String words[] = line.split("\\s+");
                word_count += words.length;
                spaces += word_count - 1;
                String sentence[] = line.split("[!?.:]+");
                sentences += sentence.length;
            }
        }
        if (sentences >= 1) {
            paragraph++;
        }
        System.out.println("Words: "+ word_count);
        System.out.println("Sentences: "+ sentences);
        System.out.println("Characters: "+ character_count);
        System.out.println("Paragraphs: "+ paragraph);
        System.out.println("Spaces: "+ spaces);
        try {
            Thread.sleep(40000,500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        PrintWriter counter = new PrintWriter(socket.getOutputStream());
        counter.println("HTTP/1.1 200 OK");
        counter.println("Content-Type: text/html; charset=utf-8");
        counter.println();
        counter.println("<html>");
        counter.println("<body><p>Total of file done!</p></body>");
        counter.println("</html>");
        counter.flush();
    }

    public void delete() throws IOException {
        Files.delete(Paths.get("dastan.txt"));
        System.out.println("File deleted");

        PrintWriter delete_output = new PrintWriter(socket.getOutputStream());
        delete_output.println("HTTP/1.1 200 OK");
        delete_output.println("Content-Type: text/html; charset=utf-8");
        delete_output.println("<html>");
        delete_output.println("<body><p>File deleted!</p></body>");
        delete_output.println("</html>");
        delete_output.flush();
    }
}
