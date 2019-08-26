package com.example.emptywallet.Tags;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.emptywallet.Database.tagTransactionRelation;
import com.example.emptywallet.Transactions.Transaction;
import com.example.emptywallet.Database.WalletRepository;

import java.util.ArrayList;
import java.util.List;

public class TagsViewModel extends AndroidViewModel {

    private WalletRepository myRepository;
    private LiveData<List<Tag>> myAllTags;
    private LiveData<List<tagTransactionRelation>> myAllTagTransactionRelations;

    public TagsViewModel(Application application){
        super(application);

        myRepository = new WalletRepository(application);
        myAllTags = myRepository.getAllTags();
        myAllTagTransactionRelations= myRepository.getAllTagsTransactionRelations();
    }


    public LiveData<List<Tag>> getAllTags(){return myAllTags;}

    public void insert(Tag pTag){ myRepository.insert(pTag);}

    public Tag getTagByID(int id){
        return myRepository.getTagById(id);
    }

    /* TRANSACTION <-> TAGS */

    public void insertTransTagRelation(Transaction pTrans, Tag pTag){
        myRepository.insert(pTrans,pTag);
    }

    public List<Tag> getTagsByTransactionID(int id){
        return myRepository.getTagsByTransactionId(id);
    }

    public  List<Tag> getTagsByTransactionID(int id,List<Tag> tags, List<tagTransactionRelation> relations){
        List<Tag> returnArray= new ArrayList<>();
        for(tagTransactionRelation r: relations){
            if(r.getTransactionKey()==id){
                returnArray.add(getTagById(r.getTagKey(),tags));
            }
        }
        return returnArray;

    }

    public Tag getTagById(int id,List<Tag> tags){
        for(Tag t : tags){
            if(t.getId()==id)
                return t;
        }
        Tag error = new Tag("not found",-1);
        error.setId(-1);
        return error;

    }

    public void deleteTagFromTransaction(Transaction pTrans, Tag pTag){
        myRepository.deleteTagFromTransaction(pTrans,pTag);
    }

    public LiveData<List<tagTransactionRelation>> getAllTagTransactionRelations(){
        return  myAllTagTransactionRelations;
    }




}
