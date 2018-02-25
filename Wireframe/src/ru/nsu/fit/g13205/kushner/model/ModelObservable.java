package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.view.SplineDialogInterface;

/**
 * Created by Konstantin on 10.04.2016.
 */
public interface ModelObservable {

    void subscribe(ModelListener listener);

    //void subscribeForDialog(SplineDialogInterface dialog);

}
