package com.cengcelil.phonenumbersappetstur.Helper;

import android.text.Editable;
import android.text.TextWatcher;

import com.cengcelil.phonenumbersappetstur.Utils;

public class MaskHelper implements TextWatcher {
    private static final String TAG = "MaskHelper";
    private String mask;
    private boolean isRunning = false;
    private boolean isDeleting = false;

    private static final String maskTR = "5## ### ## ##";

    public boolean isMaskTR() {
        return mask.equals(maskTR);
    }

    public int compareLength(String str) {
        if (str.length() == 0)
            return Utils.NO_CHARACTER_CODE;
        else if (isMaskTR())
            if (maskTR.length() != str.length())
                return Utils.NEED_CHARACTER_CODE;
            else
                return Utils.OK_CHARACTER_CODE;
        else
            return Utils.OK_CHARACTER_CODE;

    }

    private static final String maskOther = "####################";

    public MaskHelper(String mask) {
        this.mask = mask;
    }

    public static MaskHelper setMask(String code) {
        if (code.equals("+90"))
            return new MaskHelper(maskTR);
        else
            return new MaskHelper(maskOther);
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        isDeleting = i1 > i2;

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

if (editable.toString().length() !=0)
{
    if (isRunning || isDeleting) {
        return;
    }
    isRunning = true;

    int editableLength = editable.length();
    if (editableLength < mask.length() && editableLength != 0) {
        if (mask.charAt(editableLength) != '#') {
            editable.append(mask.charAt(editableLength));
        } else if (mask.charAt(editableLength - 1) != '#') {
            editable.insert(editableLength - 1, mask, editableLength - 1, editableLength);
        }
    }


    isRunning = false;
}

    }

    public void changeMask(String code) {
        if (code.equals("+90")) {
            mask = maskTR;
        } else {
            mask = maskOther;
        }
    }

    public int getMaskLength() {
        return mask.length();
    }
}
