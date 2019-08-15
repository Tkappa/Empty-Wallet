package com.example.emptywallet;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.List;

public class WalletRepository {
    private RoomDAO myRoomDao;
    private LiveData<List<Transaction>> myAllTransactions;

    WalletRepository(Application application){
        RoomDB db = RoomDB.getDatabase(application);
        myRoomDao= db.roomDAO();
        myAllTransactions = myRoomDao.getAllTransactions();

    }


    Transaction getTransactionById(int id){
        return myRoomDao.getTransactionByID(id);
    }

    LiveData<List<Transaction>> getAllTransactions(){
        return myAllTransactions;
    }

    public void insert (Transaction transaction) {
        new insertAsyncTask(myRoomDao).execute(transaction);
    }


    private static class insertAsyncTask extends AsyncTask<Transaction, Void, Void> {

        private RoomDAO mAsyncTaskDao;

        insertAsyncTask(RoomDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Transaction... params) {
            mAsyncTaskDao.insertTransaction(params[0]);
            return null;
        }
    }

    public void update (Transaction transaction) {
        new updateAsyncTask(myRoomDao).execute(transaction);
    }


    private static class updateAsyncTask extends AsyncTask<Transaction, Void, Void> {

        private RoomDAO mAsyncTaskDao;

        updateAsyncTask(RoomDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Transaction... params) {
            mAsyncTaskDao.updateTransactions(params[0]);
            return null;
        }
    }

}

