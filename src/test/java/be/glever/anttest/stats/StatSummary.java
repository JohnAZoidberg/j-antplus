package be.glever.anttest.stats;

import be.glever.antplus.hrm.datapage.main.HrmDataPage4PreviousHeartBeatEvent;

public class StatSummary {
    private HrmDataPage4PreviousHeartBeatEvent currentBeat;
    private double meanRr;
    private double stdDevRr;
    private double rmssd;
    private double meanHeartRate;

    public void setLastHeartBeat(HrmDataPage4PreviousHeartBeatEvent hrmDataPage4PreviousHeartBeatEvent) {

        this.currentBeat = hrmDataPage4PreviousHeartBeatEvent;
    }

    public void setMeanRr(double meanRr) {
        this.meanRr = meanRr;
    }


    public void setStdDevRr(double stdDevRr) {
        this.stdDevRr = stdDevRr;
    }

    @Override
    public String toString() {
        return String.format("StatsSummary{currentBeat=%d, delta=%.0fms, meanHr=%.1f, meanRr=%.1f, stdDevRr=%.1f, rmssd=%.1f}",
                currentBeat.getComputedHeartRateInBpm(),
                (currentBeat.getHeartBeatEventTime() - currentBeat.getPreviousHeartBeatEventTime()),
                meanHeartRate,
                meanRr,
                stdDevRr,
                rmssd
        );
    }

    public void setRmssd(double rmssd) {
        this.rmssd = rmssd;
    }

    public void setMeanHeartRate(double meanHeartRate) {

        this.meanHeartRate = meanHeartRate;
    }
}
