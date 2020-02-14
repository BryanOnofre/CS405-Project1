// queue of jobs that are ready. Limited to only 3 processes
// should be refilled just before the scheduler decides what PCB is next

import java.util.ArrayList;

public class PCBqueue  {

    ArrayList<PCB> queue = new ArrayList<>();

    public void add ( PCB pcb ) {
        queue.add( pcb );
    }

    public void kill ( int indexToKill ) {
        queue.remove( indexToKill );
    }

    public boolean isEmpty () {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }

    public PCB get(int index ) {
        return queue.get(index);
    }


    public PCB pullPCB_OutAtIndex ( int index ) {
        return queue.remove(index);
    }

    public PCB pullOutPCBbyProcessId ( int processId) {
        PCB returningPCB = null;
        for (int i = 0; i < queue.size(); i++) {
            if ( processId == queue.get(i).getProcessId() ) {
                returningPCB = queue.remove(i);
            }
        }
        return returningPCB;
    }

    public int getIDofPCBwithSmallestCurrentBurst() {
        int shortestBurst = queue.get(0).getCPUBurstAtIterator();
        int returningPCBid = 0;
        for( int i=0; i<queue.size(); i++ ) {
            if ( queue.get(i).getCPUBurstAtIterator() < shortestBurst  ) {
                shortestBurst = queue.get(i).getCPUBurstAtIterator();
                returningPCBid = queue.get(i).getProcessId();
            }
        }
        if ( queue.get(0).getCPUBurstAtIterator() == shortestBurst) {
            returningPCBid = queue.get(0).getProcessId();
        }
        return returningPCBid;
    }

    public String printIdList () {
        String printedIdList = "";
        for (int i = 0; i < queue.size(); i++) {
            printedIdList += queue.get(i).getProcessId() + "  ";
        }
        return printedIdList;
    }

    @Override
    public String toString() {
        return "  pcbQueue{" +
                "" + queue +
                "}\n\n";
    }
}
