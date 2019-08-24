package com.example.emptywallet;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WalletRepository {
    private RoomDAO myRoomDao;
    private LiveData<List<Transaction>> myAllTransactions;

    private LiveData<List<Tag>> myAllTags;
    private List<Tag> myAllTagsSync;

    WalletRepository(Application application){
        RoomDB db = RoomDB.getDatabase(application);
        myRoomDao= db.roomDAO();
        myAllTransactions = myRoomDao.getAllTransactions();
        myAllTags = myRoomDao.getAllTags();
        myAllTagsSync = myAllTags.getValue();
    }


    /* TRANSACTIONS */
    public void insert (Transaction transaction) {
        new insertTransactionAsyncTask(myRoomDao).execute(transaction);
    }

    public void insert (Transaction transaction,List<Tag> tags) {
        new insertTransactionThenTags(myRoomDao,tags).execute(transaction);
    }

    public void update (Transaction transaction) {
        new updateTransactionAsyncTask(myRoomDao).execute(transaction);
    }

    public Transaction getTransactionById(int id){
        return myRoomDao.getTransactionByID(id);
    }

    public LiveData<List<Transaction>> getAllTransactions(){
        return myAllTransactions;
    }

    private class insertTransactionThenTags extends AsyncTask<Transaction, Void, Long> {

        private List<Tag> toInsert;

        private RoomDAO mAsyncTaskDao;

        insertTransactionThenTags(RoomDAO dao,List<Tag> pToInsert) {
            toInsert=pToInsert;
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final Transaction... params) {
            return mAsyncTaskDao.insertTransaction(params[0]);
        }

        @Override
        protected void onPostExecute (final Long result){
            int transId = result.intValue();
            for(int i =0;i<toInsert.size();i++){
                tagTransactionRelation rel = new tagTransactionRelation(transId,toInsert.get(i).getId());
                new insertTransTagRelAsyncTask(myRoomDao).execute(rel);
            }
        }
    }


    /* TAGS */

    public void insert (Tag pTag){
        new insertTagAsyncTask(myRoomDao).execute(pTag);

    }

    public LiveData<List<Tag>> getAllTags(){
        if(myAllTags==null){
            myAllTags=myRoomDao.getAllTags();
        }
        return myAllTags;
    }

    public Tag getTagById(int id){
        return myRoomDao.getTagByID(id);
    }

    /* TRANSACTIONS <-> TAGS */
    public void insert (Transaction pTrans, Tag pTag){
        tagTransactionRelation temp = new tagTransactionRelation(pTrans.getId(),pTag.getId());

        new insertTransTagRelAsyncTask(myRoomDao).execute(temp);
    }

    public List<Tag> getTagsByTransactionId(int id){
        List<Tag> temp = myRoomDao.getAllTagsFromTransaction(id);
        Log.d("Hello", "getTagsByTransactionId: " + temp.size());
        return temp;
    }

    public void deleteTagFromTransaction(Transaction Trans, Tag pTag){
        new deleteTagFromTransactionAsyncTask(myRoomDao,pTag).execute(Trans);
    }



    /* ASYNC TASKS */
    private static class insertTransTagRelAsyncTask extends AsyncTask<tagTransactionRelation, Void, Void> {

        private RoomDAO mAsyncTaskDao;

        insertTransTagRelAsyncTask(RoomDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final tagTransactionRelation... params) {
            mAsyncTaskDao.insertTagTransactionRelation(params[0]);

            return null;
        }
    }

    private static class insertTagAsyncTask extends AsyncTask<Tag, Void, Void> {

        private RoomDAO mAsyncTaskDao;

        insertTagAsyncTask(RoomDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Tag... params) {
            mAsyncTaskDao.insertTag(params[0]);
            return null;
        }
    }

    private static class insertTransactionAsyncTask extends AsyncTask<Transaction, Void, Void> {

        private RoomDAO mAsyncTaskDao;

        insertTransactionAsyncTask(RoomDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Transaction... params) {
            mAsyncTaskDao.insertTransaction(params[0]);
            return null;
        }
    }

    private static class updateTransactionAsyncTask extends AsyncTask<Transaction, Void, Void> {

        private RoomDAO mAsyncTaskDao;

        updateTransactionAsyncTask(RoomDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Transaction... params) {
            mAsyncTaskDao.updateTransactions(params[0]);
            return null;
        }
    }

    private static class deleteTagFromTransactionAsyncTask extends AsyncTask<Transaction, Void, Void> {

        private RoomDAO mAsyncTaskDao;
        Tag toDelete;

        deleteTagFromTransactionAsyncTask(RoomDAO dao,Tag pTag) {
            mAsyncTaskDao = dao;
            toDelete=pTag;
        }

        @Override
        protected Void doInBackground(final Transaction... params) {
            mAsyncTaskDao.deleteTagFromTransaction(params[0].getId(),toDelete.getId());
            return null;
        }
    }

}

