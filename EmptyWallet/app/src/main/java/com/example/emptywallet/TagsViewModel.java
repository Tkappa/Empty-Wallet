package com.example.emptywallet;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TagsViewModel extends AndroidViewModel {

    private WalletRepository myRepository;
    private LiveData<List<Tag>> myAllTags;

    public TagsViewModel(Application application){
        super(application);

        myRepository = new WalletRepository(application);
        myAllTags = myRepository.getAllTags();
    }


    LiveData<List<Tag>> getAllTags(){return myAllTags;}

    public void insert(Tag pTag){ myRepository.insert(pTag);}

    public Tag getTagByID(int id){
        return myRepository.getTagById(id);
    }

    public void insertTransTagRelation(Transaction pTrans,Tag pTag){
        myRepository.insert(pTrans,pTag);
    }

    public List<Tag> getTagsByTransactionID(int id){
        return myRepository.getTagsByTransactionId(id);
    }

    public void deleteTagFromTransaction(Transaction pTrans, Tag pTag){
        myRepository.deleteTagFromTransaction(pTrans,pTag);
    }

}
