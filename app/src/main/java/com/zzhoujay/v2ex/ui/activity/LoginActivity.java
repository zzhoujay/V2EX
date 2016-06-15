package com.zzhoujay.v2ex.ui.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zzhoujay.v2ex.R;
import com.zzhoujay.v2ex.V2EX;
import com.zzhoujay.v2ex.data.DataManger;
import com.zzhoujay.v2ex.data.MemberProvider;
import com.zzhoujay.v2ex.interfaces.OnLoadCompleteListener;
import com.zzhoujay.v2ex.model.Member;
import com.zzhoujay.v2ex.util.UserUtils;

/**
 * Created by zzhoujay on 2015/7/24 0024.
 * 登录
 */
public class LoginActivity extends AppCompatActivity {

    private EditText username, password;
    private ProgressDialog progressDialogUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        final TextInputLayout usernameLayout = (TextInputLayout) findViewById(R.id.login_username_layout);
        final TextInputLayout passwordLayout = (TextInputLayout) findViewById(R.id.login_password_layout);
        username = (EditText) findViewById(R.id.login_username);
        password = (EditText) findViewById(R.id.login_password);
        Button login = (Button) findViewById(R.id.login_btn);

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(username.getText())) {
                    usernameLayout.setError(getString(R.string.username_empty));
                    usernameLayout.setErrorEnabled(true);
                } else {
                    usernameLayout.setErrorEnabled(false);
                }
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(password.getText())) {
                    passwordLayout.setErrorEnabled(true);
                    passwordLayout.setError(getString(R.string.password_empty));
                } else {
                    passwordLayout.setErrorEnabled(false);
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernameStr = username.getText().toString();
                final String passwordStr = password.getText().toString();
                if (usernameStr.isEmpty()) {
                    V2EX.getInstance().toast(R.string.username_empty);
                    return;
                }
                if (passwordStr.isEmpty()) {
                    V2EX.getInstance().toast(R.string.password_empty);
                }
                final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage(getString(R.string.signin_progress));
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setCancelable(false);
                progressDialog.show();
                UserUtils.login(usernameStr, passwordStr, new OnLoadCompleteListener<Boolean>() {
                    @Override
                    public void loadComplete(Boolean aBoolean) {
                        if (aBoolean) {
                            V2EX.getInstance().toast(R.string.login_success);
                            getUserInfo(usernameStr);
                        } else {
                            V2EX.getInstance().toast(R.string.login_error);
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
            V2EX.getInstance().setSelf(member);
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
