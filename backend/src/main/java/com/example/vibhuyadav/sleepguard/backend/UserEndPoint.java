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

import static com.example.vibhuyadav.sleepguard.backend.OfyService.ofy;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

@Api(name = "usereEndpoint", version = "v1", namespace = @ApiNamespace(ownerDomain = "com.example.vibhuyadav.sleepguard.backend", ownerName = "com.example.vibhuyadav.sleepguard.backend", packagePath=""))
public class UserEndPoint {
    public UserEndPoint() {

    }

    /**
     * Return a collection of users
     *
     * @param count The number of users
     * @return a list of users
     */

    @ApiMethod(name = "listUser")
    public CollectionResponse<User> listUser(@Nullable @Named("cursor") String cursorString,
                                              @Nullable @Named("count") Integer count) {

        Query<User> query = ofy().load().type(User.class);
        if (count != null) query.limit(count);
        if (cursorString != null && cursorString != "") {
            query = query.startAt(Cursor.fromWebSafeString(cursorString));
        }
        List<User> records = new ArrayList<User>();
        QueryResultIterator<User> iterator = query.iterator();
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
        return CollectionResponse.<User>builder().setItems(records).setNextPageToken(cursorString).build();
    }

    /**
     * This inserts a new <code>User</code> object.
     * @param user The object to be added.
     * @return The object to be added.
     */
    @ApiMethod(name = "insertUser")
    public User insertUser(User user) throws ConflictException {
//If if is not null, then check if it exists. If yes, throw an Exception
//that it is already present
        if (user.getMyId() != null) {
            if (findRecord(user.getMyId()) != null) {
                throw new ConflictException("Object already exists");
            }
        }
//Since our @Id field is a Long, Objectify will generate a unique value for us
//when we use put
        ofy().save().entity(user).now();
        return user;
    }

    /**
     * This updates an existing <code>User</code> object.
     * @param user The object to be added.
     * @return The object to be updated.
     */
    @ApiMethod(name = "updateUser")
    public User updateUser(User user)throws NotFoundException {
        if (findRecord(user.getMyId()) == null) {
            throw new NotFoundException("User Record does not exist");
        }
        ofy().save().entity(user).now();
        return user;
    }

    /**
     * This deletes an existing <code>User</code> object.
     * @param mDeviceId The id of the object to be deleted.
     */
    @ApiMethod(name = "removeUser")
    public void removeUser(@Named("mDeviceId") String mDeviceId) throws NotFoundException {
        User record = findRecord(mDeviceId);
        if(record == null) {
            throw new NotFoundException("User Record does not exist");
        }
        ofy().delete().entity(record).now();
    }

    //Private method to retrieve a <code>User</code> record
    private User findRecord(String mDeviceId) {
        return ofy().load().type(User.class).id(mDeviceId).now();
//or return ofy().load().type(User.class).filter("id",id).first.now();
    }

}
