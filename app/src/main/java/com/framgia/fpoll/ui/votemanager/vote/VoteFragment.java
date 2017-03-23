package com.framgia.fpoll.ui.votemanager.vote;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableField;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.framgia.fpoll.R;
import com.framgia.fpoll.data.model.poll.Option;
import com.framgia.fpoll.data.model.poll.ParticipantVotes;
import com.framgia.fpoll.data.source.remote.voteinfo.VoteInfoRepository;
import com.framgia.fpoll.databinding.FragmentVoteBinding;
import com.framgia.fpoll.ui.votemanager.itemmodel.OptionModel;
import com.framgia.fpoll.ui.votemanager.itemmodel.PollBarData;
import com.framgia.fpoll.ui.votemanager.itemmodel.PollPieData;
import com.framgia.fpoll.ui.votemanager.itemmodel.VoteInfoModel;
import com.framgia.fpoll.util.ActivityUtil;
import com.framgia.fpoll.util.ChartUtils;
import com.framgia.fpoll.util.Constant;
import com.framgia.fpoll.util.PermissionsUtil;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.PieDataSet;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.graphics.Color.TRANSPARENT;

/**
 * Created by tran.trung.phong on 22/02/2017.
 */
public class VoteFragment extends Fragment implements VoteContract.View {
    private static final String ARGUMENT_VOTE_INFO = "ARGUMENT_VOTE_INFO";
    private FragmentVoteBinding mBinding;
    private ObservableField<VoteAdapter> mAdapter = new ObservableField<>();
    private VoteInfoModel mVoteInfoModel;
    private VoteContract.Presenter mPresenter;
    private ProgressDialog mProgressDialog;

    public static VoteFragment newInstance(VoteInfoModel voteInfo) {
        VoteFragment voteInformationFragment = new VoteFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGUMENT_VOTE_INFO, voteInfo);
        voteInformationFragment.setArguments(bundle);
        return voteInformationFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            mVoteInfoModel = getArguments().getParcelable(ARGUMENT_VOTE_INFO);
        mPresenter = new VotePresenter(this, VoteInfoRepository.getInstance(getContext()));
        mAdapter.set(new VoteAdapter(mPresenter, mVoteInfoModel));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_vote, container, false);
        mBinding.setFragment(this);
        mBinding.setPresenter((VotePresenter) mPresenter);
        mBinding.setVoteInfoModel(mVoteInfoModel);
        return mBinding.getRoot();
    }

    @Override
    public void start() {
    }

    @Override
    public void updateVoteChoice(OptionModel optionModel) {
        optionModel.setChecked(!optionModel.isChecked());
        if (mVoteInfoModel.getVoteInfo().getPoll().isMultiple()) return;
        //single vote, reset other
        for (int i = 0; i < mVoteInfoModel.getOptionModels().size(); i++) {
            if (optionModel.getOption().getId() !=
                mVoteInfoModel.getOptionModels().get(i).getOption().getId()) {
                mVoteInfoModel.getOptionModels().get(i).setChecked(false);
            }
        }
    }

    /**
     * @param options : list option user voted
     */
    @Override
    public void onSubmitSuccess(List<Option> options) {
        //TODO submit vote success switch to result tab
        //Update list vote
        mVoteInfoModel.getVoteInfo().getPoll().setOptions(options);
        //Reset check list option model
        List<OptionModel> listOptionModel = new ArrayList<>();
        for (int i = 0; i < mVoteInfoModel.getVoteInfo().getPoll().getOptions().size(); i++) {
            listOptionModel
                .add(new OptionModel(mVoteInfoModel.getVoteInfo().getPoll().getOptions().get(i),
                    false));
        }
        mVoteInfoModel.setOptionModels(listOptionModel);
        //Notify chart data
        setChartData();
        //Notify change list
        mAdapter.get().notifyDataSetChanged();
    }

    private void setChartData() {
        List<String> labels = ChartUtils.createLabels(mVoteInfoModel);
        PieDataSet pieDataSet = ChartUtils.createPieData(getContext(), mVoteInfoModel);
        BarDataSet barDataSet = ChartUtils.createBarData(getContext(), mVoteInfoModel);
        mVoteInfoModel.setPieData(new PollPieData(labels, pieDataSet));
        mVoteInfoModel.setBarData(new PollBarData(labels, barDataSet));
    }

    @Override
    public void onSubmitFailed(String message) {
        ActivityUtil.showToast(getContext(), message);
    }

    @Override
    public void onNotifyVote() {
        ActivityUtil.showToast(getContext(), getString(R.string.msg_vote_your_option));
    }

    @Override
    public void setLoading(boolean isShow) {
        if (!isShow && mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            return;
        }
        mProgressDialog = ProgressDialog.show(getContext(), null, null, true, false);
        if (mProgressDialog.getWindow() != null)
            mProgressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(TRANSPARENT));
        mProgressDialog.setContentView(new ProgressBar(getContext()));
    }

    @Override
    public void showGallery() {
        if (PermissionsUtil.isAllowPermissions(getActivity())) {
            Intent intent = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, Constant.RequestCode.IMAGE_PICKER_SELECT);
        }
    }

    @Override
    public void updateAdditionOptionSuccess() {
        mAdapter.get().notifyDataSetChanged();
    }

    @Override
    public void showVoteRequirement(int msg) {
        ActivityUtil.showToast(getContext(), getString(msg));
    }

    @Override
    public void resetChoiceBox() {
        //TODO reset choice box
    }

    public ObservableField<VoteAdapter> getAdapter() {
        return mAdapter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.RequestCode.IMAGE_PICKER_SELECT
            && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            if (selectedImage == null) return;
            //Update selected image to UI
            Glide.with(this).load(selectedImage).into(mBinding.imageAdditionOption);
            //Convert to image path
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getActivity().getContentResolver()
                .query(selectedImage, filePathColumn, null, null, null);
            if (cursor == null) return;
            cursor.moveToFirst();
            String imagePath = cursor.getString(cursor.getColumnIndex(filePathColumn[0]));
            mPresenter.setImageOption(imagePath);
            cursor.close();
        }
    }
}
