package com.my.voenmeh.ui.tracker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.my.voenmeh.R;
import com.my.voenmeh.databinding.FragmentTrackerBinding;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import androidx.appcompat.widget.SwitchCompat;

public class TrackerFragment extends Fragment {

    private FragmentTrackerBinding binding;
    private SubsamplingScaleImageView map;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TrackerViewModel dashboardViewModel = new ViewModelProvider(this).get(TrackerViewModel.class);
        binding = FragmentTrackerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Инициализация SubsamplingScaleImageView и установка изображения
        map = root.findViewById(R.id.map);
        map.setImage(ImageSource.resource(R.drawable.map11));

        // Установка обработчиков для кнопок и переключателя
        root.findViewById(R.id.rad_btn1).setOnClickListener(this::onRadioButtonClicked);
        root.findViewById(R.id.rad_btn2).setOnClickListener(this::onRadioButtonClicked);
        root.findViewById(R.id.rad_btn3).setOnClickListener(this::onRadioButtonClicked);
        root.findViewById(R.id.rad_btn4).setOnClickListener(this::onRadioButtonClicked);
        SwitchCompat switch1 = root.findViewById(R.id.switch1);
        switch1.setOnClickListener(this::onRadioButtonClicked);

        return root;
    }

    public void onRadioButtonClicked(View view) {
        // Получаем выбранный этаж и корпус
        RadioGroup floorGroup = getView().findViewById(R.id.group1);
        int floorId = floorGroup.getCheckedRadioButtonId();
        RadioButton floorButton = getView().findViewById(floorId);
        String selectedFloor = floorButton.getText().toString();

        SwitchCompat buildingSwitch = getView().findViewById(R.id.switch1);
        boolean isULK = buildingSwitch.isChecked();

        // Меняем изображение в зависимости от выбранных параметров
        switch (selectedFloor) {
            case "1":
                map.setImage(ImageSource.resource(isULK ? R.drawable.map21 : R.drawable.map11));
                break;
            case "2":
                map.setImage(ImageSource.resource(isULK ? R.drawable.map22 : R.drawable.map12));
                break;
            case "3":
                map.setImage(ImageSource.resource(isULK ? R.drawable.map23 : R.drawable.map13));
                break;
            case "4":
                map.setImage(ImageSource.resource(isULK ? R.drawable.map24 : R.drawable.map14));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

