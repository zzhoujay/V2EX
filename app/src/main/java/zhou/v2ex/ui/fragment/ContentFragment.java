package zhou.v2ex.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import zhou.v2ex.R;

/**
 * Created by zzhoujay on 2015/7/24 0024.
 */
public class ContentFragment extends Fragment {

    public static final String CONTENT = "content";

    private String content;
    private TextView text;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle.containsKey(CONTENT)) {
            content = bundle.getString(CONTENT);
        } else {
            content = "";
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        text = (TextView) view.findViewById(R.id.content_text);
        setContent(content);
        return view;
    }

    public void setContent(String c) {
        content = c;
        text.setText(content);
    }

    public static ContentFragment newInstance(String content) {
        ContentFragment contentFragment = new ContentFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CONTENT, content);
        contentFragment.setArguments(bundle);
        return contentFragment;
    }
}
