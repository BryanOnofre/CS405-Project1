import java.util.ArrayList;

public class PCB {
    int processId;
    int ioIterator;
    int cpuIterator;
    ArrayList<Integer> cpuBurst;
    ArrayList<Integer> ioBurst;
    int rrTimeLeft;
    int cpuBurstTimeLeft;
    int ioBurstTimeLeft;

    public PCB(int processId, int ioIterator, int cpuIterator, ArrayList<Integer> cpuBurst,
               ArrayList<Integer> ioBurst, int rrTimeLeft) {
        this.processId = processId;
        this.ioIterator = ioIterator;
        this.cpuIterator = cpuIterator;
        this.cpuBurst = cpuBurst;
        this.ioBurst = ioBurst;
        this.rrTimeLeft = rrTimeLeft;
    }

    public int getRrTimeLeft() {
        return rrTimeLeft;
    }

    public void setRrTimeLeft(int rrTimeLeft) {
        this.rrTimeLeft = rrTimeLeft;
    }

    public void increaseRRTimeLeft() {
        int increasedRR = rrTimeLeft;
        increasedRR++;
        setRrTimeLeft(increasedRR);
    }

    public void resetRRtimeLeft() {
        this.rrTimeLeft = 0;
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

    public String printProcessID() {
        return String.valueOf(processId);
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
                ", rrTimeLeft=" + rrTimeLeft +
                ", cpuBurstTimeLeft=" + cpuBurstTimeLeft +
                ", ioBurstTimeLeft=" + ioBurstTimeLeft +
                '}';
    }

}