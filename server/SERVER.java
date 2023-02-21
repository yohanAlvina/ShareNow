package client_sharenow;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.*;


public class SERVER extends Thread {

    private Socket socket;
    int counter = 0;


    // private ArrayList<ClientHandler> a1, a2;
    int port;


    //public SERVER(int port, SocketServer socketServer, SimpleDateFormat sdf) {
    //this(port);
    //  }

    public SERVER(int port,Socket socket) {
        this.socket=socket;
        this.port = port;
        //this.ss = ss;
        // a1 = new ArrayList<ClientHandler>();

        //a2 = new ArrayList<ClientHandler>();
    }

    public void run() {
        boolean keepgoing = true;
// sab comment kyo kiya hua ha ?
        //sir ye serversocket gui bnaya tha server ka usi ka part tha ,gui remove krne pe isko comment out krdiye the
        // call karna problem dubara batana
        //ok sir
        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("Start reached");
            while (keepgoing) {
                counter++;
                System.out.println("connecting to client");
                Socket clientSocket = server.accept();
                System.out.println("Client " + counter + " is Connected");
                System.out.println("starting client handeller");
                ClientHandler t = new ClientHandler(clientSocket,counter);
                //a1.add(t);
                t.start();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }}
class ClientHandler extends Thread {


    private final Socket clientSocket;

    //SimpleDateFormat sdf;
    int counter;
    volatile boolean runin;
    public static BufferedInputStream input;
    public static BufferedOutputStream output;
    public static BufferedInputStream inp;
    public static BufferedOutputStream out;



    public ClientHandler(Socket clientSocket, int counter) {

        this.clientSocket = clientSocket;

        //this.sdf = sdf;
        this.counter = counter;
        runin=true;

        try {
            System.out.println("trying to create buffered streams ");
            input = new BufferedInputStream(clientSocket.getInputStream());
            output = new BufferedOutputStream(clientSocket.getOutputStream());
            System.out.println("buffered streams created");
        }catch(Exception e){
            e.printStackTrace();
        }


    }


    public void run() {
        InputStreamReader in = new InputStreamReader(input);
        BufferedReader is = new BufferedReader(in);
        String clientoption;
        System.out.println("Dia");
        while (runin) {
            System.out.println("Entered in while loop");
            try {
                if((clientoption = is.readLine())== null) {
                    System.out.println("a");
                    break;
                }
                else {
                    System.out.println(clientoption);
                    switch (clientoption) {
                        case "1":

                            receivefiles();

                            break;
                        case "2":
                            String fileName;
                            if ((fileName = is.readLine()) != null)
                                sendfiles(fileName);
                            break;
                        case "3":
                            runin = false;
                            break;
                        default:
                            System.out.println("Wrong choice choosen");
                            break;

                    }

                }

            } catch (IOException e) {
                e.printStackTrace();


            }
        }





              /*public void FilesDisplay(String FileName, long size,String extension) {
                  File file = new File(FileName);
                  String str = file + " " + "Client " + counter + " " + size+ " " +extension ;
                  ss.listfiles(str);

              }
          */
    }
    public void receivefiles() {

        try {

            int bytesread;
            DataInputStream dis = new DataInputStream(input);
            System.out.println("input stream created");
            String FileName = dis.readUTF();
            System.out.println("Filename read");
                      /*String extension = "";
                      int i=FileName.lastIndexOf('.');
                      if(i>= 0)
                          extension=FileName.substring(i+1);
          */
            File file = new File("C:\\Users\\akanksha sharma\\Desktop\\Softablitz\\instance_of_intelligence\\Server\\files", FileName);

            FileOutputStream fos = new FileOutputStream(file);

            long size = dis.readLong();
            byte[] buffer = new byte[1024];

            while (size > 0 && (bytesread = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesread);

                size -= bytesread;

            }
            fos.flush();

            System.out.println(FileName + " is received from Client ");
            //FilesDisplay(FileName,size,extension);


        } catch(Exception e){
            e.printStackTrace();
        }

    }
    public void sendfiles (String fileName){
        try {


            File myfile = new File("C:\\Users\\akanksha sharma\\Desktop\\Softablitz\\instance_of_intelligence\\Server\\files", fileName);
            byte mybyte[] = new byte[(int) myfile.length()];


            FileInputStream fis = new FileInputStream(myfile);

            DataOutputStream dos = new DataOutputStream(output);
            dos.writeUTF(myfile.getName());
            dos.flush();
            dos.writeLong(mybyte.length);
            dos.flush();
            int read = 0;
            while ((read = fis.read()) != -1)
                dos.writeByte(read);

            dos.flush();

            System.out.println(fileName + " is sent to the Client " + counter);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }




}



