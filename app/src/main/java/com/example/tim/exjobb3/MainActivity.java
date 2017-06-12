package com.example.tim.exjobb3;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.EnumSet;

import io.mpos.accessories.AccessoryFamily;
import io.mpos.accessories.parameters.AccessoryParameters;
import io.mpos.provider.ProviderMode;
import io.mpos.transactions.Transaction;
import io.mpos.transactions.parameters.TransactionParameters;
import io.mpos.ui.shared.MposUi;
import io.mpos.ui.shared.model.MposUiConfiguration;

public class MainActivity extends AppCompatActivity {
    //bool för att hålla koll på om Activiteten körs
    static boolean active = false;

    //då aktiviteten startar setts active till sannt
    @Override
    public void onStart()
    {
        super.onStart();
        active = true;
    }

    //då aktiviteten avslutas setts active till falskt
    @Override
    public void onStop()
    {
        super.onStop();
        active = false;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get EditText by id
        //EditText inputText = (EditText) findViewById(R.id.inputValue);
        //String str = inputText.getText().toString();


        Button a_buttonMain1 = (Button) findViewById(R.id.buttonMain1);

        a_buttonMain1.setOnClickListener(new OnClickListener(){
           public void onClick(View v){
               //if(SecondActivity.active == true)
               //{
               //     goToSecondActivity();
               //}
               //else if(SecondActivity.active == false)
               //{
                   startActivity(new Intent(MainActivity.this, SecondActivity.class));
               //}

           }
        });

        Button a_buttonPay1 = (Button) findViewById(R.id.buttonPay1);

        a_buttonPay1.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                paymentButtonClicked();
            }
        });



    }

    public void goToSecondActivity(){

    }

    void paymentButtonClicked() {
        EditText inputText = (EditText) findViewById(R.id.inputValue);
        String str = inputText.getText().toString();
        MposUi ui = MposUi.initialize(this, ProviderMode.MOCK,
                "8ee07b60-2d15-4a86-bdd3-697dc5032f62 ", "xxCP0lDGtv6i38WuNk4fRuYz7TLrEnuf");

        ui.getConfiguration().setSummaryFeatures(EnumSet.of(
                // Add this line, if you do want to offer printed receipts
                // MposUiConfiguration.SummaryFeature.PRINT_RECEIPT,
                MposUiConfiguration.SummaryFeature.SEND_RECEIPT_VIA_EMAIL)
        );

        // Start with a mocked card reader:
        AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.MOCK)
                .mocked()
                .build();
        ui.getConfiguration().setTerminalParameters(accessoryParameters);

        // Add this line if you would like to collect the customer signature on the receipt (as opposed to the digital signature)
        // ui.getConfiguration().setSignatureCapture(MposUiConfiguration.SignatureCapture.ON_RECEIPT);

    /* When using the Bluetooth Miura Shuttle / M007 / M010, use the following parameters:
    AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.MIURA_MPI)
                                                                     .bluetooth()
                                                                     .build();
    ui.getConfiguration().setTerminalParameters(accessoryParameters);
    */


    /* When using the WiFi Miura M010, use the following parameters:
    AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.MIURA_MPI)
                                                                     .tcp("192.168.254.123", 38521)
                                                                     .build();
    ui.getConfiguration().setTerminalParameters(accessoryParameters);
    */


        TransactionParameters transactionParameters = new TransactionParameters.Builder()
                .charge(new BigDecimal(str), io.mpos.transactions.Currency.EUR) // .charge(new BigDecimal("5.00"), io.mpos.transactions.Currency.EUR)
                .subject("Bouquet of Flowers")
                .customIdentifier("yourReferenceForTheTransaction")
                .build();

        Intent intent = ui.createTransactionIntent(transactionParameters);
        startActivityForResult(intent, MposUi.REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MposUi.REQUEST_CODE_PAYMENT) {
            if (resultCode == MposUi.RESULT_CODE_APPROVED) {
                // Transaction was approved
                Toast.makeText(this, "Transaction approved", Toast.LENGTH_LONG).show();
            } else {
                // Card was declined, or transaction was aborted, or failed
                // (e.g. no internet or accessory not found)
                Toast.makeText(this, "Transaction was declined, aborted, or failed",
                        Toast.LENGTH_LONG).show();
            }
            // Grab the processed transaction in case you need it
            // (e.g. the transaction identifier for a refund).
            // Keep in mind that the returned transaction might be null
            // (e.g. if it could not be registered).
            Transaction transaction = MposUi.getInitializedInstance().getTransaction();
        }
    }
}
