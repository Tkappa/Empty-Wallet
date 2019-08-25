package com.example.emptywallet.Transactions;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistoryFilter {
    private boolean checkCategories;
    private boolean excludeCategories;
    private List<Integer> categoriesToFilter;


    private boolean tagFilter;
    private boolean tagFilterExclude;
    private int[] tagFilterList;

    private boolean fromDateFilter;
    private long fromDateDate;

    private boolean toDateFilter;
    private long toDateDate;

    public TransactionHistoryFilter(){
        checkCategories=false;
        tagFilter=false;
        fromDateFilter=false;
        toDateFilter=false;
        categoriesToFilter= new ArrayList<>();
    }

    public boolean isCheckCategories() {
        return checkCategories;
    }

    public boolean isExcludeCategories() {
        return excludeCategories;
    }

    public boolean isFromDateFilter() {
        return fromDateFilter;
    }

    public boolean isTagFilter() {
        return tagFilter;
    }

    public boolean isTagFilterExclude() {
        return tagFilterExclude;
    }

    public boolean isToDateFilter() {
        return toDateFilter;
    }

    public List<Integer> getCategoriesToFilter() {
        return categoriesToFilter;
    }

    public int[] getTagFilterList() {
        return tagFilterList;
    }

    public long getFromDateDate() {
        return fromDateDate;
    }

    public long getToDateDate() {
        return toDateDate;
    }

    public void setCategoriesToFilter(int[] categoriesToFilter) {
        this.categoriesToFilter.clear();
        for (int i:categoriesToFilter){
            this.categoriesToFilter.add(new Integer(i));
        }
        Log.d("TransFiler", "set is "+new Boolean(this.categoriesToFilter==null));
    }

    public void setCheckCategories(boolean checkCategories) {
        this.checkCategories = checkCategories;
    }

    public void setExcludeCategories(boolean excludeCategories) {
        this.excludeCategories = excludeCategories;
    }

    public void setFromDateDate(long fromDateDate) {
        this.fromDateDate = fromDateDate;
    }

    public void setFromDateFilter(boolean fromDateFilter) {
        this.fromDateFilter = fromDateFilter;
    }

    public void setTagFilter(boolean tagFilter) {
        this.tagFilter = tagFilter;
    }

    public void setTagFilterExclude(boolean tagFilterExclude) {
        this.tagFilterExclude = tagFilterExclude;
    }

    public void setTagFilterList(int[] tagFilterList) {
        this.tagFilterList = tagFilterList;
    }

    public void setToDateDate(long toDateDate) {
        this.toDateDate = toDateDate;
    }

    public void setToDateFilter(boolean toDateFilter) {
        this.toDateFilter = toDateFilter;
    }

    public boolean passesFilter(Transaction trans){
        boolean returnvalue=true;

        if(checkCategories){
            if(categoriesToFilter!=null){
                Log.d("TransFiler : ", "I'm checking categories , it's not null");
                for (int i:categoriesToFilter) {
                    if(excludeCategories){
                        if(trans.getCategoryId()==i){
                            returnvalue=false;
                        }
                        Log.d("TransFiler : ", "I'm checking categories Transaction "+ trans.getTitle()+ " id :" +trans.getId()+"vs"+i);

                    }
                    else {
                        returnvalue=false;
                        if(trans.getCategoryId()==i){
                            returnvalue=true;
                        }
                    }
                }
            }
            Log.d("TransFiler : ", "After checking categories Transaction "+ trans.getTitle()+ "passes filter?"+ returnvalue);
        }

        //TODO:TAGS

        if(toDateFilter){
            if(trans.getDate().getTime()>toDateDate){
                returnvalue=false;
            }
            Log.d("TransFiler : ", "After checking toDateFilter Transaction "+ trans.getTitle()+" "+ trans.getDate()+ "passes filter?"+ returnvalue);
        }
        if(fromDateFilter){
            if(trans.getDate().getTime()<fromDateDate){
                returnvalue=false;
            }
            Log.d("TransFiler : ", "After checking fromDateFilter Transaction "+ trans.getTitle()+ "passes filter?"+ returnvalue);

        }
        Log.d("TransFiler : ", "Transaction "+ trans.getTitle()+ "passes filter?"+ returnvalue);
        return returnvalue;
    }
}
