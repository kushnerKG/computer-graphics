package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.utils.DialogProperties;
import ru.nsu.fit.g13205.kushner.utils.Properties;
import ru.nsu.fit.g13205.kushner.utils.RenderInfo;
import ru.nsu.fit.g13205.kushner.utils.SceneFileInfo;

import java.io.File;
import java.io.IOException;

/**
 * Created by Konstantin on 14.05.2016.
 */
public interface ModelObservable {

    void subscribe(ModelListener listener);

    void handleOpenSceneFile(SceneFileInfo sceneFileInfo);

    void handleUpdateMainImage();

    void handleMoveCamera(double xAngle, double yAngle, double zAngle);

    void handleZoomCamera(double delta);

    void handleMoveCameraToRight();

    void handleMoveCameraToLeft();

    void handleMoveCameraToUp();

    void handleMoveCameraToDown();

    void handleChangeZnWithWeel(double ratio);

    void handleChangeProperties(Properties properties);

    void handleGetProperties();

    void handlePushOnInitButton();

    void handleRenderScene();

    void handleShowWireframe();

    void handleOpenRenderFile(RenderInfo fileInfo);

    void handleSaveImage(File file) throws IOException;

    void handleOpenSceneFileWithRender(SceneFileInfo sceneFileInfo, RenderInfo fileInfo);

    void handleNewPropertiesFromDialog(DialogProperties dialogProperties);

    void handleSaveRenderFile(File file) throws IOException;

    void handleNewHeight(int height);
}
