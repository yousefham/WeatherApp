package com.app.weatherapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by best tech on 8/21/2017.
 */

public class WeatherArrayAdapter extends ArrayAdapter<Weather> {

    private Map<String, Bitmap> map = new HashMap<>();

    public WeatherArrayAdapter(Context context, List<Weather> forcast) {
        super(context, -1, forcast);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Weather day = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.list_item, parent, false);
            viewHolder.conditionImageView =
                    (ImageView) convertView.findViewById(R.id.imageview_item);
            viewHolder.dayTextView =
                    (TextView) convertView.findViewById(R.id.textview1);
            viewHolder.lowTextView =
                    (TextView) convertView.findViewById(R.id.textview2);
            viewHolder.hiTextView =
                    (TextView) convertView.findViewById(R.id.textview3);
            viewHolder.humidityTextView =
                    (TextView) convertView.findViewById(R.id.textview4);
            convertView.setTag(viewHolder);


        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (map.containsKey(day.iconURL)) {
            viewHolder.conditionImageView.setImageBitmap(map.get(day.iconURL));
        } else {

        }
        return convertView;
    }

    private class LoadImageRes extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageRes(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            Bitmap bitmap = null ;
            HttpURLConnection connection = null ;
            try  {

                URL url = new URL(strings[0]);
                connection = (HttpURLConnection)url.openConnection();

                try(InputStream inputStream = connection.getInputStream()) {
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    map.put(strings[0],bitmap);
                } catch (Exception e){

                }


            }catch (Exception e){

            }finally {
                connection.disconnect();

            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    private static class ViewHolder {
        ImageView conditionImageView;
        TextView dayTextView;
        TextView lowTextView;
        TextView hiTextView;
        TextView humidityTextView;

    }
}


