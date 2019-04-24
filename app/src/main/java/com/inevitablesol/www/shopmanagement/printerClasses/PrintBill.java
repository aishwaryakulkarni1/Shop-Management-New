package com.inevitablesol.www.shopmanagement.printerClasses;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.sql_lite.ItemDetalisClass;
import com.inevitablesol.www.shopmanagement.sql_lite.SqlDataBase;
import com.leopard.api.Printer;
import com.leopard.api.Setup;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;

import javax.microedition.khronos.opengles.GL;


public class PrintBill extends AppCompatActivity
{

    private static final int B_PERMISSION_CODE =101;
    private static ProgressDialog dlgPg;
    Button bt_print;
    Context context=PrintBill.this;
    static Setup setup=null;
    private boolean result;
    private Printer ptr;
    private String currenttext;
    private Button search;

    private AppCompatButton appCompatButton;
    private EditText editText;


    private boolean mbBleStatusBefore = false;

    public static BluetoothAdapter mBT = BluetoothAdapter.getDefaultAdapter();
    public static BluetoothDevice mBDevice = null;

    public static final byte REQUEST_DISCOVERY = 0x01;

    private final static int MESSAGE_BOX = 1;
    private static final String TAG = "MainActivity";

    private TextView mtvDeviceInfo = null;

    private static boolean ConnectionSTATUS=false;

    private String taxableValue,total_gst,igst,sgst,cgst,other_charges,shipping_charges,amount_paid,balanceDue,totalAmount;
    private String custName;
    private String dbname;
    private String custMobile;
    private String custemail;
    private String custaddOne;
    private String custId;
    private String h_status;
    private String gstin;
    private String supplier;



    private String shopName;
    private String shopAdddress;
    private String shopstate;
    private String pincode;


    private SqlDataBase sqlDataBase;
    private String[] sEnterTextFont = {
            "FONT LARGE NORMAL", "FONT LARGE BOLD",
            "FONT SMALL NORMAL", "FONT SMALL BOLD", "FONT ULLARGE NORMAL",
            "FONT ULLARGE BOLD", "FONT ULSMALL NORMAL", "FONT ULSMALL BOLD",
            "FONT 180LARGE NORMAL", "FONT 180LARGE BOLD",
            "FONT 180SMALL NORMAL", "FONT 180 SMALLBOLD",
            "FONT 180ULLARGE NORMAL", "FONT 180ULLARGE BOLD",
            "FONT 180ULSMALL NORMAL", "FONT 180ULSMALL BOLD"
    };
    private byte  bFontstyle = Printer.PR_FONTLARGENORMAL;
    private EditText edt_Text;
    private String sAddData;
    private  StringBuilder sAddData1=new StringBuilder();
    public static final int DEVICE_NOTCONNECTED = -100;
    private int iRetVal;
    private GlobalPool mGP;
    private GlobalPool globalPool;
    private Button connect;

    private SharedPreferences sharedpreferences;
    private  final String MyPREFERENCES = "MyPrefs";
    private byte bAddLineSyle = (byte) 0x01;;
    private ArrayList<ItemDetalisClass> itemDetalisClasses;
    private String currentDateTimeString;
    private String invId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_bill);
        appCompatButton = (AppCompatButton) findViewById(R.id.bt_print);
//        editText = (EditText) findViewById(R.id.et_print);

        sqlDataBase = new SqlDataBase(this);

        appCompatButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                print();
            }
        });

        Date dt = new Date();
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd/MMM/yy");
        // String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
    currentDateTimeString = dateFormat.format(dt);


        this.mtvDeviceInfo = (TextView) this.findViewById(R.id.actMain_tv_device_info);


      //  this.mGP = new GlobalPool();
        this.mGP=(GlobalPool)this.getApplicationContext();



        try {
            setup = new Setup();
        } catch (Exception e) {
            e.printStackTrace();
        }   // Instantiation   } catch (Exception e) {    e.printStackTrace();   }   boolean result = false;

        InputStream inSt = context.getResources().openRawResource(R.raw.licence_full);
        result = setup.blActivateLibrary(context, inSt);
        result = setup.blActivateLibrary(context, R.raw.licence_full);
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        dbname = (sharedpreferences.getString("dbname", null));

        itemDetalisClasses = sqlDataBase.getSelectedItemDetails();

        if (result)
        {
            Toast.makeText(getApplicationContext(), "Library Activated", Toast.LENGTH_SHORT).show();
            if (mGP.isConnected)
            {
               print();
                Log.d(TAG, "print:IF");
            } else
            {
                if(checkPermissionForBluetooth())
                {
                    Log.i(TAG, "onCreate:BluethoothPermition Granted ");
                    new startBluetoothDeviceTask().execute("");

                }else
                {
                    Log.d(TAG, "print:ELSE");
                    Log.i(TAG, "onCreate:BluethoothPermition Fail");
                    requestStoragePermission();

                }

                // EnterText();

            }


        } else
        {
            Toast.makeText(getApplicationContext(), "Library Not Activated", Toast.LENGTH_SHORT).show();
        }

        Intent intent = getIntent();
        if (intent.hasExtra("name"))
        {

            custName = intent.getStringExtra("name");
            custMobile = intent.getStringExtra("phone");
            custemail = intent.getStringExtra("email");
            custaddOne = intent.getStringExtra("address");
            custId = intent.getStringExtra("custid");
            h_status = intent.getStringExtra("h_status");
            gstin = intent.getStringExtra("gst");
            supplier = intent.getStringExtra("supplier");
            taxableValue = intent.getStringExtra("taxableValue");
            cgst = intent.getStringExtra("cgst");
            sgst = intent.getStringExtra("sgst");
            shipping_charges = intent.getStringExtra("shipping_charge");
            igst = intent.getStringExtra("igst");
            other_charges = intent.getStringExtra("other_charge");
            amount_paid = intent.getStringExtra("amountpaid");
            balanceDue = intent.getStringExtra("balanceDue");
            totalAmount = intent.getStringExtra("totalAmount");
            total_gst = intent.getStringExtra("totalGst");
            shopName=  intent.getStringExtra("shopName");
            shopAdddress= intent.getStringExtra("shopAddress");
            pincode= intent.getStringExtra("code");
            shopstate=intent.getStringExtra("state");
            invId=intent.getStringExtra("invId");
            Log.d(TAG, "onCreate: Inv "+invId);
        }else
        {
            Log.d(TAG, "onCreate: Els in Print class");
        }







    }


    public boolean checkPermissionForBluetooth() {
        int result = ContextCompat.checkSelfPermission(PrintBill.this, Manifest.permission.ACCESS_COARSE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED)
        {
            return true;
        } else {
            return false;
        }
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_COARSE_LOCATION)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},B_PERMISSION_CODE);
    }

    //This method will be called when the user will tap on allow or deny
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if(requestCode == B_PERMISSION_CODE){

            //If permission is granted
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                new startBluetoothDeviceTask().execute("");

                //Displaying a toast
              //  Toast.makeText(this,"Permission granted now you can read the storage",Toast.LENGTH_LONG).show();
            }else
            {
                //Displaying another toast if permission is not granted
                Toast.makeText(this,"Oops you just denied the permission",Toast.LENGTH_LONG).show();
            }
        }
    }

    private void print()
    {
        Log.d(TAG, "print: ");

        try {
            try
            {
                OutputStream outSt = BluetoothComm.mosOut;
                InputStream inSta = BluetoothComm.misIn;
                ptr = new Printer(setup, outSt, inSta);

                EnterText();
                // SAMPLE PDF CODE TO DOWNLOAD THE PDF


            } catch (Exception e)
            {
                e.printStackTrace();
            }







        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /* Handler to display UI response messages   */
    @SuppressLint("HandlerLeak")
    Handler ptrhandler = new Handler() {
        public void handleMessage(android.os.Message msg)
        {
            switch (msg.what) {
                case MESSAGE_BOX:
                    String str = (String) msg.obj;
                    showDialog(str);
                    break;
                default:
                    break;
            }
        };
    };




    private void EnterText()
    {
        Log.d(TAG, "EnterText: ");

       sAddData = "Name : "+custName+"" + "\n"+"Mobile :"+ custMobile +"\n";
        String qtspace="            ";
        StringBuilder str= new StringBuilder();
        str.append("");

         String  space="";


        for (ItemDetalisClass  item:itemDetalisClasses)
        {
           int length=  item.getItem_name().length();
            Log.d(TAG, "EnterText:Lenth"+ length);

            int cal_len=15-length;
            Log.d(TAG, "EnterText:cLength"+ cal_len);
             switch (cal_len)
             {

                 case  0:
                      space =" ";
                     break;
                 case 1:
                     space =" ";
                     break;
                 case 2:
                     space ="   ";
                     break;
                 case 3:
                     space ="   ";
                     break;
                 case 4:
                     space ="    ";
                     break;
                 case 5:
                     space ="     ";
                     break;

                 case 6:
                     space ="      ";
                     break;
                 case 7:
                     space="       ";
                     break;
                 case 8:
                     space ="        ";
                     break;

                 case 12:
                     space="            ";
                     break;
                 case 9:
                     space="         ";
                     break;
                 case 10:
                     space="          ";
                     break;
                 case 11:
                     space="           ";
                     break;



             }




            Log.d(TAG, "EnterText:"+item.getItem_name()+"qty"+item.getSelectd_qty()+"UnitPrice"+item.getUnit_price()+"Total Amount"+item.getTotal_calculared_price());
            sAddData1.append(item.getItem_name()).append(space).append(item.getSelectd_qty()).append("  ").append(item.getTotal_calculared_price()).append("\n");
        }

        //+""+"Address :"+custaddOne +"\n"+ "Email:"+custemail+"\n" ;// editText.getText().toString();
        if (sAddData.length() == 0)
        {
            ptrhandler.obtainMessage(MESSAGE_BOX, "Enter Text").sendToTarget();
        } else if (sAddData1.length() > 0)
        {
            EnterTextTask asynctask = new EnterTextTask();
            asynctask.execute(0);
        }
    }



    public class EnterTextTask extends AsyncTask<Integer, Integer, Integer> {
        /* displays the progress dialog until background task is completed*/
        @Override
        protected void onPreExecute() {
            progressDialog(context, "Please Wait...");
            super.onPreExecute();
        }
        /* Task of EnterTextAsyc performing in the background*/
        @Override
        protected Integer doInBackground(Integer... params) {
            try {
                // sAddData = edt_Text.getText().toString();
                ptr.iFlushBuf();
                String addlinestr="-";
                Character ch = addlinestr.charAt(0);
                ptr.iPrinterAddLine(bAddLineSyle, ch);


               String shop="        "+shopName + " ";
               String  cin ="CIN :    ";
                String  gst ="GSTIN:"+mGP.getGstNumnebr() +" ";
                ptr.iPrinterAddData(Printer.PR_FONTULSMALLBOLD, shop);
                ptr.iPrinterAddData(Printer.PR_FONT180SMALLBOLD, cin);
                ptr.iPrinterAddData(Printer.PR_FONT180SMALLBOLD, gst);

                String Addrss ="Address:"+shopAdddress;
                String State ="State  :"+ mGP.getShop_state();
                String code ="State Code  :";
                String Email= "Email:"+mGP.getShop_email();

               ptr.iPrinterAddData(bFontstyle, Addrss);
                ptr.iPrinterAddData(bFontstyle, State);
                ptr.iPrinterAddData(bFontstyle, code);
                ptr.iPrinterAddData(bFontstyle, Email);


               ptr.iPrinterAddLine(bAddLineSyle, ch);
                String TaxInvoice="    TAX INVOICE  ";
                ptr.iPrinterAddData(Printer.PR_FONTULSMALLBOLD, TaxInvoice);
                ptr.iPrinterAddLine(bAddLineSyle, ch);

                String invDate = "Invocie Number: "+invId+"\nInvoice Date:" +currentDateTimeString ;
                ptr.iPrinterAddData(bFontstyle, invDate);
                ptr.iPrinterAddLine(bAddLineSyle, ch);


                String  billTo="      BILL TO   ";
                ptr.iPrinterAddData(bFontstyle, billTo);


                String empty = sAddData ;//+ "\n" + "\n" + "\n" + "\n" + "\n"+ "\n";
                ptr.iPrinterAddData(bFontstyle, empty);
                ptr.iPrinterAddLine(bAddLineSyle, ch);


                String  Title =" Item       Qty   Amount";
                ptr.iPrinterAddData(bFontstyle, Title);
                ptr.iPrinterAddLine(bAddLineSyle, ch);
                ptr.iPrinterAddData(bFontstyle, String.valueOf(sAddData1));
                ptr.iPrinterAddLine(bAddLineSyle, ch);
              String TaxableValue= "   Taxable Value:"+taxableValue;
                ptr.iPrinterAddData(bFontstyle, TaxableValue);
                String CGst= "             CGST:"+cgst;
                ptr.iPrinterAddData(bFontstyle, CGst);
                String SGst= "             SGST:"+sgst;
                ptr.iPrinterAddData(bFontstyle, SGst);
                String Gst= "              GST:"+total_gst;
                ptr.iPrinterAddData(bFontstyle, Gst);

                String other = "     Other Charges:"+other_charges;
                ptr.iPrinterAddData(bFontstyle, other);
                String shipping = "  Shipping Charges:"+shipping_charges;
                ptr.iPrinterAddData(bFontstyle, shipping);
                String Total= "    Total Amount:"+totalAmount;
                ptr.iPrinterAddData(bFontstyle, Total);
                ptr.iPrinterAddLine(bAddLineSyle, ch);
                String  amountPaid= "    Amount Paid :"+amount_paid;
                ptr.iPrinterAddData(bFontstyle, amountPaid);
                String  balance = "       Balance Due :"+ balanceDue;
                ptr.iPrinterAddData(bFontstyle, balance);
                ptr.iPrinterAddLine(bAddLineSyle, ch);

                String PoweredBy="     Powered By   ";
                ptr.iPrinterAddData(bFontstyle, PoweredBy);

                String  company="           M- Hourz  ";
                ptr.iPrinterAddData(Printer.PR_FONTULSMALLBOLD, company);

                String  vision= "India's # 1 Cashless Billing Acceptance \n Mobile App & Website is here...";
                ptr.iPrinterAddData(bFontstyle, vision);
                ptr.iPrinterAddLine(bAddLineSyle, ch);

                ptr.iPrinterAddData(Printer.PR_FONTLARGENORMAL, " \n \n \n \n \n \n ");
                ptr.iPrinterAddData(Printer.PR_FONT180LARGENORMAL, " \n \n \n \n \n \n");
                iRetVal = ptr.iStartPrinting(1);


            } catch (NullPointerException e)
            {
                iRetVal = DEVICE_NOTCONNECTED;
                return iRetVal;
            }
            return iRetVal;
        }
        /* This displays the status messages of EnterTextAsyc in the dialog box */
        @Override
        protected void onPostExecute(Integer result)
        {
            dlgPg.dismiss();
            if (iRetVal == DEVICE_NOTCONNECTED) {
                ptrhandler.obtainMessage(DEVICE_NOTCONNECTED,"Device not connected").sendToTarget();
            } else if (iRetVal == Printer.PR_SUCCESS) {
                ptrhandler.obtainMessage(MESSAGE_BOX, "Print Success").sendToTarget();
            } else if (iRetVal == Printer.PR_PLATEN_OPEN) {
                ptrhandler.obtainMessage(MESSAGE_BOX,"Printer platen is open").sendToTarget();
            } else if (iRetVal == Printer.PR_PAPER_OUT) {
                ptrhandler.obtainMessage(MESSAGE_BOX,"Printer paper is out").sendToTarget();
            } else if (iRetVal == Printer.PR_IMPROPER_VOLTAGE) {
                ptrhandler.obtainMessage(MESSAGE_BOX,"Printer improper voltage").sendToTarget();
            } else if (iRetVal == Printer.PR_FAIL) {
                ptrhandler.obtainMessage(MESSAGE_BOX, "Printer failed").sendToTarget();
            } else if (iRetVal == Printer.PR_PARAM_ERROR) {
                ptrhandler.obtainMessage(MESSAGE_BOX,"Printer param error").sendToTarget();
            } else if (iRetVal == Printer.PR_NO_RESPONSE) {
                ptrhandler.obtainMessage(MESSAGE_BOX,"No response from Leopard device").sendToTarget();
            } else if (iRetVal== Printer.PR_DEMO_VERSION) {
                ptrhandler.obtainMessage(MESSAGE_BOX,"Library is in demo version").sendToTarget();
            } else if (iRetVal==Printer.PR_INVALID_DEVICE_ID) {
                ptrhandler.obtainMessage(MESSAGE_BOX,"Connected  device is not license authenticated.").sendToTarget();
            } else if (iRetVal==Printer.PR_ILLEGAL_LIBRARY) {
                ptrhandler.obtainMessage(MESSAGE_BOX,"Library not valid").sendToTarget();
            }
            super.onPostExecute(result);
        }
    }


    /* This performs Progress dialog box to show the progress of operation */
    public static void progressDialog(Context context, String msg) {
        dlgPg = new ProgressDialog(context);
        dlgPg.setMessage(msg);
        dlgPg.setIndeterminate(true);
        dlgPg.setCancelable(false);
        dlgPg.show();
    }

    /*  To show response messages  */
    public void showDialog(String str) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Shop Managament");
        alertDialogBuilder.setMessage(str).setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.dismiss();
                        finish();
                    }
                });
		/* create alert dialog*/
        AlertDialog alertDialog = alertDialogBuilder.create();
		/* show alert dialog*/
        alertDialog.show();
    }


    private class startBluetoothDeviceTask extends AsyncTask<String, String, Integer> {
        private static final int RET_BULETOOTH_IS_START = 0x0001;
        private static final int RET_BLUETOOTH_START_FAIL = 0x04;
        private static final int miWATI_TIME = 15;
        private static final int miSLEEP_TIME = 150;
        private ProgressDialog mpd;
        @Override
        public void onPreExecute(){
            mpd = new ProgressDialog(context);
            mpd.setMessage(getString(R.string.actDiscovery_msg_starting_device));
                mpd.setIndeterminate(true);
            mpd.setCancelable(true);
            //mpd.setCanceledOnTouchOutside(false);
            mpd.show();
            //mbBleStatusBefore = mBT.isEnabled();
        }
        @Override
        protected Integer doInBackground(String... arg0)
        {
            int iWait = miWATI_TIME * 1000;
			/* BT isEnable */
            if (!mBT.isEnabled()){
                mBT.enable();
                //Wait miSLEEP_TIME seconds, start the Bluetooth device before you start scanning
                while(iWait > 0){
                    if (!mBT.isEnabled())
                        iWait -= miSLEEP_TIME;
                    else
                        break;
                    SystemClock.sleep(miSLEEP_TIME);
                }
                if (iWait < 0)
                    return RET_BLUETOOTH_START_FAIL;
            }
            return RET_BULETOOTH_IS_START;
        }

        /**
         * After blocking cleanup task execution
         */
        @Override
        public void onPostExecute(Integer result){
            if (mpd.isShowing())
                mpd.dismiss();
            if (RET_BLUETOOTH_START_FAIL == result){
                AlertDialog.Builder builder = new AlertDialog.Builder(PrintBill.this);
                builder.setTitle(getString(R.string.dialog_title_sys_err));
                builder.setMessage(getString(R.string.actDiscovery_msg_start_bluetooth_fail));
                builder.setPositiveButton(R.string.btn_ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        mBT.disable();
                        finish();
                    }
                });
                builder.create().show();
            }
            else if (RET_BULETOOTH_IS_START == result)
            {
                openDiscovery();
            }
        }
    }
    private void openDiscovery(){
        Intent intent = new Intent(this, Act_BTDiscovery.class);
        this.startActivityForResult(intent, REQUEST_DISCOVERY);
    }
    private Hashtable<String, String> mhtDeviceInfo = new Hashtable<String, String>();

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        //   mainlay.setVisibility(View.VISIBLE);
        if (requestCode == REQUEST_DISCOVERY)
        {
            if (Activity.RESULT_OK == resultCode)
            {
                // this.mllDeviceCtrl.setVisibility(View.VISIBLE);
                this.mhtDeviceInfo.put("NAME", data.getStringExtra("NAME"));
                this.mhtDeviceInfo.put("MAC", data.getStringExtra("MAC"));
                this.mhtDeviceInfo.put("COD", data.getStringExtra("COD"));
                this.mhtDeviceInfo.put("RSSI", data.getStringExtra("RSSI"));
                this.mhtDeviceInfo.put("DEVICE_TYPE", data.getStringExtra("DEVICE_TYPE"));
                this.mhtDeviceInfo.put("BOND", data.getStringExtra("BOND"));
                this.showDeviceInfo();
                if (this.mhtDeviceInfo.get("BOND").equals(getString(R.string.actDiscovery_bond_nothing)))
                {
                    // this.mbtnPair.setVisibility(View.VISIBLE);
                    // this.mbtnComm.setVisibility(View.GONE);
                }else
                {
                    this.mBDevice = this.mBT.getRemoteDevice(this.mhtDeviceInfo.get("MAC"));
                    Log.d(TAG, "onActivityResult:"+mBDevice);
                    //  this.mbtnPair.setVisibility(View.GONE);
                    // this.mbtnComm.setVisibility(View.VISIBLE);
                    new connSocketTask().execute(this.mBDevice.getAddress());
                }
            }else if (Activity.RESULT_CANCELED == resultCode){
                this.finish();
            }
        }
        else if (requestCode==3) {
            finish();
        }
    }



    private void showDeviceInfo()
    {
        Log.d(TAG, "showDeviceInfo:"+this.mhtDeviceInfo);
        String Name=this.mhtDeviceInfo.get("NAME");
        mtvDeviceInfo.setText(Name);
    }



    private class connSocketTask extends AsyncTask<String, String, Integer>{
        /**Process waits prompt box*/
        private ProgressDialog mpd = null;
        /**Constants: connection fails*/
        private static final int CONN_FAIL = 0x01;
        /**Constant: the connection is established*/
        private static final int CONN_SUCCESS = 0x02;
        /**
         *Thread start initialization
         */
        @Override
        public void onPreExecute()
        {
            this.mpd = new ProgressDialog(PrintBill.this);
            this.mpd.setMessage(getString(R.string.actMain_msg_device_connecting));
            this.mpd.setCancelable(false);
            this.mpd.setCanceledOnTouchOutside(false);
            this.mpd.show();
        }
        @Override
        protected Integer doInBackground(String... arg0){
            if (mGP.createConn(arg0[0])){
                return CONN_SUCCESS;
            }
            else{
                return CONN_FAIL;
            }
        }

        /**
         * After blocking cleanup task execution
         */
        @Override
        public void onPostExecute(Integer result)
        {
            this.mpd.dismiss();
            if (CONN_SUCCESS == result)
            {
                ConnectionSTATUS=true;
                // mbtnComm.setVisibility(View.GONE);
                Toast.makeText(context,getString(R.string.actMain_msg_device_connect_succes),Toast.LENGTH_SHORT).show();
                print();

                // showBaudRateSelection();
            }else{
                Toast.makeText(context, getString(R.string.actMain_msg_device_connect_fail),Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        this.mGP.closeConn();
//        if (null != mBT && !this.mbBleStatusBefore)
//            mBT.disable();
        Log.d(TAG, "onDestroy:Close Connection");
    }

}
