package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.view.setting.GameSetting;

/**
 * Created by Konstantin on 25.02.2016.
 */
public interface LifeModelListener {

    void updateImpact(Impact[][] impact);

    void updateSize(Impact[][] impacts);


}
