/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package com.example.vibhuyadav.sleepguard.backend;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.ThreadManager;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.example.vibhuyadav.sleepguard.backend.OfyService.ofy;

/**
 * An endpoint to send messages to devices registered with the backend
 * <p/>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p/>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(name = "messaging", version = "v1", namespace = @ApiNamespace(ownerDomain = "com.example.vibhuyadav.sleepguard.backend", ownerName = "com.example.vibhuyadav.sleepguard.backend", packagePath = ""))
public class MessagingEndpoint {
    private static final Logger log = Logger.getLogger(MessagingEndpoint.class.getName());

    /**
     * Api Keys can be obtained from the google cloud console
     */
    private static final String API_KEY = System.getProperty("gcm.api.key");

    /**
     * Send to the first 10 devices (You can modify this to send to any number of devices or a specific device)
     *
     * @param message The message to send
     */

    public void sendMessage(@Named("message") String message) throws IOException {
        log.info("In Send Message: "+message);
        if (message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }
        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Sender sender = new Sender(API_KEY);
        Message msg = new Message.Builder().addData("message", message).build();
        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class).limit(10).list();
        for (RegistrationRecord record : records) {
            Result result = sender.send(msg, record.getRegId(), 5);
            if (result.getMessageId() != null) {
                log.info("Message sent to " + record.getRegId());
                String canonicalRegId = result.getCanonicalRegistrationId();
                if (canonicalRegId != null) {
                    // if the regId changed, we have to update the datastore
                    log.info("Registration Id changed for " + record.getRegId() + " updating to " + canonicalRegId);
                    record.setRegId(canonicalRegId);
                    ofy().save().entity(record).now();
                }
            } else {
                String error = result.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                    log.warning("Registration Id " + record.getRegId() + " no longer registered with GCM, removing from datastore");
                    // if the device is no longer registered with Gcm, remove it from the datastore
                    ofy().delete().entity(record).now();
                } else {
                    log.warning("Error when sending message : " + error);
                }
            }
        }
    }

    public void sendMessageToDevice(Message message, @Named("regId") String regId) throws IOException {
        log.info("In Send sendMessageToDevice - Message: "+message+", RegID: "+regId);

        Map<String,String> msg = message.getData();


        Sender sender = new Sender(API_KEY);

        Result result = sender.send(message, regId, 5);
        if (result.getMessageId() != null) {
            log.info("Message sent to " + regId);
            String canonicalRegId = result.getCanonicalRegistrationId();
            if (canonicalRegId != null) {
                // if the regId changed, we have to update the datastore
                log.info("Registration Id changed for " + regId + " updating to " + canonicalRegId);
                /*record.setRegId(canonicalRegId);
                ofy().save().entity(record).now();*/
            }
        } else {
            String error = result.getErrorCodeName();
            if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                log.warning("Registration Id " + regId + " no longer registered with GCM, removing from datastore");
                // if the device is no longer registered with Gcm, remove it from the datastore
                //ofy().delete().entity(record).now();
            } else {
                log.warning("Error when sending message : " + error);
            }
        }
    }

    public void sendMessageToAwakeDevices(@Named("message") String message) throws IOException {
        log.info("In Send sendMessageToAwakeDevices - Message: " + message);
        if (message == null || message.trim().length() == 0) {
            log.warning("Not sending message because it is empty");
            return;
        }
        // crop longer messages
        if (message.length() > 1000) {
            message = message.substring(0, 1000) + "[...]";
        }
        Sender sender = new Sender(API_KEY);
        Message msg = new Message.Builder().addData("message_type", message)
                .addData("field1", "field")
                .addData("field2", "field")
                .build();

        Query<User> query = ofy().load().type(User.class);

        List<String> candidateDevices = new ArrayList<String>();

        List<User> records = new ArrayList<User>();
        QueryResultIterator<User> iterator = query.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            records.add(iterator.next());
            /*if (count != null) {
                num++;
                if (num == count) break;
            }*/

        }

        for (User user : records) {
            if (user.getStatus() == false) {
                sendMessageToDevice(msg, user.getMyId());
            }
        }
    }

    public void sendRequest(final Request request) throws IOException {
        Query<User> query = ofy().load().type(User.class);

        List<String> candidateDevices = new ArrayList<String>();

        List<User> records = new ArrayList<User>();
        final QueryResultIterator<User> iterator = query.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            records.add(iterator.next());
            /*if (count != null) {
                num++;
                if (num == count) break;
            }*/

        }
        log.info("Request Received");

        for (User user : records) {
            if (user.getStatus() == false) {
                log.info("In user record");
                // if (Util.computeDistance(user.getLongitude(), user.getLatitude(), request.getLongitude(), request.getLatitude())){
                candidateDevices.add(user.mDeviceId);
                //     }

            }
        }

        Message msg = new Message.Builder().addData("message_type", "request")
                .addData("request", request.getDeviceId())
                .addData("timeStamp", String.valueOf(request.getTimeStamp()))
                .build();

        MessagingEndpoint messagingEndpoint=new MessagingEndpoint();
        for (String regId:candidateDevices){
            messagingEndpoint.sendMessageToDevice(msg, regId);
        }


        Thread thread = ThreadManager.createBackgroundThread(new Runnable() {
            public void run() {
                Request lRequest = new Request();
                lRequest.setDeviceId(request.getDeviceId());
                try {
                    Thread.sleep(5000);
                    while (true) {

                        log.info("In Server thread - Processing for request: " + lRequest.getDeviceId());
                        ResponseEndPoint responseEndPoint = new ResponseEndPoint();
                        List<Response> collectionResponse = responseEndPoint.listResponseRequestId(lRequest.getDeviceId());
                        System.out.println("collection response size: " + collectionResponse.size());

                        collectionResponse.sort(new Comparator<Response>() {
                            @Override
                            public int compare(Response o1, Response o2) {
                                return o2.getAverage().compareTo(o1.getAverage());
                            }
                        });

                        Message msg = new Message.Builder().addData("message_type", "response")
                                .addData("message", "Quiet down please. Someone is trying to sleep")
                                .build();
                        MessagingEndpoint messagingEndpoint = new MessagingEndpoint();
                        try {
                            messagingEndpoint.sendMessageToDevice(msg, collectionResponse.get(0).getmDeviceId());
                            System.out.println("Message sent to: " + collectionResponse.get(0).getmDeviceId() + " with average " + collectionResponse.get(0).getAverage());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        for (Response response : collectionResponse) {
                            try {
                                System.out.println("Device average: " + response.getAverage());
                                responseEndPoint.removeResponse(response.getId());
                            } catch (NotFoundException e) {
                                e.printStackTrace();
                            }
                        }

                        return;
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException("Interrupted in loop:", ex);
                }
            }
        });
        thread.start();

    }

    public void sendResponse(Response response) throws IOException {
        ResponseEndPoint responseEndPoint = new ResponseEndPoint();
        try {
            responseEndPoint.insertResponse(response);
        } catch (ConflictException e) {
            e.printStackTrace();
        }
    }
}
