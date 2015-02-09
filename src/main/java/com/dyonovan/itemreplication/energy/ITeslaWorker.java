package com.dyonovan.itemreplication.energy;

/**
 * Created by Tim on 2/8/2015.
 */
public interface ITeslaWorker {

    public boolean machineCanOperate();

    public boolean canStartWork();

    public void onWorkStart();
    public boolean doWork(int amount, int progress, int totalProcessTime); // cancellable - energy only drained when returning true
    public void onWorkFinish();
}
