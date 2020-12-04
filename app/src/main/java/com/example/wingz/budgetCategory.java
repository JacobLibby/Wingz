package com.example.wingz;

public class budgetCategory {
    private String title;
    private float amountSpent;
    private float amountBudgeted;

    // parameterized constructor
    public budgetCategory(String tit, float amountB, float amountS){
        this.title = tit;
        this.amountBudgeted = amountB;
        this.amountSpent = amountS;
    }

    public budgetCategory(String tit, float amountB){
        this.title = tit;
        this.amountBudgeted = amountB;
        this.amountSpent = 0;
    }


    // unparameterized constructor
    public budgetCategory(){
        this.title = "NO CATEGORY NAME";
        this.amountBudgeted = 0;
        this.amountSpent = 0;
    }

    // Getters
    public String getTitle()        { return this.title;          }
    public float getAmountBudgeted(){ return this.amountBudgeted; }
    public float getAmountSpent()   { return this.amountSpent;    }

    // setters
    public void setTitle(String tit)             { this.title = tit;              }
    public void setAmountBudgeted(float amountB) { this.amountBudgeted = amountB; }
    public void setAmountSpent(float amountS)    { this.amountSpent = amountS;    }
}
