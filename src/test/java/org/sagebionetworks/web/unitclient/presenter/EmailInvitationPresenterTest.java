package org.sagebionetworks.web.unitclient.presenter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.sagebionetworks.web.client.utils.FutureUtils.getDoneFuture;
import static org.sagebionetworks.web.client.utils.FutureUtils.getFailedFuture;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.sagebionetworks.repo.model.InviteeVerificationSignedToken;
import org.sagebionetworks.repo.model.MembershipInvitation;
import org.sagebionetworks.repo.model.MembershipInvtnSignedToken;
import org.sagebionetworks.repo.model.Team;
import org.sagebionetworks.repo.model.UserProfile;
import org.sagebionetworks.web.client.GlobalApplicationState;
import org.sagebionetworks.web.client.PlaceChanger;
import org.sagebionetworks.web.client.SynapseFutureClient;
import org.sagebionetworks.web.client.SynapseJavascriptClient;
import org.sagebionetworks.web.client.place.EmailInvitation;
import org.sagebionetworks.web.client.place.LoginPlace;
import org.sagebionetworks.web.client.place.Profile;
import org.sagebionetworks.web.client.place.Synapse.ProfileArea;
import org.sagebionetworks.web.client.place.users.RegisterAccount;
import org.sagebionetworks.web.client.presenter.EmailInvitationPresenter;
import org.sagebionetworks.web.client.security.AuthenticationController;
import org.sagebionetworks.web.client.view.EmailInvitationView;
import org.sagebionetworks.web.client.widget.entity.controller.SynapseAlert;
import org.sagebionetworks.web.shared.exceptions.ForbiddenException;

@RunWith(MockitoJUnitRunner.class)
public class EmailInvitationPresenterTest {
	public static final String CURRENT_USER_ID = "987387483";
	@Mock private EmailInvitationView mockView;
	@Mock private SynapseJavascriptClient mockJsClient;
	@Mock private SynapseFutureClient futureClient;
	@Mock private SynapseAlert mockSynapseAlert;
	@Mock private AuthenticationController mockAuthController;
	@Mock private GlobalApplicationState mockGlobalApplicationState;
	@Mock private PlaceChanger mockPlaceChanger;
	@Mock private EmailInvitation place;
	@Mock private InviteeVerificationSignedToken mockInviteeVerificationSignedToken;
	@Mock private MembershipInvitation mockMembershipInvitation;
	@Mock private Team mockTeam;
	@Mock private UserProfile mockInviterProfile;
	@Mock private UserProfile mockCurrentUserProfile;
	
	private EmailInvitationPresenter presenter;
	private String encodedMISignedToken;

	@Before
	public void before() {
		when(mockGlobalApplicationState.getPlaceChanger()).thenReturn(mockPlaceChanger);
		presenter = new EmailInvitationPresenter(
				mockView, mockJsClient, futureClient, mockSynapseAlert, mockAuthController, mockGlobalApplicationState);
	}

	public void beforeSetPlace(boolean loggedIn) {
		when(mockAuthController.isLoggedIn()).thenReturn(loggedIn);
		encodedMISignedToken = "encodedToken";
		when(place.toToken()).thenReturn(encodedMISignedToken);
		MembershipInvtnSignedToken decodedToken = new MembershipInvtnSignedToken();
		when(futureClient.hexDecodeAndDeserialize(anyString(), eq(encodedMISignedToken))).thenReturn(getDoneFuture(decodedToken));
		when(mockJsClient.getMembershipInvitation(decodedToken)).thenReturn(getDoneFuture(mockMembershipInvitation));
		when(mockMembershipInvitation.getId()).thenReturn("misId");
		String email = "invitee@email.com";
		when(mockMembershipInvitation.getInviteeEmail()).thenReturn(email);
		when(mockAuthController.getCurrentUserPrincipalId()).thenReturn(CURRENT_USER_ID);
		when(mockAuthController.getCurrentUserProfile()).thenReturn(mockCurrentUserProfile);
		when(mockCurrentUserProfile.getEmails()).thenReturn(Collections.singletonList(email));
		when(mockMembershipInvitation.getTeamId()).thenReturn("teamId");
		when(mockMembershipInvitation.getCreatedBy()).thenReturn("createdBy");
		when(mockMembershipInvitation.getMessage()).thenReturn("message");
	}

	@Test
	public void testLoggedInEmailOwner() {
		beforeSetPlace(true);
		when(mockJsClient.getInviteeVerificationSignedToken(mockMembershipInvitation.getId())).thenReturn(getDoneFuture(mockInviteeVerificationSignedToken));
		when(mockJsClient.updateInviteeId(mockInviteeVerificationSignedToken)).thenReturn(getDoneFuture(null));
		
		presenter.setPlace(place);
		
		verify(mockJsClient).getInviteeVerificationSignedToken(mockMembershipInvitation.getId());
		verify(mockJsClient).updateInviteeId(mockInviteeVerificationSignedToken);
		ArgumentCaptor<org.sagebionetworks.web.client.place.Profile> captor = ArgumentCaptor.forClass(org.sagebionetworks.web.client.place.Profile.class);
		verify(mockPlaceChanger).goTo(captor.capture());
		Profile profilePlace = captor.getValue();
		assertEquals(CURRENT_USER_ID, profilePlace.getUserId());
		assertEquals(ProfileArea.TEAMS, profilePlace.getArea());
	}

	@Test
	public void testLoggedInNotEmailOwner() {
		//currently logged in user does currently own the given email address, binding Synapse account to invitation will fail
		beforeSetPlace(true);
		when(mockJsClient.getInviteeVerificationSignedToken(mockMembershipInvitation.getId())).thenReturn(getFailedFuture(new ForbiddenException()));
		when(mockJsClient.updateInviteeId(mockInviteeVerificationSignedToken)).thenReturn(getDoneFuture(null));
		
		presenter.setPlace(place);
		
		//uses this call to verify authenticated user is associated to the membership invitation email
		verify(mockJsClient).getInviteeVerificationSignedToken(anyString());
		//does not attempt to bind invitation to the current user
		verify(mockJsClient, never()).updateInviteeId(mockInviteeVerificationSignedToken);
		//instead, in this edge case we log the current user out and show the option to log in or register.
		verify(mockAuthController).logoutUser();
		verify(mockView).showNotLoggedInUI();
	}
	
	@Test
	public void testLoggedInInviteHasUserID() {
		//currently logged in user has ID matching invitation (that has already been bound, so it has a user ID!)
		beforeSetPlace(true);
		when(mockMembershipInvitation.getInviteeId()).thenReturn(CURRENT_USER_ID);
		when(mockMembershipInvitation.getInviteeEmail()).thenReturn(null);
		when(mockJsClient.getInviteeVerificationSignedToken(mockMembershipInvitation.getId())).thenReturn(getDoneFuture(mockInviteeVerificationSignedToken));
		
		presenter.setPlace(place);
		
		//does not attempt to bind invitation to the current user (since that's already done
		verify(mockJsClient, never()).getInviteeVerificationSignedToken(anyString());
		verify(mockJsClient, never()).updateInviteeId(mockInviteeVerificationSignedToken);
		ArgumentCaptor<org.sagebionetworks.web.client.place.Profile> captor = ArgumentCaptor.forClass(org.sagebionetworks.web.client.place.Profile.class);
		verify(mockPlaceChanger).goTo(captor.capture());
		Profile profilePlace = captor.getValue();
		assertEquals(CURRENT_USER_ID, profilePlace.getUserId());
		assertEquals(ProfileArea.TEAMS, profilePlace.getArea());
	}
	
	@Test
	public void testNotLoggedIn() {
		beforeSetPlace(false);
		when(mockJsClient.getTeam(mockMembershipInvitation.getTeamId())).thenReturn(getDoneFuture(mockTeam));
		when(mockTeam.getName()).thenReturn("teamName");
		when(mockJsClient.getUserProfile(mockMembershipInvitation.getCreatedBy())).thenReturn(getDoneFuture(mockInviterProfile));
		when(mockInviterProfile.getFirstName()).thenReturn("First");
		when(mockInviterProfile.getLastName()).thenReturn("Last");
		when(mockInviterProfile.getUserName()).thenReturn("Nick");
		
		presenter.setPlace(place);
		verify(mockView).setSynapseAlertContainer(mockSynapseAlert.asWidget());
		verify(mockView).showNotLoggedInUI();
		
		presenter.onRegisterClick();
		ArgumentCaptor<RegisterAccount> captor = ArgumentCaptor.forClass(RegisterAccount.class);
		verify(mockPlaceChanger).goTo(captor.capture());
		assertEquals("invitee@email.com", captor.getValue().toToken());
	}

	@Test
	public void testInvalidSignedToken() {
		beforeSetPlace(false);
		when(futureClient.hexDecodeAndDeserialize(anyString(), eq(encodedMISignedToken))).thenReturn(getFailedFuture());
		presenter.setPlace(place);
		verify(mockSynapseAlert).handleException(any(Throwable.class));
		verify(mockJsClient, never()).getMembershipInvitation(any());
	}

	@Test
	public void testLoggedInUserIsNotInvitee() {
		beforeSetPlace(true);
		when(mockJsClient.getInviteeVerificationSignedToken(mockMembershipInvitation.getId())).thenReturn(getFailedFuture());
		presenter.setPlace(place);
		verify(mockSynapseAlert).handleException(any(Throwable.class));
		verify(mockJsClient, never()).updateInviteeId(any());
	}

	@Test
	public void testOnLoginClick() {
		presenter.onLoginClick();
		ArgumentCaptor<LoginPlace> captor = ArgumentCaptor.forClass(LoginPlace.class);
		verify(mockPlaceChanger).goTo(captor.capture());
		assertEquals(LoginPlace.LOGIN_TOKEN, captor.getValue().toToken());
	}
}
