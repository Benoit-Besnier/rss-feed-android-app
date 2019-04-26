package com.example.feeded.handler.error;

import android.util.Log;

import org.androidannotations.annotations.EBean;
import org.androidannotations.rest.spring.api.RestErrorHandler;
import org.springframework.core.NestedRuntimeException;

@EBean
public class ConflictErrorHandler implements RestErrorHandler {

    @Override
    public void onRestClientExceptionThrown(final NestedRuntimeException exception) {
        Log.e("CONFLICT", "Could not create : " + exception);
    }

}
