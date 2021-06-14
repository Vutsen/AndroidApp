package spa.sky.finapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "finapp.db";
    private static final String TABLE_SIGNUP = "contacts";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_MOBILE = "mobile";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";

    private static final String TABLE_TRANSACT = "transactions";
    private static final String COL_TID = "id";
    private static final String COL_U_ID = "uid";
    private static final String COL_TAG = "tag";
    private static final String COL_EXIN = "exin";
    private static final String COL_AMOUNT = "amount";
    private static final String COL_DATETIME = "created_at";

    private SQLiteDatabase db;
    private static final String CREATE_TABLE_SIGNUP = "CREATE TABLE " + TABLE_SIGNUP + "( " + COLUMN_ID + " INTEGER PRIMARY KEY NOT NULL , " + COLUMN_NAME + " TEXT NOT NULL , " + COLUMN_EMAIL + " TEXT NOT NULL , " + COLUMN_MOBILE + " INTEGER NOT NULL, "  + COLUMN_PASSWORD + " TEXT NOT NULL );";

    private static final String CREATE_TABLE_TRANSACTION = "CREATE TABLE " + TABLE_TRANSACT + "( " + COL_TID + " INTEGER PRIMARY KEY NOT NULL , " + COL_U_ID + " INTEGER NOT NULL " + " , " + COL_TAG + " TEXT NOT NULL , " + COL_EXIN + " INTEGER NOT NULL, " + COL_DATETIME + " DATETIME  NOT NULL, " + COL_AMOUNT + " INTEGER NOT NULL );";


    @Override
    public void onCreate(SQLiteDatabase db2) {
        db = db2;
        db.execSQL(CREATE_TABLE_SIGNUP);
        db.execSQL(CREATE_TABLE_TRANSACTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query1 = "DROP TABLE IF EXISTS " + TABLE_SIGNUP;
        String query2 = "DROP TABLE IF EXISTS " + TABLE_TRANSACT;
        db.execSQL(query1);
        db.execSQL(query2);
    }

    public DBHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void insertContact(Contact c) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, c.getName());
        values.put(COLUMN_EMAIL, c.getEmailId());
        values.put(COLUMN_MOBILE, c.getMobile());
        values.put(COLUMN_PASSWORD, c.getPassword());
        db.insert(TABLE_SIGNUP, null, values);
    }

    public void insertTransaction(Transactions t) {

        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_U_ID, t.getUid());
        values.put(COL_TAG, t.getTag());
        values.put(COL_AMOUNT, t.getAmount());
        values.put(COL_EXIN, t.getExin());
        values.put(COL_DATETIME, t.getDateTime());
        db.insert(TABLE_TRANSACT, null, values);
    }


    public List<String> searchPass(String user) {

        String u, id = null, pass = "Not Found";
        List<String> list = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_ID + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", " + COLUMN_NAME  + " FROM " + TABLE_SIGNUP;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    u = cursor.getString(1);
                    Log.i("пользователь", u);
                    if (u.equals(user)) {
                        pass = cursor.getString(2);
                        id = cursor.getString(0);
                        Utils.userName = cursor.getString(3);
                        Log.i("Пароль & UID", pass + id);
                        break;
                    }
                } while (cursor.moveToNext());
            }
            list.add(pass);
            list.add(id);
        }
        cursor.close();
        db.close();
        return list;
    }
    /* Transactions list array*/

    public ArrayList<String> getTransactions() {

        ArrayList<String> list = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "SELECT " + COL_AMOUNT + ", " + COL_TAG + " , " + COL_DATETIME + " , " + COL_EXIN + ", " + COL_U_ID + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " ORDER BY " + COL_DATETIME + " DESC ;";
        Cursor cursor = db.rawQuery(query, null);

        String tag, amount, exin, uid, timeA, timeB;
        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                do {
                    tag = cursor.getString(1);
                    amount = cursor.getString(0);
                    timeA = cursor.getString(2);
                    exin = cursor.getString(3);
                    uid = cursor.getString(4);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date sourceDate = null;
                    try {
                        sourceDate = dateFormat.parse(timeA);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat targetFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
                    assert sourceDate != null;
                    timeB = targetFormat.format(sourceDate);

                    Log.i("этоТранзакции", tag + " " + amount + " " + timeB + " " + exin + " " + uid);
                    if ("0".equals(exin)) {
                        amount = "- ₽ " + amount;
                    } else
                        amount = " ₽ " + amount;
                    if (tag != null)
                        list.add("\n" + tag + "\n" + amount + "\n" + timeB + "\n");
                } while (cursor.moveToNext());
            }
        }
        db.close();
        cursor.close();
        return list;
    }

    public ArrayList<String> getDateTransaction( String start, String end) {
        ArrayList<String> list = new ArrayList<>();
        db = this.getReadableDatabase();
        String query = "SELECT " + COL_AMOUNT + ", " + COL_TAG + " , " + COL_DATETIME + " , " + COL_EXIN + ", " + COL_U_ID + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_DATETIME +" BETWEEN "+"'"+ start + "'"  +" AND "+"'"+ end +"'"+" ORDER BY " + COL_DATETIME + " DESC ;";
        Cursor cursor = db.rawQuery(query, null);
        String tag, amount, exin, uid, timeA, timeB;
        if (cursor.moveToFirst()) {
            if (cursor.getCount() > 0) {
                do {
                    tag = cursor.getString(1);
                    amount = cursor.getString(0);
                    timeA = cursor.getString(2);
                    exin = cursor.getString(3);
                    uid = cursor.getString(4);


                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    Date sourceDate = null;
                    try {
                        sourceDate = dateFormat.parse(timeA);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    SimpleDateFormat targetFormat = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault());
                    assert sourceDate != null;
                    timeB = targetFormat.format(sourceDate);

                    Log.i("ЭтоТранзакции", tag + " " + amount + " " + timeB + " " + exin + " " + uid);
                    if ("0".equals(exin)) {
                        amount = "- ₽ " + amount;
                    } else
                        amount = " ₽ " + amount;
                    if (tag != null)
                        list.add("\n" + tag + "\n" + amount + "\n" + timeB + "\n");
                } while (cursor.moveToNext());
            }
        }
        db.close();
        cursor.close();
        return list;
    }


    public void setIncomeExpenses() {
        db = this.getReadableDatabase();

        String query2 = "SELECT " + " SUM(" + COL_AMOUNT + ")" + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_EXIN + " = 1 AND " + COL_DATETIME + " > datetime('now', 'start of month');";
        Cursor c1 = db.rawQuery(query2, null);

        if (c1.moveToFirst()) {
            String income = c1.getString(0);
            if (income != null) {
                Utils.income = Integer.parseInt(income);
                Log.i("ДОХОДЫ", String.valueOf(Utils.income));
            }

        }
        c1.close();

        String query3 = "SELECT " + " SUM(" + COL_AMOUNT + ")" + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_EXIN + " = 0 AND " + COL_DATETIME + " > datetime('now', 'start of month');";
        Cursor c2 = db.rawQuery(query3, null);

        if (c2.moveToFirst()) {
            String expense = c2.getString(0);
            if (expense != null)
                Utils.expense = Integer.parseInt(expense);
            Log.i("РАСХОДЫ", String.valueOf(Utils.expense));
        }
        c2.close();
    }

    public void setDateIncomeExpenses(String start, String end) {
        db = this.getReadableDatabase();

        String query5 = "SELECT " + " SUM(" + COL_AMOUNT + ")" + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_EXIN + " = 1 AND " + COL_DATETIME + " BETWEEN "+"'"+ start + "'"  +" AND "+"'"+ end +"';";
        Cursor c1 = db.rawQuery(query5, null);

        if (c1.moveToFirst()) {
            String income = c1.getString(0);
            if (income != null) {
                Utils.dateincome = Integer.parseInt(income);
                Log.i("ДОХОДЫ", String.valueOf(Utils.income));
            }

        }
        c1.close();

        String query6 = "SELECT " + " SUM(" + COL_AMOUNT + ")" + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_EXIN + " = 0 AND " + COL_DATETIME + " BETWEEN "+"'"+ start + "'"  +" AND "+"'"+ end +"';";
        Cursor c2 = db.rawQuery(query6, null);

        if (c2.moveToFirst()) {
            String expense = c2.getString(0);
            if (expense != null)
                Utils.dateexpense = Integer.parseInt(expense);
            Log.i("РАСХОДЫ", String.valueOf(Utils.expense));
        }
        c2.close();
    }

    public HashMap<String, Integer> getExpenses() {

        db = this.getReadableDatabase();

        HashMap<String, Integer> list = new HashMap<>();

        String query = "SELECT " + " SUM(" + COL_AMOUNT + ")" + ", " + COL_TAG + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_EXIN + " = 0  AND " + COL_DATETIME + " > datetime('now', 'start of month')" + " GROUP BY " + COL_TAG;
        Cursor cursor = db.rawQuery(query, null);
        String tag, amount;
        if (cursor.moveToFirst()) {
            do {
                tag = cursor.getString(1);
                amount = cursor.getString(0);
                list.put(tag, Integer.parseInt(amount));
                Log.i("ЭтоРасходы", tag + " " + amount);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return list;
    }

    public HashMap<String, Integer> getDateExpenses(String start, String end){
        db = this.getReadableDatabase();

        HashMap<String, Integer> list = new HashMap<>();

        String query = "SELECT " + " SUM(" + COL_AMOUNT + ")" + ", " + COL_TAG + " FROM " + TABLE_TRANSACT + " WHERE " + COL_U_ID + " = " + Utils.userId + " AND " + COL_EXIN + " = 0  AND " + COL_DATETIME + " BETWEEN "+ "'"+ start + "'" +" AND "+"'"+ end +"'"+  " GROUP BY " + COL_TAG;
        Cursor cursor = db.rawQuery(query, null);
        String tag, amount;
        if (cursor.moveToFirst()) {
            do {
                tag = cursor.getString(1);
                amount = cursor.getString(0);
                list.put(tag, Integer.parseInt(amount));
                Log.i("ЭтоРасходы", tag + " " + amount);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return list;

    }

    public ArrayList<Transactions> getTransactionsPdf () {

        ArrayList<Transactions> list = new ArrayList<> ();
        db = this.getReadableDatabase ();
        Transactions t;
        String query = "SELECT " + COL_AMOUNT + ", " + COL_TAG + " , " + COL_DATETIME +" , " + COL_EXIN + ", " + COL_U_ID +" FROM " + TABLE_TRANSACT +" WHERE " + COL_U_ID + " = " + Utils.userId + " ORDER BY " + COL_DATETIME + " DESC ;" ;
        Cursor cursor = db.rawQuery (query, null);

        String tag, amount, exin, uid, timeA, timeB;
        if (cursor.moveToFirst ()) {
            do {
                t = new Transactions ();
                tag = cursor.getString (1);
                amount = cursor.getString (0);
                timeA = cursor.getString (2);
                exin = cursor.getString (3);
                uid = cursor.getString (4);


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault ());
                Date sourceDate = null;
                try {
                    sourceDate = dateFormat.parse(timeA);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault ());
                timeB = targetFormat.format(sourceDate);

                Log.i("PreethisTransaction",tag +" " + amount+ " " + timeB+" " + exin + " "+uid );
                t.setUid (Integer.parseInt (uid));
                t.setTag (tag);
                t.setExin (Integer.parseInt (exin));
                t.setAmount (Integer.parseInt (amount));
                t.setCreated_at (timeB);
                list.add (t);
            }while(cursor.moveToNext ());
        }
        db.close ();
        cursor.close ();
        return list;
    }

    public String getEmail () {
        db = this.getReadableDatabase ();
        String query = "SELECT "+ COLUMN_EMAIL + " FROM "  + TABLE_SIGNUP +" WHERE " + COLUMN_ID + " = " + Utils.userId ;
        Cursor cursor = db.rawQuery(query, null);
        String text = null;
        if (cursor.moveToFirst ()) {
            do {
            text = cursor.getString(0);
            }while(cursor.moveToNext ());
        }
        db.close ();
        return text;
    };
    
}