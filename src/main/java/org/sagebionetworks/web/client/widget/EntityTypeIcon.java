package org.sagebionetworks.web.client.widget;

import org.sagebionetworks.repo.model.EntityType;
import org.sagebionetworks.web.client.jsinterop.EntityTypeIconProps;
import org.sagebionetworks.web.client.jsinterop.React;
import org.sagebionetworks.web.client.jsinterop.ReactDOM;
import org.sagebionetworks.web.client.jsinterop.ReactElement;
import org.sagebionetworks.web.client.jsinterop.SRC;

public class EntityTypeIcon extends ReactComponentSpan {

	private EntityType type;
	private boolean includeTooltip = true;

	public EntityTypeIcon(EntityType type) {
		this.type = type;
		configure();
	}


	/**
	 * This is required for XML setters to work. If you want to call this from Java, prefer {@link #EntityTypeIcon(EntityType)} for strong typing.
	 * @param type
	 */
	public EntityTypeIcon(String type) {
		setType(type);
	}

	public void configure() {
		EntityTypeIconProps props = EntityTypeIconProps.create(type, includeTooltip);
		ReactElement component = React.createElement(SRC.SynapseComponents.EntityTypeIcon, props);
		ReactDOM.render(component, getElement());
	}

	public void setType(EntityType type) {
		this.type = type;
		configure();
	}

	/**
	 * This is required for XML setters to work. If you want to call this from Java, prefer {@link #setType(EntityType)} for strong typing.
	 * @param type
	 */
	public void setType(String type) {
		EntityType enumValue = EntityType.file;
		try {
			enumValue = EntityType.valueOf(type);
		} catch (IllegalArgumentException e) {
		}
		setType(enumValue);
	}

	public void setIncludeTooltip(boolean includeTooltip) {
		this.includeTooltip = includeTooltip;
		configure();
	}
}
