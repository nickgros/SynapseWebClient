package org.sagebionetworks.web.client.mvp;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.sagebionetworks.web.client.AppLoadingView;
import org.sagebionetworks.web.client.ClientProperties;
import org.sagebionetworks.web.client.DisplayConstants;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.PortalGinInjector;
import org.sagebionetworks.web.client.SynapseJSNIUtils;
import org.sagebionetworks.web.client.place.AccessRequirementPlace;
import org.sagebionetworks.web.client.place.AccessRequirementsPlace;
import org.sagebionetworks.web.client.place.Challenges;
import org.sagebionetworks.web.client.place.ChangeUsername;
import org.sagebionetworks.web.client.place.ComingSoon;
import org.sagebionetworks.web.client.place.Down;
import org.sagebionetworks.web.client.place.EmailInvitation;
import org.sagebionetworks.web.client.place.ErrorPlace;
import org.sagebionetworks.web.client.place.Governance;
import org.sagebionetworks.web.client.place.Help;
import org.sagebionetworks.web.client.place.Home;
import org.sagebionetworks.web.client.place.LoginPlace;
import org.sagebionetworks.web.client.place.MapPlace;
import org.sagebionetworks.web.client.place.NewAccount;
import org.sagebionetworks.web.client.place.PasswordResetSignedTokenPlace;
import org.sagebionetworks.web.client.place.PeopleSearch;
import org.sagebionetworks.web.client.place.PlansPlace;
import org.sagebionetworks.web.client.place.Profile;
import org.sagebionetworks.web.client.place.Search;
import org.sagebionetworks.web.client.place.SignedToken;
import org.sagebionetworks.web.client.place.StandaloneWiki;
import org.sagebionetworks.web.client.place.Synapse;
import org.sagebionetworks.web.client.place.SynapseForumPlace;
import org.sagebionetworks.web.client.place.Team;
import org.sagebionetworks.web.client.place.TeamSearch;
import org.sagebionetworks.web.client.place.Trash;
import org.sagebionetworks.web.client.place.TrustCenterPlace;
import org.sagebionetworks.web.client.place.Wiki;
import org.sagebionetworks.web.client.place.users.PasswordReset;
import org.sagebionetworks.web.client.place.users.RegisterAccount;
import org.sagebionetworks.web.client.presenter.BulkPresenterProxy;
import org.sagebionetworks.web.client.security.AuthenticationController;

public class AppActivityMapper implements ActivityMapper {

  private static Logger log = Logger.getLogger(
    AppActivityMapper.class.getName()
  );
  private PortalGinInjector ginjector;

  @SuppressWarnings("rawtypes")
  private List<Class> openAccessPlaces;

  private List<Class> excludeFromLastPlace;
  private SynapseJSNIUtils synapseJSNIUtils;
  AppLoadingView loading;

  /**
   * AppActivityMapper associates each Place with its corresponding {@link Activity}
   *
   * @param synapseJSNIUtilsImpl
   * @param clientFactory Factory to be passed to activities
   */
  @SuppressWarnings("rawtypes")
  public AppActivityMapper(
    PortalGinInjector ginjector,
    SynapseJSNIUtils synapseJSNIUtils,
    AppLoadingView loading
  ) {
    super();
    this.ginjector = ginjector;
    this.synapseJSNIUtils = synapseJSNIUtils;
    this.loading = loading;

    openAccessPlaces = new ArrayList<Class>();
    openAccessPlaces.add(Home.class);
    openAccessPlaces.add(ErrorPlace.class);
    openAccessPlaces.add(LoginPlace.class);
    openAccessPlaces.add(PasswordReset.class);
    openAccessPlaces.add(RegisterAccount.class);
    openAccessPlaces.add(NewAccount.class);
    openAccessPlaces.add(Synapse.class);
    openAccessPlaces.add(Wiki.class);
    openAccessPlaces.add(ComingSoon.class);
    openAccessPlaces.add(Governance.class);
    openAccessPlaces.add(Challenges.class);
    openAccessPlaces.add(Help.class);
    openAccessPlaces.add(Search.class);
    openAccessPlaces.add(Team.class);
    openAccessPlaces.add(MapPlace.class);
    openAccessPlaces.add(TeamSearch.class);
    openAccessPlaces.add(PeopleSearch.class);
    openAccessPlaces.add(Down.class);
    openAccessPlaces.add(Profile.class);
    openAccessPlaces.add(StandaloneWiki.class);
    openAccessPlaces.add(SignedToken.class);
    openAccessPlaces.add(PasswordResetSignedTokenPlace.class);
    openAccessPlaces.add(SynapseForumPlace.class);
    openAccessPlaces.add(EmailInvitation.class);
    openAccessPlaces.add(AccessRequirementsPlace.class);
    openAccessPlaces.add(AccessRequirementPlace.class);
    openAccessPlaces.add(TrustCenterPlace.class);
    openAccessPlaces.add(PlansPlace.class);

    excludeFromLastPlace = new ArrayList<Class>();
    excludeFromLastPlace.add(Home.class);
    excludeFromLastPlace.add(ErrorPlace.class);
    excludeFromLastPlace.add(LoginPlace.class);
    excludeFromLastPlace.add(PasswordReset.class);
    excludeFromLastPlace.add(RegisterAccount.class);
    excludeFromLastPlace.add(NewAccount.class);
    excludeFromLastPlace.add(ChangeUsername.class);
    excludeFromLastPlace.add(Trash.class);
    excludeFromLastPlace.add(SignedToken.class);
    excludeFromLastPlace.add(PasswordResetSignedTokenPlace.class);
    excludeFromLastPlace.add(Down.class);
  }

  @Override
  public Activity getActivity(Place place) {
    synapseJSNIUtils.setPageTitle(DisplayConstants.DEFAULT_PAGE_TITLE);
    synapseJSNIUtils.setPageDescription(
      DisplayConstants.DEFAULT_PAGE_DESCRIPTION
    );

    // cancel any pending requests on place change.
    ginjector.getSynapseJavascriptClient().cancelAllPendingRequests();

    AuthenticationController authenticationController =
      this.ginjector.getAuthenticationController();
    GlobalApplicationState globalApplicationState =
      this.ginjector.getGlobalApplicationState();

    // set current and last places
    Place storedCurrentPlace = globalApplicationState.getCurrentPlace();
    // only update move storedCurrentPlace to storedLastPlace if storedCurrentPlace is
    if (
      storedCurrentPlace != null &&
      !excludeFromLastPlace.contains(storedCurrentPlace.getClass())
    ) {
      globalApplicationState.setLastPlace(storedCurrentPlace);
    }

    // If the user is not logged in then we redirect them to the login screen
    // except for the fully public places
    if (!openAccessPlaces.contains(place.getClass())) {
      if (!authenticationController.isLoggedIn()) {
        // Redirect them to the login screen
        LoginPlace loginPlace = new LoginPlace(
          ClientProperties.DEFAULT_PLACE_TOKEN
        );
        // SWC-7093: before redirecting to the login place, set the last place to the intended target!
        if (!excludeFromLastPlace.contains(place.getClass())) {
          globalApplicationState.setLastPlace(place);
        }
        return getActivity(loginPlace);
      }
    }

    // We use GIN to generate and inject all presenters with
    // their dependencies.
    if (loading != null) loading.showWidget();
    BulkPresenterProxy bulkPresenterProxy = ginjector.getBulkPresenterProxy();
    bulkPresenterProxy.setGinjector(ginjector);
    bulkPresenterProxy.setloader(loading);
    bulkPresenterProxy.setPlace(place);
    return bulkPresenterProxy;
  }

  /**
   * Get the default place
   *
   * @return
   */
  public static Place getDefaultPlace() {
    return new Home(ClientProperties.DEFAULT_PLACE_TOKEN);
  }
}
