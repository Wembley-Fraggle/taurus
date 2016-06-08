package ch.fhnw.taurus;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccelerometerFragment extends Fragment {

    private TouchLabyrinthView labyrinthView;

    public AccelerometerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accelerometer, container, false);

        labyrinthView = (TouchLabyrinthView)view.findViewById(R.id.accelerometer_control);

        // FIXME Make one converter, but pass the required parameters
        final PositionConverter angleConverter = new PositionConverter(getLabyrinth(),labyrinthView,labyrinthView.getMaxCursorRadius());

        labyrinthView.addTouchEventListener(new TouchEventListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                int pitch = angleConverter.convertToXAngle(event.getX());
                int roll = angleConverter.convertToYAngle(event.getY());
                getLabyrinth().setAngles(pitch,roll);
            }
        });

        labyrinthView.setDrawStrategy(new AccelerometerDrawStrategy());

        return view;
    }


    private Labyrinth getLabyrinth() {
        return getApp().getLabyrinth();
    }

    private App getApp() {
        return (App)getActivity().getApplication();
    }

}
