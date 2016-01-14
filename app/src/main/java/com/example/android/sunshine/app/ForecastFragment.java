package com.example.android.sunshine.app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ForecastFragment extends Fragment {

	ArrayAdapter<String> mForecastAdapter;

	public class FetchWeatherTask extends AsyncTask<String, Void, Void> {

		private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

		@Override
		protected Void doInBackground(String... params) {

			HttpURLConnection urlConnection = null;
			BufferedReader reader = null;
			String forecastJsonStr = null;
			String format = "json";
			String units = "metric";
			int numDays = 7;

			try {
				final String FORECAST_BASE_URL =
						"http://api.openweathermap.org/data/2.5/forecast/daily?";
				final String QUERY_PARAM = "q";
				final String FORMAT_PARAM = "mode";
				final String UNITS_PARAM = "units";
				final String DAYS_PARAM = "cnt";
				final String APPID_PARAM = "APPID";

				Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
						.appendQueryParameter(QUERY_PARAM, params[0])
						.appendQueryParameter(FORMAT_PARAM, format)
						.appendQueryParameter(UNITS_PARAM, units)
						.appendQueryParameter(DAYS_PARAM, Integer.toString(numDays))
						.appendQueryParameter(APPID_PARAM, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
						.build();

				URL url = new URL(builtUri.toString());

				Log.v(LOG_TAG, "Built URI " + builtUri.toString());
				// Create the request to OpenWeatherMap, and open the connection
				urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestMethod("GET");
				urlConnection.connect();

				// Read the input stream into a String
				InputStream inputStream = urlConnection.getInputStream();
				StringBuffer buffer = new StringBuffer();
				if (inputStream == null) {
					// Nothing to do.
					return null;
				}
				reader = new BufferedReader(new InputStreamReader(inputStream));

				String line;
				while ((line = reader.readLine()) != null) {
					buffer.append(line + "\n");
				}

				if (buffer.length() == 0) {
					return null;
				}
				forecastJsonStr = buffer.toString();

				Log.v(LOG_TAG, "Forecast JSON string: " + forecastJsonStr);
			} catch (IOException e) {
				Log.e(LOG_TAG, "Error ", e);
				return null;
			} finally {
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException e) {
						Log.e(LOG_TAG, "Error closing stream", e);
					}
				}
			}

			return null;
		}
	}

	public ForecastFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.forecastfragment, menu);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_main, container, false);

		String[] forecastArray = {
				"Sun",
				"Mon",
				"Tues",
				"Wed",
				"Thurs",
				"Fri",
				"Sat"
		};

		List<String> weekForecast = new ArrayList<>(Arrays.asList(forecastArray));

		mForecastAdapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

		ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
		listView.setAdapter(mForecastAdapter);

		return rootView;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_refresh) {
			FetchWeatherTask task = new FetchWeatherTask();
			task.execute("94043");
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
