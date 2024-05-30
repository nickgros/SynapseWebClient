package org.sagebionetworks.web.client.context;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.sagebionetworks.web.client.jsinterop.ReactComponentProps;
import org.sagebionetworks.web.client.jsinterop.ReactComponentType;
import org.sagebionetworks.web.client.jsinterop.SynapseReactClientFullContextProviderProps;
import org.sagebionetworks.web.client.jsni.FullContextProviderPropsJSNIObject;

/**
 * Synapse React Client components must be wrapped in a SynapseContext, react-query QueryContext, and MUI Theme context.
 * Implementers of this interface provide the props for a FullContextProvider, which provides all of these contexts.
 */
public class SynapseReactClientFullContextProviderPropsProvider
  implements Provider<SynapseReactClientFullContextProviderProps> {

  private final SynapseReactClientFullContextPropsProvider provider;

  @Inject
  SynapseReactClientFullContextProviderPropsProvider(
    SynapseReactClientFullContextPropsProvider provider
  ) {
    this.provider = provider;
  }

  @Override
  public SynapseReactClientFullContextProviderProps get() {
    return provider.getJsInteropContextProps();
  }
}
