package com.example.distdocs.encryption;

import android.util.Log;

public class CryptoException extends Exception{
    public CryptoException() {
    }

    public CryptoException(String message, Throwable throwable) {
        super(message, throwable);
        Log.i("CryptoException", throwable.getMessage());
        throwable.printStackTrace();
    }
}
