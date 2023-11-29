/**
 *
 * @author Mohamed Eslam Amin  S1    20190419
 */
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
import java.io.FileWriter;
import java.io.IOException;

class Semaphore {

    protected int x = 0;

    protected Semaphore() {
        x = 0;
    }

    protected Semaphore(int initial) {
        x = initial;
    }

    public synchronized void _wait(Device x) throws IOException { //To create a File and write the output.
        FileWriter myWriter = new FileWriter("Output.txt", true);
        this.x--;
        
        if (this.x < 0) {
            myWriter.write("(" + x.Get_NameOfDev() + ")(" + x.Get_DevType() + ")" + "arrived and waiting\n");
            try {
                wait(); //wait available Thread.
            } catch (InterruptedException e) {
            }
        }
        else {}
        
        myWriter.write("(" + x.Get_NameOfDev() + ")(" + x.Get_DevType() + ")" + "Occupied\n");
        myWriter.close();
    }

    public synchronized void signal() { //to remind you if you have available connection.
        x++;
        if (x <= 0) {
            notify();
        }
    }

}

 class Router {

    public int limitOfDev;
    Semaphore Avail_Thread;
    ArrayList<Integer> list = new ArrayList<Integer>();

    Router(int limitOfDev) {
        this.limitOfDev = limitOfDev;
        Avail_Thread = new Semaphore(limitOfDev);
        for (int n = 1; n <= limitOfDev; n++) {
            list.add(n);
        }
    }

    public synchronized int get_ThreadNum() {
        int i = list.get(0);
        list.remove(0);
        return i;
    }

    public synchronized void set_ThreadNum(int x) {
        list.add(x);
    }

    public void connect(Device dev) throws IOException {
        Avail_Thread._wait(dev);
    }

    public void Activity(Device dev) throws IOException { //To take a sleep time and perform some activeties on Wi-Fi 
        Random ran = new Random();
        int sleepTime = ran.nextInt(3000);
        dev.Set_Thread(get_ThreadNum());
        FileWriter myWriter = new FileWriter("Output.txt", true);
        myWriter.write("Thread" + dev.Get_Thread() + " :" + dev.Get_NameOfDev() + " Performs Online activity\n");
        myWriter.close();
        try {
            Thread.sleep((long) sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void Disconnect(Device dev) throws IOException { //to logout the current device.
        set_ThreadNum(dev.Get_Thread());
        FileWriter myWriter = new FileWriter("Output.txt", true);
        myWriter.write("*******************************************\n");
        myWriter.write("(" + dev.Get_NameOfDev() + ")(" + dev.Get_DevType() + ")" + "Logged out\n");
        myWriter.write("*******************************************\n");
        myWriter.close();
        Avail_Thread.signal();
    }
}


class Device extends Thread {

    private String _name;
    private String _type;
    private Router _router;
    private int _thread;

    @Override
    public void run() {
        try {
            _router.connect(this);
            _router.Activity(this);
            _router.Disconnect(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Device(int num, Router router) throws IOException {
        this._router = new Router(num);
        this._router = router;

    }

    public void Set_NameOfDev(String name) {
        this._name = name;
    }

    public void Set_TypeOfDev(String type) {
        this._type = type;
    }

    public String Get_NameOfDev() {
        return _name;
    }

    public String Get_DevType() {
        return _type;
    }

    public void Set_Thread(int x) {
        this._thread = x;
    }

    public int Get_Thread() {
        return _thread;
    }
}

class Network {

    public Device[] dev;
    private final Router router;
    public int NumOfDev;

    Network(int limit_Devices, int Dev_num) throws IOException {
        router = new Router(limit_Devices);
        this.NumOfDev = Dev_num;
        this.dev = new Device[Dev_num];
        Scanner in = new Scanner(System.in);
        for (int i = 0; i < Dev_num; i++) {
            dev[i] = new Device(limit_Devices, router);

            System.out.println("Enter Name of Device<" + (i + 1) + ">");
            String input = in.nextLine();
            dev[i].Set_NameOfDev(input.substring(0, input.indexOf(" ")));
            dev[i].Set_TypeOfDev(input.substring(input.indexOf(" ") + 1));
            while (input.matches("")) {
                dev[i].Set_NameOfDev(input.substring(0, input.indexOf(" ")));
                dev[i].Set_TypeOfDev(input.substring(input.indexOf(" ") + 1));
            }
        }
        System.out.println("***********************************************\n");
    }

    public void startNetwork() {
        for (int i = 0; i < NumOfDev; i++) {
            dev[i].start();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        System.out.println("What is the number of WI-FI Connections?");
        String limit_Devices = in.nextLine();
        System.out.println("What is the number of devices Clients want to connect?");
        String Devices_num = in.nextLine();
        Network x = new Network(Integer.parseInt(limit_Devices), Integer.parseInt(Devices_num));
        x.startNetwork();
    }
}
