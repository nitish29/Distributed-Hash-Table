package edu.buffalo.cse.cse486586.simpledht;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import java.io.FileInputStream;

public class OnLDumpTestListener implements OnClickListener {

    private static final String TAG = OnLDumpTestListener.class.getName();

    //private static final int TEST_CNT = 50;
    //private static final String KEY_FIELD = "key";
    //private static final String VALUE_FIELD = "value";

    private final TextView mTextView;
    private final ContentResolver mContentResolver;
    private final Uri mUri;

    public OnLDumpTestListener(TextView _tv, ContentResolver _cr) {
        mTextView = _tv;
        mContentResolver = _cr;
        mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");

    }

    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

    /*private ContentValues[] initTestValues() {
        ContentValues[] cv = new ContentValues[TEST_CNT];
        for (int i = 0; i < TEST_CNT; i++) {
            cv[i] = new ContentValues();
            cv[i].put(KEY_FIELD, "key" + Integer.toString(i));
            cv[i].put(VALUE_FIELD, "val" + Integer.toString(i));
        }

        return cv;
    }*/

    @Override
    public void onClick(View v) {
        new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private class Task extends AsyncTask<Void, String, Void> {

        @Override
        protected Void doInBackground(Void... params) {



            //showLocalDumpKeys();
            getKeyFromAnotherAvd();




//            if (testInsert()) {
//                publishProgress("Insert success\n");
//            } else {
//                publishProgress("Insert fail\n");
//                return null;
//            }
//
//            if (testQuery()) {
//                publishProgress("Query success\n");
//            } else {
//                publishProgress("Query fail\n");
//            }

            return null;
        }

        protected void onProgressUpdate(String...strings) {
            //mTextView.append(strings[0]+"\n");
            mTextView.append(strings[1]+"\n");

            return;
        }

        private void showLocalDumpKeys() {

            Cursor resultCursor = mContentResolver.query(mUri, null, "@", null, null);

//            String[] columns = new String[2];
//            columns[0] = "key";
//            columns[1] = "value";
//            StringBuffer fileContent = new StringBuffer("");
//
//            MatrixCursor cursor = new MatrixCursor(columns);
//            MatrixCursor.RowBuilder builder;
            //String[] val = cursor.getColumnNames();

            if ( resultCursor != null ) {


                Log.d(TAG, " " + resultCursor.getCount());

                int count = resultCursor.getCount();

                for ( int i = 0; i < count; i++ ) {

                    String[] record = new String[2];

                    int keyIndex = resultCursor.getColumnIndex("key");
                    int valueIndex = resultCursor.getColumnIndex("value");

                    while(resultCursor.moveToNext())
                    {
                        Log.d("Key:" , resultCursor.getString(keyIndex));
                        Log.d("Value:", resultCursor.getString(valueIndex));
                    }
                    //publishProgress(record);


                }


            } else {

                Log.d(TAG, "Null Hai");

            }


        }

        //dumps key that was forwarded(and inserted) to it
        private void getKeyFromAnotherAvd() {

            Cursor resultCursor = mContentResolver.query(mUri, null, "*", null, null);

//            if ( resultCursor != null ) {
//
//                Log.d(TAG, " " + resultCursor.getCount());
//
//                int count = resultCursor.getCount();
//
//                for ( int i = 0; i < count; i++ ) {
//
//                    String[] record = new String[2];
//
//                    int keyIndex = resultCursor.getColumnIndex("key");
//                    int valueIndex = resultCursor.getColumnIndex("value");
//
//                    while(resultCursor.moveToNext())
//                    {
//                        Log.d("Key:" , resultCursor.getString(keyIndex));
//                        Log.d("Value:", resultCursor.getString(valueIndex));
//                    }
//                    //publishProgress(record);
//
//
//                }
//
//
//            } else {
//
//                Log.d(TAG, "Not working");
//
//            }


        }


        /*private boolean testQuery() {
            try {
                for (int i = 0; i < TEST_CNT; i++) {
                    String key = (String) mContentValues[i].get(KEY_FIELD);
                    String val = (String) mContentValues[i].get(VALUE_FIELD);

                    Cursor resultCursor = mContentResolver.query(mUri, null,
                            key, null, null);
                    if (resultCursor == null) {
                        Log.e(TAG, "Result null");
                        throw new Exception();
                    }

                    int keyIndex = resultCursor.getColumnIndex(KEY_FIELD);
                    int valueIndex = resultCursor.getColumnIndex(VALUE_FIELD);
                    if (keyIndex == -1 || valueIndex == -1) {
                        Log.e(TAG, "Wrong columns");
                        resultCursor.close();
                        throw new Exception();
                    }

                    resultCursor.moveToFirst();

                    if (!(resultCursor.isFirst() && resultCursor.isLast())) {
                        Log.e(TAG, "Wrong number of rows");
                        resultCursor.close();
                        throw new Exception();
                    }

                    String returnKey = resultCursor.getString(keyIndex);
                    String returnValue = resultCursor.getString(valueIndex);
                    if (!(returnKey.equals(key) && returnValue.equals(val))) {
                        Log.e(TAG, "(key, value) pairs don't match\n");
                        resultCursor.close();
                        throw new Exception();
                    }

                    resultCursor.close();
                }
            } catch (Exception e) {
                return false;
            }

            return true;
        }*/
    }
}
