package com.example.sightreadingpractice.musicscore;

import android.util.Log;

public class Pitch {

    private int octave;
    private Key key;

    public static final int PITCH_OFFSET_ON_STAVE = 0;

    public Pitch() {
        this.octave = 0;
        this.key = Key.C;
    }

    public Pitch(int octave, Key key) {
        this.octave = octave;
        this.key = key;
    }

    public int getOctave() {
        return octave;
    }

    public Key getKey() {
        return key;
    }

    public void set(int octave, Key key) {
        this.octave = octave;
        this.key = key;
    }

    public int toStavePos(Key staveKey, Clef clef) {
        int keyNum = key.asNumber(), staveKeyNum = staveKey.asNumber();
        for(int i = 0; i < 12; i++) {
            if ((i * 7) % 12 == staveKeyNum) {
                if (i < 6) return octave * 7 + (int)Math.floor((keyNum + (keyNum < 5 ? 0 : 1)) / 2f) - clef.getStaveCenterNoteNum();
                else if (i > 6) return octave * 7 + (int)Math.floor((keyNum + (keyNum < 7 ? 0 : 1)) / 2f) - clef.getStaveCenterNoteNum();
                else return octave * 7 + (int)Math.floor((keyNum + (keyNum < 5 ? 1 : 2)) / 2f) - clef.getStaveCenterNoteNum();
            }
        }
        return 0;
    }

}
