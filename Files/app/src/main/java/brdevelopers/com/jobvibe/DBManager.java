package brdevelopers.com.jobvibe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBManager extends SQLiteOpenHelper {

    private static String DBName="JobDetails.db";
    private static int DBVersion=1;
    private static String Viewed="Viewed";
    private static String Saved="Saved";

    public DBManager(Context context){
        super(context,DBName,null,DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+Viewed+"(Auto primary key AUTOINCREMENT,JOB_ID text,JOB_Title text)");
        db.execSQL("CREATE TABLE "+Saved+"(Auto primary key AUTOINCREMENT,JOB_ID text,JOB_Title text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Viewed);
        db.execSQL("DROP TABLE IF EXISTS "+Saved);
    }

    public boolean insertViewed(String jobid,String jobtitle){

        ContentValues cv=new ContentValues();
        cv.put("JOB_ID",jobid);
        cv.put("JOB_Title",jobtitle);

        long success=-1;
        try{
            SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
            success=sqLiteDatabase.insert(Viewed,null,cv);
        }
        catch (Exception ex){
            ex.printStackTrace();
            success=-1;
        }
        if(success!=1)
            return true;
        else
            return false;
    }


    public boolean insertData(String jobid,String jobtitle){

        ContentValues cv=new ContentValues();
        cv.put("JOB_ID",jobid);
        cv.put("JOB_Title",jobtitle);

        long success=-1;
        try{
            SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
            success=sqLiteDatabase.insert(Saved,null,cv);
        }
        catch (Exception ex){
            ex.printStackTrace();
            success=-1;
        }
        if(success!=1)
            return true;
        else
            return false;
    }

    public boolean isViewedExists(String jobid){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cs=null;
        try{
            cs=db.rawQuery("Select * from "+Viewed+" where JOB_ID='"+jobid+"'",null);
            if(cs.getCount()>0){
                cs.close();
                return true;
            }
            else{
                cs.close();
                return false;
            }
        }
        catch (Exception ex){
            cs.close();
            return  false;
        }
    }

    public boolean isSavedExists(String jobid){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cs=null;
        try{
            cs=db.rawQuery("Select * from "+Saved+" where JOB_ID='"+jobid+"'",null);
            if(cs.getCount()>0){
                cs.close();
                return true;
            }
            else{
                cs.close();
                return false;
            }
        }
        catch (Exception ex){
            cs.close();
            return  false;
        }
    }

    public boolean deleteViewed(String jobid){
        long success=1;
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            success=db.delete(Viewed,"JOB_ID=?",new String[]{jobid});
        }
        catch (Exception ex){
            success=-1;
        }
        if(success!=-1)
            return true;
        else
            return false;
    }

    public boolean deleteSaved(String jobid){
        long success=1;
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            success=db.delete(Viewed,"JOB_ID=?",new String[]{jobid});
        }
        catch (Exception ex){
            success=-1;
        }
        if(success!=-1)
            return true;
        else
            return false;
    }

}
