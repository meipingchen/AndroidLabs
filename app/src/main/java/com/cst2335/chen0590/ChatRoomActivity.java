package com.cst2335.chen0590;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.ContentValues;
import android.content.Intent;
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

public class ChatRoomActivity extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    MessageAdapter adapter = new MessageAdapter();
    MyOpenHelper myOpener;
    SQLiteDatabase theDatabase;
    public static boolean isTablet;
    ArrayList<ChatMessage> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        isTablet = findViewById(R.id.framelayout1) != null;
        myOpener = new MyOpenHelper(this);
        theDatabase = myOpener.getWritableDatabase();

        Cursor result = theDatabase.rawQuery("Select * from "
                + MyOpenHelper.TABLE_NAME + ";", null);

        int idIndex = result.getColumnIndex(MyOpenHelper.COL_ID);
        int messageIndex = result.getColumnIndex(MyOpenHelper.COL_MESSAGE);
        int booleanIndex = result.getColumnIndex(MyOpenHelper.COL_SEND_RECEIVE);

        while (result.moveToNext()) {

            int id = result.getInt(idIndex);
            String message = result.getString(messageIndex);
            boolean sOrR;
            if(result.getInt(booleanIndex) == 0)
                sOrR = false;
            else
                sOrR = true;

            list.add(new ChatMessage(sOrR, message, id));
        }

        Button leftButton = (Button) findViewById(R.id.button_left);
        Button rightButton = (Button) findViewById(R.id.button_right);
        EditText typeMessage = findViewById(R.id.typeMessage);

        ListView myList = (ListView) findViewById(R.id.listview1);
        myList.setAdapter(adapter = new MessageAdapter());

        leftButton.setOnClickListener(click -> {
            String typeText = typeMessage.getText().toString();

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.COL_MESSAGE, typeText);
            newRow.put(MyOpenHelper.COL_SEND_RECEIVE, 1);
            long id = theDatabase.insert(MyOpenHelper.TABLE_NAME, null, newRow);

            ChatMessage newMsg = new ChatMessage(true, typeText, id);

            list.add(newMsg);
            typeMessage.setText("");
            adapter.notifyDataSetChanged();
        });

        rightButton.setOnClickListener(click -> {
            String typeText = typeMessage.getText().toString();

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.COL_MESSAGE, typeText);
            newRow.put(MyOpenHelper.COL_SEND_RECEIVE, 0);
            long id = theDatabase.insert(MyOpenHelper.TABLE_NAME, null, newRow);

            ChatMessage newMessage = new ChatMessage(false, typeText, id);

            list.add(newMessage);
            typeMessage.setText("");
            adapter.notifyDataSetChanged();
        });

        myList.setOnItemLongClickListener((adapterView, view1, i, l) -> {
            ChatMessage deleteRow = list.get(i);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatRoomActivity.this);
            alertDialogBuilder.setTitle("Do you want to delete this?")
                    .setMessage("The selected row is:" + i + "The database id is:" + l)
                    .setPositiveButton("Yes", (click, arg) -> {
                        list.remove(i);
                        theDatabase.delete(MyOpenHelper.TABLE_NAME,
                                MyOpenHelper.COL_ID + "=?", new String[]{Long.toString(deleteRow.getId())});
                        adapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (click, arg) -> { })

                    .create().show();
            return true;
        });

        myList.setOnItemClickListener((adapterView, view, position, id) -> {

            boolean isSend=list.get(position).sendOrReceive;
            String text=list.get(position).textViewInput;

            DetailsFragment fragment = new DetailsFragment();
            Bundle bundle=new Bundle();

            //put message, id and boolean in bundle
           bundle.putString("Message",text);
           bundle.putLong("positionID", id);
           bundle.putBoolean("isSend", isSend);

          //  fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            //go to next activity
            if (isTablet) {

                fragmentManager
                        .beginTransaction()
                        .replace(R.id.framelayout1, fragment)
                        .commit();
            } else {

                Intent intent1 = new Intent(ChatRoomActivity.this, EmptyActivity.class);
                intent1.putExtra("MessageTrans",bundle);
                startActivity(intent1);

            }
        });

        printCursor(result,1);

    }

    public class ChatMessage{
        private String textViewInput;
        boolean sendOrReceive;
        long id;

        public ChatMessage(boolean sendOrReceive, String textViewInput, long id) {
            this.textViewInput = textViewInput;
            this.sendOrReceive = sendOrReceive;
            this.id = id;
        }
        public String getTextViewInput() {
            return textViewInput;
        }
        public boolean isSendOrReceive(){
            return sendOrReceive;
        }
        public long getId(){return id;}


    }

    public class MessageAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position).textViewInput;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();

            if (list.get(position).isSendOrReceive()) {
                View newView1 = inflater.inflate(R.layout.msg_left, parent, false);
                TextView messageTyped1 = newView1.findViewById(R.id.text_input);
                messageTyped1.setText(getItem(position).toString());
                return newView1;
            } else {
                View newView2 = inflater.inflate(R.layout.msg_right, parent, false);
                TextView messageTyped2 = newView2.findViewById(R.id.text_inputr);
                messageTyped2.setText(getItem(position).toString());
                return newView2;
            }
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







