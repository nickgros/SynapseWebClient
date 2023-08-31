package org.sagebionetworks.web.shared;

public class APIConstants {

  public static final String DOI = "/doi";

  public static final String USER = "/user";
  public static final String BUNDLE_MASK_PATH = "/bundle?mask=";
  public static final String ACCEPT = "Accept";
  public static final String AUTHORIZATION_HEADER = "authorization";
  public static final String BEARER_PREFIX = "Bearer ";
  public static final String SYNAPSE_ENCODING_CHARSET = "UTF-8";
  public static final String APPLICATION_JSON_CHARSET_UTF8 =
    "application/json; charset=" + SYNAPSE_ENCODING_CHARSET;
  public static final String REPO_SUFFIX_VERSION = "/version";
  public static final String TEAM = "/team";
  public static final String MEMBER = "/member";
  public static final String WIKI_VERSION_PARAMETER = "?wikiVersion=";
  public static final String FAVORITE_URI_PATH = "/favorite";
  public static final String USER_GROUP_HEADER_PREFIX_PATH =
    "/userGroupHeaders?prefix=";

  public static final String MEMBERSHIP_REQUEST = "/membershipRequest";
  public static final String OPEN_MEMBERSHIP_REQUEST_COUNT =
    MEMBERSHIP_REQUEST + "/openRequestCount";
  public static final String MEMBERSHIP_STATUS = "/membershipStatus";
  public static final String MEMBERSHIP_INVITATION = "/membershipInvitation";
  public static final String OPEN_MEMBERSHIP_INVITATION_COUNT =
    MEMBERSHIP_INVITATION + "/openInvitationCount";
  public static final String ACCEPT_INVITATION_ENDPOINT_PARAM =
    "acceptInvitationEndpoint";
  public static final String NOTIFICATION_UNSUBSCRIBE_ENDPOINT_PARAM =
    "notificationUnsubscribeEndpoint";
  public static final String INVITEE_VERIFICATION_SIGNED_TOKEN =
    "/inviteeVerificationSignedToken";
  public static final String INVITEE_ID = "/inviteeId";
  public static final String ICON = "/icon";
  public static final String PROJECTS_URI_PATH = "/projects";
  public static final String DOCKER_TAG = "/dockerTag";
  public static final String OFFSET_PARAMETER = "offset=";
  public static final String LIMIT_PARAMETER = "limit=";
  public static final String OBJECT_TYPE_PARAMETER = "objectType=";
  private static final String NEXT_PAGE_TOKEN_PARAM = "nextPageToken=";
  public static final String SKIP_TRASH_CAN_PARAM = "skipTrashCan";
  private static final String ASCENDING_PARAM = "ascending=";

  public static final String USER_PASSWORD_RESET = "/user/password/reset";
  public static final String USER_CHANGE_PASSWORD = "/user/changePassword";

  public static final String COLUMN = "/column";
  public static final String COLUMN_VIEW_DEFAULT =
    COLUMN + "/tableview/defaults/";
  public static final String TABLE = "/table";
  public static final String TABLE_QUERY = TABLE + "/query";
  public static final String TABLE_QUERY_NEXTPAGE = TABLE_QUERY + "/nextPage";
  public static final String TABLE_DOWNLOAD_CSV = TABLE + "/download/csv";
  public static final String TABLE_UPLOAD_CSV = TABLE + "/upload/csv";
  public static final String TABLE_UPLOAD_CSV_PREVIEW =
    TABLE + "/upload/csv/preview";
  public static final String TABLE_APPEND = TABLE + "/append";
  public static final String TABLE_TRANSACTION = TABLE + "/transaction";
  public static final String FILE = "/file";
  public static final String FILE_BULK = FILE + "/bulk";
  public static final String ACTIVITY_URI_PATH = "/activity";
  public static final String SCHEMA_TYPE_CREATE = "/schema/type/create/";
  public static final String VIEW_COLUMN_MODEL_REQUEST = "/column/view/scope";
  public static final String SCHEMA_TYPE_VALIDATION =
    "/schema/type/validation/";
  public static final String DOWNLOAD_LIST_V2 = "/download/list/query/";
  public static final String DOWNLOAD_LIST_ADD_V2 = "/download/list/add/";
  public static final String DOWNLOAD_LIST_PACKAGE_V2 =
    "/download/list/package/";
  public static final String DOWNLOAD_LIST_MANIFEST_V2 =
    "/download/list/package/";
  public static final String FILE_HANDLE_RESTORE = "/fileHandle/restore/";

  public static final String ASYNC_START = "/async/start";
  public static final String ASYNC_GET = "/async/get/";
  public static final String AUTH_OAUTH_2 = "/oauth2";
  public static final String AUTH_OAUTH_2_ALIAS = AUTH_OAUTH_2 + "/alias";
  public static final String DOWNLOAD = "/download";
  public static final String DOWNLOAD_LIST = DOWNLOAD + "/list";
  public static final String DOWNLOAD_LIST_ADD = DOWNLOAD_LIST + "/add";
  public static final String DOWNLOAD_LIST_REMOVE = DOWNLOAD_LIST + "/remove";
  public static final String DOWNLOAD_LIST_CLEAR = DOWNLOAD_LIST + "/clear";
  public static final String ACCESS_REQUIREMENT = "/accessRequirement/";
  public static final String ACL = "/acl";
  public static final String ACCESS_APPROVAL = "/accessApproval";
  public static final String SUBMISSIONS = "/submissions";
  public static final String DOWNLOAD_ORDER = DOWNLOAD + "/order";
  public static final String DOWNLOAD_ORDER_HISTORY =
    DOWNLOAD_ORDER + "/history";
  public static final String STORAGE_REPORT = "/storageReport";
  public static final String SEARCH = "/search";
  public static final String TEAM_MEMBERS = "/teamMembers/";
  public static final String NAME_FRAGMENT_FILTER = "fragment=";
  public static final String NAME_MEMBERTYPE_FILTER = "memberType=";
  public static final String VERSION_INFO = "/version";
  public static final String FILE_PREVIEW = "/filepreview";
  public static final String REDIRECT_PARAMETER = "redirect=";
  public static final String ACCOUNT = "/account";
  public static final String EMAIL_VALIDATION = "/emailValidation";
  public static final String ACTIONS = "/actions";
  public static final String PORTAL_ENDPOINT_PARAM = "portalEndpoint=";
  public static final int LIMIT_50 = 50;
  public static final String SIGNED_TOKEN = "#!SignedToken:";
}
