package com.example.sightreadingpractice.appcomponent;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.sightreadingpractice.R;
import com.example.sightreadingpractice.musicscore.Clef;
import com.example.sightreadingpractice.musicscore.Key;
import com.example.sightreadingpractice.musicscore.Pitch;

public class MusicScoreView extends View {

    private Rect destBuffer;
    private Paint brush;
    private Pitch pitchBuffer;

    private Bitmap bmNoteSolid;
    private Bitmap bmNoteHollow;
    private Bitmap bmNoteSemibreve;

    private float noteSolidWidth;
    private float noteSolidOffsetY;
    private float noteHollowOffsetY;
    private float noteSemibreveOffsetY;

    private Bitmap bmTailQuaver;
    private Bitmap bmTailSubQuaver;

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
        destBuffer = new Rect();
        brush = new Paint();
        pitchBuffer = new Pitch();
        staveLineMarginLeft = 50;
        staveLineMarginTop = 50;
        staveLineMarginRight = 50;
        staveLineMarginBottom = 50;
        noteSolidWidth = 50;
        staveLineSpacing = 30;
        staveLineThickness = 3;
        noteSolidOffsetY = -7;
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
        pitchBuffer.set(3, Key.G);
        drawNote(canvas, bmNoteSolid, centerX - 100f, pitchBuffer, Key.C, Clef.TREBLE);
        pitchBuffer.set(3, Key.A);
        drawNote(canvas, bmNoteSolid, centerX, pitchBuffer, Key.C, Clef.TREBLE);
        pitchBuffer.set(3, Key.B);
        drawNote(canvas, bmNoteSolid, centerX + 100f, pitchBuffer, Key.C, Clef.TREBLE);
        pitchBuffer.set(4, Key.C);
        drawNote(canvas, bmNoteSolid, centerX + 200f, pitchBuffer, Key.C, Clef.TREBLE);
    }

    private void drawNote(Canvas canvas, Bitmap noteHead, float noteHeadPosX, Pitch pitch, Key staveKey, Clef clef) {
        // Draw note head.
        float noteHeadPosY = canvas.getHeight()/2 + noteSolidOffsetY - staveLineSpacing / 2 * pitch.toStavePos(staveKey, clef);
        float width = 50, height = staveLineSpacing * 2 / 3;
        destBuffer.set(
                (int)(noteHeadPosX - width/2),
                (int)(noteHeadPosY - height/2),
                (int)(noteHeadPosX + width/2),
                (int)(noteHeadPosY + width/2)
        );
        canvas.drawBitmap(noteHead, null, destBuffer, null);
        // Draw note line.
        brush.setColor(Color.BLACK);
        brush.setStrokeWidth(staveLineThickness);
        int stavePos = pitch.toStavePos(staveKey, clef);
        float lineX = noteHeadPosX + (stavePos < 0 ? 1 : -1) * noteSolidWidth / 2f;
        float startY = canvas.getHeight()/2 - staveLineSpacing / 2 * stavePos;
        float stopY = startY + (stavePos < 0 ? -1 : 1) * staveLineSpacing * 3.5f;
        startY = startY + (stavePos < 0 ? -1 : 1) * staveLineSpacing / 6f;
        canvas.drawLine(lineX, startY, lineX, stopY, brush);
    }

    private void drawStave(Canvas canvas, float left, float top, float right, float bottom) {
        brush.setColor(Color.BLACK);
        brush.setStrokeWidth(staveLineThickness);
        canvas.drawLine(left, top, right, top, brush);
        canvas.drawLine(left, top * 0.75f + bottom * 0.25f, right, top * 0.75f + bottom * 0.25f, brush);
        canvas.drawLine(left, (top + bottom) * 0.5f, right, (top + bottom) * 0.5f, brush);
        canvas.drawLine(left, top * 0.25f + bottom * 0.75f, right, top * 0.25f + bottom * 0.75f, brush);
        canvas.drawLine(left, bottom, right, bottom, brush);
    }

}