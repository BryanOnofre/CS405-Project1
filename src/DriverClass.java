import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;

public class DriverClass {
    public static void main(String[] args) {

        //declare scanner for use in rest of program
        Scanner console = new Scanner(System.in);

        //queues of PCBs
        PCBqueue job = new PCBqueue();
        PCBqueue ready = new PCBqueue();
        PCBqueue disk = new PCBqueue();
        PCBqueue ioWait = new PCBqueue();
        PCBqueue cpu = new PCBqueue();

        //Print with fancyPrint
        fancyPrint( " Welcome to Bryan's CPU scheduler !");

        String testData = "";
        System.out.print("\nrun test data in reverse? y/n : ");
        String yesNo = console.nextLine();

        if (yesNo.equalsIgnoreCase("n") ) {
            testData = "jobs-testdata.txt";
        } else {
            testData = "reverse-testdata.txt";
        }

        //load test data from file into java
        try {
            FileInputStream fstream = new FileInputStream(testData);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));


            //ints for use in initializing PCB objects
            int indexOfBurst = 1;

            String strLine;

            while ((strLine = br.readLine()) != null) {

                ArrayList<Integer> ioBurstCreationArray = new ArrayList<>();
                ArrayList<Integer> cpuBurstCreationArray = new ArrayList<>();

                int ioBurst, cpuBurst;

                String[] tokens = strLine.split(" ");

                for (int i = 0; i < tokens.length; i++) {
                    if ((i % 2) == 1) {
                        ioBurst = Integer.parseInt(tokens[i]);
                        ioBurstCreationArray.add((ioBurst));
                    } else {
                        cpuBurst = Integer.parseInt(tokens[i]);
                        cpuBurstCreationArray.add((cpuBurst));
                    }
                }

                PCB newPCB = new PCB(indexOfBurst, 0, 0, cpuBurstCreationArray,
                        ioBurstCreationArray, 0);

                job.add(newPCB);

                indexOfBurst++;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        //initially populate ready queue for either scheduler
        for (int i = 0; i < 3; i++) {
            ready.add(job.pullPCB_OutAtIndex(0)); //Add PCB from JOB to READY
        }

        //establish initial system time
        int systemTime = 0;

        //ask user if round robin or SJF
        System.out.print("Shortest job first or Round Robin? (s/r): ");
        String shortestOrRoundRobin = console.nextLine();

        if (shortestOrRoundRobin.equalsIgnoreCase("s")) {

            //welcome user to SJF
            fancyPrint( "      Using SJF CPU Scheduling");


            //prepare for SJF
            //initially place PCB into CPU
            cpu.add(ready.pullOutPCBbyProcessId(ready.getIDofPCBwithSmallestCurrentBurst()));

            //initial SystemPrint
            SystemPrint(job, ready, disk, ioWait, cpu, systemTime);

            while (!cpu.isEmpty()) {

                //if ioWait is NOT empty
                if (!ioWait.isEmpty() && (ready.size() < 3) ) {
                    ready.add(ioWait.pullPCB_OutAtIndex(0));    //Push from IOWait To CPU... FIRST
                }

                //if cpuBurst reaches zero
                if (cpu.get(0).getCPUBurstAtIterator() == 0) {

                    //increase cpu iterator
                    cpu.get(0).increaseIteratorCPU();

                    // if cpu iterator is greater than size of cpuBurst array. Done...  Kill Process
                    //else... push to disk for IO
                    if (cpu.get(0).cpuIterator == cpu.get(0).cpuBurst.size()) {
                        cpu.kill(0);    //kill PCB in CPU
                    } else {
                        disk.add(cpu.pullPCB_OutAtIndex(0));        //push from CPU to DISK for IO
                    }

                    // if READY size less than 3 && JOB is NOT empty
                    if( ready.size() < 3 && !job.isEmpty()){
                        ready.add( job.pullPCB_OutAtIndex(0));      //push from JOB to READY
                    }

                    //if cpu is empty && ready is NOT empty.
                    //place shortest job in ready queue into CPU
                    if ( cpu.isEmpty() && !ready.isEmpty() ) {
                            cpu.add(ready.pullOutPCBbyProcessId(ready.getIDofPCBwithSmallestCurrentBurst()));
                    }
                    SystemPrint(job, ready, disk, ioWait, cpu, systemTime);     // Print Id's
                }


                //if Disk not empty
                if (!disk.isEmpty()) {

                    //deduct from IOburst at iterator for each PCB in DISK
                    for (int i = 0; i < disk.size(); i++) {

                        disk.get(i).deductFromIOBurstAtIterator();      //deduct from IOburst at iterator

                        //If IOBurst finished at iterator. Increase IO iterator. Push to IOWAit
                        if (disk.get(i).getIOBurstAtIterator() == 0) {
                            disk.get(i).increaseIteratorIO();       //incrase IO iterator
                            ioWait.add(disk.pullPCB_OutAtIndex(i));     //push to IOwait queue
                        }
                    }
                }

                //if CPU is NOT empty
                if (!cpu.isEmpty()) {
                    cpu.get(0).deductFromCPUBurstAtIterator();      //deduct one from CPU burst at iterator
                }
                systemTime++;       //increase system time by one
            }

            //print execution finished with fancy stars
            fancyPrint("        execution finished");

        } else if ( shortestOrRoundRobin.equalsIgnoreCase("r" )) {

            //prepare for RR
            fancyPrint( "  Using Round Robin CPU Scheduling");      //welcome user to Round Robin
            System.out.print("\nEnter time quantum: ");     // ask user to enter time quantum
            int timeQuantum = console.nextInt();

            //initially place PCB into CPU
            cpu.add(ready.pullPCB_OutAtIndex(0));

            //initially print Id's
            SystemPrint( job, ready, disk, ioWait, cpu, systemTime);

            while (!cpu.isEmpty()) {

                //if cpuBurst reaches zero
                if (cpu.get(0).getCPUBurstAtIterator() == 0) {

                    //increase cpu iterator
                    cpu.get(0).increaseIteratorCPU();

                    // if cpu iterator is greater than size of cpuBurst array. Done...  Kill Process
                    //else... push to disk for IO
                    if (cpu.get(0).cpuIterator >= cpu.get(0).cpuBurst.size()) {
                        cpu.kill(0);        //kill process in CPU
                    } else {
                        disk.add(cpu.pullPCB_OutAtIndex(0));        //push from CPU to DISK for i/o

                    }

                    //if ioWait is NOT empty,
                    if (!ioWait.isEmpty() && (ready.size() <= 2) ) {
                        ready.add(ioWait.pullPCB_OutAtIndex(0));        //Push from IOWait To CPU... FIRST
                    }

                    // if READY sie is < 2 && JOB is NOT empty
                    if( ready.size() <= 2 && !job.isEmpty()){
                        ready.add( job.pullPCB_OutAtIndex(0));      //push from JOB to READY
                    }

                    //if cpu is empty && READY is NOT empty
                    if (cpu.isEmpty() && !ready.isEmpty()) {
                            cpu.add(ready.pullPCB_OutAtIndex(0));       //push from CPU to READY
                            cpu.get(0).resetRRtimeLeft();       //reset RRtimeLeft
                            SystemPrint( job, ready, disk, ioWait, cpu, systemTime);        //print Id's
                    }
                }
                // Else if PCB in CPU timeLeft over && systemTime != 0.     prempt current process
                else if ( (cpu.get(0).getRrTimeLeft() % timeQuantum == 0) && (systemTime != 0) ) {
                    ready.add(cpu.pullPCB_OutAtIndex(0));       //push from CPU to READY

                    //if ioWait is NOT empty && READY size < 2
                    if (!ioWait.isEmpty() && (ready.size() <= 2) ) {
                        ready.add(ioWait.pullPCB_OutAtIndex(0));        //Push from IOWait To CPU... FIRST
                    }

                    // if READY size is < 2 && JOB is NOT empty
                    if( ready.size() <= 2 && !job.isEmpty()){
                        ready.add( job.pullPCB_OutAtIndex(0));      //push to READY from JOB
                    }

                    //if cpu is empty. && ready is NOT empty.
                    if (cpu.isEmpty() && !ready.isEmpty()) {
                        cpu.add(ready.pullPCB_OutAtIndex(0));       //Push to CPU from READY
                        cpu.get(0).resetRRtimeLeft();       //Reset RRtimeLeft for new PCB
                        SystemPrint(job, ready, disk, ioWait, cpu, systemTime);     //Print Id's of PCB's in queues
                    }
                }

                //if Disk not empty
                if (!disk.isEmpty()) {
                    //deduct from IOburst at iterator
                    for (int i = 0; i < disk.size(); i++) {
                        disk.get(i).deductFromIOBurstAtIterator();

                        //If IOBurst finished at iterator. Done with IO. Increase IOiterator. Push to IOWAit
                        if (disk.get(i).getIOBurstAtIterator() == 0) {
                            //increase IO iterator
                            disk.get(i).increaseIteratorIO();
                            //push to IOwait queue
                            ioWait.add(disk.pullPCB_OutAtIndex(i));
                        }
                    }
                }

                //if CPU is NOT empty
                if (!cpu.isEmpty()) {
                    cpu.get(0).increaseRRTimeLeft();        ////increase RRtimeLeft
                    cpu.get(0).deductFromCPUBurstAtIterator();      //deduct one from CPU burst at iterator
                }

                systemTime++;       //increase system time
            }

            fancyPrint("        execution finished");      //print execution finished with fancy stars
        }
    }

    //Prints line sandwiched between stars to look fancy
    public static void fancyPrint ( String line ) {

        System.out.printf(  "\n" +
                            "********************************************\n" +
                            "    %s\n" +
                            "********************************************\n", line);
    }

    //prints SystemTime and the Id's of PCBs in queues given to it
    public static void SystemPrint(PCBqueue job, PCBqueue ready, PCBqueue disk, PCBqueue ioWait,
                                   PCBqueue cpu, int systemTime) {
        System.out.println("\nTimer = " + systemTime + "\n    CPU:     " + cpu.printIdList() + "\n    JOB:     "
                + job.printIdList() + "\n    READY:   " + ready.printIdList() + "\n    DISK:    "
                + disk.printIdList() + "\n    IOWait:  " + ioWait.printIdList() );
    }
}

