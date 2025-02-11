package org.sagebionetworks.web.server.servlet;

import com.google.inject.Singleton;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.servlet.ServletContext;
import org.json.JSONObject;
import org.json.JSONTokener;

@Singleton
public class ViteManifestProviderImpl implements ViteManifestProvider {

  private static final String PATH_TO_MANIFEST =
    "generated/vite/.vite/manifest.json";
  private JSONObject manifest = null;

  @Override
  public JSONObject getManifest(ServletContext context) {
    if (manifest != null) {
      return manifest;
    }
    try (InputStream stream = context.getResourceAsStream(PATH_TO_MANIFEST);) {
      JSONTokener tokener = new JSONTokener(stream);
      this.manifest = new JSONObject(tokener);
      return manifest;
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
