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

    private Canvas canvas;
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

    private Bitmap bmTailEighthNote;
    private Bitmap bmTailSixteenthNote;
    private Bitmap bmTailThirtySecondNote;

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
        bmTailEighthNote = BitmapFactory.decodeResource(getResources(), R.drawable.tail_quaver);
        bmTailSixteenthNote = BitmapFactory.decodeResource(getResources(), R.drawable.tail_semiquaver);
        bmTailThirtySecondNote = BitmapFactory.decodeResource(getResources(), R.drawable.tail_demisemiquaver);

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
        this.canvas = canvas;
        canvas.drawColor(Color.WHITE);
        drawStave(canvas, staveLineMarginLeft,
                centerY - staveLineSpacing * 2 + staveCenterOffset,
                canvas.getWidth() - staveLineMarginRight,
                centerY + staveLineSpacing * 2 + staveCenterOffset);
        scoreBrush.setKey(Key.C);
        scoreBrush.setClef(Clef.TREBLE);
        pitchBuffer.set(3, Key.E);
        drawSingleNote(centerX - 350f, Beat.WHOLE_NOTE);
        pitchBuffer.set(3, Key.F);
        drawSingleNote(centerX - 250f, Beat.SIXTEENTH_NOTE);
        pitchBuffer.set(3, Key.G);
        drawSingleNote(centerX - 150f, Beat.QUARTER_NOTE);
        pitchBuffer.set(3, Key.A);
        drawSingleNote(centerX - 50f, Beat.DOUBLE_NOTE);
        pitchBuffer.set(3, Key.B);
        drawSingleNote(centerX + 50f, Beat.THIRTYSECOND_NOTE);
        pitchBuffer.set(4, Key.C);
        drawSingleNote(centerX + 150f, Beat.EIGHTH_NOTE);
        pitchBuffer.set(4, Key.D);
        drawSingleNote(centerX + 250f, Beat.HALF_NOTE);
    }

    private void drawWholeNoteHead(float centerX) {
        float originalCenterY = canvas.getHeight() / 2 - staveLineSpacing / 2 * pitchBuffer.toStavePos(scoreBrush);
        float centerY = originalCenterY + noteHeadOffsetY * 2.75f;
        float width = noteSolidWidth * 1.5f, height = staveLineSpacing * 0.05f;
        destBuffer.set(
                (int)(centerX - width/2f),
                (int)(centerY - height/2f),
                (int)(centerX + width/2f),
                (int)(centerY + width/2f)
        );
        canvas.drawBitmap(bmNoteSemibreve, null, destBuffer, null);
    }
    
    private void drawHalfNoteHead(float centerX) {
        float originalCenterY = canvas.getHeight() / 2 - staveLineSpacing / 2 * pitchBuffer.toStavePos(scoreBrush);
        float centerY = originalCenterY + noteHeadOffsetY;
        float width = noteSolidWidth, height = staveLineSpacing * 2f / 3;
        destBuffer.set(
                (int)(centerX - width/2f),
                (int)(centerY - height/2f),
                (int)(centerX + width/2f),
                (int)(centerY + width/2f)
        );
        canvas.drawBitmap(bmNoteHollow, null, destBuffer, null);
    }

    private void drawSolidNoteHead(float centerX) {
        float originalCenterY = canvas.getHeight() / 2 - staveLineSpacing / 2 * pitchBuffer.toStavePos(scoreBrush);
        float centerY = originalCenterY + noteHeadOffsetY;
        float width = noteSolidWidth, height = staveLineSpacing * 2f / 3;
        destBuffer.set(
                (int)(centerX - width/2f),
                (int)(centerY - height/2f),
                (int)(centerX + width/2f),
                (int)(centerY + width/2f)
        );
        canvas.drawBitmap(bmNoteSolid, null, destBuffer, null);
    }

    private void drawStem(float noteHeadCenterX, float stemHeightToLineSpacingRatio) {
        int stavePos = pitchBuffer.toStavePos(scoreBrush);
        float lineX = noteHeadCenterX + (stavePos < 0 ? 1 : -1) * noteSolidWidth / 2f;
        float startY = canvas.getHeight() / 2 - staveLineSpacing / 2 * stavePos;
        float stopY = startY + (stavePos < 0 ? -1 : 1) * staveLineSpacing * stemHeightToLineSpacingRatio;
        startY = startY + (stavePos < 0 ? -1 : 1) * staveLineSpacing / 6f;
        canvas.drawLine(lineX, startY, lineX, stopY, canvasBrush);
    }

    private void drawEighthNoteTail(float noteHeadCenterX, float stemHeightToLineSpacingRatio) {
        int stavePos = pitchBuffer.toStavePos(scoreBrush);
        float lineX = noteHeadCenterX + (stavePos < 0 ? 1 : -1) * noteSolidWidth / 2f;
        float startY = canvas.getHeight() / 2 - staveLineSpacing / 2 * stavePos;
        float stopY = startY + (stavePos < 0 ? -1 : 1) * staveLineSpacing * stemHeightToLineSpacingRatio;
        Bitmap tail = stavePos < 0 ? bmTailEighthNote :
                Bitmap.createBitmap(bmTailEighthNote, 0, 0, bmTailEighthNote.getWidth(), bmTailEighthNote.getHeight(),
                        mtrxFlipBitmap, false);
        float stemHeight = staveLineSpacing * stemHeightToLineSpacingRatio;
        destBuffer.set(
                (int) lineX,
                (int) (stopY - (stavePos < 0 ? 0 : stemHeight * 0.8f)),
                (int) (lineX + stemHeight * 0.8f / 3f),
                (int) (stopY + (stavePos < 0 ? stemHeight * 0.8f : 0))
        );
        canvas.drawBitmap(tail, null, destBuffer, null);
    }

    private void drawSixteenthNoteTail(float noteHeadCenterX, float stemHeightToLineSpacingRatio) {
        int stavePos = pitchBuffer.toStavePos(scoreBrush);
        float lineX = noteHeadCenterX + (stavePos < 0 ? 1 : -1) * noteSolidWidth / 2f;
        float startY = canvas.getHeight() / 2 - staveLineSpacing / 2 * stavePos;
        float stopY = startY + (stavePos < 0 ? -1 : 1) * staveLineSpacing * stemHeightToLineSpacingRatio;
        Bitmap tail = stavePos < 0 ? bmTailSixteenthNote :
                Bitmap.createBitmap(bmTailSixteenthNote, 0, 0, bmTailSixteenthNote.getWidth(), bmTailSixteenthNote.getHeight(),
                        mtrxFlipBitmap, false);
        float stemHeight = staveLineSpacing * stemHeightToLineSpacingRatio;
        destBuffer.set(
                (int) lineX,
                (int) (stopY - (stavePos < 0 ? 0 : stemHeight * 0.8f)),
                (int) (lineX + stemHeight * 0.8f / 3f),
                (int) (stopY + (stavePos < 0 ? stemHeight * 0.8f : 0))
        );
        canvas.drawBitmap(tail, null, destBuffer, null);
    }

    private void drawThirtySecondNoteTail(float noteHeadCenterX, float stemHeightToLineSpacingRatio) {
        int stavePos = pitchBuffer.toStavePos(scoreBrush);
        float lineX = noteHeadCenterX + (stavePos < 0 ? 1 : -1) * noteSolidWidth / 2f;
        float startY = canvas.getHeight() / 2 - staveLineSpacing / 2 * stavePos;
        float stopY = startY + (stavePos < 0 ? -1 : 1) * staveLineSpacing * stemHeightToLineSpacingRatio;
        Bitmap tail = stavePos < 0 ? bmTailThirtySecondNote :
                Bitmap.createBitmap(bmTailThirtySecondNote, 0, 0, bmTailThirtySecondNote.getWidth(), bmTailThirtySecondNote.getHeight(),
                        mtrxFlipBitmap, false);
        float stemHeight = staveLineSpacing * stemHeightToLineSpacingRatio;
        destBuffer.set(
                (int) lineX,
                (int) (stopY - (stavePos < 0 ? 0 : stemHeight * 0.9f)),
                (int) (lineX + stemHeight * 0.9f / 2.5f),
                (int) (stopY + (stavePos < 0 ? stemHeight * 0.9f : 0))
        );
        canvas.drawBitmap(tail, null, destBuffer, null);
    }

    private void drawDoubleNoteSideLines(float noteHeadPosX) {
        float doubleNoteLinePadding = 10f;
        float noteCenterY = canvas.getHeight() / 2 - staveLineSpacing / 2 * pitchBuffer.toStavePos(scoreBrush);
        float width = noteSolidWidth * 1.5f, height = staveLineSpacing * 0.05f;
        canvas.drawLine(noteHeadPosX - (width / 2f + doubleNoteLinePadding), noteCenterY - staveLineSpacing / 2, noteHeadPosX - (width / 2f + doubleNoteLinePadding), noteCenterY + staveLineSpacing / 2, canvasBrush);
        canvas.drawLine(noteHeadPosX - width / 2f, noteCenterY - staveLineSpacing / 2, noteHeadPosX - width / 2f, noteCenterY + staveLineSpacing / 2, canvasBrush);
        canvas.drawLine(noteHeadPosX + width / 2f, noteCenterY - staveLineSpacing / 2, noteHeadPosX + width / 2f, noteCenterY + staveLineSpacing / 2, canvasBrush);
        canvas.drawLine(noteHeadPosX + width / 2f + doubleNoteLinePadding, noteCenterY - staveLineSpacing / 2, noteHeadPosX + width / 2f + doubleNoteLinePadding, noteCenterY + staveLineSpacing / 2, canvasBrush);
    }

    private void drawSingleNote(float noteHeadPosX, Beat beat) {
        switch (beat) {
        case DOUBLE_NOTE:
            drawWholeNoteHead(noteHeadPosX);
            drawDoubleNoteSideLines(noteHeadPosX);
            break;
        case WHOLE_NOTE:
            drawWholeNoteHead(noteHeadPosX);
            break;
        case HALF_NOTE:
            drawHalfNoteHead(noteHeadPosX);
            drawStem(noteHeadPosX, 3.5f);
            break;
        case QUARTER_NOTE:
            drawSolidNoteHead(noteHeadPosX);
            drawStem(noteHeadPosX, 3.5f);
            break;
        case EIGHTH_NOTE:
            drawSolidNoteHead(noteHeadPosX);
            drawStem(noteHeadPosX, 3.5f);
            drawEighthNoteTail(noteHeadPosX, 3.5f);
            break;
        case SIXTEENTH_NOTE:
            drawSolidNoteHead(noteHeadPosX);
            drawStem(noteHeadPosX, 3.5f);
            drawSixteenthNoteTail(noteHeadPosX, 3.5f);
            break;
        case THIRTYSECOND_NOTE:
            drawSolidNoteHead(noteHeadPosX);
            drawStem(noteHeadPosX, 3.5f);
            drawThirtySecondNoteTail(noteHeadPosX, 3.5f);
            break;
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

}