package org.sagebionetworks.web.client.widget.entity.dialog;

import java.util.List;

import org.sagebionetworks.repo.model.attachment.AttachmentData;
import org.sagebionetworks.repo.model.widget.WidgetDescriptor;
import org.sagebionetworks.web.client.SynapseView;
import org.sagebionetworks.web.client.events.WidgetDescriptorUpdatedHandler;

public interface BaseEditWidgetDescriptorView extends SynapseView {
	
	public void setPresenter(Presenter presenter);
	
	/**
	 * Show the popup
	 */
	public void show();
	
	/**
	 * Hide the popup
	 */
	public void hide();
	
	public void setWidgetDescriptor(WidgetDescriptor widgetDescriptor);

	/**
	 * Call to tell the widget descriptor view to update the widgetDescriptor based on user input (called on save) 
	 */
	public void updateDescriptorFromView();
	
	public void showBaseParams(boolean visible);
	
	/**
	 * @return the name of this widget
	 */
	public String getName();

	/**
	 * set the name of this widget
	 */
	public void setName(String name);
	
	
	public interface Presenter {
		/**
		 * Pop up an editor for an existing widget attachment
		 * @param entityId
		 * @param attachmentName
		 */
		public void editExisting(String entityId, String attachmentName, List<AttachmentData> attachments);
		
		/**
		 * Pop up an editor for a new widget attachment (of the given widget type)
		 * @param entityId
		 * @param widgetType
		 */
		public void editNew(String entityId, String widgetType, List<AttachmentData> attachments);
		
		/**
		 * 
		 * @param handler
		 */
		public void addWidgetDescriptorUpdatedHandler(WidgetDescriptorUpdatedHandler handler);
		
		/**
		 * The user selected apply
		 */
		public void apply();
	}

}
