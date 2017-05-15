package com.framgia.fpoll.util;

/**
 * Created by Nhahv0902 on 2/13/2017.
 * <></>
 */
public class Constant {
    public static final int MIN_LENGTH_PASSWORD = 6;
    public static final CharSequence TITLE_TYPE_TEXT = "text";
    public static final String TYPE_IMAGE = "image";
    public static final long TIME_OUT_SERVER = 5;
    public static final String TYPE_DIALOG_FRAGMENT = "type_dialog_fragment";
    public static final int POSITION_LINK_ADMIN = 1;
    public static final int POSITION_LINK_INVITE = 0;
    public static final int LIMIT_VOTE_NUMBER_MINIUM = 2;

    public static class WebUrl {
        public static final String BASE_URL = "http://poll.framgia.vn/";
        public static final String POLL_URL = BASE_URL + "link/";
        public static final String HELP_URL =
                "https://docs.google.com/viewer?url=" + BASE_URL + "tutorial";
        public static final String DATA_SCOPE =
                "oauth2:https://www.googleapis.com/auth/userinfo.profile";
        public static final String OPTION_SPLIT = ";";
        public static final String OPTION_FORMAT = "%s";
        public static final int OPTION_TITLE = 0;
        public static final int OPTION_DATE = 1;
        public static final int OPTION_SIZE = 2;
    }

    public static class DataConstant {
        public static final String DATA_PUBLIC_PROFILE = "public_profile";
        public static final String DATA_SPACE = "";
        public static final int NUMBER_SPACE = 1;
        public static final String DATA_PLASH = "/";
        public static final String DATA_NO_TITLE = "";
        public static final String DATA_PREFIX_TOKEN = "Bearer ";
        public static final int STATUS_SUCCESS = 200;
        public static final String DATA_IMAGE = "image/*";
    }

    public static class BundleConstant {
        public static final String BUNDLE_TYPE_HISTORY = "BUNDLE_TYPE_HISTORY";
        public static final String BUNDLE_EVENT_SWITCH_UI = "BUNDLE_EVENT_SWITCH_UI";
        public static final String BUNDLE_POLL_ITEM = "BUNDLE_POLL_ITEM";
        public static final String BUNDLE_TOKEN = "BUNDLE_TOKEN";
        public static final String BUNDLE_EVENT = "BUNDLE_EVENT";
        public static final String BUNDLE_OPTION = "BUNDLE_OPTION";
        public static final String BUNDLE_POSITION = "BUNDLE_POSITION";
    }

    public static class RequestCode {
        public static final int IMAGE_PICKER_SELECT = 1;
        public static final int REQUEST_GOOGLE = 3;
        public static final int PERMISSIONS_REQUEST_WRITE_EXTERNAL = 4;
        public static final int REQUEST_LOGIN = 5;
        public static final int REQUEST_CREATE_POLL = 6;
        public static final int REQUEST_CODE_RESULT = 2;
        public static final int REQUEST_PROFILE_DETAIL = 8;
    }

    public static class TimeFormat {
        public static final String DATE_OUTPUT = "dd-MM-yyyy HH:mm";
    }

    public static class Tag {
        public static final String DATE_PICKER_TAG = "date_picker_dialog";
        public static final String TIME_PICKER_TAG = "time_picker_dialog";
    }

    public static class ConstantApi {
        public static final String BASE_URL = "http://quiet-fjord-67201.herokuapp.com/";
        public static final String KEY_TOKEN = "token";
        public static final int KEY_MULTI_CHOOSE = 1;
        public static final int KEY_SINGER_CHOOSE = 0;
        public static final String KEY_POLL_OPTION = "key_poll_option";
        public static final String KEY_POLL_SETTING = "key_poll_setting";
    }

    public static class Setting {
        public static final int EMAIL_REQUIRED = 1;
        public static final int HIDDEN_RESULT = 2;
        public static final int LINK_EDITABLE = 3;
        public static final int LIMIT_VOTE_NUMBER = 4;
        public static final int PASSWORD_REQUIRED = 5;
        public static final int NAME_REQUIRED = 7;
        public static final int NAME_AND_EMAIL_REQUIRED = 8;
        public static final int CAN_ADD_OPTION = 9;
        public static final int EMAIL_NOT_DUPLICATE = 10;
        public static final int OPTION_EDITABLE = 11;
    }

    public static class PreferenceConstant {
        public static final String PREF_USER = "PREF_USER";
        public static final String PREF_TOKEN = "PREF_TOKEN";
        public static final String PREF_IS_LOGIN = "PREF_IS_LOGIN";
        public static final String PREF_LANGUAGE = "PREF_LANGUAGE";
        public static final String PREF_EMAIL = "PREF_EMAIL";
        public static final String PREF_PASSWORD = "PREF_PASSWORD";
        public static final String PREF_REMEM_BER = "PREF_REMEM_BER";
        public static final String PREF_SHOW_INTRODUCT = "PREF_SHOW_INTRODUCT";
    }

    public class TypeSetting {
        public static final int TYPE_INPUT_EMAIL = 1;
        public static final int TYPE_INPUT_NAME = 7;
        public static final int TYPE_INPUT_EMAIL_NAME = 8;
        public static final int TYPE_NOT_EQUAL_EMAIL = 10;
        public static final int HIDENT_RESULT = 2;
        public static final int TYPE_EDIT_LINK = 3;
        public static final int TYPE_COUNT_VOTE = 4;
        public static final int TYPE_CREATE_PASSWORD = 5;
        public static final int TYPE_ADD_OPTION = 9;
        public static final int TYPE_EDIT_OPTION = 11;
    }

    public class Export {
        public static final String FILE_NAME_SAVED_PDF = "_save_file.pdf";
        public static final String FILE_NAME_SAVED_EXCEL = "_save_file.xls";
        public static final String TITLE_NAME = "name";
        public static final String TITLE_VOTE = "vote";
        public static final String FPOLL_FOLDER_NAME = "save_report";
        public static final int TYPE_EXPORT_PDF = 1;
    }

    public class Language {
        public static final String LANGUAGE_VN = "vi";
        public static final String LANGUAGE_JP = "jp";
        public static final String LANGUAGE_EN = "en";
    }

}
