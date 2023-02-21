package client_sharenow;

import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.lang.*;

public class Client {

    public static Socket sock;

    private static int port;
    private static ClientGUI ss;
    private static SimpleDateFormat sdf;
    //private static PrintWriter os;

    private static InputStream input;
    private static OutputStream output;
    private static PrintStream os;
    //private static BufferedReader stdin;


    public Client(int port) throws IOException {
        this(port, null);
    }

    public Client(int port,ClientGUI ss) throws IOException {

        this.port = port;
        this.ss = ss;
        sdf = new SimpleDateFormat("HH:mm:ss");

    }

    public static void display(String msg) {
        String time = sdf.format(new Date()) + " " + msg;
        if (ss == null)
            System.out.println(time);
        else
            ss.appendEvent(time + "\n");
    }

    public boolean start() throws IOException {

        try {
            Socket socket=new Socket("localhost",5001);
            System.out.println("Printstream created");

            //stdin=new BufferedReader(new InputStreamReader(System.in));
            input = new DataInputStream(socket.getInputStream());
            output = new DataOutputStream(socket.getOutputStream());
            os=new PrintStream(socket.getOutputStream());
        } catch (Exception ec) {
            display("Error connection to server:" + ec);
            return false;
        }




        display("Connection accepted " + Main.socket.getInetAddress() + ":" + Main.socket.getPort());

        // Creating both Data Stream
       /* try {
            input = new DataInputStream(sock.getInputStream());
            output = new DataOutputStream(sock.getOutputStream());


        } catch (Exception eIO) {
            display("Exception creating new Input/output Streams: " + eIO);
            return false;
        }
*/
        return true;
    }

    public static void sendFile(String directory,String fileName) throws IOException {

        os.println("1");
        try {
            /*System.out.println("Enter the number of times it should be downloaded");
            String nooftimes = stdin.readLine();

             */


            File myFile = new File("" +directory, String.valueOf(fileName));

            byte[] mybytearray = new byte[(int) myFile.length()];



            FileInputStream fis = new FileInputStream(myFile);
            DataOutputStream dos = new DataOutputStream(output);
            dos.writeUTF(myFile.getName());
            dos.flush();
            dos.writeLong(myFile.length());
            dos.flush();

            int read = 0;
            while ((read = fis.read()) != -1) {
                dos.writeByte(read);
            }

            dos.flush();
            output.flush();
            display("File " + fileName + " sent to Server.");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    static void receiveFile(String fileName) throws IOException {
        os.println("2");
        os.println(fileName);


/*
Main.oos.writeObject(client_sharenow.Functions.SEND);
Main.oos.flush();
Main.oos.writeObject(fileName);
Main.oos.flush();

 */
        try {

            int bytesread, time = 10;

            DataInputStream dis = new DataInputStream(input);
            File file = new File("C:\\Users\\akanksha sharma\\Desktop\\Softablitz\\instance_of_intelligence\\client\\files", fileName);
            OutputStream output = new FileOutputStream(file);
            String FileName = dis.readUTF();
            long size = dis.readLong();
            byte[] buffer = new byte[1024];

            while (size > 0 && (bytesread = dis.read(buffer, 0, (int) Math.min(buffer.length, size))) != -1) {
                output.write(buffer, 0, bytesread);
                size -= bytesread;

            }
            output.flush();


            display(FileName + " is received from Client");


        } catch (IOException e) {
            e.printStackTrace();


        }

    }



}
