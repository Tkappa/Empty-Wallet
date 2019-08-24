package com.example.emptywallet.Categories;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.emptywallet.Database.WalletRepository;
import com.example.emptywallet.Tags.Tag;

import java.util.List;

public class CategoryViewModel extends AndroidViewModel {
    private WalletRepository myRepository;
    private LiveData<List<Category>> myAllCategories;

    public CategoryViewModel(Application application){
        super(application);
        myRepository= new WalletRepository(application);
        myAllCategories = myRepository.getAllCategories();
    }

    public void insert(Category category){myRepository.insert(category);}

    public Category getCategoryById(int id){ return myRepository.getCategoryById(id);}

    public LiveData<List<Category>> getAllCategories(){return myAllCategories;}
}
