package com.example.sightreadingpractice.appcomponent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sightreadingpractice.R;
import com.example.sightreadingpractice.musicscore.Beat;
import com.example.sightreadingpractice.musicscore.Clef;
import com.example.sightreadingpractice.musicscore.Key;
import com.example.sightreadingpractice.musicscore.Pitch;
import com.example.sightreadingpractice.musicscore.ScoreBrush;

public class MusicScoreView extends View {

    private Rect destBuffer;
    private Paint canvasBrush;
    private Pitch pitchBuffer;
    private ScoreBrush scoreBrush;
    private Matrix mtrxFlipBitmap;

    private Bitmap bmNoteSolid;
    private Bitmap bmNoteHollow;
    private Bitmap bmNoteSemibreve;

    private float noteSolidWidth;
    private float noteHeadOffsetY;
    private float noteHollowOffsetY;
    private float noteSemibreveOffsetY;

    private Bitmap bmTailQuaver;
    private Bitmap bmTailSubQuaver;

    private float tailHeightToWidthRatio;
    private float tailHeightToStemHeightRatio;

    private float staveLineMarginLeft;
    private float staveLineMarginTop;
    private float staveLineMarginRight;
    private float staveLineMarginBottom;
    private float staveLineSpacing;
    private float staveLineThickness;
    private float staveCenterOffset;

    public MusicScoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        bmNoteSolid = BitmapFactory.decodeResource(getResources(), R.drawable.note_solid);
        bmNoteHollow = BitmapFactory.decodeResource(getResources(), R.drawable.note_hollow);
        bmNoteSemibreve = BitmapFactory.decodeResource(getResources(), R.drawable.note_semibreve);
        bmTailQuaver = BitmapFactory.decodeResource(getResources(), R.drawable.tail_quaver);

        destBuffer = new Rect();
        canvasBrush = new Paint();
        pitchBuffer = new Pitch();
        scoreBrush = new ScoreBrush();
        mtrxFlipBitmap = new Matrix();
        mtrxFlipBitmap.preScale(1, -1);

        noteSolidWidth = 40;
        noteHeadOffsetY = -5;

        tailHeightToStemHeightRatio = 0.8f;
        tailHeightToWidthRatio = 3f;

        staveLineMarginLeft = 50;
        staveLineMarginTop = 50;
        staveLineMarginRight = 50;
        staveLineMarginBottom = 50;
        staveLineSpacing = 30;
        staveLineThickness = 3;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerX = canvas.getWidth()/2, centerY = canvas.getHeight()/2;
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        drawStave(canvas, staveLineMarginLeft,
                centerY - staveLineSpacing * 2 + staveCenterOffset,
                canvas.getWidth() - staveLineMarginRight,
                centerY + staveLineSpacing * 2 + staveCenterOffset);
        scoreBrush.setKey(Key.C);
        scoreBrush.setClef(Clef.TREBLE);
        pitchBuffer.set(3, Key.B);
        drawSingleNote(canvas, Beat.WHOLE_NOTE, centerX - 200f, pitchBuffer, scoreBrush);
        pitchBuffer.set(3, Key.G);
        drawSingleNote(canvas, Beat.HALF_NOTE, centerX - 100f, pitchBuffer, scoreBrush);
        pitchBuffer.set(3, Key.A);
        drawSingleNote(canvas, Beat.EIGHTH_NOTE, centerX, pitchBuffer, scoreBrush);
        pitchBuffer.set(3, Key.B);
        drawSingleNote(canvas, Beat.QUARTER_NOTE, centerX + 100f, pitchBuffer, scoreBrush);
        pitchBuffer.set(4, Key.C);
        drawSingleNote(canvas, Beat.EIGHTH_NOTE, centerX + 200f, pitchBuffer, scoreBrush);
    }

    private void drawNote(Canvas canvas, Bitmap noteHead, boolean hasStem, Bitmap noteTail, float noteHeadPosX, float stemHeightToLineSpacingRatio, Pitch pitch, ScoreBrush scoreBrush) {
        canvasBrush.setColor(Color.BLACK);
        canvasBrush.setStrokeWidth(staveLineThickness);
        // Draw note head.
        float noteHeadPosY = canvas.getHeight()/2 + noteHeadOffsetY * (noteHead == bmNoteSemibreve ? 2.75f : 1f) - staveLineSpacing / 2 * pitch.toStavePos(scoreBrush);
        float width = noteSolidWidth * (noteHead == bmNoteSemibreve ? 1.5f : 1f), height = staveLineSpacing * (noteHead == bmNoteSemibreve ? 0.05f : 2f / 3);
        destBuffer.set(
                (int)(noteHeadPosX - width/2f),
                (int)(noteHeadPosY - height/2f),
                (int)(noteHeadPosX + width/2f),
                (int)(noteHeadPosY + width/2f)
        );
        canvas.drawBitmap(noteHead, null, destBuffer, null);
        // Draw note line.
        if (hasStem) {
            int stavePos = pitch.toStavePos(scoreBrush);
            float lineX = noteHeadPosX + (stavePos < 0 ? 1 : -1) * noteSolidWidth / 2f;
            float startY = canvas.getHeight() / 2 - staveLineSpacing / 2 * stavePos;
            float stopY = startY + (stavePos < 0 ? -1 : 1) * staveLineSpacing * stemHeightToLineSpacingRatio;
            startY = startY + (stavePos < 0 ? -1 : 1) * staveLineSpacing / 6f;
            canvas.drawLine(lineX, startY, lineX, stopY, canvasBrush);
            if (noteTail != null) {
                // Draw tail.
                Bitmap tail = stavePos < 0 ? noteTail :
                        Bitmap.createBitmap(noteTail, 0, 0, noteTail.getWidth(), noteTail.getHeight(),
                                mtrxFlipBitmap, false);
                float stemHeight = staveLineSpacing * stemHeightToLineSpacingRatio;
                destBuffer.set(
                        (int) lineX,
                        (int) (stopY - (stavePos < 0 ? 0 : stemHeight * tailHeightToStemHeightRatio)),
                        (int) (lineX + stemHeight * tailHeightToStemHeightRatio / tailHeightToWidthRatio),
                        (int) (stopY + (stavePos < 0 ? stemHeight * tailHeightToStemHeightRatio : 0))
                );
                Log.d("DEBUG", "Drawing tail.");
                canvas.drawBitmap(tail, null, destBuffer, null);
            }
        }
    }

    private void drawStave(Canvas canvas, float left, float top, float right, float bottom) {
        canvasBrush.setColor(Color.BLACK);
        canvasBrush.setStrokeWidth(staveLineThickness);
        canvas.drawLine(left, top, right, top, canvasBrush);
        canvas.drawLine(left, top * 0.75f + bottom * 0.25f, right, top * 0.75f + bottom * 0.25f, canvasBrush);
        canvas.drawLine(left, (top + bottom) * 0.5f, right, (top + bottom) * 0.5f, canvasBrush);
        canvas.drawLine(left, top * 0.25f + bottom * 0.75f, right, top * 0.25f + bottom * 0.75f, canvasBrush);
        canvas.drawLine(left, bottom, right, bottom, canvasBrush);
    }

    private void drawSingleNote(Canvas canvas, Beat beat, float noteHeadPosX, Pitch pitch, ScoreBrush scoreBrush) {
        Bitmap noteHead =
                beat == Beat.WHOLE_NOTE ? bmNoteSemibreve
                : beat == Beat.HALF_NOTE ? bmNoteHollow
                : bmNoteSolid;
        Bitmap noteTail =
                beat == Beat.EIGHTH_NOTE ? bmTailQuaver
                : beat == Beat.SIXTEENTH_NOTE ? bmTailSubQuaver
                : null;
        drawNote(canvas, noteHead, beat.getRank() > Beat.WHOLE_NOTE.getRank(), noteTail,
                noteHeadPosX, 3.5f, pitch, scoreBrush);
    }

}