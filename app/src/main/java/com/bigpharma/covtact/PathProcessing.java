package com.bigpharma.covtact;

import android.util.Log;

import com.bigpharma.covtact.model.PathPointModel;

import java.lang.reflect.ParameterizedType;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PathProcessing {
    // All constants in meters.
    private static int EXPOSURE_DISTANCE = 40;
    private static int EARTH_RADIUS = 6371000;

    private List<PathPointModel> path1;
    private List<PathPointModel> path2;

    public PathProcessing(){
        path1 = new ArrayList<PathPointModel>();
        path2 = new ArrayList<PathPointModel>();

        /*for (int i = 0; i < 12; i++) {
            Calendar calendar = Calendar.getInstance();
            Calendar calendar2 = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DATE);
            calendar.set(year, month, day, 10, i*5, 00);
            calendar2.set(year, month, day, 10, i*5, 00);
            calendar2.add(Calendar.MINUTE, -5);
            path1.add(new PathPointModel(calendar.getTime(),0.0 + i* 0.0001,0.0 + i * 0.0001));
            path2.add(new PathPointModel(calendar2.getTime(), 0.0 + i * 0.0001, 0.0012 - i * 0.0001));
        }*/
    }

    public boolean checkExposure(List<PathPointModel> userPoints, List<PathPointModel> strangerPoints) {
        /*userPoints = new ArrayList<PathPointModel>(path1);
        strangerPoints = new ArrayList<PathPointModel>(path2);*/

        Iterator<PathPointModel> iUsr = userPoints.iterator();
        Iterator<PathPointModel> iStr = strangerPoints.iterator();
        PathPointModel pUsr = null;
        PathPointModel pStr = null;

        boolean skipIteration = false;

        while(iUsr.hasNext() && iStr.hasNext()) {
            pUsr = iUsr.next();

            while (iStr.hasNext() || skipIteration) {
                if (skipIteration) {
                    skipIteration = false;
                } else {
                    pStr = iStr.next();
                }

                if (pUsr.getDateHHMM() == pStr.getDateHHMM()) {
                    if(comparePoints(pUsr,pStr)) {
                        return true;
                    }
                    break;
                } else if (pUsr.getDateHHMM() > pStr.getDateHHMM()) {
                    continue;
                } else {
                    skipIteration = true;
                    break;
                }
            }
        }

        return false;
    }

    private boolean comparePoints(PathPointModel point1, PathPointModel point2) {
        Log.e("Comparing points", Integer.toString(point1.getDateHHMM()));
        Log.e(Double.toString(point1.getLatitude()) + "/" + Double.toString(point1.getLongtitude()),Double.toString(point2.getLatitude()) + "/" + Double.toString(point2.getLongtitude()));
        double lon1 = Math.toRadians(point1.getLongtitude());
        double lon2 = Math.toRadians(point2.getLongtitude());
        double lat1 = Math.toRadians(point1.getLatitude());
        double lat2 = Math.toRadians(point2.getLatitude());

        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        double a = Math.pow(Math.sin(dlat / 2), 2)
                 + Math.cos(lat1) * Math.cos(lat2)
                 * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));
        double distance = c * EARTH_RADIUS;

        Log.e("Distance",Double.toString(distance));

        return distance <= EXPOSURE_DISTANCE;
    }

    private boolean stripToCommonPeriod(List<PathPointModel> points1, List<PathPointModel> points2) { // Depreciated
        if (points1.isEmpty() || points2.isEmpty()) {
            return false;
        }

        int time1 = points1.get(0).getDateHHMM();
        int time2 = points2.get(0).getDateHHMM();

        while (time1 != time2) { // Searching for a common starting point.
            if (time1 > time2) {
                points2.remove(0);
                if (points2.isEmpty()) { // If the list is empty then there is no same time period, therefore nothing to comapare.
                    return false;
                }
            } else {
                points1.remove(0);
                if (points1.isEmpty()) {
                    return false;
                }
            }

            time1 = points1.get(0).getDateHHMM();
            time2 = points2.get(0).getDateHHMM();
        }

        Log.wtf("Start time", Integer.toString(time1) + " = " + Integer.toString(time2));

        time1 = points1.get(points1.size()-1).getDateHHMM();
        time2 = points2.get(points2.size()-1).getDateHHMM();

        while (time1 != time2) { // Searching for a common ending point.
            if (time1 > time2) {
                points1.remove(points1.size()-1);
                if (points1.isEmpty()) { // If the list is empty then there is no same time period, therefore nothing to comapare.
                    return false;
                }
            } else {
                points2.remove(points2.size()-1);
                if (points2.isEmpty()) {
                    return false;
                }
            }

            time1 = points1.get(points1.size()-1).getDateHHMM();
            time2 = points2.get(points2.size()-1).getDateHHMM();
        }

        return true;
    }
}
