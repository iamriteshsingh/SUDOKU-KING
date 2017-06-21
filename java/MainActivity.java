package com.toddlertechiez.sudokuyou;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity implements View.OnClickListener {

    public static int level[] = new int[]{10, 15, 20, 25, 30, 35, 40, 45, 50, 55};
    public static int currentSolution[];
    public static int orgMat[][] = new int[10][10];
    static int a[][] = new int[10][10];
    static boolean b[][] = new boolean[10][10];
    
    static SudokuAdapter adapter;
    static GridItems chosenItem, prevItem;
    static Context mContext;
    static String col2 = "#FFB37212", col1 = "#ffffff";
    private Handler handler;
    int sec, min;
    private TextView timer;
    private Button level_button;
    Runnable runnable;
    public static ArrayList<GridItems> mlist;
    LinearLayout linear_layout;
    ImageView imageView;
    int cs = 1 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
         linear_layout = (LinearLayout)findViewById(R.id.upperll);

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);

//        AdRequest adRequest = new AdRequest.Builder()
//
//                // Add a test device to show Test Ads
//                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
//                .addTestDevice("DFE5C08C6D9134AB87D01D1A1431C144") //Random Text
//                .build();
//        // Load ads into Banner Ads
//        adView.loadAd(adRequest);


        mContext = this.getApplicationContext();

        GridView gridView = (GridView) findViewById(R.id.gridview);
        timer = (TextView) findViewById(R.id.timer);
        imageView=(ImageView)findViewById(R.id.imageView);
        level_button= (Button) findViewById(R.id.button_level);
        level_button.setOnClickListener(this);
        level_button.setText("Toddler");
        mlist = new ArrayList<GridItems>();

        quesGenerator();

        Button button = (Button) findViewById(R.id.submit_button);
        button.setOnClickListener(this);

        String c = col1;
        int m = -1, n = -1, k = 0;
        int index[] = new int[]{
                1, 1, 1, 2, 2, 2, 3, 3, 3,
                1, 1, 1, 2, 2, 2, 3, 3, 3,
                1, 1, 1, 2, 2, 2, 3, 3, 3,
                4, 4, 4, 5, 5, 5, 6, 6, 6,
                4, 4, 4, 5, 5, 5, 6, 6, 6,
                4, 4, 4, 5, 5, 5, 6, 6, 6,
                7, 7, 7, 8, 8, 8, 9, 9, 9,
                7, 7, 7, 8, 8, 8, 9, 9, 9,
                7, 7, 7, 8, 8, 8, 9, 9, 9};
        for (int i = 1; i <= 9; i++) {
            m++;
            if (m % 3 != 0)          // GRID_BLACK & GRID_GRAY CONSTANTS
                if (c.equals(col1))
                    c = col2;
                else if (c.equals(col2))
                    c = col1;
            for (int j = 1; j <= 9; j++) {
                n++;
                if (n % 3 == 0)
                    if (c.equals(col1))
                        c = col2;
                    else if (c.equals(col2))
                        c = col1;
                GridItems gridItem = new GridItems();
                gridItem.setItembgcolor(c);
                gridItem.setNumber(a[i][j]);
                gridItem.setIndex(index[k]);
                gridItem.setNumRow(i);
                gridItem.setnumCol(j);
                gridItem.setOrgValue(orgMat[i][j]);
                k++;
                if (a[i][j] != 0)
                    gridItem.setModifable(false);
                mlist.add(gridItem);
            }
        }

        adapter = new SudokuAdapter(this, mlist);
        gridView.setAdapter(adapter);

        handler = new Handler();
         runnable = new Runnable() {
            @Override
            public void run() {

                if (sec == 60) {
                    min++;
                    sec = 0;
                }
                String s = String.format(" %02d : %02d",min,sec);
                timer.setText(s);
                sec = sec + 1;
                switch(cs)
                {
                    case 1 :
                        imageView.setImageResource(R.drawable.clock_12);
                        cs = 2 ;
                        break;
                    case 2 :
                        imageView.setImageResource(R.drawable.clock_03);
                        cs = 3 ;
                        break;
                    case 3 :
                        imageView.setImageResource(R.drawable.clock_06);
                        cs = 4 ;
                        break;
                    case 4 :
                        imageView.setImageResource(R.drawable.clock_09);
                        cs = 1 ;
                        break;
                }

                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);


    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(runnable);
        mlist.clear();
        super.onDestroy();
    }

    public void quesGenerator() {

        int Array[][] = {
                {4, 2, 9, 3, 1, 6, 5, 7, 8, 8, 6, 7, 5, 2, 4, 1, 9, 3, 5, 1, 3, 8, 9, 7, 2, 4, 6, 9, 3, 1, 7, 8, 5, 6, 2, 4, 6, 8, 2, 9, 4, 1, 7, 3, 5, 7, 4, 5, 2, 6, 3, 9, 8, 1, 3, 5, 4, 6, 7, 2, 8, 1, 9, 1, 7, 8, 4, 5, 9, 3, 6, 2, 2, 9, 6, 1, 3, 8, 4, 5, 7},
                {9, 6, 5, 4, 1, 8, 7, 3, 2, 1, 4, 3, 2, 6, 7, 9, 5, 8, 8, 2, 7, 9, 5, 3, 6, 1, 4, 5, 7, 9, 3, 8, 4, 1, 2, 6, 4, 1, 2, 6, 9, 5, 3, 8, 7, 6, 3, 8, 1, 7, 2, 4, 9, 5, 3, 5, 4, 7, 2, 1, 8, 6, 9, 7, 8, 6, 5, 3, 9, 2, 4, 1, 2, 9, 1, 8, 4, 6, 5, 7, 3},
                {1, 2, 5, 8, 9, 7, 6, 3, 4, 6, 7, 4, 5, 1, 3, 9, 2, 8, 3, 9, 8, 4, 2, 6, 1, 5, 7, 4, 8, 2, 6, 5, 9, 7, 1, 3, 7, 6, 9, 2, 3, 1, 4, 8, 5, 5, 3, 1, 7, 8, 4, 2, 9, 6, 2, 4, 3, 9, 7, 5, 8, 6, 1, 9, 5, 6, 1, 4, 8, 3, 7, 2, 8, 1, 7, 3, 6, 2, 5, 4, 9},
                {5, 2, 8, 6, 7, 3, 9, 1, 4, 4, 1, 3, 9, 2, 5, 8, 6, 7, 7, 6, 9, 4, 1, 8, 3, 2, 5, 1, 3, 5, 2, 6, 7, 4, 8, 9, 9, 8, 6, 1, 5, 4, 2, 7, 3, 2, 7, 4, 8, 3, 9, 6, 5, 1, 6, 4, 2, 7, 9, 1, 5, 3, 8, 8, 5, 7, 3, 4, 6, 1, 9, 2, 3, 9, 1, 5, 8, 2, 7, 4, 6}};
        
            Random rand = new Random();
            int ar = rand.nextInt(3)+1;
            System.out.println("Array current" + ar);
            display(Array[ar]);
            currentSolution = Array[ar];
            int mix = rand.nextInt(3) + 1;
            while (mix > 0) {
                int t = rand.nextInt(6) + 1;
                transformSudoku(t);
                mix--;
            }
            System.out.println("\nafter transformation  currentSolution\n");
            display(currentSolution);
            int  k = 0;

            int lv = getLevel();
            currentSolution = digHoles(currentSolution, lv);
            System.out.println("\nafter digholes  currentSolution\n");
            display(currentSolution);

            
            for (int i = 1; i < 10; i++)
                for (int j = 1; j < 10; j++) {
                    a[i][j] = currentSolution[k];
                    if (a[i][j] == 0) b[i][j] = true;
                    else b[i][j] = false;
                    k++;
                }
      

        System.out.println("COMPLETE");


    }

    public  void doSubGridColor()  
    {
        String c = col1;
        int m = -1, n = -1, k = 0;
        int index[] = new int[]{
                1, 1, 1, 2, 2, 2, 3, 3, 3,
                1, 1, 1, 2, 2, 2, 3, 3, 3,
                1, 1, 1, 2, 2, 2, 3, 3, 3,
                4, 4, 4, 5, 5, 5, 6, 6, 6,
                4, 4, 4, 5, 5, 5, 6, 6, 6,
                4, 4, 4, 5, 5, 5, 6, 6, 6,
                7, 7, 7, 8, 8, 8, 9, 9, 9,
                7, 7, 7, 8, 8, 8, 9, 9, 9,
                7, 7, 7, 8, 8, 8, 9, 9, 9};
        for (int i = 1; i <= 9; i++) {
            m++;
            if (m % 3 != 0)          // GRID_BLACK & GRID_GRAY CONSTANTS
                if (c.equals(col1))
                    c = col2;
                else if (c.equals(col2))
                    c = col1;
            for (int j = 1; j <= 9; j++) {
                n++;
                if (n % 3 == 0)
                    if (c.equals(col1))
                        c = col2;
                    else if (c.equals(col2))
                        c = col1;
                GridItems gridItem = new GridItems();
                gridItem.setItembgcolor(c);
                gridItem.setNumber(a[i][j]);
                gridItem.setIndex(index[k]);
                gridItem.setNumRow(i);
                gridItem.setnumCol(j);
                gridItem.setOrgValue(orgMat[i][j]);
                k++;
                if (a[i][j] != 0)
                    gridItem.setModifable(false);
                mlist.add(gridItem);
            }
        }

        min=0;sec=0;
        adapter.notifyDataSetChanged();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {

                if (sec == 60) {
                    min++;
                    sec = 0;
                }

                String s = String.format(" %02d : %02d",min,sec);
                timer.setText(s);
                sec = sec + 1;
                switch(cs)
                {
                    case 1 :
                        imageView.setImageResource(R.drawable.clock_12);
                        cs = 2 ;
                        break;
                    case 2 :
                        imageView.setImageResource(R.drawable.clock_03);
                        cs = 3 ;
                        break;
                    case 3 :
                        imageView.setImageResource(R.drawable.clock_06);
                        cs = 4 ;
                        break;
                    case 4 :
                        imageView.setImageResource(R.drawable.clock_09);
                        cs = 1 ;
                        break;
                }
                handler.postDelayed(this, 1000);
            }
        };
        handler.postDelayed(runnable, 1000);
    }
    public int getLevel()
    {
        int l=0;
        String s=level_button.getText().toString();
        if(s.equals("Toddler")) l=0;
        else if(s.equals("Rookie"))l=1;
        else if(s.equals("Master"))l=2;
        else if(s.equals("Professional"))l=3;
        else if(s.equals("Ultimate"))l=4;
        else if(s.equals("Expert"))l=5;
        else if(s.equals("Devil"))l=6;
        else if(s.equals("Genius"))l=7;
        else if(s.equals("Boss"))l=8;

        return  l;

    }


    private static void transformSudoku(int s) {
        int tmp, tmpIx, mod9, div9;
        int i;
        Random rand = new Random();
        switch (s) {
            case 1:   //digitswap
                int a, b;
                do {
                    a = rand.nextInt((9 - 1) + 1) + 1;
                    b = rand.nextInt((9 - 1) + 1) + 1;
                } while (a == b);
                for (i = 0; i < 81; i++) {
                    if (currentSolution[i] == a)
                        currentSolution[i] = b;
                    else if (currentSolution[i] == b)
                        currentSolution[i] = a;

                }
                break;
            case 2:      //rotate
                int r = rand.nextInt((3 - 1) + 1) + 1;
                System.out.println("r : " + r);
                while (r > 0) {
                    currentSolution = rotate(currentSolution);
                    r--;
                }
                break;
            case 3:  //vertical
                for (i = 0; i < 81; i++) {
                    if (i % 9 < 4) {
                        tmp = currentSolution[i];
                        div9 = (int) Math.floor((double) (i / 9));
                        tmpIx = (9 * div9 + 8) - (i - (9 * div9));
                        currentSolution[i] = currentSolution[tmpIx];
                        currentSolution[tmpIx] = tmp;
                    }
                }
                break;
            case 4: //mainDiagonal
                for (i = 0; i < 81; i++) {
                    //
                    // Upper Main diagonal: row + column < 8
                    //
                    if ((Math.floor(i / 9) + i % 9) < 8) {
                        mod9 = (int) Math.floor(i % 9);
                        div9 = (int) Math.floor(i / 9);
                        tmp = currentSolution[i];
                        tmpIx = (8 - mod9) * 9 + 8 - div9;
                        currentSolution[i] = currentSolution[tmpIx];
                        currentSolution[tmpIx] = tmp;
                    }
                }
                break;
            case 5:  //minorDiagonal
                for (i = 0; i < 81; i++) {
                    //
                    // Upper Minor diagonal: row > column
                    //
                    if (Math.floor(i / 9) > i % 9) {
                        mod9 = (int) Math.floor(i % 9);
                        div9 = (int) Math.floor(i / 9);
                        tmp = currentSolution[i];
                        tmpIx = div9 + mod9 * 9;
                        currentSolution[i] = currentSolution[tmpIx];
                        currentSolution[tmpIx] = tmp;
                    }
                }
                break;
            case 6: //horizontal
            default:
                for (i = 0; i < 81; i++) {
                    //
                    // Row < 4
                    //
                    if (Math.floor(i / 9) < 4) {
                        mod9 = (int) Math.floor(i % 9);
                        div9 = (int) Math.floor(i / 9);
                        tmp = currentSolution[i];
                        tmpIx = mod9 + (8 - div9) * 9;
                        currentSolution[i] = currentSolution[tmpIx];
                        currentSolution[tmpIx] = tmp;
                    }
                }
                break;
        }
    }

    public static int[] rotate(int a[]) {
        int j = 0, i = 0, k = 0;
        int temp[][] = new int[9][9];
        int temp1[][] = new int[9][9];
        for (i = 0; i < 9; i++)
            for (j = 0; j < 9; j++) {
                temp[i][j] = a[k];
                k++;
            }
        for (i = 0; i < 9; i++)
            for (j = 0; j < 9; j++) {
                temp1[i][j] = temp[9 - j - 1][i];
            }
        k = 0;
        for (i = 0; i < 9; i++)
            for (j = 0; j < 9; j++) {
                a[k] = temp1[i][j];
                k++;
            }
        return a;
    }

    public static void display(int a[]) {
        int j = 0;
        for (int i = 0; i < 81; i++) {
            System.out.print(a[i] + " ");
            if (j == 8) {
                j = 0;
                System.out.println("");
            } else
                j++;
        }
    }

    public static int[] digHoles(int a[], int lv) {
        int l, x;
        l = level[lv];
        ArrayList<Integer> digList = new ArrayList<>() ;
        while (l > 0) {
            Random rand = new Random();
            x = rand.nextInt((80 - 1) + 1) + 1;
            if(!digList.contains(x))
            {
                digList.add(x);
                l--;
                a[x] = 0;

                System.out.print("x : " + x);
                ////////////////////////////////////////////////////////////x=-1;
            }
        }
        digList.clear();
        return a;
    }

    private static boolean check(int r, int c, int x) {
        int i, j, r1, c1;
        a[r][c] = 0;
        for (i = 1; i <= 9; i++)
            if (a[r][i] == x)
                return false;

        for (j = 1; j <= 9; j++)
            if (a[j][c] == x)
                return false;

        r1 = r - ((r - 1) % 3);
        c1 = c - ((c - 1) % 3);
        for (i = r1; i < r1 + 3; i++)
            for (j = c1; j < c1 + 3; j++)
                if (a[i][j] == x)
                    return false;

        a[r][c] = x;

        return true;
    }

    public static void setSelectedItem(GridItems item) {

        if (prevItem != null) {
            for (GridItems i : mlist)
                if (i.gridNum % 2 == 0)
                    i.setItembgcolor(col1);
                else
                    i.setItembgcolor(col2);
        }

        chosenItem = item;
        if (item.modifable) {

            for (GridItems i : mlist) {
                if (item.getIndex() == i.getIndex())
                    i.setItembgcolor("#FF98B8D7");
                if (item.getnumCol() == i.getnumCol())
                    i.setItembgcolor("#FF98B8D7");
                if (item.getnumRow() == i.getnumRow())
                    i.setItembgcolor("#FF98B8D7");
            }
            item.setItembgcolor("#69aef0");
        }
        prevItem = chosenItem;
        adapter.notifyDataSetChanged();


    }

    public static void setNumber(int number) {
        if (chosenItem == null) {
            Toast.makeText(mContext, "Select A Cell", Toast.LENGTH_SHORT).show();
            return;
        }
        if (chosenItem.modifable) {

            chosenItem.setNumber(number);
            adapter.notifyDataSetChanged();
            prevItem = chosenItem;
        }
    }

    public boolean checkfill() {
        for (GridItems i : mlist) {
            if (i.getNumber() == 0)
                return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.submit_button: {
                boolean flag = true;
                if (checkfill()) {
                    int madeSoln[][]=new int[10][10];
                    int j=1,k=1,i;
                    System.out.println("madesoln");
                    for(GridItems gi:mlist)
                    {
                        if (gi.gridNum % 2 == 0)
                            gi.setItembgcolor(col1);  // to remove blue clolor and set original grid color
                        else
                            gi.setItembgcolor(col2); // to remove blue clolor and set original grid color
                        madeSoln[j][k]=gi.getNumber();
                        System.out.print(madeSoln[j][k]+" ");
                        k++;
                        if(k==10)
                        {
                            j++;
                            k=1;
                            System.out.println();
                        }
                    }
                    for( i=1;i<=9;i++)
                        for( j=1;j<=9;j++)
                        {
                            if(a[i][j]==0)
                            {
                                if(!check(i,j,madeSoln[i][j]))
                                {
                                    flag=false;
                                    GridItems item= mlist.get(((i-1)*9)+(j-1));//formulae by ritesh _|_
                                    item.setItembgcolor("#E4F24936");//red
                                }
                            }

                        }
                    if (flag)
                    {
                        handler.removeCallbacks(runnable);
                        Toast.makeText(mContext, "Great! Sudoku Solved ", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(mContext, "Cells are unfilled", Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
                break;
            }
            case R.id.button_level:
            {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
                builderSingle.setIcon(R.drawable.logo);
                builderSingle.setTitle("Select Game Level :-");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                        MainActivity.this,
                        android.R.layout.simple_list_item_checked);
                arrayAdapter.add("Toddler");
                arrayAdapter.add("Rookie");
                arrayAdapter.add("Master");
                arrayAdapter.add("Professional");
                arrayAdapter.add("Ultimate");
                arrayAdapter.add("Expert");
                arrayAdapter.add("Devil");
                arrayAdapter.add("Genius");
                arrayAdapter.add("Boss");



                builderSingle.setNegativeButton(
                        "cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                builderSingle.setAdapter(
                        arrayAdapter,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                level_button.setText(strName);
                                handler.removeCallbacks(runnable);
                                mlist.clear();
                                Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                                        R.anim.slide_down);
// Start animation
                                linear_layout.startAnimation(slide_down);
                                cs=1;
                                quesGenerator();
                                doSubGridColor();
                            }
                        });
                builderSingle.show();
                break;
            }

        }
    }


}
