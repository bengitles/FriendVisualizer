/*******************************************************************************
 * Copyright 2011 Google Inc. All Rights Reserved.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package edu.upenn.mkse212.hw4.client;

import java.util.Set;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;

import edu.upenn.mkse212.hw4.client.FriendVisualization;
import edu.upenn.mkse212.hw4.server.GetFriendsForImpl;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FriendViewer implements EntryPoint {
	private JavaScriptObject j;
	public void onModuleLoad() {
		if (RootPanel.get("content") == null) System.out.print("null");
		
		//Create the on-screen widgets
		Label l = new Label("Query for user by ID: ");
		RootPanel.get("content").add(l);
		
		Button queryButton = new Button();
		RootPanel.get("content").add(queryButton);
		queryButton.setText("Submit");
		
		final TextBox box = new TextBox();
		RootPanel.get("content").add(box);
		
		//When a node is clicked, show its neighbors.
		queryButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				// Call a method to fetch the list of friends
				// & display them
				drawNodeAndNeighbors(box.getText());
			}
		});
	}
	
	public void drawNodeAndNeighbors(final String text) {
		final FriendViewer fv = this;
		GetFriendsForAsync friendService = new GetFriendsFor.Util().getInstance();
		
		friendService.getFriendsList(text, new AsyncCallback<Set<String>>() {
			public void onFailure(Throwable caught) {
				Window.alert("Unable to talk to server");
			}
			public void onSuccess(Set<String> result) {
				// Visualize the results!
				//This StringBuffer creates the JSON string
				StringBuffer sb = new StringBuffer();
				sb.append("{\"id\": \"");
				sb.append(text);
				sb.append("\", \"name\": \"");
				sb.append(text);
				sb.append("\", \"children\": [\n");
				boolean isFirstOne = true;
				for(String friend : result) {
					//Fence-post case--accounts for no comma after last child
					//in JSON string
					if (isFirstOne) {
						sb.append("\t{\"id\": \"");
						isFirstOne = false;
					}
					else { sb.append(",\n\t{\"id\": \""); }
					sb.append(friend);
					sb.append("\", \"name\": \"");
					sb.append(friend);
					sb.append("\", \"children\": []}");
				}
				sb.append("\n]}");
				//Creates the graph if it has not already been created.
				//Otherwise, add to the graph.
				if (j==null) {
					j = FriendVisualization.createGraph(sb.toString(), fv);
				}
				else {
					FriendVisualization.addToGraph(j, sb.toString());
				}
			}
		});
	}
}
