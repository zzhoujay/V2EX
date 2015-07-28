package zhou.v2ex.ui.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import zhou.v2ex.R;

/**
 * Created by zzhoujay on 2015/7/28 0028.
 * 显示内容的Dialog
 */
public class ContentDialog extends DialogFragment {

    public static final String CONTENT = "content";
    public static final String TITLE = "title";

    private CharSequence content;
    private String title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle.containsKey(CONTENT)) {
            content = bundle.getString(CONTENT);
        }
        if (bundle.containsKey(TITLE)) {
            title = bundle.getString(TITLE);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (title != null) {
            builder.setTitle(title);
        }
        if (content != null) {
            builder.setMessage(content);
        }
        builder.setPositiveButton(R.string.confirm, null);
        return builder.create();
    }

    public static ContentDialog newInstance(@Nullable String title, @Nullable CharSequence content) {
        ContentDialog contentDialog = new ContentDialog();
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putCharSequence(CONTENT, content);
        contentDialog.setArguments(bundle);
        return contentDialog;
    }
}
