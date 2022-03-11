package com.cst2335.chen0590;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ChatRoomActivity extends AppCompatActivity implements View.OnClickListener{
    MessageAdapter adapter = new MessageAdapter();
    List<ChatMessage> message = new ArrayList<ChatMessage>();

    MyOpenHelper myOpener;
    SQLiteDatabase theDatabase;

    ArrayList<ChatMessage> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        myOpener = new MyOpenHelper(this);
        theDatabase = myOpener.getWritableDatabase();

        Cursor result = theDatabase.rawQuery("Select * from "
                + MyOpenHelper.TABLE_NAME + ";", null);

        int idIndex = result.getColumnIndex(MyOpenHelper.COL_ID);
        int messageIndex = result.getColumnIndex(MyOpenHelper.COL_MESSAGE);

        while (result.moveToNext()) {

            int id = result.getInt(idIndex);
            String message = result.getString(messageIndex);

            list.add(new ChatMessage(true, message, id));
            list.add(new ChatMessage(false, message, id));
        }

        Button leftButton = (Button) findViewById(R.id.button_left);
        Button rightButton = (Button) findViewById(R.id.button_right);
        leftButton.setOnClickListener(this);
        rightButton.setOnClickListener(this);

        printCursor(result,1);

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
        myList.setOnItemClickListener((adapterView, view1, i, l) -> {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatRoomActivity.this);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is:" + i + "The database id is:" + l)
                    .setPositiveButton("Yes", (click, arg) -> {
                        message.remove(i);
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> { })

                    .create().show();

        });
    }
        private void showListViewRight(){
            EditText editText = (EditText) findViewById(R.id.typeMessage);
            ListView myList = (ListView) findViewById(R.id.listview1);
            myList.setAdapter(adapter);
            String typeMessage = editText.getText().toString();
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.COL_MESSAGE, typeMessage);
            newRow.put(MyOpenHelper.COL_SEND_RECEIVE, 1);
            long id = theDatabase.insert(MyOpenHelper.TABLE_NAME, null, newRow);
            ChatMessage rightMessage = new ChatMessage(true, typeMessage, id);
            rightMessage.setTextViewInput(editText.getText().toString());
            rightMessage.setType(adapter.SEND_RIGHT);
            message.add(rightMessage);
            adapter.notifyDataSetChanged();
            editText.setText("");
        }

        private void showListViewLeft(){
            EditText editText = (EditText) findViewById(R.id.typeMessage);
            ListView myList = findViewById(R.id.listview1);
            myList.setAdapter(adapter);
            String typeMessage = editText.getText().toString();
            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.COL_MESSAGE, typeMessage);
            newRow.put(MyOpenHelper.COL_SEND_RECEIVE, 0);
            long id = theDatabase.insert(MyOpenHelper.TABLE_NAME, null, newRow);
            ChatMessage leftMessage = new ChatMessage(true, typeMessage, id);
            leftMessage.setTextViewInput(editText.getText().toString());
            leftMessage.setType(adapter.SEND_LEFT);
            message.add(leftMessage);
            adapter.notifyDataSetChanged();
            editText.setText("");
    }

    public class ChatMessage{
        private String textViewInput;
        private int type;
        boolean sendOrReceive;
        long id;

        public ChatMessage(boolean sendOrReceive, String textViewInput, long id) {
            this.textViewInput = textViewInput;
            this.sendOrReceive = sendOrReceive;
            this.id = id;
        }

        public int getType(){ return type; }
        public void setType(int type){ this.type = type; }

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

    public void printCursor (Cursor c,int version){

        int numberColumns = c.getColumnCount();
        String[] nameColumns = c.getColumnNames();
        int numberRows = c.getCount();

        c.moveToFirst();

            int typeIndex = c.getColumnIndex(MyOpenHelper.COL_MESSAGE);
            int idColIndex = c.getColumnIndex(MyOpenHelper.COL_ID);
            int sOrRColIndex=c.getColumnIndex(MyOpenHelper.COL_SEND_RECEIVE);
        do {
            String type = c.getString(typeIndex);
            long id = c.getLong(idColIndex);
            int sOrR = c.getInt(sOrRColIndex);

            String rowValue = String.format("ID: " + id + ", Message: " + type + ", SendOrReceive: " + sOrR);
            Log.i("ROW OF RESULTS", rowValue);

        } while (c.moveToNext());

        Log.i("Version number: ", Integer.toString(version));
        Log.i("Number of columns: ", Integer.toString(numberColumns));
        Log.i("Name of columns: ", Arrays.toString(nameColumns));
        Log.i("Number of rows: ", Integer.toString(numberRows));

    }
}







