package com.orgware.polling.interfaces;

/**
 * Created by cool on 09-Sep-15.
 */
public interface Appinterface {

    /*Push Notification*/
    String APP_ID = "567852824286", NOTIFICATION = "notification", NOTIFICATION_OBJECT = "notifcation", TypeID = "TypeID", GET_DEVICE_TOKEN_KEY = "GET_DEVICE_TOKEN_KEY",
            SERVER_KEY = "AIzaSyDsF2yFanZD302BOPk76a1EUCn1qgPqvH4",
            DEVICE_TOKEN_WHITE = "APA91bFcxBKvLZqfN5TgloOxIdE5WGpOjWLruYrxUNqleN__U6SgTzaqu9CbPVaTcYHEW-FJkNgsYRpqBqnDEgIcvtrb_MeCMtDcP0iVcKtItW7XY6LIY2BGwcPomFohFISyVVMq9Hnn",
            DEVICE_TOKEN_BLACK = "APA91bGmLVR_nfPsrrveTAAoXuQ6S8edNmbgXYkMT2iTdv8hmWYa83oYAVO-ZU8_r4MWNG-X0Y0pl3mdXc6RxPhkBmWVFKSzjpNTYpndnLR8OPjoI_ynDPdnDSmnMWPnIZmXh-mGJhL8",
            DEVICE_TOKEN_NEW = "APA91bEVmzf0Ls7NaVRTjDge-iL5Lzu55wuhKNd4KYZhhF6I6wfnkT16zVdes3Pqm5eQ6cdFrbUVCgipTQSw4rMXx81faaDnV2KQWq7CsaIDMOaj0SuKaxCff62KQ2gDAGRopVxkhBnf",
            DEVICE_TOKEN_7INCH = "APA91bEHeWo3RGLlTZpKk5n2C9BeXyP7dEeQqB1XugPBixHE3-JEnPhmwJM14g3ETVCb605fg_3yDwJvkIyyMgONQbP_QrFvjIMlxxT9BDRxPqMtRlWTcb6e_SIDs9hjGF6CboUiS_Ks";


    String OPINION_NUMOFQTS = "OPINION_NUMOFQTS", QUICK_NUMOFQTS = "QUICK_NUMOFQTS", SURVEY_NUMOFQTS = "SURVEY_NUMOFQTS", CURRENT_POLLDB = "CURRENT_POLLDB",
            CATEGORY_SPINNER = "CATEGORY_SPINNER", OPNINON_RB_ONE = "OPNINON_RB_ONE", OPNINON_RB_TWO = "OPNINON_RB_TWO", OPNINON_RB_THREE = "OPNINON_RB_THREE";

    /*Shared Preferences Data*/
    String SHARED_PREFERENCES_POLLING = "SHARED_PREFERENCES_POLLING", PAGER_COUNT = "PAGER_COUNT", WELCOME_SCREEN = "WELCOME_SCREEN", COUNTRY_DATA = "COUNTRY_DATA";
    String LOG_Exception = "Exception";


    /*Current Poll*/
    String QUESTION_SIZE = "QUESTION_SIZE", QUESTION_SIZE_0 = "QUESTION_SIZE_0", QUESTION_SIZE_1 = "QUESTION_SIZE_1", QUESTION_SIZE_2 = "QUESTION_SIZE_2", CHOICE_SIZE_0 = "CHOICE_SIZE_0", CHOICE_SIZE_1 = "CHOICE_SIZE_1", CHOICE_SIZE_2 = "CHOICE_SIZE_2", PAGE_ARGUMENT = "PAGE_ARGUMENT", CHOICES_SIZE = "CHOICES_SIZE";

    /*Create Poll - Survey*/
    String SURVEY_POLLNAME = "SURVEY_POLLNAME", FIRSTNAME_SURVEY = "FIRSTNAME_SURVEY", LASTNAME_SURVEY = "LASTNAME_SURVEY", SURVEY_MOBILE = "SURVEY_MOBILE";

    /*Poll Id*/
    String DASHBOARD_ID = "DASHBOARD_ID";
    /*Poll Answer*/
    String ANS_QUESTIONLIST = "questionList";

    /*OTP Data*/
    String INCOMING_RECEIVER_SMS = "INCOMING_SMS", GET_INCOMING_SMS_CONTENT = "GET_INCOMING_SMS_CONTENT", OTP_VALUE = "OTP_VALUE", OTP_MESSAGE_CONTENT = "OTP_MESSAGE_CONTENT",
            SMS_INTENTFILTER = "SmsMessage.intent.MAIN";

    /*Database*/
    int DB_VERSION = 1;
    String DATABASE_NAME = "goounj_social.db";

    /*Country and Calling Code Table*/
    String COUNTRY_TABLE_NAME = "get_country_code", COUNTRY_NAMES = "country", COUNTRY_CODE = "calling_code";
    int COUNTRY_NAME_ID = 1, COUNTRY_CODE_ID = 2;

    /*Current Poll*/
    String CURRENT_POLL_TABLE_NAME = "get_current_poll", CURRENT_POLL_START_DATE = "startDate", CURRENT_POLL_END_DATE = "endDate", CURRENT_POLL_NAME = "pollName", CURRENT_ISBOOST = "is_boost",
            CURRENT_CREATED_USER_NAME = "createdUserName", CURRENT_POLL_ID = "pollId", CONTACT_ARRAY = "CONTACT_ARRAY";

    /*Web Services*/
    String BASE_URL = "http://api.goounj.com/";
//    String BASE_URL = "http://goounjdb.cloudapp.net/";

    String BASE_URL_ONE = "http://192.168.0.131:3000/", ID = "id", BASE_URL_TWO = "http://goounjdb.cloudapp.net/", BASE_URL_MAIN = "http://goounjdb.cloudapp.net/";

    String RESULT_URL = "polls/v1/result/", RESULT_QUESTION_SIZE = "RESULT_QUESTION_SIZE", RES_COUNT_0 = "RES_COUNT_0", RES_COUNT_1 = "RES_COUNT_1", RES_COUNT_2 = "RES_COUNT_2",
            RES_COUNT_3 = "RES_COUNT_3", RES_COUNT_4 = "RES_COUNT_4", RES_COUNT_5 = "RES_COUNT_5", RES_COUNT_6 = "RES_COUNT_6", RES_COUNT_7 = "RES_COUNT_7", RES_COUNT_8 = "RES_COUNT_8",
            RES_QUESTION_0 = "RES_QUESTION_0", RES_QUESTION_1 = "RES_QUESTION_1", RES_QUESTION_2 = "RES_QUESTION_2";

    String SURVEY_POLL_URL = "survey/v1/survey";

    /*Api Login*/

    String USER_LOGIN_URL = "users/v1/user", COUNTRY = "country", USERNAME = "USERNAME", CITY = "city", PHONE = "phone", DEVICE_ID = "device_id", DEVICE_TOKEN = "device_token", OS_TYPE = "os_type", OS_VERSION = "os_version",
            SECRET_KEY = "secretKey", PUBLIC_KEY = "publicKey", USER_ID = "userId", ERROR = "error", MOBILE = "mobile";

    /*OTP Validation*/
    String OTP_VERIFY_URL = "users/v1/verification/";

    /*Delete , Show, Update User*/
    String DELETE_SHOW_UPDATE_URL = "users/v1/user/:id";

    /*Verification User  -- PUT*/
    String VERIFY_USER = "users/v1/verification/:id";

    /*Show User -- GET*/
    String SHOW_USER_URL = "users/v1/user/", SHOW_ACCESS_TIME = "access_time", SHOW_DOB = "dob", SHOW_COUNTRY_CODE = "country_code", SHOW_LASTNAME = "last_name", SHOW_ISVERIFIED = "is_verified",
            SHOW_GENDER = "gender", SHOW_EMAIL = "email", AUTH_CODE = "auth_code", AUTH_CODE_ID = "auth_type_id", CREATED_TIME = "created_time", SHOW_ROLE_DI = "role_id", PASSWORD = "password",
            SHOW_UPDATED_TIME = "updated_time", SHOW_LAST_NAME = "first_name";

    /*Update User  -- PUT*/
    String UPDATE_FNAME = "fname", UPDATE_LNAME = "lname";

    /*Answer Poll*/
    String ANSWER_POLL_URL = "polls/v1/answer", POLL_ID = "pollId", POLL_QUESTION_LIST = "questionList", POLL_QUESTION_ID = "questionId", POLL_OPTION_ID = "optionId",
            QUESTION_ONE = "QUESTION_ONE", QUESTION_TWO = "QUESTION_TWO", QUESTION_THREE = "QUESTION_TWO", OPTION_ONE = "OPTION_ONE", OPTION_TWO = "OPTION_TWO", OPTION_THREE = "OPTION_THREE";

    /*Create Poll*/
    String CREATE_POLL_URL = "polls/v1/poll", POLL_NAME = "pollName", POLL_ISBOOST = "isBoost", POLL_VISIBILITY_TYPE = "visibilityType", POLL_REWARD_TYPE = "rewardType",
            POLL_CATEGORY = "category", POLL_CREATE_USERID = "createdUserId", POLL_TYPE = "pollType", POLL_QUESTION = "question", POLL_QUESTION_TYPE = "questionType", POLL_choicesS = "choices",
            POLL_AUDIENCE = "audience", SURVEY_POLL = "survey", OPINION_POLL = "opinion", QUICK_POLL = "quick", SOCIAL_POLL = "social", QUESTION = "question", CHOICES = "choices", QUESTION_ID = "questionId";

    /*Delete Poll*/
    String DELETE_POLL_URL = "polls/v1/poll/:id";

    /*Show Poll Results*/
    String SHOW_POLL_RESULTS_URL = "polls/v1/result/:id", DETAIL_QUESTION_1 = "DETAIL_QUESTION_1", DETAIL_QUESTION_2 = "DETAIL_QUESTION_2",
            DETAIL_QUESTION_3 = "DETAIL_QUESTION_3", CHOICE_1 = "CHOICE_1", CHOICE_2 = "CHOICE_2", CHOICE_3 = "CHOICE_3", CHOICE_4 = "CHOICE_4", CHOICE_0 = "CHOICE_0",
            QUESTION_ID_0 = "QUESTION_ID_0", QUESTION_ID_1 = "QUESTION_ID_1", QUESTION_ID_2 = "QUESTION_ID_2";

    /*SHOW POLL -- GET*/
    String SHOW_POLL_URL = "polls/v1/poll/";

    /*Show Polls For Audience*/
    String SHOW_POLL_FOR_AUDIENCE = "polls/v1/pollList", POLL_LIMIT = "limit", POLL_START_DATE = "start_date", POLL_ISSKIPPED = "is_skipped", POLL_ENDDATE = "end_date", POLL_TYPE_ID = "poll_type_id",
            POLL_ISANSWERED = "is_answered", POLL_REWARD_TYPEID = "reward_type_id", POLL_VISIBILITY_TYPEID = "visibility_type_id", POLL_ANSWERED_TIME = "poll_answered_time";

    /*Show Poll For Created User -- GET*/
    String SHOW_POLL_FOR_CREATED_USER_URL = "polls/v1/pollList/:id";

    /*Parameters*/
    /*LoginActivity*/
    String P_NAME = "name", P_COUNRTY = "country", P_CITY = "city", P_PHONE = "phone", P_DEVICEID = "deviceId", P_DEVICETOKEN = "deviceToken", P_OSTYPE = "osType", P_OSVERSION = "osVersion";


}
