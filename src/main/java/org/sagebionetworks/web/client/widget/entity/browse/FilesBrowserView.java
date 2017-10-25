package org.sagebionetworks.web.client.widget.entity.browse;

import org.sagebionetworks.web.client.SynapseView;
import org.sagebionetworks.web.client.utils.CallbackP;

import com.google.gwt.user.client.ui.IsWidget;

public interface FilesBrowserView extends IsWidget, SynapseView {

	/**
	 * Configure the view with the parent id
	 * @param entityId
	 */
	void configure(String entityId);
	public void refreshTreeView(String entityId);
	public void setEntityClickedHandler(CallbackP<String> callback);
}
