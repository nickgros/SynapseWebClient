package org.sagebionetworks.web.server.servlet;

import org.json.JSONObject;
import org.json.JSONTokener;

public class ViteManifest extends JSONObject {

  public ViteManifest(String json) {
    super(json);
  }

  public ViteManifest(JSONTokener json) {
    super(json);
  }
}
