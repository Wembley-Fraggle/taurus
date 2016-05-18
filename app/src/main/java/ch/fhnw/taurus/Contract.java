package ch.fhnw.taurus;

/**
 * Created by nozdormu on 03/05/2016.
 */
public class Contract {

    /**
     * Data key for a {@link ConnectionModel} which holds the connection information
     * as a Serializable
     */
    public static final String TAG_CONNECTION_MODEL = "TAG_CONNECTION_MODEL";


    public static final String TAG_HTTP_STATUS = "TAG_HTTP_STATUS";
    public static final String RESULT_MESSAGE = "RESULT_MESSAGE";
    public static final String TAG_CONNECTION_TEST_STARTED = "TAG_CONNECTION_TEST_STARTED";
    public static final int CONNECT_REQUEST_CODE = 1;

    public static final int RESULT_OK = 0;
    public static final int RESULT_CONNECTION_FAILED = 1;
}
