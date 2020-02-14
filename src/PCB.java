import java.util.ArrayList;

public class PCB {
    int processId;
    int ioIterator;
    int cpuIterator;
    ArrayList<Integer> cpuBurst;
    ArrayList<Integer> ioBurst;
    int cpuBurstTimeLeft;
    int ioBurstTimeLeft;
    int nextPointer;
    boolean isDone;
    boolean inCPU;
    boolean inDisk;
    boolean inIOWait;

    public PCB(int processId, int ioIterator, int cpuIterator, ArrayList<Integer> cpuBurst,
               ArrayList<Integer> ioBurst, boolean isDone, boolean inCPU, boolean inDisk) {
        this.processId = processId;
        this.ioIterator = ioIterator;
        this.cpuIterator = cpuIterator;
        this.cpuBurst = cpuBurst;
        this.ioBurst = ioBurst;
        this.isDone = isDone;
        this.inCPU = inCPU;
        this.inDisk = inDisk;
    }

    public boolean isInIOWait() {
        return inIOWait;
    }

    public void setInIOWait(boolean inIOWait) {
        this.inIOWait = inIOWait;
    }

    public int getProcessId() {
        return processId;
    }

    public void setProcessId(int processId) {
        this.processId = processId;
    }

    public int getIoIterator() {
        return ioIterator;
    }

    public void setIoIterator(int ioIterator) {
        this.ioIterator = ioIterator;
    }

    public int getCpuIterator() {
        return cpuIterator;
    }

    public void setCpuIterator(int cpuIterator) {
        this.cpuIterator = cpuIterator;
    }

    public ArrayList getCpuBurst() {
        return cpuBurst;
    }

    public void setCpuBurst(ArrayList cpuBurst) {
        this.cpuBurst = cpuBurst;
    }

    public ArrayList getIoBurst() {
        return ioBurst;
    }

    public void setIoBurst(ArrayList ioBurst) {
        this.ioBurst = ioBurst;
    }

    public int getCpuBurstTimeLeft() {
        return cpuBurstTimeLeft;
    }

    public void setCpuBurstTimeLeft(int cpuBurstTimeLeft) {
        this.cpuBurstTimeLeft = cpuBurstTimeLeft;
    }

    public int getIoBurstTimeLeft() {
        return ioBurstTimeLeft;
    }

    public void setIoBurstTimeLeft(int ioBurstTimeLeft) {
        this.ioBurstTimeLeft = ioBurstTimeLeft;
    }

    public int getNextPointer() {
        return nextPointer;
    }

    public void setNextPointer(int nextPointer) {
        this.nextPointer = nextPointer;
    }

    public boolean isDone() {
        return isDone;
    }

    public void setIsDone(boolean done) {
        isDone = done;
    }

    public boolean isInCPU() {
        return inCPU;
    }

    public void setInCPU(boolean inCPU) {
        this.inCPU = inCPU;
    }

    public boolean isInDisk() {
        return inDisk;
    }

    public void setInDisk(boolean inDisk) {
        this.inDisk = inDisk;
    }

    public String printProcessID() {
        return String.valueOf(processId);
    }

    public void syncCPUBurstAtIteratortWithBurstTimeLeft() {
        cpuBurstTimeLeft = cpuBurst.get(cpuIterator);
    }

    public void syncIOBurstAtIteratortWithBurstTimeLeft() {
        ioBurstTimeLeft = ioBurst.get(cpuIterator);
    }

    public void deductFromIOIterator() {
        ioBurstTimeLeft--;
    }

    public int getShortestCPUBurst() {
        int shortestBurst = cpuBurst.get(0);
        for (int i = 0; i < cpuBurst.size(); i++) {
            if (cpuBurst.get(i) < shortestBurst) {
                shortestBurst = cpuBurst.get(i);
            }
        }
        return shortestBurst;
    }

    public int getCPUBurstAtIterator() {
        return cpuBurst.get(cpuIterator);
    }

    public int getIOBurstAtIterator() {
        return ioBurst.get(ioIterator);
    }


    public void deductFromIOBurstAtIterator() {
        int deductedBurst = ioBurst.get(ioIterator);
        deductedBurst--;
        ioBurst.set(ioIterator, deductedBurst);
    }

    public void deductFromCPUBurstAtIterator() {
        int deductedBurst = cpuBurst.get(cpuIterator);
        deductedBurst--;
        cpuBurst.set(cpuIterator, deductedBurst);
    }

    public void increaseIteratorIO() {
        int increasedIterator = ioIterator;
        increasedIterator++;
        setIoIterator(increasedIterator);
    }

    public void increaseIteratorCPU() {
        int increasedIterator = cpuIterator;
        increasedIterator++;
        setCpuIterator(increasedIterator);

    }

    @Override
    public String toString() {
        return "PCB{" +
                "processId=" + processId +
                ", ioIterator=" + ioIterator +
                ", cpuIterator=" + cpuIterator +
                ", cpuBurst=" + cpuBurst +
                ", ioBurst=" + ioBurst +
//                ", cpuBurstTimeLeft=" + cpuBurstTimeLeft +
//                ", ioBurstTimeLeft=" + ioBurstTimeLeft +
//                ", nextPointer=" + nextPointer +
//                ", isDone=" + isDone +
//                ", inCPU=" + inCPU +
//                ", inDisk=" + inDisk +
//                ", inIOWait=" + inIOWait +
                '}';
    }

}