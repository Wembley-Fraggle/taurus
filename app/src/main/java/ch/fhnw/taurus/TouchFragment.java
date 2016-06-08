package ch.fhnw.taurus;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TouchFragment extends Fragment {

    private AbstractLabyrinthView pitchView;
    private AbstractLabyrinthView rollView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =   inflater.inflate(
                R.layout.fragment_touch,
                container,
                false); //!!! this is important

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        initTouchView(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initTouchView(View view) {

        pitchView = (AbstractLabyrinthView)view.findViewById(R.id.pitch_control);
        rollView = (AbstractLabyrinthView)getView().findViewById(R.id.roll_control);

        final TouchAngleConverter pitchConverter = new TouchAngleConverter(getLabyrinth(),pitchView,pitchView.getMaxCursorRadius());
        final TouchAngleConverter rollConverter = new TouchAngleConverter(getLabyrinth(),rollView,rollView.getMaxCursorRadius());


        pitchView.addTouchEventListener(new PositionChangedListener() {
            @Override
            public void onPositionChanged(float x, float y) {
                int pitch = pitchConverter.convertToXAngle(x);
                getLabyrinth().setPitch(pitch);
            }
        });

        rollView.addTouchEventListener(new PositionChangedListener() {
            @Override
            public void onPositionChanged(float x, float y) {
                int roll = rollConverter.convertToYAngle(y);
                getLabyrinth().setRoll(roll);
            }
        });

        pitchView.setDrawStrategy(new HorizontalTouchDrawStrategy());
        rollView.setDrawStrategy(new VerticalTouchDrawStrategy());
    }


    private Labyrinth getLabyrinth() {
        return getApp().getLabyrinth();
    }

    private App getApp() {
        return (App)getActivity().getApplication();
    }
}

