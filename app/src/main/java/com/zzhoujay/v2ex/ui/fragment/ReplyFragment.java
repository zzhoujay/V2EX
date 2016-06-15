package com.zzhoujay.v2ex.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.zzhoujay.v2ex.R;
import com.zzhoujay.v2ex.V2EX;
import com.zzhoujay.v2ex.interfaces.OnLoadCompleteListener;
import com.zzhoujay.v2ex.model.Topic;
import com.zzhoujay.v2ex.util.UserUtils;

/**
 * Created by zzhoujay on 2015/7/27 0027.
 * 回复栏
 */
public class ReplyFragment extends Fragment {

    private EditText editText;
    private Topic topic;
    private OnReplySuccessListener onReplySuccessListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(Topic.TOPIC)) {
            topic = bundle.getParcelable(Topic.TOPIC);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply, container, false);
        editText = (EditText) view.findViewById(R.id.reply_content);
        ImageButton imageButton = (ImageButton) view.findViewById(R.id.reply_btn);
        imageButton.setOnClickListener(replyListener);
        return view;
    }

    private View.OnClickListener replyListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String content = editText.getText().toString();
            if (content.isEmpty()) {
                V2EX.getInstance().toast(R.string.empty_reply);
            } else {
                if (!V2EX.getInstance().isNetworkConnected()) {
                    V2EX.getInstance().toast(R.string.network_error);
                } else {
                    if (topic != null) {
                        UserUtils.replyTopic(topic.id, content, new OnLoadCompleteListener<Boolean>() {
                            @Override
                            public void loadComplete(Boolean aBoolean) {
                                if (aBoolean) {
                                    V2EX.getInstance().toast(R.string.reply_success);
                                    editText.setText("");
                                    if (onReplySuccessListener != null) {
                                        onReplySuccessListener.replySuccess();
                                    }
                                } else {
                                    V2EX.getInstance().toast(R.string.reply_error);
                                }
                            }
                        });
                    }
                }
            }
        }
    };

    public void setContent(String content) {
        editText.setText(content);
    }

    public void setSelection(int len) {
        editText.setSelection(len);
    }

    public void setOnReplySuccessListener(OnReplySuccessListener onReplySuccessListener) {
        this.onReplySuccessListener = onReplySuccessListener;
    }

    public static ReplyFragment newInstance(@Nullable Topic topic) {
        ReplyFragment replyFragment = new ReplyFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(Topic.TOPIC, topic);
        replyFragment.setArguments(bundle);
        return replyFragment;
    }

    public interface OnReplySuccessListener {
        void replySuccess();
    }
}
