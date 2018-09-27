package com.example.administrator.myapps;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.function.DoubleUnaryOperator;

public class CalculatorActivity extends AppCompatActivity {

    private EditText etLoanAmount, etDownPayment, etTerm, etAnnualInterestRate;
    private TextView tvMonthlyPayment, tvTotalRepayment, tvTotalInterest, tvAverageMonthlyInterest;
    private SharedPreferences  spLocalCalHistory;

    private double monthlyPayment = 0.00, totalRepayment = 0.00, totalInterest = 0.00,  monthlyInterest = 0.00;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        etLoanAmount = findViewById(R.id.loan_amount);
        etDownPayment = findViewById(R.id.down_payment);
        etTerm = findViewById(R.id.term);
        etAnnualInterestRate = findViewById(R.id.annual_interest_rate);

        tvMonthlyPayment = findViewById(R.id.monthly_repayment);
        tvTotalRepayment = findViewById(R.id.total_repayment);
        tvTotalInterest = findViewById(R.id.total_interest);
        tvAverageMonthlyInterest = findViewById(R.id.average_monthly_interest);

        spLocalCalHistory = getSharedPreferences("LocalCalHistory", Context.MODE_PRIVATE);

        etLoanAmount.setText(spLocalCalHistory.getString("amount", "0"));
        etDownPayment.setText(spLocalCalHistory.getString("downPayment", "0"));
        etTerm.setText(spLocalCalHistory.getString("interestRate", "0"));
        etAnnualInterestRate.setText(spLocalCalHistory.getString("term", "0"));

        tvMonthlyPayment.setText(spLocalCalHistory.getString("monthlyPayment", "0.00"));
        tvTotalRepayment.setText(spLocalCalHistory.getString("totalRepayment", "0.00"));
        tvTotalInterest.setText(spLocalCalHistory.getString("totalInterest", "0.00"));
        tvAverageMonthlyInterest.setText(spLocalCalHistory.getString("monthlyInterest", "0.00"));
    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.button_calculate:
                calculate();
                Log.d("Check", "button_calculate is clicked");
                break;
            case R.id.button_reset:
                reset();
                Log.d("Check", "button_reset is clicked");
                break;
        }
    }

    private void calculate(){
        String amount = etLoanAmount.getText().toString();
        String downPayment = etDownPayment.getText().toString();
        String interestRate = etAnnualInterestRate.getText().toString();
        String term = etTerm.getText().toString();

        double loanAmount = Double.parseDouble(amount) - Double.parseDouble(downPayment);
        double interest  = Double.parseDouble(interestRate) / 12 / 100;
        double noOfMonth = (Integer.parseInt(term) * 12);

        if(noOfMonth > 0){
            monthlyPayment = loanAmount * (interest + (interest/(java.lang.Math.pow((1+interest), noOfMonth)-1)));
            totalRepayment = monthlyPayment * noOfMonth;
            totalInterest = totalRepayment - loanAmount;
            monthlyInterest = totalInterest / noOfMonth;

            tvMonthlyPayment.setText(String.valueOf(monthlyPayment));
            tvTotalRepayment.setText(String.valueOf(totalRepayment));
            tvTotalInterest.setText(String.valueOf(totalInterest));
            tvAverageMonthlyInterest.setText(String.valueOf(monthlyInterest));
        }

        SharedPreferences.Editor editor = spLocalCalHistory.edit()
                .putString("amount", String.valueOf(amount))
                .putString("downPayment", String.valueOf(downPayment))
                .putString("interestRate", String.valueOf(interestRate))
                .putString("term", String.valueOf(term))
                .putString("monthlyPayment",String.valueOf(monthlyPayment))
                .putString("totalRepayment",String.valueOf(totalRepayment))
                .putString("totalInterest",String.valueOf(totalInterest))
                .putString("monthlyInterest",String.valueOf(monthlyInterest));
        editor.apply();
    }

    private void reset(){
        etLoanAmount.setText("");
        etDownPayment.setText("");
        etTerm.setText("");
        etAnnualInterestRate.setText("");

        tvMonthlyPayment.setText(R.string.default_result);
        tvTotalRepayment.setText(R.string.default_result);
        tvTotalInterest.setText(R.string.default_result);
        tvAverageMonthlyInterest.setText(R.string.default_result);

    }




}
