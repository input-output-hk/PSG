package io.psg.nativeassets.exception;

import sss.openstar.ui.rpc.AppError;

public class NativeAssetException extends RuntimeException {
    public NativeAssetException(AppError appError) {
        super(appError.code() + "," + appError.msg());
    }
}