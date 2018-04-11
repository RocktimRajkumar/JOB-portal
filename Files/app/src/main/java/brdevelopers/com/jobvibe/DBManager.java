package brdevelopers.com.jobvibe;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    private static String DBName="JobDetails.db";
    private static int DBVersion=1;
    private static String Viewed="Viewed";
    private static String Saved="Saved";
    private static String Notification="Notification";
    private static String TableImage="TableImage";

    public DBManager(Context context){
        super(context,DBName,null,DBVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+Viewed+"(Auto integer primary key AUTOINCREMENT,JOB_ID text,Cemail text)");
        db.execSQL("CREATE TABLE "+Saved+"(Auto integer primary key AUTOINCREMENT,JOB_ID text,Cemail text)");
        db.execSQL("CREATE TABLE "+Notification+"(Auto integer primary key AUTOINCREMENT,JOB_ID text,Cemail text,ViewedJob text)");
        db.execSQL("CREATE TABLE "+TableImage+"(Auto integer primary key AUTOINCREMENT,Cemail text,Image blob not null)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+Viewed);
        db.execSQL("DROP TABLE IF EXISTS "+Saved);
        db.execSQL("DROP TABLE IF EXISTS "+Notification);
        db.execSQL("DROP TABLE IF EXISTS "+TableImage);
    }

    public boolean insertViewed(String jobid,String cemail){

        ContentValues cv=new ContentValues();
        cv.put("JOB_ID",jobid);
        cv.put("Cemail",cemail);

        long success=-1;
        try{
            SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
            success=sqLiteDatabase.insert(Viewed,null,cv);
        }
        catch (Exception ex){
            ex.printStackTrace();
            success=-1;
        }
        if(success!=-1)
            return true;
        else
            return false;
    }


    public boolean insertData(String jobid,String cemail){

        ContentValues cv=new ContentValues();
        cv.put("JOB_ID",jobid);
        cv.put("Cemail",cemail);

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

    public boolean insertNotifcation(String jobid,String cemail,String viewjob){

        ContentValues cv=new ContentValues();
        cv.put("JOB_ID",jobid);
        cv.put("Cemail",cemail);
        cv.put("ViewedJob",viewjob);

        long success=-1;
        try{
            SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
            success=sqLiteDatabase.insert(Notification,null,cv);
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

    public boolean insertImage(String cemail,byte[] image){

        ContentValues cv=new ContentValues();
        cv.put("Cemail",cemail);
        cv.put("Image",image);

        long success=-1;
        try{
            SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
            success=sqLiteDatabase.insert(TableImage,null,cv);
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

    public boolean isViewedExists(String jobid,String cemail){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cs=null;
        try{
            cs=db.rawQuery("Select * from "+Viewed+" where Cemail='"+cemail+"' AND JOB_ID='"+jobid+"'",null);
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
            Log.d("logcheck","is viewed "+ex);
            return  false;
        }
    }

    public boolean isSavedExists(String jobid,String cemail){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cs=null;
        try{
            cs=db.rawQuery("Select * from "+Saved+" where Cemail='"+cemail+"' AND JOB_ID='"+jobid+"'",null);
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
            Log.d("logcheck","is viewed "+ex);
            return  false;
        }
    }

    public boolean isImgExists(String cemail){
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cs=null;
        try{
            cs=db.rawQuery("Select * from "+TableImage+" where Cemail='"+cemail+"'",null);
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
            Log.d("logcheck","is viewed "+ex);
            return  false;
        }
    }

    public boolean deleteViewed(String jobid, String cemail){
        long success=1;
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            success=db.delete(Viewed,"JOB_ID=? AND Cemail=?",new String[]{jobid,cemail});
        }
        catch (Exception ex){
            success=-1;
        }
        if(success!=-1)
            return true;
        else
            return false;
    }

    public boolean deleteSaved(String jobid,String cemail){
        long success=1;
        try{
            SQLiteDatabase db=this.getWritableDatabase();
            success=db.delete(Saved,"JOB_ID=? AND Cemail=?",new String[]{jobid,cemail});
        }
        catch (Exception ex){
            success=-1;
            Log.d("logcheck","delete saved "+ex);
        }
        if(success!=-1)
            return true;
        else
            return false;
    }

    public List<JobActivity> getViewedData(String cemail){
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<JobActivity> jobv=new ArrayList<>();
        Cursor cs=null;
        try{
            cs=db.rawQuery("Select * from "+Viewed+" where Cemail='"+cemail+"'",null);
            if(cs.getCount()>0){
                while(cs.moveToNext()){
                    String jobID=cs.getString(1);
                    String candidateEmail=cs.getString(2);

                    JobActivity jobActivity=new JobActivity();
                    jobActivity.setJobid(jobID);
                    jobActivity.setCemail(candidateEmail);

                    jobv.add(jobActivity);
                }
            }
            else
                jobv=null;
            cs.close();
        }
        catch (Exception ex){
            cs.close();
            Log.d("logcheck","error "+ex);
        }
        return jobv;
    }

    public List<JobActivity> getSavedData(String cemail){
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<JobActivity> jobv=new ArrayList<>();
        Cursor cs=null;
        try{
            cs=db.rawQuery("Select * from "+Saved+" where Cemail='"+cemail+"'",null);
            if(cs.getCount()>0){
                while(cs.moveToNext()){
                    String jobID=cs.getString(1);
                    String candidateEmail=cs.getString(2);

                    JobActivity jobActivity=new JobActivity();
                    jobActivity.setJobid(jobID);
                    jobActivity.setCemail(candidateEmail);

                    jobv.add(jobActivity);
                }
            }
            else
                jobv=null;
            cs.close();
        }
        catch (Exception ex){
            cs.close();
            Log.d("logcheck","error "+ex);
        }
        return jobv;
    }

    public List<JobActivity> getNotificationData(String cemail){
        SQLiteDatabase db=this.getReadableDatabase();
        ArrayList<JobActivity> jobv=new ArrayList<>();
        Cursor cs=null;
        try{
            cs=db.rawQuery("Select * from "+Notification+" where Cemail='"+cemail+"'",null);
            if(cs.getCount()>0){
                while(cs.moveToNext()){
                    String jobID=cs.getString(1);
                    String candidateEmail=cs.getString(2);
                    String viewjob=cs.getString(3);

                    JobActivity jobActivity=new JobActivity();
                    jobActivity.setJobid(jobID);
                    jobActivity.setCemail(candidateEmail);
                    jobActivity.setViewedjob(viewjob);

                    jobv.add(jobActivity);
                }
            }
            else
                jobv=null;
            cs.close();
        }
        catch (Exception ex){
            cs.close();
            Log.d("logcheck","error "+ex);
        }
        return jobv;
    }

    public byte[] getImage(String cemail){
        SQLiteDatabase db=this.getReadableDatabase();
        byte[] img=null;
        Cursor cs=null;
        try{
            cs=db.rawQuery("Select * from "+TableImage+" where Cemail='"+cemail+"'",null);
            if(cs.getCount()>0){
                while(cs.moveToNext()) {
                    img=cs.getBlob(2);
                }
            }
            else
              img=null;
            cs.close();
        }
        catch (Exception ex){
            cs.close();
            Log.d("logcheck","error "+ex);
        }
        return img;
    }

    public boolean updateNotificationView(String jobid, String cemail,String viewNotification){

        ContentValues cv=new ContentValues();
        cv.put("JOB_ID",jobid);
        cv.put("Cemail",cemail);
        cv.put("ViewedJob",viewNotification);

        long success=-1;
        try{
            SQLiteDatabase database=this.getWritableDatabase();
            success=database.update(Notification,cv,"JOB_ID=? AND Cemail=?",new String[]{jobid,cemail});
        }
        catch (Exception ex){
            success=-1;
        }
        if(success!=-1)
            return true;
        else
            return false;

    }

    public boolean updateImage(String cemail,byte[] img){

        ContentValues cv=new ContentValues();
        cv.put("Cemail",cemail);
        cv.put("Image",img);

        long success=-1;
        try{
            SQLiteDatabase database=this.getWritableDatabase();
            success=database.update(TableImage,cv,"Cemail=?",new String[]{cemail});
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
