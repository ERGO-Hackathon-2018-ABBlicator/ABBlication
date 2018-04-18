package abblication.ergo.de.abblication;

import android.content.Context;
import android.util.Log;

import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class AWSConnector {

    public static void downloadWithTransferUtility(Context context, String bucketFilename, final ResultHandler resultHandler) {

        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(context)
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(new AmazonS3Client(AWSMobileClient.getInstance().getCredentialsProvider()))
                        .build();

        File cacheDir = context.getCacheDir();
        final File dl;
        try {
            dl = File.createTempFile("tmp", bucketFilename, cacheDir);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        TransferObserver downloadObserver = transferUtility.download(bucketFilename, dl);

        // Attach a listener to the observer to get state update and progress notifications
        downloadObserver.setTransferListener(new TransferListener() {

            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    try {
                        resultHandler.onComplete(IOUtils.toString(new FileInputStream(dl)));
                    } catch (Exception e) {
                        onError(id, e);
                    } finally {
                        dl.delete();
                    }
                }
            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                float percentDonef = ((float) bytesCurrent / (float) bytesTotal) * 100;
                int percentDone = (int) percentDonef;

                Log.d(this.getClass().getSimpleName(), "   ID:" + id + "   bytesCurrent: " + bytesCurrent + "   bytesTotal: " + bytesTotal + " " + percentDone + "%");
            }

            @Override
            public void onError(int id, Exception ex) {
                Log.e(this.getClass().getSimpleName(), ex.toString(), ex);
            }

        });

        Log.d(AWSConnector.class.getSimpleName(), "Bytes Transferrred: " + downloadObserver.getBytesTransferred());
        Log.d(AWSConnector.class.getSimpleName(), "Bytes Total: " + downloadObserver.getBytesTotal());
    }

    public static abstract class ResultHandler {

        public abstract void onComplete(String json);

    }

}
