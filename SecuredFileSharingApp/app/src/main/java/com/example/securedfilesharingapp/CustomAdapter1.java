package com.example.securedfilesharingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

class CustomAdapter1 implements ListAdapter {
    ArrayList<ItemData> arrayList;
    ArrayAdapter<String> arrayAdapter;
    Context context;
    String fname_Name="",fname="";

    public CustomAdapter1(Context context, ArrayList<ItemData> arrayList) {
        this.arrayList=arrayList;
        this.context=context;




    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ItemData item_data = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.list_row1, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            TextView tittle = convertView.findViewById(R.id.title);


            Button b=convertView.findViewById(R.id.btnDownload);

            tittle.setText(item_data.fname_Name);




            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    fname=item_data.fname_Name;
                    new get_my_file().execute();







                }
            });


            
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
    private class get_my_file extends AsyncTask<Void, String, String> {
        @Override
        public String doInBackground(Void... Void)
        {
            JSONObject jsn = new JSONObject();
            String response = "";



            try {
                URL url = new URL(Global.url +"get_my_file");
                jsn.put("user",user_home.user);
                jsn.put("fname",fname);

                response = HttpConnection.getResponse(url,jsn);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch(JSONException e)
            {
                e.printStackTrace();
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }

            //  if (response.equals())
            return response;
        }
        @Override
        protected void onPostExecute(String s) {

            if(s.endsWith("null"))
            {

                s=s.substring(0,s.length()-4);
            }
            Log.d("status",s);
            Toast.makeText(context,s, Toast.LENGTH_LONG).show();
            if(s.equals("ok"))
            {

                pdf_view.pdfurl=Global.url+fname;
                pdf_view.fname=fname;
                Intent next=new Intent(context,pdf_view.class);
                context.startActivity(next);
            }


        }
    }
//    protected void showInputDialog(String sizes[], int qts[]) {
//        final int qts1[]=qts;
//        // get prompts.xml view
//        LayoutInflater layoutInflater = LayoutInflater.from(context);
//        View promptView = layoutInflater.inflate(R.layout.prompts1, null);
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
//        alertDialogBuilder.setView(promptView);
//
//        final EditText editText = (EditText) promptView.findViewById(R.id.edittext);
//        final Spinner sp=(Spinner)promptView.findViewById(R.id.spSize);
//        ArrayAdapter<String> adapterFrom = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,sizes);
//        //adapterFrom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        sp.setAdapter(adapterFrom);
//        // setup a dialog window
//        alertDialogBuilder.setCancelable(false)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        String sz=sp.getSelectedItem().toString();
//                        int index=sp.getSelectedItemPosition();
//                        int q= Integer.parseInt(editText.getText().toString());
//
//                        int max_qty=qts1[index];
//
//
//                        if(q>max_qty)
//                        {
//                           Toast.makeText(context,"Quantity out of stock", Toast.LENGTH_LONG).show();
//                        }
//                        else{
//
//                            size=sz;
//                            qty=q;
//                            tot_price=price*qty;
//                            Buyer_Home.al_id.add(item_id);
//                            Buyer_Home.al_name.add(item_name);
//                            Buyer_Home.al_brand.add(brand);
//                            Buyer_Home.al_weight.add(seller_id);
//                            Buyer_Home.al_type.add(qty);
//                            Buyer_Home.al_price.add(price);
//
//                            Buyer_Home.al_desc.add(desc);
//
//                            Toast.makeText(context,"size="+sz+",Qty="+q+"Tot Price="+tot_price, Toast.LENGTH_LONG).show();
//                            Toast.makeText(context,item_name+" has been added to cart", Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                })
//                .setNegativeButton("Cancel",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                dialog.cancel();
//                            }
//                        });
//
//        // create an alert dialog
//        AlertDialog alert = alertDialogBuilder.create();
//        alert.show();
//    }

}