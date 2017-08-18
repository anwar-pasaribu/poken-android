package id.unware.poken.tools.glide;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import id.unware.poken.domain.Featured;

/**
 * Loads metadata from the media store for images and videos.
 */
public class MediaStoreDataLoader extends AsyncTaskLoader<List<Featured>> {
    private static final String[] IMAGE_PROJECTION =
            new String[]{
                    MediaStore.Images.ImageColumns._ID,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED,
                    MediaStore.Images.ImageColumns.MIME_TYPE,
                    MediaStore.Images.ImageColumns.ORIENTATION,
            };

    private static final String[] VIDEO_PROJECTION =
            new String[]{
                    MediaStore.Video.VideoColumns._ID,
                    MediaStore.Video.VideoColumns.DATE_TAKEN,
                    MediaStore.Video.VideoColumns.DATE_MODIFIED,
                    MediaStore.Video.VideoColumns.MIME_TYPE,
                    "0 AS " + MediaStore.Images.ImageColumns.ORIENTATION,
            };
    private final ForceLoadContentObserver forceLoadContentObserver = new ForceLoadContentObserver();
    private List<Featured> cached;
    private boolean observerRegistered = false;

    public MediaStoreDataLoader(Context context) {
        super(context);
    }

    @Override
    public void deliverResult(List<Featured> data) {
        if (!isReset() && isStarted()) {
            super.deliverResult(data);
        }
    }

    @Override
    protected void onStartLoading() {
        if (cached != null) {
            deliverResult(cached);
        }
        if (takeContentChanged() || cached == null) {
            forceLoad();
        }
        registerContentObserver();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        super.onReset();

        onStopLoading();
        cached = null;
        unregisterContentObserver();
    }

    @Override
    protected void onAbandon() {
        super.onAbandon();
        unregisterContentObserver();
    }

    @Override
    public List<Featured> loadInBackground() {
        List<Featured> data = queryImages();
        data.addAll(queryVideos());
        Collections.sort(data, new Comparator<Featured>() {
            @Override
            public int compare(Featured mediaStoreData, Featured mediaStoreData2) {
                return Long.valueOf(mediaStoreData2.id).compareTo(mediaStoreData.id);
            }
        });
        return data;
    }

    private List<Featured> queryImages() {
        return query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns._ID,
                MediaStore.Images.ImageColumns.DATE_TAKEN, MediaStore.Images.ImageColumns.DATE_MODIFIED,
                MediaStore.Images.ImageColumns.MIME_TYPE, MediaStore.Images.ImageColumns.ORIENTATION,
                Featured.Type.IMAGE);
    }

    private List<Featured> queryVideos() {
        return query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, VIDEO_PROJECTION,
                MediaStore.Video.VideoColumns.DATE_TAKEN, MediaStore.Video.VideoColumns._ID,
                MediaStore.Video.VideoColumns.DATE_TAKEN, MediaStore.Video.VideoColumns.DATE_MODIFIED,
                MediaStore.Video.VideoColumns.MIME_TYPE, MediaStore.Images.ImageColumns.ORIENTATION,
                Featured.Type.VIDEO);
    }

    private List<Featured> query(Uri contentUri, String[] projection, String sortByCol,
                                 String idCol, String dateTakenCol, String dateModifiedCol, String mimeTypeCol,
                                 String orientationCol, Featured.Type type) {
        final List<Featured> data = new ArrayList<Featured>();
        Cursor cursor = getContext().getContentResolver()
                .query(contentUri, projection, null, null, sortByCol + " DESC");

        if (cursor == null) {
            return data;
        }

        try {
            final int idColNum = cursor.getColumnIndexOrThrow(idCol);
            final int dateTakenColNum = cursor.getColumnIndexOrThrow(dateTakenCol);
            final int dateModifiedColNum = cursor.getColumnIndexOrThrow(dateModifiedCol);
            final int mimeTypeColNum = cursor.getColumnIndex(mimeTypeCol);
            final int orientationColNum = cursor.getColumnIndexOrThrow(orientationCol);

            while (cursor.moveToNext()) {
                long id = cursor.getLong(idColNum);
                long dateTaken = cursor.getLong(dateTakenColNum);
                String mimeType = cursor.getString(mimeTypeColNum);
                long dateModified = cursor.getLong(dateModifiedColNum);
                int orientation = cursor.getInt(orientationColNum);
                // TODO Create Featured data
                data.add(new Featured());
            }
        } finally {
            cursor.close();
        }

        return data;
    }

    private void registerContentObserver() {
        if (!observerRegistered) {
            ContentResolver cr = getContext().getContentResolver();
            cr.registerContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, false,
                    forceLoadContentObserver);
            cr.registerContentObserver(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, false,
                    forceLoadContentObserver);

            observerRegistered = true;
        }
    }

    private void unregisterContentObserver() {
        if (observerRegistered) {
            observerRegistered = false;

            getContext().getContentResolver().unregisterContentObserver(forceLoadContentObserver);
        }
    }
}
