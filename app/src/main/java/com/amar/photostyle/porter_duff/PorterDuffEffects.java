package com.amar.photostyle.porter_duff;

import android.graphics.PorterDuff;

public class PorterDuffEffects {
    public static PorterDuff.Mode[] BlendModes = {
            PorterDuff.Mode.ADD            //0
            , PorterDuff.Mode.CLEAR        //1
            , PorterDuff.Mode.DARKEN       //2
            , PorterDuff.Mode.DST          //3
            , PorterDuff.Mode.DST_ATOP     //4
            , PorterDuff.Mode.DST_IN       //5
            , PorterDuff.Mode.DST_OUT      //6
            , PorterDuff.Mode.DST_OVER     //7
            , PorterDuff.Mode.LIGHTEN      //8
            , PorterDuff.Mode.MULTIPLY     //9
            , PorterDuff.Mode.OVERLAY      //10
            , PorterDuff.Mode.SCREEN       //11
            , PorterDuff.Mode.SRC          //12
            , PorterDuff.Mode.SRC_ATOP     //13
            , PorterDuff.Mode.SRC_IN       //14
            , PorterDuff.Mode.SRC_OUT      //15
            , PorterDuff.Mode.SRC_OVER     //16
            , PorterDuff.Mode.XOR          //17
    };

    //1.8:3 ratio for masks 480x800
    //Static masks blend mode indexes
    public static String[] innerBlendModeIndexesMasks = {
            "5", "9", "10", "10", "10"
    };
    public static String[] maskDimensionRatios = {
            "1:1", "1:1", "1:1", "1:1", "1:1"
    };
    public static String[] overlayDimensionRatios = {
            "1:1", "1:1", "1:1", "1:1", "1:1"
    };
    public static String[] pixelDimensionRatios = {
            "1:1", "1:1", "1:1", "1:1", "1:1"
    };
    public static String[] maskBgColors = {
            "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF", "#FFFFFF"
    };

    public static String[] innerBlendModeIndexesOverlays = {
            "5", "11", "10", "11", "11"
    };
    public static String[] innerBlendModeIndexesPixels = {
            "5", "11", "11", "11", "10"
    };
}
