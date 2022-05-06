package com.example.medicalbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.medicalbox.Model.Box;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import static com.example.medicalbox.Common.currentUserFb;

public class AddMedicine extends AppCompatActivity {

    MaterialEditText edtMedicine,edtIllness,edtTotalPills,edtPillDose;//edtBoxNo
    Button addTime,submit,btnEdit,delBox,newPills;
    ListView timeList;
    ArrayList<String> alt;
    ArrayAdapter aa;
    String boxNumber;
    TextView txtBoxNo;
    Boolean Editable;
    Boolean unChanged;
    Box box;

    FirebaseDatabase database;
    DatabaseReference boxUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medicine);

        addTime=(Button)findViewById(R.id.addTime);
        submit=(Button)findViewById(R.id.addMedicine);
        btnEdit=(Button)findViewById(R.id.btnEdit);
        delBox=(Button)findViewById(R.id.delBox);
        newPills=(Button)findViewById(R.id.newPills);
        timeList=(ListView)findViewById(R.id.tlv);
        txtBoxNo=(TextView)findViewById(R.id.txtBoxNo);
        edtMedicine=(MaterialEditText)findViewById(R.id.edtMedicine);
        edtIllness=(MaterialEditText)findViewById(R.id.edtIllness);
        edtTotalPills=(MaterialEditText)findViewById(R.id.edtTotalPills);
        edtPillDose=(MaterialEditText)findViewById(R.id.edtPillDose);
        txtBoxNo=(TextView)findViewById(R.id.txtBoxNo);

        database = FirebaseDatabase.getInstance();
        boxUser = database.getReference("Boxes/"+currentUserFb.getDisplayName());

        Bundle b=getIntent().getExtras();
        boxNumber=b.getString("boxNo");
        txtBoxNo.append(boxNumber);

        boxUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(boxNumber).exists())
                {
                    box=dataSnapshot.child(boxNumber).getValue(Box.class);
                    edtIllness.setText(box.getIllness());
                    edtMedicine.setText(box.getMed());
                    edtIllness.setText(box.getIllness());
                    edtTotalPills.setText(String.valueOf(box.getAvailable()));
                    edtPillDose.setText(String.valueOf(box.getTake()));

                    delBox.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                    btnEdit.setVisibility(View.VISIBLE);

                    Editable=false;
                    unChanged=true;

                    alt=box.getTimeList();
                    aa=new ArrayAdapter(AddMedicine.this,android.R.layout.simple_list_item_1,alt);
                    timeList.setAdapter(aa);
                }
                else
                {
                    edtMedicine.setFocusableInTouchMode(true);
                    edtIllness.setFocusableInTouchMode(true);
                    edtPillDose.setFocusableInTouchMode(true);
                    edtTotalPills.setFocusableInTouchMode(true);

                    addTime.setVisibility(View.VISIBLE);
                    newPills.setVisibility(View.INVISIBLE);
                    submit.setVisibility(View.VISIBLE);

                    alt=new ArrayList<String>();
                    aa=new ArrayAdapter(AddMedicine.this,android.R.layout.simple_list_item_1,alt);
                    timeList.setAdapter(aa);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        timeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int index, long l) {
                if(Editable)
                {
                    AlertDialog.Builder b;
                    b = new AlertDialog.Builder(AddMedicine.this);
                    b.setMessage("Are you sure you want to exit?")
                            .setPositiveButton("Remove Time", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface di, int i) {
                                    alt.remove(index);
                                    aa.notifyDataSetChanged();
                                    unChanged=false;
                                }
                            })
                            .setNegativeButton("cancel", null);
                    AlertDialog ad = b.create();
                    ad.show();
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String BN,M,I,TM,PD;
                BN=boxNumber;
                M=edtMedicine.getText().toString();
                I=edtIllness.getText().toString();
                TM=edtTotalPills.getText().toString();
                PD=edtPillDose.getText().toString();

                if(M.isEmpty() || I.isEmpty() || TM.isEmpty() || PD.isEmpty())
                    Toast.makeText(AddMedicine.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                else
                {
                    Integer bn,tm,pd;
                    bn=Integer.parseInt(BN);
                    tm=Integer.parseInt(TM);
                    pd=Integer.parseInt(PD);

                    if(tm<pd)
                        Toast.makeText(AddMedicine.this, "Total pills are less than pill dose", Toast.LENGTH_SHORT).show();
                    else
                    {
                        if (alt.isEmpty())
                            Toast.makeText(AddMedicine.this, "Please enter Time for taking pills", Toast.LENGTH_SHORT).show();
                        else
                        {
                            //final Box b = new Box(M, bn, pd, tm, I, alt);
                            box = new Box(M, bn, pd, tm, I, alt);

                            boxUser.addListenerForSingleValueEvent(new ValueEventListener()
                            {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                                {
                                    boxUser.child(BN).setValue(box);
                                    Toast.makeText(AddMedicine.this, "Success", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError)
                                { Toast.makeText(AddMedicine.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show(); }
                            });
                        }
                    }
                }
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnEdit.setVisibility(View.GONE);
                addTime.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                newPills.setVisibility(View.VISIBLE);
                delBox.setVisibility(View.GONE);
                Editable=true;

                edtMedicine.setFocusableInTouchMode(true);
                edtIllness.setFocusableInTouchMode(true);
                edtPillDose.setFocusableInTouchMode(true);
                edtTotalPills.setFocusableInTouchMode(true);
            }
        });

        addTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog tpd=new TimePickerDialog(AddMedicine.this,
                        new TimePickerDialog.OnTimeSetListener()
                        {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                alt.add(i+":"+i1);
                                aa.notifyDataSetChanged();
                                unChanged=false;
                            }
                        },0,0,true);
                tpd.show();
            }
        });

        newPills.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b;
                b = new AlertDialog.Builder(AddMedicine.this);
                final View view= LayoutInflater.from(AddMedicine.this).inflate(R.layout.add_med_dialog,null);
                b.setView(view)
                        .setPositiveButton("Add", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface di, int i)
                            {
                                MaterialEditText me=(MaterialEditText)view.findViewById(R.id.extraMed);
                                if(me.getText().toString().isEmpty())
                                    Toast.makeText(AddMedicine.this, "0 Pills Added", Toast.LENGTH_SHORT).show();
                                else
                                {
                                    edtTotalPills.setText(String.valueOf(Integer.parseInt(edtTotalPills.getText().toString())+Integer.parseInt(me.getText().toString())));
                                    Toast.makeText(AddMedicine.this, me.getText().toString() +" pills are added, click on save to save changes ", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("cancel",null);
                AlertDialog ad=b.create();
                ad.show();
            }
        });

        delBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boxUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boxUser.child(boxNumber).removeValue();
                        Toast.makeText(AddMedicine.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        });
    }
}
