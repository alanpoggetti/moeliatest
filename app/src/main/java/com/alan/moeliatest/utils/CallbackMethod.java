package com.alan.moeliatest.utils;

/**
 * This abstract class will allow us to customize the actions when calling the Connection manager
 */
public abstract class CallbackMethod {

    public abstract void success(String response);

    public abstract void failed(String response);

}
