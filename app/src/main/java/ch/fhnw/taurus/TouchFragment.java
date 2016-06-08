package ch.fhnw.taurus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


public class TouchFragment extends Fragment {

    private LabyrinthView pitchView;
    private LabyrinthView rollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =   inflater.inflate(
                R.layout.fragment_touch,
                container,
                false); //!!! this is important

        pitchView = (LabyrinthView)view.findViewById(R.id.pitch_control);
        rollView = (LabyrinthView)view.findViewById(R.id.roll_control);

        // FIXME Make one converter, but pass the required parameters
        final PositionConverter pitchConverter = new PositionConverter(getLabyrinth(),pitchView,pitchView.getMaxCursorRadius());
        final PositionConverter rollConverter = new PositionConverter(getLabyrinth(),rollView,rollView.getMaxCursorRadius());

        pitchView.addTouchEventListener(new TouchEventListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                int pitch = pitchConverter.convertToXAngle(event.getX());
                getLabyrinth().setPitch(pitch);
            }
        });

        rollView.addTouchEventListener(new TouchEventListener() {
            @Override
            public void onTouchEvent(MotionEvent event) {
                int roll = rollConverter.convertToYAngle(event.getY());
                getLabyrinth().setRoll(roll);
            }
        });

        pitchView.setDrawStrategy(new HorizontalTouchDrawStrategy());
        rollView.setDrawStrategy(new VerticalTouchDrawStrategy());
        return view;
    }


    private Labyrinth getLabyrinth() {
        return getApp().getLabyrinth();
    }

    private App getApp() {
        return (App)getActivity().getApplication();
    }
}

