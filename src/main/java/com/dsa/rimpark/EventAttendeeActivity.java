package com.dsa.rimpark;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.dsa.rimpark.FireBaseSvr.AttendeeFBDB;
import com.dsa.rimpark.model.Attendee;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.Iterator;




public class EventAttendeeActivity extends AppCompatActivity {
    Bundle bundle;
    DataSnapshot eventDataSnapShot;
    int STATE_EVENT_POSITION;
    static String eventKey="";
    Fragment eventAttendeeActivityFragment;
    AttendeeFBDB attendeeFBDB;
    TextView attendedCountTV;
    TextView unAttendedCountTV;
    TextView pendingCountTV;

    private static final int READ_REQUEST_CODE = 42;

    int attendedCount;
    ChildEventListener attendedStatusListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            attendedCount++;
            setStatusText();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setStatusText();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            attendedCount--;
            setStatusText();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    int unAttendedCount;
    ChildEventListener unAttendedStatusListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            unAttendedCount++;
            setStatusText();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setStatusText();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            unAttendedCount--;
            setStatusText();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    int pendingCount;
    ChildEventListener pendingStatusListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            pendingCount++;
            setStatusText();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            setStatusText();
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {
            pendingCount--;
            setStatusText();
        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public EventAttendeeActivity()
    {


    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_attendee);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        attendedCountTV = (TextView) findViewById(R.id.attendedCountTV);
        unAttendedCountTV = (TextView) findViewById(R.id.unAttendedCountTV);
        pendingCountTV = (TextView) findViewById(R.id.pendingCountTV);

        bundle=getIntent().getExtras();
        if(bundle != null) {
            STATE_EVENT_POSITION = bundle.getInt("data_snapshot_position");
        }else if(savedInstanceState != null)
        {
            STATE_EVENT_POSITION = savedInstanceState.getInt("data_snapshot_position");
        }
        eventDataSnapShot = MainActivity.dataSnapshotList.get(STATE_EVENT_POSITION);
        eventKey=eventDataSnapShot.getKey();
        attendedCount=0;
        unAttendedCount=0;
        pendingCount=0;

        attendeeFBDB = new AttendeeFBDB(eventKey);
        attendeeFBDB.getReference().orderByChild("status").equalTo("PENDING").addChildEventListener(pendingStatusListener);
        attendeeFBDB.getReference().orderByChild("status").equalTo("ATTENDED").addChildEventListener(attendedStatusListener);
        attendeeFBDB.getReference().orderByChild("status").equalTo("UNATTENDED").addChildEventListener(unAttendedStatusListener);

        eventAttendeeActivityFragment = new EventAttendeeActivityFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.attendeeListFragContainer, eventAttendeeActivityFragment);
        transaction.commit();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                  //      .setAction("Action", null).show();
                Intent attendeeAddIntent= new Intent(getApplicationContext(), AttendeeAddActivity.class);
                attendeeAddIntent.putExtra("event_key", eventDataSnapShot.getKey());
                startActivity(attendeeAddIntent);
            }
        });

    }

    private void setStatusText()
    {

        if(attendedCountTV!=null)
        {
            attendedCountTV.setText(String.valueOf(attendedCount));
        }
        if(unAttendedCountTV!=null)
        {
            unAttendedCountTV.setText(String.valueOf(unAttendedCount));
        }
        if(pendingCountTV!=null)
        {
            pendingCountTV.setText(String.valueOf(pendingCount));
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        setStatusText();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("data_snapshot_position", STATE_EVENT_POSITION);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_event_attendee, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //Intent excelIntent = new Intent(getApplicationContext(), ImportExcel.class);
            //startActivity(excelIntent);
            // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
            // browser.
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);

            // Filter to only show results that can be "opened", such as a
            // file (as opposed to a list of contacts or timezones)
            intent.addCategory(Intent.CATEGORY_OPENABLE);

            // Filter to show only images, using the image MIME data type.
            // If one wanted to search for ogg vorbis files, the type would be "audio/ogg".
            // To search for all documents available via installed storage providers,
            // it would be "*/*".
            intent.setType("*/*");

            startActivityForResult(intent, READ_REQUEST_CODE);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri = null;
            if (data != null) {
                uri = data.getData();

                readExcelFile(this, uri);
                //Log.i(TAG, "Uri: " + uri.toString());
                //showImage(uri);
            }
        }
    }

    private void readExcelFile(Context context, Uri uri) {

        if (!isExternalStorageAvailable() || isExternalStorageReadOnly())
        {
            //Log.e(TAG, "Storage not available or read only");
            return;
        }

        try{
            // Creating Input Stream
            //File file = new File(context.getExternalFilesDir(null), filename);
            //File file;
           // file =
            //file = getContentResolver().openFileDescriptor(uri, "read");

            InputStream myInput = getContentResolver().openInputStream(uri);
            //NPOIFSFileSystem fs = new NPOIFSFileSystem(myInput);
            //XSSFWorkbook attendeeWorkbook = new XSSFWorkbook(myInput);
            Workbook attendeeWorkbook = WorkbookFactory.create(myInput);
            // Get the first sheet from workbook
            Sheet mySheet = attendeeWorkbook.getSheetAt(0);

            /** We now need something to iterate through the cells.**/
            Iterator rowIter = mySheet.rowIterator();

            int rowCount=1;
            Attendee attendee;
            while(rowIter.hasNext()){
                Row myRow = (Row) rowIter.next();
                Iterator cellIter = myRow.cellIterator();
                int coloumnCount=1;
                if(rowCount > 1) {
                    attendee = new Attendee();
                    while (cellIter.hasNext()) {
                        Cell currentCell = (Cell) cellIter.next();
                        //Log.d(TAG, "Cell Value: " +  myCell.toString());
                        //Toast.makeText(context, "cell Value: " + myCell.toString(), Toast.LENGTH_SHORT).show();
                        switch(coloumnCount)
                        {
                            case 2: //attendeeName
                                attendee.setName(currentCell.toString());
                                break;
                            case 3: //attendeeEmail
                                attendee.setEmail(currentCell.toString());
                                break;
                            case 4://mobile
                                double mobile=currentCell.getNumericCellValue();
                                try {
                                    attendee.setMobile((long)mobile);
                                }
                                catch (Exception ex){}
                                break;
                            case 5://notes
                                attendee.setNotes(currentCell.toString());
                                break;
                            case 6:
                                attendee.setCompany(currentCell.toString());
                                break;
                        }
                        coloumnCount++;
                    }
                    attendee.setStatus("PENDING");
                    attendeeFBDB.save(attendee);
                }
                rowCount++;
            }
        }catch (Exception e){e.printStackTrace(); }

        return;
    }

    public static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }

    public static boolean isExternalStorageAvailable() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
