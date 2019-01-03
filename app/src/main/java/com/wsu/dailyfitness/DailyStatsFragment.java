package com.wsu.dailyfitness;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.content.Context;
import java.util.List;
import java.util.Vector;
import android.widget.ListView;

public class DailyStatsFragment extends Fragment {
	private DatabaseHelper database;
	private LayoutInflater mInflater;
	private Vector<Statistics> data;
	ListView statsList;
	private FetchStepsTask fetchdata;
	CustomAdapter adapter;
	View mRootView;

	public DailyStatsFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		database = new DatabaseHelper(this.getActivity());
	}

	@Override
	public View onCreateView( LayoutInflater inflater, ViewGroup container,
							  Bundle savedInstanceState) {

		final View rootView = inflater.inflate(R.layout.stats_layout, container, false);
		mRootView = rootView;
		mInflater = (LayoutInflater) getActivity().getBaseContext().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
		data = new Vector<Statistics>();
		fetchdata = new FetchStepsTask(LoginActivity.mUsername);
		fetchdata.execute();
		return rootView;

	}

	class CustomAdapter extends ArrayAdapter<Statistics> {

		public CustomAdapter(Context context, int resource, int textViewResourceId,
							 List<Statistics> objects) {

			super(context, resource, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder = null;
			TextView date = null;
			TextView steps = null;

			Statistics rowData = getItem(position);
			if (null == convertView) {
				convertView = mInflater.inflate(R.layout.daily_stats_layout_details, null);
				holder = new ViewHolder(convertView);
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			date = holder.getDate();
			date.setText(rowData.getDate());
			steps = holder.getSteps();
			steps.setText(rowData.getSteps()*Constants.step_length + " Feet");

			return convertView;
		}
	}

	class ViewHolder {
		private View mRow;
		private TextView send_date = null;
		private TextView send_steps = null;

		public ViewHolder(View row) {
			mRow = row;
		}

		public TextView getDate() {
			if (null == send_date) {
				send_date = (TextView) mRow.findViewById(R.id.datetextview);
			}
			return send_date;
		}

		public TextView getSteps() {
			if (null == send_steps) {
				send_steps = (TextView) mRow.findViewById(R.id.stepstextview);
			}
			return send_steps;
		}
	}

	public class FetchStepsTask extends AsyncTask<Void, Void, Boolean> {

		private final String mUsername;

		FetchStepsTask(String username) {
			mUsername = username;
		}

		@Override
		protected Boolean doInBackground(Void... params) {

			try {

				data = database.getSteps(mUsername);

				return true;
			} catch (Exception e) {
				return false;
			}
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if(success){
				//Toast.makeText(getActivity().getBaseContext(), "GOT DATA " + data.size(), Toast.LENGTH_SHORT).show();
				adapter = new CustomAdapter(getActivity().getBaseContext(), R.layout.daily_stats_layout_details, R.id.datetextview, data);
				statsList = (ListView)mRootView.findViewById(R.id.statslist);
				statsList.setAdapter(adapter);
			}
		}

		@Override
		protected void onCancelled() {

		}
	}
}
