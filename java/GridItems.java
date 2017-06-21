package com.toddlertechiez.sudokuyou;

/**
 * Created by chandan on 06-11-2016.
 */

public class GridItems {

    int number,gridNum,numRow,numCol,orgValue;
    boolean modifable= true;
    String bgcolor;
    public void setNumRow(int numRow)
    {
        this.numRow = numRow;
    }
    public int getnumRow()
    {
        return this.numRow;
    }
    public  void setOrgValue(int orgValue)
    {
        this.orgValue=orgValue;
    }
    public int getOrgValue()
    {
        return this.orgValue;
    }
    public void setnumCol(int numCol)
    {
        this.numCol = numCol;
    }
    public int getnumCol()
    {
        return this.numCol;
    }
    public void setIndex(int gridNum)
    {
        this.gridNum = gridNum;
    }
    public int getIndex()
    {
        return this.gridNum;
    }
    public void setNumber(int number)
    {
        this.number=number;
    }
    public int getNumber()
    {
        return this.number;
    }
    public void setModifable(boolean value)
    {
        this.modifable=value;
    }

    public void setItembgcolor(String bgcolor)
    {
      this.bgcolor=bgcolor;
    }
    public String getItembgcolor()
    {
        return this.bgcolor;
    }


}
