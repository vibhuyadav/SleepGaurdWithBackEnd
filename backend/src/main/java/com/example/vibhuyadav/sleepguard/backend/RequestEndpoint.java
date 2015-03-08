package com.example.vibhuyadav.sleepguard.backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Named;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;
import static com.example.vibhuyadav.sleepguard.backend.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;

/**

/**
 * Created by zohaibakmal on 3/4/2015.
 */



@Api(name = "requestEndpoint", version = "v1", namespace = @ApiNamespace(ownerDomain = "com.example.vibhuyadav.sleepguard.backend", ownerName = "com.example.vibhuyadav.sleepguard.backend", packagePath=""))
public class RequestEndpoint {
    public RequestEndpoint() {

    }

    /**
     * Return a collection of users
     *
     * @param count The number of users
     * @return a list of users
     */

    @ApiMethod(name = "listRequest")
    public CollectionResponse<Request> listRequest(@Nullable @Named("cursor") String cursorString,
                                                     @Nullable @Named("count") Integer count) {

        Query<Request> query = ofy().load().type(Request.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }
        List<Request> records = new ArrayList<Request>();
        QueryResultIterator<Request> iterator = query.iterator();
        int num = 0;
        while (iterator.hasNext()) {
            records.add(iterator.next());
            if (count != null) {
                num++;
                if (num == count) break;
            }

        }
        //Find the next cursor
        if (cursorString != null && cursorString != "") {
            Cursor cursor = iterator.getCursor();
            if (cursor != null) {
                cursorString = cursor.toWebSafeString();
            }
        }
        return CollectionResponse.<Request>builder().setItems(records).setNextPageToken(cursorString).build();
    }

    /**
     * This inserts a new <code>Request</code> object.
     * @param request The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertRequest")
    public Request insertRequest(Request request) throws ConflictException {
//If if is not null, then check if it exists. If yes, throw an Exception
//that it is already present
//Since our @Id field is a Long, Objectify will generate a unique value for us
//when we use put
        ofy().save().entity(request).now();
        return request;
    }

    /**
     * This updates an existing <code>Request</code> object.
     * @param request The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateRequest")
    public Request updateRequest(Request request)throws NotFoundException {
        if (findRecord(request.getDeviceId()) == null) {
            throw new NotFoundException("Request Record does not exist");
        }
        ofy().save().entity(request).now();
        return request;
    }

    /**
     * This deletes an existing <code>Request</code> object.
     * @param mDeviceId The id of the object to be deleted.
     */
    @ApiMethod(name = "removeRequest")
    public void removeRequest(@Named("mDeviceId") String mDeviceId) throws NotFoundException {
        Request record = findRecord(mDeviceId);
        if(record == null) {
            throw new NotFoundException("Request Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    //Private method to retrieve a <code>Request</code> record
    private Request findRecord(String mDeviceId) {
        return ofy().load().type(Request.class).id(mDeviceId).now();
//or return ofy().load().type(Request.class).filter("id",id).first.now();
    }

}
