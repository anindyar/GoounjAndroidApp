package com.bvocal.goounj.exceptions;

/**
 * Created by nandagopal on 2/2/16.
 */
public class ErroHandler extends Exception {


    public ErroHandler(String excpetion) {
        super((excpetion != null) || excpetion.length() > 0 ? excpetion : " Something went wrong");

    }
}
