package com.example.guardian.views.activitys;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.guardian.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneAuthActivity extends AppCompatActivity {

    private EditText et_phone_number,et_code;
    private Spinner sp_countries;
    private Button bt_sent_msm, bt_validate, bt_pasar;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String mVerificationId;
    Context context;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_phone_auth);

        et_phone_number = (EditText) findViewById(R.id.et_number_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        bt_sent_msm = (Button) findViewById(R.id.bt_send_msm);

        bt_validate = (Button) findViewById(R.id.bt_validate);

        mAuth = FirebaseAuth.getInstance();


        bt_sent_msm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCode(view);
            }
        });

        bt_validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar(view);
            }
        });
    }

    private void requestCode (View view){
        String phoneNumber = et_phone_number.getText().toString();
        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(PhoneAuthActivity.this,"Ingrese un Numero de Celular",Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber, 60, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        //Toast.makeText(PhoneAuthActivity.this,"Ingrese un numero de celular correcto" + e.getMessage(),Toast.LENGTH_SHORT).show();
                        Toast.makeText(PhoneAuthActivity.this,"Ingrese un numero de celular correcto",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        mVerificationId = verificationId;
                    }

                    @Override
                    public void onCodeAutoRetrievalTimeOut(@NonNull String verificationId) {
                        super.onCodeAutoRetrievalTimeOut(verificationId);
                        Toast.makeText(PhoneAuthActivity.this,"onCodeAutoRetrievalTimeOut : "+ verificationId,Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        mAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(PhoneAuthActivity.this, RegisterActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            Toast.makeText(PhoneAuthActivity.this,"Signed Success",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(PhoneAuthActivity.this,"Credential Failed" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void validar(View view){
        String code = et_code.getText().toString();
        if(TextUtils.isEmpty(code)){
            return;
        }
        signInWithCredential(PhoneAuthProvider.getCredential(mVerificationId,code));
    }
}
