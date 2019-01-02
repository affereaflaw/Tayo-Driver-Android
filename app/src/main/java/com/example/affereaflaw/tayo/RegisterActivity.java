package com.example.affereaflaw.tayo;

import android.app.DatePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ProgressBar;
import android.content.Intent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.HashMap;


public class RegisterActivity extends AppCompatActivity {

    private String userKey;
    private EditText inputName, inputMotto, etDate, inputEmail, inputPassword, inputJalur;
    private Button btnDaftar;
    private RadioButton radGender, radGender2, gender;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private DatabaseReference userProfil;
    private Calendar tanggal;
    private RadioGroup groupGender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //meninstance firebase auth
        auth = FirebaseAuth.getInstance();

        btnDaftar       = (Button) findViewById(R.id.btn_Daftar);
        inputName       = (EditText) findViewById(R.id.txtNama);
        inputMotto      = (EditText) findViewById(R.id.txtMotto);
        inputEmail      = (EditText) findViewById(R.id.txtEmail_daftar);
        inputJalur      = (EditText) findViewById(R.id.txtJalur);
        inputPassword   = (EditText) findViewById(R.id.txtPassword_daftar);
        radGender       = (RadioButton) findViewById(R.id.btn_Lk);
        radGender2       = (RadioButton) findViewById(R.id.btn_Pr);
        etDate          = (EditText) findViewById(R.id.txtDate);
        groupGender     = (RadioGroup) findViewById(R.id.groupGender);

        if(etDate.hasSelection()){
            etDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    tanggal = Calendar.getInstance();
                    int tahun = tanggal.get(Calendar.YEAR);
                    int bulan = tanggal.get(Calendar.MONTH);
                    int hari = tanggal.get(Calendar.DAY_OF_MONTH);

                    DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                            int bulan = i1+1;
                            etDate.setText(i2+"/"+bulan+"/"+i);
                        }
                    }, tahun, bulan, hari);
                    datePickerDialog.show();
                }
            });
        }

        //ketika button daftar diklik
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String name  = inputName.getText().toString().trim();
                final String motto = inputMotto.getText().toString().trim();
                final String email    = inputEmail.getText().toString().trim();
                final String password = inputPassword.getText().toString().trim();
                final String jalur  = inputJalur.getText().toString().trim();
                final String ttl  = etDate.getText().toString().trim();
                final String genderStr;
                final int tumpang = 0;

                Integer genderLP = groupGender.getCheckedRadioButtonId();
                radGender = (RadioButton) findViewById(genderLP);
                radGender2 = (RadioButton) findViewById(genderLP);


                if (!radGender.isChecked() && !radGender2.isChecked()){
                    Toast.makeText(getApplicationContext(), "Please choose your Gender", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(radGender.isChecked()){
                    RadioButton rb = (RadioButton) findViewById(R.id.btn_Lk);
                    genderStr = rb.getText().toString().trim();
                } else {
                    RadioButton rb = (RadioButton) findViewById(R.id.btn_Pr);
                    genderStr = rb.getText().toString().trim();
                }


                if (TextUtils.isEmpty(name)){
                    Toast.makeText(getApplicationContext(),"Please input Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),"Please input your Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(motto)){
                    Toast.makeText(getApplicationContext(),"Please input your Motto", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length()<6){
                    Toast.makeText(getApplicationContext(), "Password minimum 6 characters",Toast.LENGTH_SHORT).show();
                    return;
                }

                //membuat user

                auth.createUserWithEmailAndPassword(email,password)
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(RegisterActivity.this, "createUserWithEmail:OnComplete" + task.isSuccessful()
                                        ,Toast.LENGTH_SHORT).show();
                                //Mengambil key user yang unik
                                userKey = auth.getCurrentUser().getUid();
                                //Simpan ke database
                                userProfil = FirebaseDatabase.getInstance().getReference().child("Users").child(userKey);
                                HashMap<String, String> userMap = new HashMap<>();
                                userMap.put("nama", name);
                                userMap.put("email", email);
                                userMap.put("password", password);
                                userMap.put("jalur", jalur);
                                userMap.put("gender", genderStr);
                                userMap.put("ttl", ttl);
                                userProfil.setValue(userMap);
                                userProfil.child("tumpang").setValue(tumpang);
                                //jika pendaftaran gagal, akan muncul pesan
                                //jika berhasil akan ada notif sukses

                                if(!task.isSuccessful()){
                                    Toast.makeText(RegisterActivity.this, "Authentication failed" + task.getException()
                                            ,Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    startActivity(new Intent(RegisterActivity.this, MainMenu.class));
                                    finish();
                                }
                            }
                        });
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        //progressBar.setVisibility(View.GONE);
    }

}

