package com.framgia.fpoll.networking.api;

import android.text.TextUtils;
import com.framgia.fpoll.data.model.DataInfoItem;
import com.framgia.fpoll.data.model.PollItem;
import com.framgia.fpoll.data.model.poll.Option;
import com.framgia.fpoll.networking.ResponseItem;
import com.google.gson.annotations.SerializedName;
import java.io.File;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

import static com.framgia.fpoll.networking.api.UpdatePollService.EditTypeConstant.TYPE_INFORMATION;
import static com.framgia.fpoll.networking.api.UpdatePollService.EditTypeConstant.TYPE_OPTION;
import static com.framgia.fpoll.networking.api.UpdatePollService.EditTypeConstant.TYPE_SETTING;
import static com.framgia.fpoll.util.Constant.Setting.CAN_ADD_OPTION;
import static com.framgia.fpoll.util.Constant.Setting.EMAIL_NOT_DUPLICATE;
import static com.framgia.fpoll.util.Constant.Setting.HIDDEN_RESULT;
import static com.framgia.fpoll.util.Constant.Setting.LIMIT_VOTE_NUMBER;
import static com.framgia.fpoll.util.Constant.Setting.OPTION_EDITABLE;
import static com.framgia.fpoll.util.Constant.Setting.PASSWORD_REQUIRED;

/**
 * Created by tuanbg on 3/20/17.
 * <></>
 */
public interface UpdatePollService {
    @POST("api/v1/poll/update/{id}")
    Call<ResponseItem<DataInfoItem>> updateInfo(@Path("id") int id, @Body PollInfoBody poll);

    @POST("api/v1/poll/update/{id}")
    Call<ResponseItem<DataInfoItem>> updateOption(@Path("id") int id,
            @Body RequestBody requestBody);

    public static final class EditTypeConstant {
        public static final int TYPE_INFORMATION = 1;
        public static final int TYPE_OPTION = 2;
        public static final int TYPE_SETTING = 3;
    }

    class PollInfoBody {
        @SerializedName("name")
        private String mName;
        @SerializedName("email")
        private String mEmail;
        @SerializedName("title")
        private String mTitle;
        @SerializedName("multiple")
        private int mMultiple;
        @SerializedName("type_edit")
        private int mEditType = TYPE_INFORMATION;
        @SerializedName("date_close")
        private String mDateClose;
        @SerializedName("description")
        private String mDescription;
        @SerializedName("location")
        private String mLocation;

        public PollInfoBody(String name, String email, String title, int multiple, String dateClose,
                String description, String location) {
            mName = name;
            mEmail = email;
            mTitle = title;
            mMultiple = multiple;
            mDateClose = dateClose;
            mDescription = description;
            mLocation = location;
        }
    }

    public class UpdatePoll {
        private static final String OPTION_TEXT = "optionText[%s]";
        private static final String OPTION_IMAGE = "optionImage[%s]";
        private static final String TYPE_EDIT = "type_edit";
        private static final String IS_REQUIRE_VOTE = "setting[0]";
        private static final String REQUIRE_TYPE = "setting_child[0]";
        private static final String IS_SAME_EMAIL = "setting[10]";
        private static final String IS_MAX_VOTE = "setting[4]";
        private static final String NUM_MAX_VOTE = "value[4]";
        private static final String IS_HAS_PASS = "setting[5]";
        private static final String PASS = "value[5]";
        private static final String ALLOW_ADD_OPTION = "setting[9]";
        private static final String ALLOW_EDIT_OPTION = "setting[11]";
        private static final String IS_HIDE_RESULT = "setting[2]";

        public static RequestBody getOptionRequestBody(PollItem pollItem) {
            List<Option> options = pollItem.getOptions();
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            if (options == null || options.size() == 0) return builder.build();
            for (int i = 0; i < options.size(); i++) {
                if (!TextUtils.isEmpty(options.get(i).getName())) {
                    builder.addFormDataPart(String.format(OPTION_TEXT, i),
                            options.get(i).getName());
                }
                if (!TextUtils.isEmpty(options.get(i).getImage())) {
                    File file = new File(options.get(i).getImage());
                    if (!file.exists()) continue;
                    RequestBody requestBody =
                            RequestBody.create(MediaType.parse(options.get(i).getImage()), file);
                    builder.addFormDataPart(String.format(OPTION_IMAGE, i), file.getName(),
                            requestBody);
                }
            }
            builder.addFormDataPart(TYPE_EDIT, String.valueOf(TYPE_OPTION));
            return builder.build();
        }

        public static RequestBody getSettingRequestBody(PollItem pollItem) {
            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            if (pollItem.isRequireVote()) {
                builder.addFormDataPart(IS_REQUIRE_VOTE, String.valueOf(0));
                builder.addFormDataPart(REQUIRE_TYPE, String.valueOf(pollItem.getRequiteType()));
            }
            if (pollItem.isSameEmail()) {
                builder.addFormDataPart(IS_SAME_EMAIL, String.valueOf(EMAIL_NOT_DUPLICATE));
            }
            if (pollItem.isMaxVote()) {
                builder.addFormDataPart(IS_MAX_VOTE, String.valueOf(LIMIT_VOTE_NUMBER));
                builder.addFormDataPart(NUM_MAX_VOTE, String.valueOf(pollItem.getNumMaxVote()));
            }
            if (pollItem.isHasPass()) {
                builder.addFormDataPart(IS_HAS_PASS, String.valueOf(PASSWORD_REQUIRED));
                builder.addFormDataPart(PASS, pollItem.getPass());
            }
            if (pollItem.isHideResult()) {
                builder.addFormDataPart(IS_HIDE_RESULT, String.valueOf(HIDDEN_RESULT));
            }
            if (pollItem.isAllowAddOption()) {
                builder.addFormDataPart(ALLOW_ADD_OPTION, String.valueOf(CAN_ADD_OPTION));
            }
            if (pollItem.isAllowEditOption()) {
                builder.addFormDataPart(ALLOW_EDIT_OPTION, String.valueOf(OPTION_EDITABLE));
            }
            builder.addFormDataPart(TYPE_EDIT, String.valueOf(TYPE_SETTING));
            return builder.build();
        }
    }
}
