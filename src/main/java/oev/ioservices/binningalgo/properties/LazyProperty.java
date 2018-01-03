package oev.ioservices.binningalgo.properties;

import oev.colorprocessing.ColorComparisonFunction;

public enum LazyProperty {

    COLOR_FUNCTION(ColorComparisonFunction.class), NORMALIZATION_DIVIDEND(Integer.class);

    public final Class type;

    LazyProperty(Class type){
        this.type = type;
    }

}
