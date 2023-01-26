package iog.psg.assets.exception;


import sss.openstar.ui.rpc.AppError;

public class NativeAssetException extends RuntimeException {
    public NativeAssetException(AppError appError) {
        super(appError.getCode() + "," + appError.getMsg());
    }
}
