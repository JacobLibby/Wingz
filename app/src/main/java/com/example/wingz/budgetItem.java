package com.example.wingz;

import java.util.Date;

public class budgetItem {
    private String category;
    private String title;
    private float amountSpent;
    private Date date;


    // parameterized constructor
    public budgetItem(String cat, String tit, float amountS, Date d){
        this.category = cat;
        this.title = tit;
        this.amountSpent = amountS;
        this.date = d;
    }

    // unparameterized constructor
    public budgetItem(){
        this.category = "NO CATEGORY";
        this.title = "NO TITLE";
        this.amountSpent = 0;
    }

    // getters
    public String getCategory()      { return this.category;       }
    public String getTitle()         { return this.title;          }
    public float getAmountSpent()    { return this.amountSpent;    }
    public Date getDate()            { return this.date;           }

    // setters
    public void setCategory(String cat)         { this.category = cat;          }
    public void setTitle(String tit)            { this.title = tit;             }
    public void setAmountSpent(float amountS)   { this.amountSpent = amountS;   }
    public void setDate(Date d)                 { this.date = d;                }
}
