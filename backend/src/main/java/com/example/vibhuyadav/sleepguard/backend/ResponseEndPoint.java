package com.example.vibhuyadav.sleepguard.backend;

/**
 * Created by zohaibakmal on 3/4/2015.
 */

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.config.Nullable;
import com.google.api.server.spi.response.CollectionResponse;
import com.google.api.server.spi.response.ConflictException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Named;

import static com.example.vibhuyadav.sleepguard.backend.OfyService.ofy;

@Api(name = "responseEndpoint", version = "v1", namespace = @ApiNamespace(ownerDomain = "com.example.vibhuyadav.sleepguard.backend", ownerName = "com.example.vibhuyadav.sleepguard.backend", packagePath=""))
public class ResponseEndPoint {
    public ResponseEndPoint() {

    }

    /**
     * Return a collection of users
     *
     * @param count The number of users
     * @return a list of users
     */

    @ApiMethod(name = "listResponse")
    public CollectionResponse<Response> listResponse(@Nullable @Named("cursor") String cursorString,
                                              @Nullable @Named("count") Integer count) {

        Query<Response> query = ofy().load().type(Response.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }
        List<Response> records = new ArrayList<Response>();
        QueryResultIterator<Response> iterator = query.iterator();
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
        return CollectionResponse.<Response>builder().setItems(records).setNextPageToken(cursorString).build();
    }

    /**
     * This inserts a new <code>Response</code> object.
     * @param response The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertResponse")
    public Response insertResponse(Response response) throws ConflictException {
//If if is not null, then check if it exists. If yes, throw an Exception
//that it is already present
//Since our @Id field is a Long, Objectify will generate a unique value for us
//when we use put
        ofy().save().entity(response).now();
        return response;
    }

    /**
     * This updates an existing <code>Response</code> object.
     * @param response The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateResponse")
    public Response updateResponse(Response response)throws NotFoundException {
        if (findRecord(response.getmDeviceId()) == null) {
            throw new NotFoundException("Response Record does not exist");
        }
        ofy().save().entity(response).now();
        return response;
    }

    /**
     * This deletes an existing <code>Response</code> object.
     * @param mDeviceId The id of the object to be deleted.
     */
    @ApiMethod(name = "removeResponse")
    public void removeResponse(@Named("mDeviceId") String mDeviceId) throws NotFoundException {
        Response record = findRecord(mDeviceId);
        if(record == null) {
            throw new NotFoundException("Response Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    //Private method to retrieve a <code>Response</code> record
    private Response findRecord(String mDeviceId) {
        return ofy().load().type(Response.class).id(mDeviceId).now();
//or return ofy().load().type(Response.class).filter("id",id).first.now();
    }

}
