package ru.nsu.fit.g13205.kushner.model;

/**
 * Created by Konstantin on 24.03.2016.
 */
public interface ModelObservable {

    void subscribe(ModelListener listener);

}
