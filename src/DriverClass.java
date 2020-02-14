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

        String testData = "jobs-testdata.txt";
//        System.out.print("run test data in reverse? y/n : ");
//        String yesNo = console.nextLine();
//
//        yesNo = "n"; // used to bypass for testing
//
//        if (yesNo.equalsIgnoreCase("n") ) {
//            testData = "jobs-testdata.txt";
//        } else {
//            testData = "reverse-testdata.txt";
//        }

        try {
            FileInputStream fstream = new FileInputStream(testData);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));


            //ints for use in initializing PCB objects
            int indexOfBurst = 1;
            int iteratorInitialValue = 0;

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

                PCB newPCB = new PCB(indexOfBurst, iteratorInitialValue, iteratorInitialValue, cpuBurstCreationArray,
                        ioBurstCreationArray, false, false, false);

                job.add(newPCB);

                indexOfBurst++;
            }
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        //initially populate ready queue
        for (int i = 0; i < 3; i++) {
            ready.add(job.pullPCB_OutAtIndex(0));
        }

        //establish initial system time
        int systemTime = 0;

        //ask user if round robin or SJF
//        System.out.print("Shortest job first or Round Robin? (s/r): ");
//        String shortestOrRoundRobin = console.nextLine();

        String shortestOrRoundRobin = "r"; //used to bypass for testing

        if (shortestOrRoundRobin.equalsIgnoreCase("s")) {

            //prepare for SJF
            //initially place PCB into CPU
            cpu.add(ready.pullOutPCBbyProcessId(ready.getIDofPCBwithSmallestCurrentBurst()));


            while (!cpu.isEmpty()) {

                //if ioWait is NOT empty, Push from IOWait To CPU... FIRST
                if (!ioWait.isEmpty() && (ready.size() < 3) ) {
                    ready.add(ioWait.pullPCB_OutAtIndex(0));
                }
                //if cpuBurst reaches zero. Increase iterator.
                //If process is done. Kill it
                //Else. push to disk for IO
                //finally. If CPU is Empty
                //If IO wait is NOT empty. Push from IOWait to READY
                //if READY less than 3. pull from JOB
                //if READY IS NOT empty && CPU IS empty. Pick SJF from ready queue
                if (cpu.get(0).getCPUBurstAtIterator() == 0) {

                    //increase cpu iterator
                    cpu.get(0).increaseIteratorCPU();

                    // if cpu iterator is greater than size of cpuBurst array. Done...  Kill Process
                    //else... push to disk for IO
                    if (cpu.get(0).cpuBurst.size()-1 < cpu.get(0).cpuIterator) {
                        cpu.kill(0);
                    } else {
                        //push from CPU to DISK for IO
                        disk.add(cpu.pullPCB_OutAtIndex(0));
                    }

                    if( ready.size() < 3 && !job.isEmpty()){
                        ready.add( job.pullPCB_OutAtIndex(0));
                    }

                    //if cpu is empty.
                    if (cpu.isEmpty()) {
                        if (!ready.isEmpty()) {
                            cpu.add(ready.pullOutPCBbyProcessId(ready.getIDofPCBwithSmallestCurrentBurst()));
                        }
                    }
                }
                // SystemPrint
                SystemPrint(job, ready, disk, ioWait, cpu, systemTime);


                //if Disk not empty
                if (!disk.isEmpty()) {
                    //deduct from IOburst at iterator

                    for (int i = 0; i < disk.size(); i++) {
                        disk.get(i).deductFromIOBurstAtIterator();

                        //If IOBurst finished at iterator. Done with IO. Push to IOWAit
                        if (disk.get(i).getIOBurstAtIterator() == 0) {
                            //incrase IO iterator
                            disk.get(i).increaseIteratorIO();
                            //push to IOwait queue
                            ioWait.add(disk.pullPCB_OutAtIndex(i));
                        }
                    }
                }

                //deduct one from CPU burst at iterator
                if (!cpu.isEmpty()) {
                    cpu.get(0).deductFromCPUBurstAtIterator();
                }
                //increase system time by one
                systemTime++;
            }

            System.out.println( "execution finished. \n\n");

        } else if ( shortestOrRoundRobin.equalsIgnoreCase("r" )) {
            //Do something else
//            System.out.print("Enter time quantum: ");
            int timeQuantum = 2;
//                    console.nextInt();


            //prepare for RR
            //initially place PCB into CPU
            cpu.add(ready.pullPCB_OutAtIndex(0));


            while (!cpu.isEmpty()) {

                //if cpuBurst reaches zero. Increase iterator.
                //If process is done. Kill it
                //Else. push to disk for IO
                //finally. If CPU is Empty
                //If IO wait is NOT empty. Push from IOWait to READY
                //if READY less than 3. pull from JOB
                //if READY IS NOT empty && CPU IS empty. Pick SJF from ready queue
                if (cpu.get(0).getCPUBurstAtIterator() == 0) {

                    //increase cpu iterator
                    cpu.get(0).increaseIteratorCPU();

                    // if cpu iterator is greater than size of cpuBurst array. Done...  Kill Process
                    //else... push to disk for IO
                    if (cpu.get(0).cpuBurst.size()-1 < cpu.get(0).cpuIterator) {
                        cpu.kill(0);
                    } else {
                        //push from CPU to DISK for IO
                        disk.add(cpu.pullPCB_OutAtIndex(0));
                    }

                    //if cpu is empty.
                    if (cpu.isEmpty()) {
                        if (!ready.isEmpty()) {
                            cpu.add(ready.pullPCB_OutAtIndex(0));
                        }
                    }

                    //if ioWait is NOT empty, Push from IOWait To CPU... FIRST
                    if (!ioWait.isEmpty() && (ready.size() < 3) ) {
                        ready.add(ioWait.pullPCB_OutAtIndex(0));
                    }



                    //testing commenting this out
                    if( ready.size() < 2 && !job.isEmpty()){
                        ready.add( job.pullPCB_OutAtIndex(0));
                    }

                }
                // if cpu.get burst at iterator zero above true. Dont check this anymore... ELSE IF?
                else if ( systemTime % timeQuantum == 0 && systemTime != 0) {
                    //prempt current process
                    //push from CPU to READY
                    ready.add(cpu.pullPCB_OutAtIndex(0));

//                    if( ready.size() < 3 && job.isEmpty() == false ){
//                        ready.add( job.pullPCB_OutAtIndex(0));
//                    }

                    //if cpu is empty.
                    if (cpu.isEmpty()) {
                        if (!ready.isEmpty()) {
                            cpu.add(ready.pullPCB_OutAtIndex(0));
                        }
                    }


                }

                // SystemPrint
                SystemPrint(job, ready, disk, ioWait, cpu, systemTime);


                //if Disk not empty
                if (!disk.isEmpty()) {
                    //deduct from IOburst at iterator
                    for (int i = 0; i < disk.size(); i++) {
                        disk.get(i).deductFromIOBurstAtIterator();

                        //If IOBurst finished at iterator. Done with IO. Increase IOiterator. Push to IOWAit
                        if (disk.get(i).getIOBurstAtIterator() == 0) {
                            //incrase IO iterator
                            disk.get(i).increaseIteratorIO();
                            //push to IOwait queue
                            ioWait.add(disk.pullPCB_OutAtIndex(i));
                        }
                    }
                }

                //deduct one from CPU burst at iterator
                if (!cpu.isEmpty()) {
                    cpu.get(0).deductFromCPUBurstAtIterator();
                }
                //increase system time by one
                systemTime++;
            }

            System.out.println( "execution finished. \n\n");

        }
    }

    public static void SystemPrint(PCBqueue job, PCBqueue ready, PCBqueue disk, PCBqueue ioWait, PCBqueue cpu, int systemTime) {
        System.out.println("Timer = " + systemTime + "\n    CPU:     " + cpu.printIdList() + "\n    JOB:     "
                + job.printIdList() + "\n    READY:   " + ready.printIdList() + "\n    DISK:    "
                + disk.printIdList() + "\n    IOWait:  " + ioWait.printIdList() + "\n\n");
    }
}

