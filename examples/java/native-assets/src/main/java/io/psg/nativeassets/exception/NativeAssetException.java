package io.psg.nativeassets.exception;


import sss.openstar.ui.rpc.errors.AppError;

public class NativeAssetException extends RuntimeException {
    public NativeAssetException(AppError appError) {
        super(appError.code() + "," + appError.msg());
    }
}
