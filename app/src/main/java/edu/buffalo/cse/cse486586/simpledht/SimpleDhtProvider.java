package edu.buffalo.cse.cse486586.simpledht;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;


public class SimpleDhtProvider extends ContentProvider {

    //TODO: make async tasks for handling different operations

    private static final String TAG = SimpleDhtProvider.class.getName();
    static final String[] ports = {"11108", "11112", "11116", "11120", "11124"};
    static String myPort;
    static final int SERVER_PORT = 10000;
    static String predecessor = "";
    static String successor = "";
    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";



    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub

        if ( !selection.equals("@") && !selection.equals("*") ) {

            //new File(uri.toString()).delete();
            if (getContext().deleteFile(selection)) {

                return 1;

            } else {

                Log.d("Del-Not Working-File:", selection);

            }


        } else if ( selection.equals("@") ) {

            try {

                int count = 0;

                Log.d(TAG, "inside @ delete");

                String[] filenames = getContext().fileList();

                for ( String name : filenames ) {

//                    if (getContext().deleteFile(name)) {
//
//                        count = count + 1;
//
//                    } else {
//
//                        Log.d("Delete", "Nahi chala");
//
//                    }

                    File dir = getContext().getFilesDir();
                    File file = new File(dir, name);
                    boolean deleted = file.delete();


                    if (deleted) {

                        count = count + 1;

                    } else {

                        Log.d("Delete", "Nahi chala");

                    }


                }
                return count;

            } catch (Exception e) {

                Log.e(TAG, "File delete failed");

            }


        } else {

            try {

                int count = 0;

                Log.d(TAG, "inside * delete");

                String[] filenames = getContext().fileList();

                for ( String name : filenames ) {


                    File dir = getContext().getFilesDir();
                    File file = new File(dir, name);
                    boolean deleted = file.delete();


                    if (deleted) {

                        count = count + 1;

                    } else {

                        Log.d("Delete", "Nahi chala");

                    }


                }
                return count;

            } catch (Exception e) {

                Log.e(TAG, "File delete failed");

            }


        }

        return 0;
    }

    @Override
    public String getType(Uri uri) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        // TODO Auto-generated method stub

        Set<Map.Entry<String, Object>> data = values.valueSet();
        Iterator itr = data.iterator();
        String key = "";
        String value = "";

        while (itr.hasNext()) {

            Map.Entry me = (Map.Entry)itr.next();
            value = me.getValue().toString();
            me = (Map.Entry)itr.next();
            key = me.getValue().toString();

            Log.d("key-", key);
            Log.d("value-", value);

        }

        try {

            String currentAvdNumber = getAvdNumberFromPort(myPort);

            String predecessorAVDNumber = getAvdNumberFromPort(predecessor);
            String successorAVDNumber = getAvdNumberFromPort(successor);


            String hashOfKey = genHash(key);
            Log.d("Hash Key", hashOfKey);

            String hashOfCurrent = genHash(currentAvdNumber);
            Log.d("Hash Current", hashOfCurrent);

            String hashOfCurrentPredecessor = genHash(predecessorAVDNumber);
            Log.d("Hash Predecessor", hashOfCurrentPredecessor);

            String hashOfCurrentSuccessor = genHash(successorAVDNumber);
            Log.d("Hash Successor", hashOfCurrentSuccessor);



            //When single node only - is 5554 or some other node
            if ( myPort.equals(successor) ) {
            //if ( ( myPort.equals(successor) ) || ( (predecessor == "") && (successor == "") ) ) {

                String filename = key;
                FileOutputStream outputStream;

                try {
                    outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(value.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    Log.e(TAG, "File write failed");
                }

                Log.d("Testing Insert", "Key:" + key + ", values:" + value);
                Log.v("insert", values.toString());


            }


            // if key is less than current node and key is greater than current's predecessor
            //key is lexicographically first than current node
            //predecessor is lexicographically first than key
            else if ((hashOfKey.compareTo(hashOfCurrent) < 0) && (hashOfKey.compareTo(hashOfCurrentPredecessor) > 0)) {


                String filename = key;
                FileOutputStream outputStream;

                try {
                    outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(value.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    Log.e(TAG, "File write failed");
                }
                Log.d("Testing Insert", "Key:" + key + ", values:" + value);
                Log.v("insert", values.toString());


                //when wrap around condition - that is current is less than predecessor and key is less than current
            } else if ((hashOfCurrent.compareTo(hashOfCurrentPredecessor) < 0) && (hashOfKey.compareTo(hashOfCurrent) < 0)) {

                String filename = key;
                String string = value;
                FileOutputStream outputStream;

                try {
                    outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(value.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    Log.e(TAG, "File write failed");
                }
                Log.d("Testing Insert", "Key:" + key + ", values:" + value);
                Log.v("insert", values.toString());


            } else if ((hashOfCurrent.compareTo(hashOfCurrentPredecessor) < 0) && (hashOfKey.compareTo(hashOfCurrentPredecessor) > 0)) {


                String filename = key;
                FileOutputStream outputStream;

                try {
                    outputStream = getContext().openFileOutput(filename, Context.MODE_PRIVATE);
                    outputStream.write(value.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    Log.e(TAG, "File write failed");
                }

                Log.d("Testing Insert", "Key:" + key + ", values:" + value);
                Log.v("insert", values.toString());

            } else {

                String message = "insert" + "@" + key + "@" + value;
                //forward initial join request to successor
                sendMessageToAVD(message, successor);


            }


        } catch (NoSuchAlgorithmException e) {

            Log.d(TAG, "Error Generating Hash Value");

        }


        return uri;

    }

    @Override
    public boolean onCreate() {
        // TODO Auto-generated method stub

        TelephonyManager tel = (TelephonyManager) getContext().getSystemService(Context.TELEPHONY_SERVICE);
        String portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);
        myPort = String.valueOf((Integer.parseInt(portStr) * 2));


        //point 5554's predecessor and successor to itself
        if ( portStr.equals("5554") ) {

            //Log.d("Avd-Number:", portStr);

            predecessor = "11108";
            successor = "11108";



        } else {

            predecessor = myPort;
            successor = myPort;

        }

//        Log.d(TAG, "I was here");
//        Log.d("Predecessor:", " "+ predecessor);
//        Log.d("Successor:", " " + successor);

        try {

            ServerSocket serverSocket = new ServerSocket(SERVER_PORT);

            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);


        } catch (IOException e) {

            Log.e(TAG, "Can't create a ServerSocket");

        }

        // if my avd is not 5554, send a join request to 5554
        if ( !portStr.equals("5554") ) {

            new SendJoinRequestTask().executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, myPort);

        }


        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        // TODO Auto-generated method stub

        String[] columns = new String[2];
        columns[0] = "key";
        columns[1] = "value";
        StringBuffer fileContent = new StringBuffer("");

        MatrixCursor cursor = new MatrixCursor(columns);
        MatrixCursor.RowBuilder builder;

        // Code referred from Stack Overflow - http://stackoverflow.com/questions/9095610/android-fileinputstream-read-txt-file-to-string

        if( !selection.equals("@") && !selection.equals("*") ) {

            String predecessorAVDNumber = getAvdNumberFromPort(predecessor);
            String currentAVDNumber = getAvdNumberFromPort(myPort);



            //String hashOfKey = genHash(selection);
            //Log.d("Hash Key", hashOfKey);

            //String hashOfCurrent = genHash(currentAVDNumber);
            //Log.d("Hash Current", hashOfCurrent);

            //String hashOfCurrentPredecessor = genHash(predecessorAVDNumber);
            //Log.d("Hash Predecessor", hashOfCurrentPredecessor);

            File file = getContext().getFileStreamPath(selection);

            if ( file.exists() ) {


                try {


                    FileInputStream fis;
                    fis = getContext().openFileInput(selection);

                    byte[] buffer = new byte[1024];

                    int n;

                    while ((n = fis.read(buffer)) != -1) {

                        fileContent.append(new String(buffer, 0, n));

                    }
                    Log.d("File Content:", fileContent.toString());

                    builder = cursor.newRow();
                    builder.add(selection);
                    builder.add(fileContent);

                    Log.v("query", selection);
                    cursor.close();
                    return cursor;

                } catch (Exception e) {

                    Log.e(TAG, "File read failed");

                }


            } else {


                String receivedSuccessor = successor;

                while ( !receivedSuccessor.equals( myPort ) )  {


                    try {

                        String message = "find_key" + "@" + selection + "@" + myPort + '\n';
                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                                Integer.parseInt(receivedSuccessor));

                        PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        out.print(message);
                        out.flush();

                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String responseMessage = in.readLine();

                        Log.d("Message response:", responseMessage);

                        String unformattedMessage = responseMessage.trim();
                        String[] messageData = unformattedMessage.split("@");

                        if ( messageData[0].equals("key_found") ) {

                            String key = messageData[1];
                            String value = messageData[2];
                            Log.d("Recv-Key", key );
                            Log.d("Recv-val", value );

                            builder = cursor.newRow();
                            builder.add(key);
                            builder.add(value);
                            break;
                            //return cursor;

                        } else {

                            Log.d("Key-Not", "found");
                            receivedSuccessor = messageData[1];


                        }



                    } catch (UnknownHostException e) {
                        Log.e(TAG, "UnknownHostException");
                    } catch (IOException e) {
                        Log.e(TAG, "IOException");
                    }


                }


            }



        } else if ( selection.equals("@")) {


            try {


                Log.d("Selection:", selection);

                FileInputStream fis;

                String[] filenames = getContext().fileList();

                for ( String name : filenames ) {

                    String[] record = new String[2];


                    Log.d("FileName:", name);
                    fis = getContext().openFileInput(name);

                    byte[] buffer = new byte[1024];

                    int n;

                    while ((n = fis.read(buffer)) != -1) {

                        fileContent.append(new String(buffer, 0, n));

                    }

                    record[0] = name;
                    record[1] = fileContent.toString();

                    //reset string buffer
                    fileContent.delete(0, fileContent.length());


                    cursor.addRow(record);


                }

            } catch (Exception e) {

                Log.e(TAG, "File read failed");

            }


        } else {

            Log.d("Inside-Star", "case");


            try {

                FileInputStream fis;

                String[] filenames = getContext().fileList();

                for ( String name : filenames ) {

                    String[] record = new String[2];


                    Log.d("FileName:", name);
                    fis = getContext().openFileInput(name);

                    byte[] buffer = new byte[1024];

                    int n;

                    while ((n = fis.read(buffer)) != -1) {

                        fileContent.append(new String(buffer, 0, n));

                    }

                    record[0] = name;
                    record[1] = fileContent.toString();

                    //reset string buffer
                    fileContent.delete(0, fileContent.length());


                    cursor.addRow(record);


                }

                String receivedSuccessor = successor;

                while ( !receivedSuccessor.equals( myPort ) )  {


                    try {

                        String message = "retrieve_all" + "@" + myPort + '\n';
                        Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                                Integer.parseInt(receivedSuccessor));

                        PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                        out.print(message);
                        out.flush();

                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String responseMessage = in.readLine();

                        Log.d("Message response:", responseMessage);

                        String unformattedMessage = responseMessage.trim();
                        String[] messageData = unformattedMessage.split("@");

                        if ( !messageData[0].equals("no_files") ) {


                            String keyValuePairsString = messageData[0];

                            Log.d("Key-Value-String", keyValuePairsString);

                            String[] keyValuePairArray = keyValuePairsString.split("-");

                            for (String pair : keyValuePairArray ) {

                                Log.d("Pair", pair);

                                String[] pairSplit = pair.split(",");

//                            String[] record = new String[2];
//                            record[0] = pairSplit[0];
//                            record[1] = pairSplit[1];
//                            cursor.addRow(record);
                                builder = cursor.newRow();
                                builder.add(pairSplit[0]);
                                builder.add(pairSplit[1]);

                            }


                        }

                        receivedSuccessor = messageData[1];



                    } catch (UnknownHostException e) {
                        Log.e(TAG, "UnknownHostException");
                    } catch (IOException e) {
                        Log.e(TAG, "IOException");
                    }


                }



            } catch (Exception e) {

                Log.e(TAG, "File read failed");

            }



        }

        return cursor;



    }



    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // TODO Auto-generated method stub
        return 0;
    }

    private String genHash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    private class ServerTask extends AsyncTask<ServerSocket, String, Void> {

        @Override
        protected Void doInBackground(ServerSocket... sockets) {
            ServerSocket serverSocket = sockets[0];

            /*
             * TODO: Fill in your server code that receives messages and passes them
             * to onProgressUpdate().
             */
            Socket socket;

            try
            {
                    while( true ) {


                        socket = serverSocket.accept();
                        Log.d("InsideServer-async-task", myPort);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String message = "null";
                        String unformattedMessage = "";
                        String[] messageData = new String[0];

                        message = in.readLine();
                        Log.d("Message-Server", message);
                        unformattedMessage = message.trim();
                        messageData = unformattedMessage.split("@");

                        Log.d(TAG, messageData[0]);
                        //Log.d(TAG, messageData[1]);

                        //socket.close();

                        if (messageData[0].equals("join_request")) {

                            Log.d(TAG, "In side initial Join");

                            String requesterPortNumber = messageData[1];
                            String requesterAvdNumber = getAvdNumberFromPort(requesterPortNumber);
                            String currentAvdNumber = getAvdNumberFromPort(myPort);

                            String predecessorAVDNumber = getAvdNumberFromPort(predecessor);
                            String successorAVDNumber = getAvdNumberFromPort(successor);

                            try {


                                String hashOfRequesterAvd = genHash(requesterAvdNumber);
                                Log.d("Hash Requester", hashOfRequesterAvd);

                                String hashOfCurrent = genHash(currentAvdNumber);
                                Log.d("Hash Current", hashOfCurrent);

                                String hashOfCurrentPredecessor = genHash(predecessorAVDNumber);
                                Log.d("Hash Predecessor", hashOfCurrentPredecessor);

                                String hashOfCurrentSuccessor = genHash(successorAVDNumber);
                                Log.d("Hash Successor", hashOfCurrentSuccessor);

                                String testValue = String.valueOf(hashOfRequesterAvd.compareTo(hashOfCurrent));
                                Log.d("Hash-Compare-New-Curr", testValue);

                                String testValue2 = String.valueOf(hashOfRequesterAvd.compareTo(hashOfCurrentPredecessor));
                                Log.d("Hash-Compare-New-Pred", testValue2);

//                            String testValue3 = String.valueOf(hashOfRequesterAvd.compareTo(hashOfCurrentSuccessor));
//                            Log.d("Hash-Compare-New-Succ", testValue3);


                                //When single node joins
                                if (myPort.equals(successor)) {

                                    // tell new node to update it's predecessor and successor to 5554
                                    String updateNodes = "update_pred_succ" + "@" + myPort + "@" + myPort;
                                    sendMessageToAVD(updateNodes, requesterPortNumber);

                                    predecessor = requesterPortNumber;
                                    successor = requesterPortNumber;

                                    Log.d("5554 node predecessor", predecessor);
                                    Log.d("5554 node successor", successor);


                                }


                                // if requester is less than current node and requester is greater than current's predecessor
                                //new node is lexicographically first than current node
                                //predecessor is lexicographically first than new node
                                else if ((hashOfRequesterAvd.compareTo(hashOfCurrent) < 0) && (hashOfRequesterAvd.compareTo(hashOfCurrentPredecessor) > 0)) {

                                    //send request - update predecessor and successor of requester node
                                    String updateNodes = "update_pred_succ" + "@" + predecessor + "@" + myPort;

                                    //TODO: instead of doing this -> i could have sent the same message using the same socket
                                    sendMessageToAVD(updateNodes, requesterPortNumber);

                                    //send request - update successor node pointer of current node's predecessor node to requester node
                                    String updatePredecessorNodesSuccessor = "update_succ" + "@" + requesterPortNumber;
                                    sendMessageToAVD(updatePredecessorNodesSuccessor, predecessor);

                                    //update predecessor of current node
                                    predecessor = requesterPortNumber;

                                    Log.d("Avd", getAvdNumberFromPort(myPort));
                                    Log.d("Port:", myPort);
                                    Log.d("Predecessor:", predecessor);
                                    Log.d("Successor:", successor);


                                    //when wrap around condition - that is current is less than predecessor and new node is less than current
                                } else if ((hashOfCurrent.compareTo(hashOfCurrentPredecessor) < 0) && (hashOfRequesterAvd.compareTo(hashOfCurrent) < 0)) {

                                    String updateNodes = "update_pred_succ" + "@" + predecessor + "@" + myPort;
                                    sendMessageToAVD(updateNodes, requesterPortNumber);

                                    String updatePredecessorNodesSuccessor = "update_succ" + "@" + requesterPortNumber;
                                    sendMessageToAVD(updatePredecessorNodesSuccessor, predecessor);

                                    predecessor = requesterPortNumber;

                                    Log.d("Avd", getAvdNumberFromPort(myPort));
                                    Log.d("Port:", myPort);
                                    Log.d("Predecessor:", predecessor);
                                    Log.d("Successor:", successor);


                                } else if ((hashOfCurrent.compareTo(hashOfCurrentPredecessor) < 0) && (hashOfRequesterAvd.compareTo(hashOfCurrentPredecessor) > 0)) {


                                    String updateNodes = "update_pred_succ" + "@" + predecessor + "@" + myPort;
                                    sendMessageToAVD(updateNodes, requesterPortNumber);

                                    String updatePredecessorNodesSuccessor = "update_succ" + "@" + requesterPortNumber;
                                    sendMessageToAVD(updatePredecessorNodesSuccessor, predecessor);

                                    predecessor = requesterPortNumber;

                                    Log.d("Avd", getAvdNumberFromPort(myPort));
                                    Log.d("Port:", myPort);
                                    Log.d("Predecessor:", predecessor);
                                    Log.d("Successor:", successor);

                                } else {


                                    //forward initial join request to successor
                                    sendMessageToAVD(message, successor);


                                }


                            } catch (NoSuchAlgorithmException e) {

                                Log.d(TAG, "Error Generating Hash Value");

                            }


                        } else if (messageData[0].equals("update_pred_succ")) {

                            //Log.d("Pred", messageData[1]);
                            //Log.d("Successor", messageData[2]);
                            predecessor = messageData[1];
                            successor = messageData[2];

                            Log.d("Avd", getAvdNumberFromPort(myPort));
                            Log.d("Port:", myPort);
                            Log.d("Predecessor:", predecessor);
                            Log.d("Successor:", successor);


                        } else if (messageData[0].equals("update_succ")) {

                            //Log.d("Successor", messageData[1]);
                            successor = messageData[1];

                            Log.d("Avd", getAvdNumberFromPort(myPort));
                            Log.d("Port:", myPort);
                            Log.d("Predecessor:", predecessor);
                            Log.d("Successor:", successor);


                        } else if (messageData[0].equals("insert")) {

                            String key = messageData[1];
                            String value = messageData[2];

                            ContentValues cv = new ContentValues();
                            cv.put(KEY_FIELD, key);
                            cv.put(VALUE_FIELD, value);
                            Uri mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
                            insert(mUri, cv);



                        } else if ( messageData[0].equals("find_key") ) {

                            String key = messageData[1];
                            String originalRequestorPort =messageData[2];

                            Log.d("message_token", messageData[0]);
                            Log.d("Key--", key);
                            Log.d("OriginalPortRequester--", originalRequestorPort);


                            try {

                                String currentAvdNumber = getAvdNumberFromPort(myPort);

                                String predecessorAVDNumber = getAvdNumberFromPort(predecessor);

                                String hashOfKey = genHash(key);

                                String hashOfCurrent = genHash(currentAvdNumber);
                                //Log.d("Hash Current", hashOfCurrent);

                                String hashOfCurrentPredecessor = genHash(predecessorAVDNumber);
                                //Log.d("Hash Predecessor", hashOfCurrentPredecessor);

                                File file = getContext().getFileStreamPath(key);

                                if ( file.exists() ) {

                                //if ( ( hashOfKey.compareTo(hashOfCurrentPredecessor) > 0 ) && ( hashOfKey.compareTo(hashOfCurrent) < 0 ) ) {

                                    Log.d("I'm Here", myPort);
                                    Uri mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");

                                    Cursor resultCursor = query(mUri, null, key, null, null);

                                    int keyIndex = resultCursor.getColumnIndex("key");
                                    int valueIndex = resultCursor.getColumnIndex("value");


                                    resultCursor.moveToFirst();

                                    String cursorKey = resultCursor.getString(keyIndex);
                                    String cursorValue = resultCursor.getString(valueIndex);


                                    String sendmessage = "key_found" + "@" + cursorKey + "@" + cursorValue + '\n';
                                    Log.d("SendMessageFormat", sendmessage);

                                    // going back to server task of original Requestor, I want this to go to query method of original Requestor instead
                                    PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                                    out.print(sendmessage);
                                    out.flush();

                                    //socket.close();


                                } else {

                                    Log.d("Check my successor", successor);

                                    String sendmessage = "key_not_found" + "@" + successor + "@" + key +'\n';
                                    Log.d("SendMessageFormat", sendmessage);

                                    PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                                    out.print(sendmessage);
                                    out.flush();
                                    //trying to listen over here again - I don't want to do that
                                    //socket.close();
                                }

                            } catch (NoSuchAlgorithmException e ) {

                                Log.d(TAG, "Error Generating Hash Value");

                            }

                        } else if ( messageData[0].equals("retrieve_all") ) {

                            //send message back to query method

                            Log.d("Inside-ret-all", message);
                            String keyValuePairs = "";
                            FileInputStream fis;

                            //Cursor resultCursor = query(mUri, null, "@", null, null);

                            String[] filenames = getContext().fileList();

                            if (filenames.length != 0) {


                                StringBuffer fileContent = new StringBuffer("");

                                for ( String name : filenames ) {

                                    String[] record = new String[2];


                                    Log.d("FileName:", name);
                                    fis = getContext().openFileInput(name);

                                    byte[] buffer = new byte[1024];

                                    int n;

                                    while ((n = fis.read(buffer)) != -1) {

                                        fileContent.append(new String(buffer, 0, n));

                                    }

                                    //record[0] = name;
                                    //record[1] = fileContent.toString();

                                    keyValuePairs += name + "," + fileContent.toString() + "-";


                                    //reset string buffer
                                    fileContent.delete(0, fileContent.length());


                                }
                                String updatedKeyValuePair = keyValuePairs.substring(0, keyValuePairs.length()-1);


                                String sendMessage = updatedKeyValuePair + "@" + successor +'\n';

                                PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                                out.print(sendMessage);
                                out.flush();

                            } else {

                                String sendMessage = "no_files" + "@" + successor +'\n';

                                PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                                out.print(sendMessage);
                                out.flush();

                            }




                        } else {

                            Log.d("Error", "Should not be inside this else condition");


                        }

                    }




            } catch(IOException e)
            {
                Log.e(TAG, "Error receiving messages");

            }
            return null;
        }

        protected void onProgressUpdate(String...strings) {


            return;
        }



    }

    private void sendMessageToAVD ( String message, String port ) {


        try {

            Log.d("HereSendMessage", port);
            Log.d("Message", message);

            Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                    Integer.parseInt(port));

            PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            out.print(message);
            out.flush();
            socket.close();

        }  catch (UnknownHostException e) {
            Log.e(TAG, "SendAVD UnknownHostException");
        } catch (IOException e) {
            Log.e(TAG, "SendAVD socket IOException");
        }


    }



    private String getAvdNumberFromPort ( String port ) {

        String avdNumber = "";

        int portNumber = Integer.valueOf(port);
        int avd = portNumber / 2;
        avdNumber = String.valueOf(avd);

        return avdNumber;
    }


    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }


    private class SendJoinRequestTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... msgs) {

            String requestorPort = "11108";

            try {

                Socket socket = new Socket(InetAddress.getByAddress(new byte[]{10, 0, 2, 2}),
                        Integer.parseInt(requestorPort));

                String message = "join_request" + "@" + myPort;

                PrintWriter out= new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                out.print(message);
                out.flush();
                socket.close();

            } catch (UnknownHostException e) {
                Log.e(TAG, "SendJoinRequestTask UnknownHostException");
            } catch (IOException e) {
                Log.e(TAG, "SendJoinRequestTask socket IOException");
            }



            return null;
        }
    }

}
