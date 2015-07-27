package zhou.v2ex.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import zhou.v2ex.R;
import zhou.v2ex.Z2EX;
import zhou.v2ex.data.DataManger;
import zhou.v2ex.data.MemberProvider;
import zhou.v2ex.interfaces.OnLoadCompleteListener;
import zhou.v2ex.model.Member;
import zhou.v2ex.util.UserUtils;

/**
 * Created by zzhoujay on 2015/7/24 0024.
 */
public class LoginActivity extends AppCompatActivity {

    private TextInputLayout usernameLayout, passwordLayout;
    private EditText username, password;
    private Button login;
    private ProgressDialog progressDialogUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(true);
        }
        setTitle(R.string.signin);

        initView();
    }

    private void initView() {
        usernameLayout = (TextInputLayout) findViewById(R.id.login_username_layout);
        passwordLayout = (TextInputLayout) findViewById(R.id.login_password_layout);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        login = (Button) findViewById(R.id.login_btn);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage(getString(R.string.signin_progress));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                final String usernameStr = username.getText().toString();
                UserUtils.login(usernameStr, password.getText().toString(), new OnLoadCompleteListener<Boolean>() {
                    @Override
                    public void loadComplete(Boolean aBoolean) {
                        if (aBoolean) {
                            Z2EX.getInstance().toast(R.string.login_success);
                            getUserInfo(usernameStr);
                        } else {
                            Z2EX.getInstance().toast(R.string.login_error);
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        });

    }

    private void getUserInfo(String username) {
        MemberProvider memberProvider = new MemberProvider(DataManger.getInstance().getRestAdapter(), username, true);
        DataManger.getInstance().addProvider(memberProvider.FILE_NAME, memberProvider, true);
        progressDialogUserInfo = new ProgressDialog(LoginActivity.this);
        progressDialogUserInfo.setMessage(getString(R.string.get_user_info));
        progressDialogUserInfo.show();
        DataManger.getInstance().getData(MemberProvider.SElF, onLoadListener);
    }

    private OnLoadCompleteListener<Member> onLoadListener = new OnLoadCompleteListener<Member>() {
        @Override
        public void loadComplete(Member member) {
            Z2EX.getInstance().setSelf(member);
            progressDialogUserInfo.dismiss();
            Intent intent = new Intent(LoginActivity.this, MemberActivity.class);
            intent.putExtra(Member.MEMBER, (Parcelable) member);
            startActivity(intent);
            finish();
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
