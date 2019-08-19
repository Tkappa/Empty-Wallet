package com.example.emptywallet;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TransactionsViewModel extends AndroidViewModel {

    private WalletRepository myRepository;
    private LiveData<List<Transaction>> myAllTransactions;
    private Transaction getTransactionByIDResult;
    public TransactionsViewModel(Application application){
        super(application);
        myRepository = new WalletRepository(application);
        myAllTransactions = myRepository.getAllTransactions();
    }

    LiveData<List<Transaction>> getAllTransactions(){
        return myAllTransactions;
    }

    Integer getTotalAmountSpent(int displaySetting){
        Integer totalamount=0;
        List<Transaction> temp = myAllTransactions.getValue();
        Calendar beginning;
        Calendar end;



        Date today = new Date();
        beginning = Calendar.getInstance();
        beginning.setTime(today);
        end = Calendar.getInstance();
        end.setTime(today);
        switch (displaySetting){
            case Constants.DISPLAY_SHOWDAY:


                beginning.set(Calendar.HOUR_OF_DAY,0);
                beginning.set(Calendar.MINUTE,0);
                beginning.set(Calendar.SECOND,0);

                end.setTime(beginning.getTime());
                end.add(Calendar.DAY_OF_MONTH,1);


                break;
            case Constants.DISPLAY_SHOWWEEK:


                beginning.set(Calendar.HOUR_OF_DAY,0);
                beginning.set(Calendar.MINUTE,0);
                beginning.set(Calendar.SECOND,0);
                beginning.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);

                end.setTime(beginning.getTime());
                end.add(Calendar.WEEK_OF_MONTH,1);
                break;
            case Constants.DISPLAY_SHOWMONTH:
                beginning.set(Calendar.HOUR_OF_DAY,0);
                beginning.set(Calendar.MINUTE,0);
                beginning.set(Calendar.SECOND,0);
                beginning.set(Calendar.DAY_OF_MONTH,1);

                end.setTime(beginning.getTime());
                end.add(Calendar.MONTH,1);
                break;
            default:
                break;
        }
        for (int i =0;i<temp.size();i++){
            Transaction curr = temp.get(i);
            Calendar currDate = Calendar.getInstance();
            currDate.setTime(curr.getDate());
            if(currDate.after(beginning)&&currDate.before(end) && curr.getIsPurchase()){
                totalamount+=curr.getAmount();
            }
        }
        return totalamount;
    }

    public void insert(Transaction transaction){ myRepository.insert(transaction);}
    public void update (Transaction transaction){ myRepository.update(transaction);}
    public Transaction getTransactionByID(int id){
        getTransactionByIDResult= myRepository.getTransactionById(id);
        return getTransactionByIDResult;
    }
    public Transaction getGetTransactionByIDResult(int id){
        if (getTransactionByIDResult.getId()==id){
            return getTransactionByIDResult;
        }
        else{
            return null;
        }
    }
}
