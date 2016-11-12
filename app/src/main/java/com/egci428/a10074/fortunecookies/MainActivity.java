package com.egci428.a10074.fortunecookies;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CommentsDataSource dataSource;
    private ListView listView;
    private String[] cookies = {"gradea","lucky","surprise","panic","work"};
    private String[] cookieMsg = {"You will get A","You're Lucky","Something surprise you today","Don't Panic","Work Harder"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //DATABASE
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataSource = new CommentsDataSource(this);
        dataSource.open();
        List<Comment> values = dataSource.getAllComments();
        //ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this, android.R.layout.simple_list_item_1,values);
        final ArrayAdapter<Comment> adapter = new CommentArrayAdapter(this,0,values);

        listView = (ListView)findViewById(R.id.CookieList);
        //setListAdapter(adapter);
        listView.setAdapter(adapter);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Comment comment = (Comment) adapter.getItem(position);
                dataSource.deleteComment(comment);
                adapter.remove(comment);
                adapter.notifyDataSetChanged();
                return false;
            }
        });
        /*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Comment comment = (Comment) adapter.getItem(position);
                dataSource.deleteComment(comment);
                adapter.remove(comment);
                adapter.notifyDataSetChanged();
            }
        });
        */

        //ACTION BAR
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle intentData = getIntent().getExtras();

        System.out.println("INTENT SENT FROM: "+ getIntent().getComponent().getClassName());


        if (intentData!=null) {
            if (intentData.getString(ActivityNewCookie.cookieDate) != null) {
                System.out.println("INTENT NOT NULL" + getClass().getName());
                int newCookieID = intentData.getInt(ActivityNewCookie.cookieID);
                String newCookieDate = intentData.getString(ActivityNewCookie.cookieDate);
                Comment comment = null;
                comment = dataSource.createComment(newCookieID, newCookieDate);
                adapter.add(comment);
                adapter.notifyDataSetChanged();
                intentData.remove(ActivityNewCookie.cookieID);
                intentData.remove(ActivityNewCookie.cookieDate);
            }
        }
    }

    public void actionNewCookie(MenuItem item){
        Intent intent = new Intent(MainActivity.this,ActivityNewCookie.class);
        startActivity(intent);
    }

    class CommentArrayAdapter extends ArrayAdapter<Comment>{
        Context context;
        List<Comment> objects;

        public CommentArrayAdapter(Context context,int resource, List<Comment> objects){
            super(context,resource,objects);
            this.context = context;
            this.objects = objects;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            Comment comment = objects.get(position);
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.main_menu_cookie, null);

            ImageView image = (ImageView)view.findViewById(R.id.cookieImage);
            TextView cookieTxt = (TextView)view.findViewById(R.id.cookieText);
            TextView dateTxt = (TextView)view.findViewById(R.id.dateText);

            cookieTxt.setText(cookieMsg[comment.getCookieID()]);
            if(comment.getCookieID() > 2) cookieTxt.setTextColor(Color.RED);
            else cookieTxt.setTextColor(Color.BLUE);

            dateTxt.setText(comment.getDate());
            int res = context.getResources().getIdentifier("opened_cookie_"+cookies[comment.getCookieID()],"drawable",context.getPackageName());

            image.setImageResource(res);

            return view;
        }


    }

    @Override
    public void onResume(){
        dataSource.open();
        super.onResume();
    }

    @Override
    public void onPause(){
        dataSource.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }
}
