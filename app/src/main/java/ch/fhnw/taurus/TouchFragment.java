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


        rollView = (AbstractLabyrinthView)getView().findViewById(R.id.roll_control);
        pitchView = (AbstractLabyrinthView)view.findViewById(R.id.pitch_control);

        final TouchAngleConverter rollConverter = new TouchAngleConverter(getLabyrinth(),rollView,rollView.getMaxCursorRadius());
        final TouchAngleConverter pitchConverter = new TouchAngleConverter(getLabyrinth(),pitchView,pitchView.getMaxCursorRadius());

        rollView.addTouchEventListener(new PositionChangedListener() {
            @Override
            public void onPositionChanged(float x, float y) {
                int roll = rollConverter.convertToXAngle(x);
                getLabyrinth().setRoll(roll);
            }
        });

        pitchView.addTouchEventListener(new PositionChangedListener() {
            @Override
            public void onPositionChanged(float x, float y) {
                int pitch = pitchConverter.convertToYAngle(y);
                getLabyrinth().setPitch(pitch);
            }
        });

        rollView.setDrawStrategy(new HorizontalTouchDrawStrategy());
        pitchView.setDrawStrategy(new VerticalTouchDrawStrategy());
    }


    private Labyrinth getLabyrinth() {
        return getApp().getLabyrinth();
    }

    private App getApp() {
        return (App)getActivity().getApplication();
    }
}

