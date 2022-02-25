package com.cst2335.chen0590;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{
    MessageAdapter adapter = new MessageAdapter();
    List<ChatMessage> message = new ArrayList<ChatMessage>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        Button leftButton = (Button) findViewById(R.id.button_left);
        Button rightButton = (Button) findViewById(R.id.button_right);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);

    }

    public void onClick(View view){
        switch (view.getId()){
        case R.id.button_left:
            showListViewLeft();
            break;
        case R.id.button_right:
            showListViewRight();
            break;
        default:
            break;
        }

        ListView myList = (ListView) findViewById(R.id.listview1);
        myList.setAdapter(adapter = new MessageAdapter());
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatRoomActivity.this);
                alertDialogBuilder.setTitle("Do you want to delete this?")
                        .setMessage("The selected row is:" + i + "The database id is:" + l)
                        .setPositiveButton("Yes", (click, arg) -> {
                            message.remove(i);
                            adapter.notifyDataSetChanged();
                        })
                        .setNegativeButton("No", (click, arg) -> { })

                        .create().show();

            }
        });

    }

        private void showListViewRight(){
            ChatMessage rightMessage = new ChatMessage();
            EditText editText = (EditText) findViewById(R.id.typeMessage);
            ListView myList = (ListView) findViewById(R.id.listview1);
            rightMessage.setTextViewInput(editText.getText().toString());
            rightMessage.setType(adapter.SEND_RIGHT);
            myList.setAdapter(adapter);
            message.add(rightMessage);
            adapter.notifyDataSetChanged();
            editText.setText("");
        }
        private void showListViewLeft(){
            ChatMessage leftMessage = new ChatMessage();
            EditText editText = (EditText) findViewById(R.id.typeMessage);
            ListView myList = findViewById(R.id.listview1);
            leftMessage.setTextViewInput(editText.getText().toString());
            leftMessage.setType(adapter.SEND_LEFT);
            myList.setAdapter(adapter);
            message.add(leftMessage);
            adapter.notifyDataSetChanged();
            editText.setText("");
    }

    public class ChatMessage{
        private int imageViewPerson;
        private String textViewInput;
        private int type;

        public ChatMessage(){ }
        public ChatMessage(int imageViewPerson, String textViewInput) {
            this.imageViewPerson = imageViewPerson;
            this.textViewInput = textViewInput; }
        public int getType(){ return type; }
        public void setType(int type){ this.type = type; }

        public int getImageViewPerson() {
            return imageViewPerson;
        }

        public void setImageViewPerson(int imageViewPerson) {
            this.imageViewPerson = imageViewPerson;
        }

        public String getTextViewInput() {
            return textViewInput;
        }

        public void setTextViewInput(String textViewInput) {
            this.textViewInput = textViewInput;
        }
    }

    public class MessageAdapter extends BaseAdapter{

        public final static int SEND_LEFT = 0;
        public final static int SEND_RIGHT = 1;

        @Override
        public int getItemViewType(int position) {
            if (0 == message.get(position).getType()){
                return SEND_LEFT;
            } else if (1 == message.get(position).getType()){
                return SEND_RIGHT;
            } else{
                return 0;
            }
        }

        @Override
        public int getCount() { return message.size(); }

        public Object getItem(int position){
            return message.get(position); }

        @Override
        public long getItemId(int position) { return (long) position; }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            switch (getItemViewType(position)){
                case SEND_LEFT:
                    convertView = inflater.inflate(R.layout.msg_left,parent,false);
                    break;
                case SEND_RIGHT:
                   convertView = inflater.inflate(R.layout.msg_right,parent,false);
                default:
                    break;
            }
            TextView lView= (TextView) convertView.findViewById(R.id.text_input);
            lView.setText(message.get(position).getTextViewInput());
            return convertView;
        }
    }
}







