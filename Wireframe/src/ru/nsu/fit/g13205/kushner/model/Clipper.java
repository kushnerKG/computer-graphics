package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.utils.AreaProperties;
import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;

/**
 * Created by Konstantin on 27.04.2016.
 */
public class Clipper {

    public static boolean check(Coordinates3D coordinates3D, AreaProperties properties){
        double u = coordinates3D.gettTransformU();
        double x = coordinates3D.getTransformX();
        double y = coordinates3D.getTransformY();
        double z = coordinates3D.getTransformZ()*u;


        double zn = properties.getZn();
        double zf = properties.getZf();
        double sw = properties.getSw();
        double sh = properties.getSh();

        //System.out.println(x+ " " + y + " " + z + " " + " " + u + " " + zn + " " +zf);
        if(x >= -sw/2 && x <= sw/2 && y >= -sh/2 &&  y <= sh/2 && z >= zn && z <= zf){
            return true;
        }else{
            return false;
        }

    }

    public static boolean cubCheck(Coordinates3D coordinates3D, AreaProperties properties){
        double u = coordinates3D.gettTransformU();
        double x = -coordinates3D.getTransformX();
        double y = -coordinates3D.getTransformY();
        double z = coordinates3D.getTransformZ()*u;


        double zn = properties.getZn();
        double zf = properties.getZf();
        double sw = properties.getSw();
        double sh = properties.getSh();


        if(x >= -sw/2 && x <= sw/2 && y >= -sh/2 &&  y <= sh/2 && z >= zn && z <= zf){
            return true;
        }else{
            return false;
        }

    }

}
