package com.toddlertechiez.sudokuyou.buttonsgrid;

import android.content.Context;
import android.graphics.Color;
import android.print.PrintAttributes;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.toddlertechiez.sudokuyou.MainActivity;


public class NumberButton extends Button implements OnClickListener{

	private int number;
	
	public NumberButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		MainActivity.setNumber(number);
	}
	
	public void setNumber(int number){
		this.number = number;
	}
}
