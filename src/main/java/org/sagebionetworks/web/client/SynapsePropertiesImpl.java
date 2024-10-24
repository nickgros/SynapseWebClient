package org.sagebionetworks.web.client;

import static org.sagebionetworks.web.client.ServiceEntryPointUtils.fixServiceEntryPoint;
import static org.sagebionetworks.web.client.utils.FutureUtils.getFuture;

import com.google.common.util.concurrent.FluentFuture;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import java.util.HashMap;
import org.sagebionetworks.web.client.utils.Callback;
import org.sagebionetworks.web.shared.PublicPrincipalIds;
import org.sagebionetworks.web.shared.WebConstants;

public class SynapsePropertiesImpl implements SynapseProperties {

  private HashMap<String, String> synapseProperties = new HashMap<>();
  private PublicPrincipalIds publicPrincipalIds;
  private StackConfigServiceAsync stackConfigService;
  private SynapseJSNIUtils synapseJSNIUtils;

  @Inject
  public SynapsePropertiesImpl(
    StackConfigServiceAsync stackConfigService,
    SynapseJSNIUtils synapseJSNIUtils
  ) {
    this.stackConfigService = stackConfigService;
    fixServiceEntryPoint(stackConfigService);
    this.synapseJSNIUtils = synapseJSNIUtils;
  }

  @Override
  public FluentFuture<
    HashMap<String, String>
  > getInitSynapsePropertiesFuture() {
    return getFuture(cb ->
      stackConfigService.getSynapseProperties(
        new AsyncCallback<HashMap<String, String>>() {
          @Override
          public void onSuccess(HashMap<String, String> properties) {
            for (String key : properties.keySet()) {
              synapseProperties.put(key, properties.get(key));
            }
            cb.onSuccess(properties);
          }

          @Override
          public void onFailure(Throwable caught) {
            synapseJSNIUtils.consoleError(caught.getMessage());
            cb.onFailure(caught);
          }
        }
      )
    );
  }

  @Override
  public String getSynapseProperty(String key) {
    return synapseProperties.get(key);
  }

  @Override
  public PublicPrincipalIds getPublicPrincipalIds() {
    if (publicPrincipalIds == null) {
      publicPrincipalIds = new PublicPrincipalIds();
      publicPrincipalIds.setPublicAclPrincipalId(
        Long.parseLong(getSynapseProperty(WebConstants.PUBLIC_ACL_PRINCIPAL_ID))
      );
      publicPrincipalIds.setAnonymousUserId(
        Long.parseLong(
          getSynapseProperty(WebConstants.ANONYMOUS_USER_PRINCIPAL_ID)
        )
      );
      publicPrincipalIds.setAuthenticatedAclPrincipalId(
        Long.parseLong(
          getSynapseProperty(WebConstants.AUTHENTICATED_ACL_PRINCIPAL_ID)
        )
      );
    }
    return publicPrincipalIds;
  }
}
