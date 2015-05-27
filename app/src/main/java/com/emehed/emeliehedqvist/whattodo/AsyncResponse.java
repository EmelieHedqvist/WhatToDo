package com.emehed.emeliehedqvist.whattodo;

/**
 * Created by henriktaljedal on 2015-05-11.
 */

//This interface is needed to make a call 'AsyncResponse delegate' from the class PlaceFinder when a thread,
// away from the UI thread is finished in the class PlaceFinder, to send an instance of the class WPlace from PlaceFinder to
// the method processFinished(...) in DisplayActivity

public interface AsyncResponse {
    void processFinish(WPlace output);
}
