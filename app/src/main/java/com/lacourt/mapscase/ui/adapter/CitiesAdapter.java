package com.lacourt.mapscase.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.lacourt.mapscase.R;
import com.lacourt.mapscase.data.City;
import com.lacourt.mapscase.data.Main;
import com.lacourt.mapscase.data.Weather;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.CityViewHolder> {
    private Context context;
    private List<City> cities;
    private CityClick cityClick;

    public CitiesAdapter(Context context, CityClick cityClick, List<City> cities) {
        this.context = context;
        this.cityClick = cityClick;
        this.cities = cities;
    }

    @NotNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CityViewHolder(LayoutInflater.from(context).inflate(R.layout.city_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, final int position) {
        final City city = cities.get(position);
        Main main;
        Weather weather;

        if (city != null)

        holder.cityName.setText(city.getName());

        holder.cityItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cityClick.onCityClick(
                        city.getName(),
                        city.getMain().getTempMax(),
                        city.getMain().getTempMin(),
                        city.getWeather().get(0).getDescription()
                        );

            }
        });
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    public void setCities(List<City> cities) {
        this.cities = cities;
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {
        public TextView cityName;
        public ConstraintLayout cityItemLayout;
        public CityViewHolder(View itemView) {
            super(itemView);
            cityName = (TextView) itemView.findViewById(R.id.list_city_name);
            cityItemLayout = (ConstraintLayout) itemView.findViewById(R.id.city_item_layout);
        }
    }
}
