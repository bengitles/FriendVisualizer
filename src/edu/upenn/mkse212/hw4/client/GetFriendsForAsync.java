package edu.upenn.mkse212.hw4.client;

import java.util.Set;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GetFriendsForAsync {
	public void getFriendsList (String userIdAsString,
			AsyncCallback<Set<String>> callback);
}
