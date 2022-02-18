package com.cst2335.chen0590;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity {

    ListView myList = findViewById(R.id.listview1);
    Button sendButton = findViewById(R.id.button2);
    Button receiveButton = findViewById(R.id.button4);
    EditText editText = (EditText) findViewById(R.id.typeMessage);
    private ArrayList<String> messages = new ArrayList<>();
    SendAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        myAdapter = new SendAdapter(ChatRoomActivity.this,R.layout.msg_left,messages);
        myList.setAdapter(myAdapter);
        onSendBtnClick();

        //myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
         //   @Override
         //   public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
         //       messages.remove(i);
         //       myAdapter.notifyDataSetChanged();
        //    }
       // });
    }
    public void onSendBtnClick(){
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = editText.getText().toString();
                messages.add(result);
                myAdapter.notifyDataSetChanged();
            }
        });
    }
    public class SendAdapter extends ArrayAdapter{

        public SendAdapter(Context context, int textViewResourcedId, List<String> objects){
            super(context,textViewResourcedId,objects);
        }
        @Override
        public int getCount() { return messages.size(); }

        public Object getItem(int position){
            return messages.get(position); }

        @Override
        public long getItemId(int position) { return (long) position; }

        @Override
        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            old = inflater.inflate(R.layout.msg_left,null);
            TextView tView = old.findViewById(R.id.typeMessage);
            tView.setText(getItem(position).toString());
            return old;
        }
    }

}