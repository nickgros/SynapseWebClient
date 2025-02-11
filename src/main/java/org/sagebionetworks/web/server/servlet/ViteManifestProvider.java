package org.sagebionetworks.web.server.servlet;

import javax.servlet.ServletContext;
import org.json.JSONObject;

public interface ViteManifestProvider {
  JSONObject getManifest(ServletContext context);
}
