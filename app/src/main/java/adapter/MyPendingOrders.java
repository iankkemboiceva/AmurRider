/*
 * Copyright (C) 2012 jfrankie (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import amurrider.rider.com.amur.amurrider.R;

public class MyPendingOrders extends ArrayAdapter<TripDetails> implements Filterable {

	private List<TripDetails> planetList;
	private Context context;
	private Filter planetFilter;
	private List<TripDetails> origPlanetList;

	public MyPendingOrders(List<TripDetails> planetList, Context ctx) {
		super(ctx, R.layout.mytrips, planetList);
		this.planetList = planetList;
		this.context = ctx;
		this.origPlanetList = planetList;
	}
	
	public int getCount() {
		return planetList.size();
	}

	public TripDetails getItem(int position) {
		return planetList.get(position);
	}

	public long getItemId(int position) {
		return planetList.get(position).hashCode();
	}

	public View getView(final int position, View convertView, final ViewGroup parent) {
		View v = convertView;
		
		PlanetHolder holder = new PlanetHolder();
		
		// First let's verify the convertView is not null
		if (convertView == null) {
			// This a new view we inflate the new layout
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.mytrips, null);
			// Now we can fill the layout with the right values

			TextView accname = (TextView) v.findViewById(R.id.txtname);
			TextView accdate = (TextView) v.findViewById(R.id.txtdate);
			TextView txamo = (TextView) v.findViewById(R.id.txtamo);
			TextView txorderid = (TextView) v.findViewById(R.id.txtorder);
			TextView txtimest = (TextView) v.findViewById(R.id.txttimest);
		/*	TextView accid = (TextView) v.findViewById(R.id.txt);
            TextView curr = (TextView) v.findViewById(R.id.txt2);
			TextView taxref = (TextView) v.findViewById(R.id.txtref);
			TextView txtstt = (TextView) v.findViewById(R.id.txtstatus);
			Button btnrec = (Button) v.findViewById(R.id.btnrec);
			Button olbtnshare = (Button) v.findViewById(R.id.btnshr);*/

			
			holder.txtname = accname;
            holder.txtdate = accdate;
			holder.txtamo = txamo;
			holder.txtorderid = txorderid;
			holder.txttimest = txtimest;

			
			v.setTag(holder);
		}
		else 
			holder = (PlanetHolder) v.getTag();

		TripDetails p = planetList.get(position);

		final String amo = p.getAmo()+" KSHS";
		final String toname = p.getCust();
		String toorder = p.getShworderid();

		String mobnumb = p.getMobno();
		String timest = p.getTxntimest();
       // String convd = getDateTimeStamp(tdate);


		holder.txtname.setText("DELIVER TO: "+toname.toUpperCase());
        holder.txtamo.setText(amo);
        holder.txtorderid.setText("ORDER ID:"+toorder);
        holder.txtdate.setText("MOBILE NUMBER: "+mobnumb);
        holder.txttimest.setText(timest);


		
		return v;
	}

	public void resetData() {
		planetList = origPlanetList;
	}
	
	
	/* *********************************
	 * We use the holder pattern        
	 * It makes the view faster and avoid finding the component
	 * **********************************/
	
	private static class PlanetHolder {
		public TextView txtname;
		public TextView txtamo;
		public TextView txtdate;
		public TextView txtorderid;
		public TextView txttimest;
	}
    public static String getDateTimeStamp(String tdate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd MMM yyy HH:mm");
        Date convertedCurrentDate = null;

        try {
            convertedCurrentDate = sdf.parse(tdate);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        String strDate = sdf2.format(convertedCurrentDate);

        return strDate;
    }
	

	


}
