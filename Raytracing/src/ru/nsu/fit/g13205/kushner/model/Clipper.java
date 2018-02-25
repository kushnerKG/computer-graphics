package ru.nsu.fit.g13205.kushner.model;

import ru.nsu.fit.g13205.kushner.Settings;
import ru.nsu.fit.g13205.kushner.utils.Coordinates3D;

import java.util.Set;

/**
 * Created by Konstantin on 16.05.2016.
 */
public class Clipper {

    public static boolean check(Coordinates3D coor1, Coordinates3D coor2, double zn, double zf, double sw, double sh){
        double u1 = coor1.getU();
        double x1 = coor1.getX();
        double y1 = coor1.getY();
        double z1 = coor1.getZ();

        double u2 = coor2.getU();
        double x2 = coor2.getX();
        double y2 = coor2.getY();
        double z2 = coor2.getZ();

        //System.out.println(sw + " " + sh + " " + zn + " " + zf);

        //System.out.println(x+ " " + y + " " + z + " " + " " + u + " " + zn + " " +zf);
        if(x1 >= -sw/2 && x1 <= sw/2 && y1 >= -sh/2 &&  y1 <= sh/2 && z1 >= zn && z1 <= zf &&
                x2 >= -sw/2 && x2 <= sw/2 && y2 >= -sh/2 &&  y2 <= sh/2 && z2 >= zn && z2 <= zf ){
            return true;
        }else{
            return false;
        }
    }

    public static boolean cubCheck(Coordinates3D coordinates3D/*, AreaProperties properties*/){
        /*double u = coordinates3D.gettTransformU();
        double x = -coordinates3D.getTransform X();
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
        }*/

        return false;

    }
}
