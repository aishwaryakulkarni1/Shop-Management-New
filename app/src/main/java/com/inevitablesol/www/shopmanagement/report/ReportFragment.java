package com.inevitablesol.www.shopmanagement.report;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.inevitablesol.www.shopmanagement.R;
import com.inevitablesol.www.shopmanagement.analysis.date.DatePickFragments;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ReportFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.*/

public class ReportFragment extends Fragment implements DatePickFragments.OnDateSetItem {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private String DayBook;

    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1,mParam2,mParam3,mParam4,mParam5,mParam6,mParam7,mParam8;
//    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private  Context context;

    private static final String TAG = "ReportFragment";
    private FragmentActivity fragmentActivity;

    TextView txt_FromDate,txt_fromMonth,txt_fromYear,txt_tillDate,txt_toDate;
    Button download;
    RadioButton pdf,xls,total_from,total_month,total_year,till;
    ImageView fromDate,toDate,month,year,onlyYear;
    RadioGroup rg1,rg2;


    public ReportFragment() {
        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment ReportFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static ReportFragment newInstance(String param1, String param2)
//    {
//        Log.d(TAG, "newInstance:  mParam"+param1);
//        Log.d(TAG, "newInstance: mParam2"+param2);
//        ReportFragment fragment = new ReportFragment();
//        Bundle args = new Bundle();
//       // args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString("DayBook");
            mParam2 = getArguments().getString("Discount");
            mParam3 = getArguments().getString("ProfitLoss");
            mParam4 = getArguments().getString("Sales");
            mParam5 = getArguments().getString("Purchase");
            mParam6 = getArguments().getString("Expenses");
            mParam7 = getArguments().getString("Tax");
            mParam8 = getArguments().getString("TaxRate");

        }
        Log.d(TAG, "onCreate: ");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.report_dialog, container, false);
        TextView tx_title= (TextView) view.findViewById(R.id.fm_title);

        txt_FromDate= (TextView) view.findViewById(R.id.total_fromdate);
        txt_fromMonth=(TextView) view.findViewById(R.id.t_month);
        txt_fromYear = (TextView) view.findViewById(R.id.txt_onlyYear);
        txt_tillDate = (TextView) view.findViewById(R.id.txt_tillDate);
        txt_toDate = (TextView) view.findViewById(R.id.total_toDate);
        //Doing Something

        pdf = (RadioButton) view.findViewById(R.id.pdf);
        xls = (RadioButton) view.findViewById(R.id.xls);
        download = (Button) view.findViewById(R.id.total_saleDownload);

        total_from = (RadioButton) view.findViewById(R.id.total_from);
        total_month = (RadioButton) view.findViewById(R.id.total_month);
        total_year = (RadioButton) view.findViewById(R.id.total_year);
        till = (RadioButton) view.findViewById(R.id.till);
//
        fromDate = (ImageView)view.findViewById(R.id.report_imgfromDate);
        toDate = (ImageView)view.findViewById(R.id.total_todate_image);
        month = (ImageView)view.findViewById(R.id.total_imgMonth);
        onlyYear = (ImageView) view.findViewById(R.id.img_onlyYear);
        rg1 = (RadioGroup) view.findViewById(R.id.total_SaleRadio);
        rg2 = (RadioGroup) view.findViewById(R.id.total_Sale);

        if(mParam1.equalsIgnoreCase("DayBook")){

             tx_title.setText("Day Book Report");

             fromDate.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View view) {

                     DatePickFragments datePickFragments=  DatePickFragments.getInstance(ReportFragment.this);
                        Log.d(TAG, "onClick: datePicker"+datePickFragments);

                        //   DatePickFragments datePickFragments=new DatePickFragments(ReportFragment.this);
                       // datePickFragments.show((fragmentActivity.getSupportFragmentManager()),"date");
                     final Calendar myCalendar = Calendar.getInstance();

                     DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                         @Override
                         public void onDateSet(DatePicker view, int year, int monthOfYear,
                                               int dayOfMonth) {
                             // TODO Auto-generated method stub
                             myCalendar.set(Calendar.YEAR, year);
                             myCalendar.set(Calendar.MONTH, monthOfYear);
                             myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                             Date date1 = myCalendar.getTime();

                             //updateDate(myCalendar);
                             java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                             String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                             txt_FromDate.setText(currentDateTimeString);

                         }

                     };

                     new DatePickerDialog(getContext(),date,
                             myCalendar.get(Calendar.YEAR),
                             myCalendar.get(Calendar.MONTH),
                             myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                 }
             });

            toDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickFragments datePickFragments=  DatePickFragments.getInstance(ReportFragment.this);
                    Log.d(TAG, "onClick: datePicker"+datePickFragments);

                    //   DatePickFragments datePickFragments=new DatePickFragments(ReportFragment.this);
                    //datePickFragments.show((fragmentActivity.getSupportFragmentManager()),"date");

                    final Calendar myCalendar = Calendar.getInstance();

                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date date1 = myCalendar.getTime();

                            //updateDate(myCalendar);
                            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                            txt_toDate.setText(currentDateTimeString);

                        }

                    };

                    new DatePickerDialog(getContext(),date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });


            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   int selectedId1 = rg1.getCheckedRadioButtonId();
//                    RadioButton rb1 = (RadioButton) view.findViewById(rg1.getCheckedRadioButtonId());
//                    Log.d("rb1:",String.valueOf(rb1));
//                    String  fileFormat1 = rb1.getText().toString();

                    int selectedId2 = rg2.getCheckedRadioButtonId();
//                    RadioButton rb2 = (RadioButton) view.findViewById(selectedId2);
//                    String fileFormat2 = rb2.getText().toString().trim();

//                    if(fileFormat1.equalsIgnoreCase("From") ){
//                        if(fileFormat2.equalsIgnoreCase(".pdf")) {
//                            Uri uri = Uri.parse("http://www.google.co.in");
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(intent);
//                        }
//                    }
//                    else if (fileFormat1.equalsIgnoreCase("From") && fileFormat2.equalsIgnoreCase(".xls")){
//                        Toast.makeText(getContext(),"In XLS",Toast.LENGTH_LONG);
//                    }


                    Uri uri = Uri.parse("http://www.google.co.in");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
            });
        }

        else if(mParam2.equalsIgnoreCase("Discount")){
//
            tx_title.setText("Discount Report");

            fromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    DatePickFragments datePickFragments=  DatePickFragments.getInstance(ReportFragment.this);
//                    Log.d(TAG, "onClick: datePicker"+datePickFragments);

                    //   DatePickFragments datePickFragments=new DatePickFragments(ReportFragment.this);
                    // datePickFragments.show((fragmentActivity.getSupportFragmentManager()),"date");
                    final Calendar myCalendar = Calendar.getInstance();

                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date date1 = myCalendar.getTime();

                            //updateDate(myCalendar);
                            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                            txt_FromDate.setText(currentDateTimeString);

                        }

                    };

                    new DatePickerDialog(getContext(),date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            toDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickFragments datePickFragments=  DatePickFragments.getInstance(ReportFragment.this);
                    Log.d(TAG, "onClick: datePicker"+datePickFragments);

                    //   DatePickFragments datePickFragments=new DatePickFragments(ReportFragment.this);
                    //datePickFragments.show((fragmentActivity.getSupportFragmentManager()),"date");

                    final Calendar myCalendar = Calendar.getInstance();

                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date date1 = myCalendar.getTime();

                            //updateDate(myCalendar);
                            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                            txt_toDate.setText(currentDateTimeString);

                        }

                    };

                    new DatePickerDialog(getContext(),date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });


            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int selectedId1 = rg1.getCheckedRadioButtonId();
//                    RadioButton rb1 = (RadioButton) view.findViewById(rg1.getCheckedRadioButtonId());
//                    Log.d("rb1:",String.valueOf(rb1));
//                    String  fileFormat1 = rb1.getText().toString();

                    int selectedId2 = rg2.getCheckedRadioButtonId();
//                    RadioButton rb2 = (RadioButton) view.findViewById(selectedId2);
//                    String fileFormat2 = rb2.getText().toString().trim();

//                    if(fileFormat1.equalsIgnoreCase("From") ){
//                        if(fileFormat2.equalsIgnoreCase(".pdf")) {
//                            Uri uri = Uri.parse("http://www.google.co.in");
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(intent);
//                        }
//                    }
//                    else if (fileFormat1.equalsIgnoreCase("From") && fileFormat2.equalsIgnoreCase(".xls")){
//                        Toast.makeText(getContext(),"In XLS",Toast.LENGTH_LONG);
//                    }


                    Uri uri = Uri.parse("https://facebook.com/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
            });
//            }
//
//            //MONTH RADIOBUTTON
//
//            if(total_month.isSelected())
//            {
//                month.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DatePickFragments datePickFragments=  DatePickFragments.getInstance(ReportFragment.this);
//                        Log.d(TAG, "onClick: datePicker"+datePickFragments);
//
//                        datePickFragments.show((fragmentActivity.getSupportFragmentManager()),"date");
//
//                        new YearMonthPickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onYearMonthSet(int year, int month) {
//
//                                Calendar calendar = Calendar.getInstance();
//                                calendar.set(Calendar.YEAR,year);
//                                calendar.set(Calendar.MONTH,month);
//                                calendar.set(Calendar.DAY_OF_MONTH,1);
//
//                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
//                                String currentMonthYear = dateFormat.format(calendar.getTime());
//                                txt_fromMonth.setText(currentMonthYear);
//
//                                calendar.add(Calendar.MONTH,+1);
//                                calendar.add(Calendar.DATE,-1);
//                            }
//                        };
//                    }
//                });
//
//                if(pdf.isChecked())
//                {
//                    download.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//
//                            Uri uri = Uri.parse("http://www.google.co.in");
//                            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//                            startActivity(intent);
//                        }
//                    });
//                }
//                else if(xls.isChecked()){
//                    download.setOnClickListener(new View.OnClickListener(){
//                        @Override
//                        public void onClick(View v){
//
//
//                        }
//                    });
//                }
//                else{
//                    Toast.makeText(getContext(),"Please select one option to proceed",Toast.LENGTH_LONG);
//                }
//            }
//
//            //year = (ImageView)view.findViewById(R.id.total_imgYear);
//
//            //YEAR RADIOBUTTON
////            if (total_year.isChecked()){
////                year.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        DatePickFragments datePickFragments=  DatePickFragments.getInstance(ReportFragment.this);
////                        Log.d(TAG, "onClick: datePicker"+datePickFragments);
////
////                        datePickFragments.show((fragmentActivity.getSupportFragmentManager()),"date");
////
////                        new YearMonthPickerDialog.OnDateSetListener() {
////                            @Override
////                            public void onYearMonthSet(int year, int month) {
////
////                                Calendar calendar = Calendar.getInstance();
////                                calendar.set(Calendar.YEAR,year);
////                                calendar.set(Calendar.MONTH,month);
////                                calendar.set(Calendar.DAY_OF_MONTH,1);
////
////                                SimpleDateFormat dateFormat = new SimpleDateFormat("MM-yyyy");
////                                String currentMonthYear = dateFormat.format(calendar.getTime());
////                                txt_fromYear.setText(currentMonthYear);
////
////                                calendar.add(Calendar.MONTH,+1);
////                                calendar.add(Calendar.DATE,-1);
////                            }
////                        };
////                    }
////                });
////                if(pdf.isChecked())
////                {
////                    download.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////
////                            Uri uri = Uri.parse("http://www.google.co.in");
////                            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
////                            startActivity(intent);
////                        }
////                    });
////                }
////                else if(xls.isChecked()){
////                    download.setOnClickListener(new View.OnClickListener(){
////                        @Override
////                        public void onClick(View v){
////
////
////                        }
////                    });
////                }
////                else{
////                    Toast.makeText(getContext(),"Please select one option to proceed",Toast.LENGTH_LONG);
////                }
////            }
//
//            //TILL RADIO BUTTON
////            if(till.isChecked()){
////                txt_tillDate.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        DatePickFragments datePickFragments=  DatePickFragments.getInstance(ReportFragment.this);
////                        Log.d(TAG, "onClick: datePicker"+datePickFragments);
////
////                        datePickFragments.show((fragmentActivity.getSupportFragmentManager()),"date");
////                        final Calendar myCalendar = Calendar.getInstance();
////                        new DatePickerDialog.OnDateSetListener() {
////                            @Override
////                            public void onDateSet(DatePicker view, int year, int monthOfYear,
////                                                  int dayOfMonth) {
////                                // TODO Auto-generated method stub
////                                myCalendar.set(Calendar.YEAR, year);
////                                myCalendar.set(Calendar.MONTH, monthOfYear);
////                                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
////                                Date date1 = myCalendar.getTime();
////
////                                java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
////                                String currentDateTimeString= dateFormat.format(date1);
////                                txt_tillDate.setText(currentDateTimeString);
////                            }
////
////                        };
////                        Log.d("date", String.valueOf(myCalendar.getTime()));
////                    }
////                });
////                if(pdf.isChecked())
////                {
////                    download.setOnClickListener(new View.OnClickListener() {
////                        @Override
////                        public void onClick(View v) {
////
////                            Uri uri = Uri.parse("http://www.google.co.in");
////                            Intent intent = new Intent(Intent.ACTION_VIEW,uri);
////                            startActivity(intent);
////                        }
////                    });
////                }
////                else if(xls.isChecked()){
////                    download.setOnClickListener(new View.OnClickListener(){
////                        @Override
////                        public void onClick(View v){
////
////
////                        }
////                    });
////                }
////                else{
////                    Toast.makeText(getContext(),"Please select one option to proceed",Toast.LENGTH_LONG);
////                }
////            }
        }

        else if(mParam3.equalsIgnoreCase("ProfitLoss")){

            tx_title.setText("Profit & Loss Report");

            fromDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    DatePickFragments datePickFragments=  DatePickFragments.getInstance(ReportFragment.this);
//                    Log.d(TAG, "onClick: datePicker"+datePickFragments);

                    //   DatePickFragments datePickFragments=new DatePickFragments(ReportFragment.this);
                    // datePickFragments.show((fragmentActivity.getSupportFragmentManager()),"date");
                    final Calendar myCalendar = Calendar.getInstance();

                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date date1 = myCalendar.getTime();

                            //updateDate(myCalendar);
                            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                            txt_FromDate.setText(currentDateTimeString);

                        }

                    };

                    new DatePickerDialog(getContext(),date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            toDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DatePickFragments datePickFragments=  DatePickFragments.getInstance(ReportFragment.this);
                    Log.d(TAG, "onClick: datePicker"+datePickFragments);

                    //   DatePickFragments datePickFragments=new DatePickFragments(ReportFragment.this);
                    //datePickFragments.show((fragmentActivity.getSupportFragmentManager()),"date");

                    final Calendar myCalendar = Calendar.getInstance();

                    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear,
                                              int dayOfMonth) {
                            // TODO Auto-generated method stub
                            myCalendar.set(Calendar.YEAR, year);
                            myCalendar.set(Calendar.MONTH, monthOfYear);
                            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                            Date date1 = myCalendar.getTime();

                            //updateDate(myCalendar);
                            java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("yyyy-MM-dd");
                            String currentDateTimeString = dateFormat.format(myCalendar.getTime());
                            txt_toDate.setText(currentDateTimeString);

                        }

                    };

                    new DatePickerDialog(getContext(),date,
                            myCalendar.get(Calendar.YEAR),
                            myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });


            download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    int selectedId1 = rg1.getCheckedRadioButtonId();
//                    RadioButton rb1 = (RadioButton) view.findViewById(rg1.getCheckedRadioButtonId());
//                    Log.d("rb1:",String.valueOf(rb1));
//                    String  fileFormat1 = rb1.getText().toString();

//                    int selectedId2 = rg2.getCheckedRadioButtonId();
//                    RadioButton rb2 = (RadioButton) view.findViewById(selectedId2);
//                    String fileFormat2 = rb2.getText().toString().trim();

//                    if(fileFormat1.equalsIgnoreCase("From") ){
//                        if(fileFormat2.equalsIgnoreCase(".pdf")) {
//                            Uri uri = Uri.parse("http://www.google.co.in");
//                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                            startActivity(intent);
//                        }
//                    }
//                    else if (fileFormat1.equalsIgnoreCase("From") && fileFormat2.equalsIgnoreCase(".xls")){
//                        Toast.makeText(getContext(),"In XLS",Toast.LENGTH_LONG);
//                    }


                    Uri uri = Uri.parse("https://in.linkedin.com");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);

                }
            });
        }

        else{Toast.makeText(getContext(),"Please select one option to proceed",Toast.LENGTH_LONG); }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        fragmentActivity=(FragmentActivity)context;
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void getDate(String date)
    {
        Log.d(TAG, "getDate in Fragment:"+date);

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}