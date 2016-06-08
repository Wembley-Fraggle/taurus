package ch.fhnw.taurus;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class AccelerometerFragment extends Fragment {

    private AbstractLabyrinthView labyrinthView;

    public AccelerometerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accelerometer, container, false);

        labyrinthView = (AbstractLabyrinthView)view.findViewById(R.id.accelerometer_control);

        // FIXME Make one converter, but pass the required parameters
        final TouchAngleConverter angleConverter = new TouchAngleConverter(getLabyrinth(),labyrinthView,labyrinthView.getMaxCursorRadius());

        labyrinthView.addTouchEventListener(new PositionChangedListener() {
            @Override
            public void onPositionChanged(float x, float y) {
                int pitch = angleConverter.convertToXAngle(x);
                int roll = angleConverter.convertToYAngle(y);
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
