package com.krypt.bluecoin.User;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.BufferedSink;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ProgressRequestBody extends RequestBody {

    private File mFile;
    private ProgressListener mListener;
    private static final int DEFAULT_BUFFER_SIZE = 2048;

    public interface ProgressListener {
        void update(long bytesWritten, long contentLength);
    }

    public ProgressRequestBody(final File file, final ProgressListener listener) {
        mFile = file;
        mListener = listener;
    }

    @Override
    public MediaType contentType() {

        return MediaType.parse("video/mp4");
    }

    @Override
    public long contentLength() throws IOException {
        return mFile.length();
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        long fileLength = mFile.length();
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        FileInputStream in = new FileInputStream(mFile);
        long uploaded = 0;

        try {
            int read;
            while ((read = in.read(buffer)) != -1) {
                mListener.update(uploaded, fileLength);
                uploaded += read;
                sink.write(buffer, 0, read);
            }
        } finally {
            in.close();
        }
    }
}

