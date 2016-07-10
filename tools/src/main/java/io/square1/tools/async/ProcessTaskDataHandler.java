package io.square1.tools.async;

import android.content.Context;
import android.os.Parcelable;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by roberto on 12/11/14.
 */
public interface ProcessTaskDataHandler<T> {
    Parcelable processReceivedData(Context context, T data, AtomicBoolean operationCanceled) throws Exception;
}
