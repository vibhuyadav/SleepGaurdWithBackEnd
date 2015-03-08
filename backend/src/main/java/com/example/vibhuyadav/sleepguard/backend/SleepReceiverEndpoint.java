package com.example.vibhuyadav.sleepguard.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import org.json.simple.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import static com.example.vibhuyadav.sleepguard.backend.OfyService.ofy;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "sleepReceiverApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.sleepguard.vibhuyadav.example.com",
                ownerName = "backend.sleepguard.vibhuyadav.example.com",
                packagePath = ""
        )
)
public class SleepReceiverEndpoint {

    private static final Logger logger = Logger.getLogger(SleepReceiverEndpoint.class.getName());

    @ApiMethod(name = "sendTimeStamp")
    public void sendTimeStamp(@Named("regId") String regId,@Named("timeStamp") String timeStamp) throws IOException {
        MessagingEndpoint messagingEndpoint=new MessagingEndpoint();
        System.out.println("inSendTimeStampFunction");
        messagingEndpoint.sendMessage(timeStamp);
    }




}