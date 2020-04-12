package com.example.placement;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

import models.application;
import models.intern;
import models.job;
import models.student;

public class descriptionActivity extends Activity {
    private static final int PERMISSION_STORAGE_CODE = 13;
    TextView type, company_name, position, description, branches, cpi, date;
    ImageView company_icon;
    ArrayList<String> branchList;
    Button downloadButton;
    String typeOpp, compID, typeID, pdfURL;
    String c_name,c_email,s_name,s_email,offtype,offpos,cutoffCPI,studCPI,studBranch,deadlineDate;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.description_page);

        type = findViewById(R.id.textView15);
        company_name = findViewById(R.id.textView6);
        position = findViewById(R.id.textView8);
        description = findViewById(R.id.textView10);
        branches = findViewById(R.id.textView12);
        cpi = findViewById(R.id.textView14);
        company_icon = findViewById(R.id.imageView4);
        date = findViewById(R.id.textView17);
        downloadButton = findViewById(R.id.button2);
        typeOpp = getIntent().getStringExtra("Type");
        compID = getIntent().getStringExtra("Company_ID");

        if(typeOpp.equals("1")){
            typeID = getIntent().getStringExtra("Intern_ID");
            type.setText("INTERNSHIP @");
            offtype="INTERNSHIP";

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Internships").child(compID).child(typeID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    intern intern = Objects.requireNonNull(dataSnapshot.getValue(intern.class));
                    c_name=intern.getCompany_name();
                    offpos=intern.getPosition();
                    pdfURL = intern.getUrl_of_brochure();
                    branchList = intern.getBranches_allowed();
                    StringBuilder branchString = new StringBuilder();
                    for(int i=0;i<branchList.size();i++){
                        if(i<branchList.size()-1){
                            branchString.append(branchList.get(i)).append("\n");
                        } else{
                            branchString.append(branchList.get(i));
                        }
                    }
                    branches.setText(branchString.toString());
                    company_name.setText(intern.getCompany_name());
                    position.setText(intern.getPosition());
                    description.setText(intern.getDescription());
                    cutoffCPI = intern.getCpi_cutoff();
                    cpi.setText(cutoffCPI);
                    deadlineDate = intern.getLast_day_to_apply();
                    date.setText(deadlineDate);
                    Glide.with(descriptionActivity.this).load(intern.getImageURL()).into(company_icon);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
        else{
            typeID = getIntent().getStringExtra("Job_ID");
            type.setText("JOB @");
            offtype = "JOBS";
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
            ref.child("Jobs").child(compID).child(typeID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    job job = Objects.requireNonNull(dataSnapshot.getValue(job.class));
                    c_name=job.getCompany_name();
                    offpos=job.getPosition();
                    pdfURL = job.getUrl_of_brochure();
                    branchList = job.getBranches_allowed();
                    StringBuilder branchString = new StringBuilder();
                    for(int i=0;i<branchList.size();i++){
                        branchString.append(branchList.get(i)).append("/n");
                    }
                    branches.setText(branchString.toString());
                    company_name.setText(job.getCompany_name());
                    position.setText(job.getPosition());
                    description.setText(job.getDescription());
                    cutoffCPI = job.getCpi_cutoff();
                    cpi.setText(cutoffCPI);
                    deadlineDate = job.getLast_day_to_apply();
                    date.setText(deadlineDate);
                    Glide.with(descriptionActivity.this).load(job.getImageURL()).into(company_icon);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) { }
            });
        }
        DatabaseReference refs = FirebaseDatabase.getInstance().getReference();
        refs.child("Companies").child(compID).child("email").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                c_email=dataSnapshot.getValue(String.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        refs.child("Students").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                student student =Objects.requireNonNull(dataSnapshot.getValue(student.class));
                s_email = student.getEmail();
                s_name = student.getName();
                studBranch = student.getBranch();
                studCPI = student.getCpi();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
                    download();
                } else{
                    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissions,PERMISSION_STORAGE_CODE);
                }
            }
        });
    }

    public  void apply(View view){
        if(Double.parseDouble(studCPI)>=Double.parseDouble(cutoffCPI) && branchList.contains(studBranch)){
            final application application = new application(s_name,s_email,offtype,c_name,c_email,offpos);
            DatabaseReference refer = FirebaseDatabase.getInstance().getReference();
            refer.child("Applicants").child(compID).child(typeID).child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).setValue(application).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        /*try {
                            GMailSender sender = new GMailSender("Harshaljj1000@gmail.com", "Harshal@123");
                            sender.sendMail("This is Subject",
                                    "This is Body",
                                    "tarunuba@gmail.com",
                                    "tarunuba@gmail.com");
                        } catch (Exception e) {
                            Log.e("SendMail", e.getMessage(), e);
                        }*/
                        JavaMailAPI javaMailAPI = new JavaMailAPI(com.example.placement.descriptionActivity.this,"tarunuba@gmail.com","New applicant","Their is a fish on sky");
                        javaMailAPI.execute();
                        DatabaseReference ds = FirebaseDatabase.getInstance().getReference("Applications").child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                        ds.child(typeID).setValue(application).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(descriptionActivity.this, "Congratulations, you have successfully applied for this offer", Toast.LENGTH_SHORT).show();
                                } else{
                                    Toast.makeText(descriptionActivity.this, "Error while adding into user's applications", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(descriptionActivity.this, "Sorry,something went wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(descriptionActivity.this, "Sorry, You are Ineligible to Apply", Toast.LENGTH_SHORT).show();
        }
    }


    public void back(View view) {
        if(typeOpp.equals("1")){
            startActivity(new Intent(descriptionActivity.this, student_intern_activity.class));
        }
        else{
            startActivity(new Intent(descriptionActivity.this, student_job_activity.class));
        }
        finish();
    }

    public void download() {
        DownloadManager.Request request = new DownloadManager.Request((Uri.parse(pdfURL)));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE | DownloadManager.Request.NETWORK_WIFI);
        request.setTitle("Download").setDescription("Downloading File...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, c_name + " " + offpos);

        DownloadManager manager = (DownloadManager)getSystemService(Context.DOWNLOAD_SERVICE);
        assert manager != null;
        manager.enqueue(request);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_STORAGE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                download();
            } else {
                Toast.makeText(descriptionActivity.this, "Permission Denied...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
